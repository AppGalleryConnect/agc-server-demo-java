/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2022-2023. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.agc.clouddb.quickstart.migrate;

import com.huawei.agc.clouddb.quickstart.connector.DbConnector;
import com.huawei.agc.clouddb.quickstart.except.ServiceException;
import com.huawei.agc.clouddb.quickstart.progress.IProgressHandler;
import com.huawei.agc.clouddb.quickstart.source.model.AbstractSourceModel;
import com.huawei.agconnect.server.clouddb.exception.AGConnectCloudDBException;
import com.huawei.agconnect.server.clouddb.request.CloudDBZoneGenericObject;
import com.huawei.agconnect.server.clouddb.request.CloudDBZoneObject;
import com.huawei.agconnect.server.clouddb.service.CloudDBZone;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Provide interfaces to migrate data from source database to Cloud DB
 *
 * @param <S> class of source model
 * @param <T> class of target model
 * @since 2022-12-30
 */
public class DataMigrate<S extends AbstractSourceModel, T extends CloudDBZoneObject> {
    private static final int BATCH = 1000;

    private static final String PROCESSING = "processing...";

    private static final String PROCESS_FINISHED = "process table[%s] finished.";

    private final IProgressHandler progressHandler;

    private final DataHandler<S, T> dataHandler;

    public DataMigrate(DataHandler<S, T> dataHandler, IProgressHandler progressHandler) {
        this.dataHandler = dataHandler;
        this.progressHandler = progressHandler;
    }

    /**
     * migrate data from source database to Cloud DB as generic object
     *
     * @param cloudDBZone cloud DB zone
     */
    public void migrateAsGenericObject(CloudDBZone cloudDBZone) {
        long count;
        float progress = 0.0f;
        try (Connection connection = DbConnector.getConnection()) {
            count = getCount(connection);

            long offset = 0L;
            while (offset < count) {
                List<CloudDBZoneGenericObject> batchObjs = dataHandler.selectAsGenericObject(connection, offset, BATCH);
                doUpsertByBatch(cloudDBZone, batchObjs);

                offset += batchObjs.size();
                progress = (float) offset / (float) count;
                if (offset < count) {
                    progressHandler.progress(progress, PROCESSING);
                }
            }

            String finishMsg = String.format(Locale.ROOT, PROCESS_FINISHED, dataHandler.getSourceTableName());
            progressHandler.progress(1f, finishMsg);
        } catch (SQLException | ServiceException | AGConnectCloudDBException | ExecutionException ex) {
            progressHandler.progress(progress, ex.getMessage());
        } catch (InterruptedException ex) {
            progressHandler.progress(progress, ex.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    /**
     * migrate data from source database to Cloud DB as target model
     *
     * @param cloudDBZone cloud DB zone
     */
    public void migrateAsTargetObject(CloudDBZone cloudDBZone) {
        long count;
        float progress = 0.0f;
        try (Connection connection = DbConnector.getConnection()) {
            count = getCount(connection);

            long offset = 0L;
            while (offset < count) {
                List<T> batchObjs = dataHandler.selectAsTarget(connection, offset, BATCH);
                doUpsertByBatch(cloudDBZone, batchObjs);

                offset += batchObjs.size();
                progress = (float) offset / (float) count;
                if (offset < count) {
                    progressHandler.progress(progress, PROCESSING);
                }
            }

            String finishMsg = String.format(Locale.ROOT, PROCESS_FINISHED, dataHandler.getSourceTableName());
            progressHandler.progress(1f, finishMsg);
        } catch (SQLException | ServiceException | AGConnectCloudDBException | ExecutionException ex) {
            progressHandler.progress(progress, ex.getMessage());
        } catch (InterruptedException ex) {
            progressHandler.progress(progress, ex.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    private void doUpsertByBatch(CloudDBZone cloudDBZone, List<? extends CloudDBZoneObject> batchObjs)
        throws AGConnectCloudDBException, ExecutionException, InterruptedException, ServiceException {
        CompletableFuture<Integer> result = cloudDBZone.executeUpsert(batchObjs);
        if (result.get() <= 0) {
            throw new ServiceException("upsert data error");
        }
    }

    private long getCount(Connection connection) throws ServiceException {
        long count = dataHandler.count(connection);
        String startMsg = String.format(Locale.ROOT, "start to process table[%s], total size is: %d",
            dataHandler.getSourceTableName(), count);
        progressHandler.progress(0f, startMsg);
        return count;
    }

    /**
     * export data to json file
     *
     * @param zoneName cloud DB zone name
     */
    public void export(String zoneName) {
        long count;
        float progress = 0.0f;
        try (Connection connection = DbConnector.getConnection()) {
            count = getCount(connection);

            long offset = 0L;
            int sequence = 0;
            while (offset < count) {
                List<S> batchObjs = dataHandler.selectAsSource(connection, offset, BATCH);
                writeToJson(zoneName, batchObjs, sequence++);

                offset += batchObjs.size();
                progress = (float) offset / (float) count;
                if (offset < count) {
                    progressHandler.progress(progress, PROCESSING);
                }
            }
            String finishMsg = String.format(Locale.ROOT, PROCESS_FINISHED, dataHandler.getSourceTableName());
            progressHandler.progress(1f, finishMsg);
        } catch (SQLException | ServiceException ex) {
            progressHandler.progress(progress, ex.getMessage());
        }
    }

    private void writeToJson(String zoneName, List<S> batchObjs, int sequence) throws ServiceException {
        try {
            File outputPath = new File("./output");
            if (!outputPath.exists() && !outputPath.mkdir()) {
                throw new ServiceException("create output directory failed.");
            }
            String fileName = String.format(Locale.ROOT, "./output/%s_%s_%d.json",
                zoneName, dataHandler.getTargetTableName(), sequence);
            File file = new File(fileName);
            if (!file.exists() && !file.createNewFile()) {
                throw new ServiceException("create output file failed.");
            }

            JSONObject jsonObject = dataHandler.convert(zoneName, batchObjs);
            try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                fileOutputStream.write(jsonObject.toJSONString().getBytes(StandardCharsets.UTF_8));
            }
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
