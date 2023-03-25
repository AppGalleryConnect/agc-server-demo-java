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

package com.huawei.agc.clouddb.quickstart;

import static com.huawei.agconnect.server.clouddb.service.ClientNameEnum.CLOUDDB_CN;

import com.huawei.agc.clouddb.quickstart.annotation.Table;
import com.huawei.agc.clouddb.quickstart.auth.AuthenticateTool;
import com.huawei.agc.clouddb.quickstart.except.ServiceException;
import com.huawei.agc.clouddb.quickstart.migrate.DataHandler;
import com.huawei.agc.clouddb.quickstart.migrate.DataMigrate;
import com.huawei.agc.clouddb.quickstart.progress.LoggerProgressHandler;
import com.huawei.agc.clouddb.quickstart.source.model.AbstractSourceModel;
import com.huawei.agc.clouddb.quickstart.source.model.SourceBookInfo;
import com.huawei.agconnect.server.clouddb.request.CloudDBZoneConfig;
import com.huawei.agconnect.server.clouddb.request.CloudDBZoneObject;
import com.huawei.agconnect.server.clouddb.service.AGConnectCloudDB;
import com.huawei.agconnect.server.clouddb.service.CloudDBZone;
import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.exception.AGCException;

import java.util.ArrayList;
import java.util.List;

/**
 * main class
 *
 * @since 2022-12-30
 */
public class Application {
    /**
     * demo application entry
     *
     * @param args params
     */
    public static void main(String[] args) {
        // assign zone name in Cloud DB
        String cloudDBZoneName = "AdminSdkZoneTest";
        CloudDBZone cloudDBZone;
        try {
            AuthenticateTool.init();
            AGCClient agcClient = AGCClient.getInstance(CLOUDDB_CN.getClientName());
            AGConnectCloudDB cloudDB = AGConnectCloudDB.getInstance(agcClient);

            CloudDBZoneConfig cloudDBZoneConfig = new CloudDBZoneConfig(cloudDBZoneName);
            cloudDBZone = cloudDB.openCloudDBZone(cloudDBZoneConfig);
        } catch (AGCException ex) {
            ex.printStackTrace();
            return;
        }

        // assign the tables, and do migrate or export data files
        List<Class<? extends AbstractSourceModel>> classes = new ArrayList<>();
        classes.add(SourceBookInfo.class);

        for (Class<? extends AbstractSourceModel> clazz : classes) {
            try {
                Class<? extends CloudDBZoneObject> targetClazz = clazz.getAnnotation(Table.class).targetClass();
                DataHandler<? extends AbstractSourceModel, ? extends CloudDBZoneObject> dataHandler =
                    new DataHandler<>(clazz, targetClazz);
                dataHandler.init();

                DataMigrate<? extends AbstractSourceModel, ? extends CloudDBZoneObject> dataMigrate =
                    new DataMigrate<>(dataHandler, new LoggerProgressHandler());

                // option1: migrate data with target object
                dataMigrate.migrateAsTargetObject(cloudDBZone);

                // option2: migrate data with generic object
                dataMigrate.migrateAsGenericObject(cloudDBZone);

                // option3: export data to json files
                dataMigrate.export(cloudDBZoneName);
            } catch (ServiceException ex) {
                ex.printStackTrace();
            }
        }

        System.exit(0);
    }
}
