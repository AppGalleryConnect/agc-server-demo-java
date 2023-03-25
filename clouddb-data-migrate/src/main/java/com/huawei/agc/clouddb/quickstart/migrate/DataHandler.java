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

import com.huawei.agc.clouddb.quickstart.annotation.Column;
import com.huawei.agc.clouddb.quickstart.annotation.Table;
import com.huawei.agc.clouddb.quickstart.except.ServiceException;
import com.huawei.agc.clouddb.quickstart.source.ColumnMapping;
import com.huawei.agc.clouddb.quickstart.source.model.AbstractSourceModel;
import com.huawei.agconnect.server.clouddb.exception.AGConnectCloudDBException;
import com.huawei.agconnect.server.clouddb.request.CloudDBZoneGenericObject;
import com.huawei.agconnect.server.clouddb.request.CloudDBZoneObject;
import com.huawei.agconnect.server.clouddb.request.Text;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler to process data in database
 *
 * @param <S> class of source model
 * @param <T> class of target model
 * @since 2022-12-30
 */
public class DataHandler<S extends AbstractSourceModel, T extends CloudDBZoneObject> {
    private final Class<S> sourceClazz;

    private final Class<T> targetClazz;

    private final Map<String, ColumnMapping> originFields = new HashMap<>();

    private final List<String> primaryKeys = new ArrayList<>();

    private String sourceTableName;

    private String targetTableName;

    public DataHandler(Class<S> sourceClazz, Class<T> targetClazz) {
        this.sourceClazz = sourceClazz;
        this.targetClazz = targetClazz;
    }

    public String getSourceTableName() {
        return sourceTableName;
    }

    public String getTargetTableName() {
        return targetTableName;
    }

    /**
     * init to analyse the model class
     *
     * @throws ServiceException exception
     */
    public void init() throws ServiceException {
        Table tableAnnotation = sourceClazz.getAnnotation(Table.class);
        if (tableAnnotation == null || this.targetClazz != tableAnnotation.targetClass()) {
            throw new ServiceException("class is invalid.");
        }
        this.sourceTableName = tableAnnotation.source();
        this.targetTableName = tableAnnotation.target();

        try {
            Field[] fields = sourceClazz.getDeclaredFields();
            for (Field field : fields) {
                Column columnAnnotation = field.getAnnotation(Column.class);
                String sourceColumnName = columnAnnotation.source();
                String targetColumnName = columnAnnotation.target();

                field.setAccessible(true);
                ColumnMapping columnMapping = new ColumnMapping();
                columnMapping.setSourceField(field);
                columnMapping.setSourceName(sourceColumnName);
                columnMapping.setTargetName(targetColumnName);

                Field targetField = targetClazz.getDeclaredField(targetColumnName);
                targetField.setAccessible(true);
                columnMapping.setTargetField(targetField);

                originFields.put(sourceColumnName, columnMapping);

                if (columnAnnotation.isPrimaryKey()) {
                    primaryKeys.add(sourceColumnName);
                }
            }
        } catch (SecurityException | NoSuchFieldException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * select all the records as source model
     *
     * @param connection db connection
     * @return list of the records
     * @throws ServiceException exception
     */
    public List<S> selectAsSource(Connection connection) throws ServiceException {
        try (PreparedStatement psmt = connection.prepareStatement(makeupSelectSql());
            ResultSet resultSet = psmt.executeQuery()) {
            return extractResultAsSource(resultSet);
        } catch (SQLException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * select all the records as target model
     *
     * @param connection db connection
     * @return list of the records
     * @throws ServiceException exception
     */
    public List<T> selectAsTarget(Connection connection) throws ServiceException {
        try (PreparedStatement psmt = connection.prepareStatement(makeupSelectSql());
             ResultSet resultSet = psmt.executeQuery()) {
            return extractResultAsTarget(resultSet);
        } catch (SQLException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * select all the records as target generic model
     *
     * @param connection db connection
     * @return list of the records
     * @throws ServiceException exception
     */
    public List<CloudDBZoneGenericObject> selectAsGenericObject(Connection connection) throws ServiceException {
        try (PreparedStatement psmt = connection.prepareStatement(makeupSelectSql());
             ResultSet resultSet = psmt.executeQuery()) {
            return extractResultAsGenericObject(resultSet);
        } catch (SQLException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    private String makeupSelectSql() {
        return String.format("select %s from %s order by %s;",
            String.join(", ", originFields.keySet()), this.sourceTableName, String.join(", ", primaryKeys));
    }

    /**
     * select records by limit as source model
     *
     * @param connection db connection
     * @param offset start position
     * @param limit maximum count of result
     * @return list of the records
     * @throws ServiceException exception
     */
    public List<S> selectAsSource(Connection connection, long offset, long limit) throws ServiceException {
        String sql = makeupSelectByLimitSql();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setLong(1, limit);
            psmt.setLong(2, offset);
            try (ResultSet resultSet = psmt.executeQuery()) {
                return extractResultAsSource(resultSet);
            }
        } catch (SQLException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * select records by limit as target model
     *
     * @param connection db connection
     * @param offset start position
     * @param limit maximum count of result
     * @return list of the records
     * @throws ServiceException exception
     */
    public List<T> selectAsTarget(Connection connection, long offset, long limit) throws ServiceException {
        String sql = makeupSelectByLimitSql();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setLong(1, limit);
            psmt.setLong(2, offset);
            try (ResultSet resultSet = psmt.executeQuery()) {
                return extractResultAsTarget(resultSet);
            }
        } catch (SQLException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    /**
     * select records by limit as target generic model
     *
     * @param connection db connection
     * @param offset start position
     * @param limit maximum count of result
     * @return list of the records
     * @throws ServiceException exception
     */
    public List<CloudDBZoneGenericObject> selectAsGenericObject(Connection connection, long offset, long limit)
        throws ServiceException {
        String sql = makeupSelectByLimitSql();
        try (PreparedStatement psmt = connection.prepareStatement(sql)) {
            psmt.setLong(1, limit);
            psmt.setLong(2, offset);
            try (ResultSet resultSet = psmt.executeQuery()) {
                return extractResultAsGenericObject(resultSet);
            }
        } catch (SQLException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    private String makeupSelectByLimitSql() {
        return String.format("select %s from %s order by %s limit ? offset ?;",
            String.join(", ", originFields.keySet()), this.sourceTableName, String.join(", ", primaryKeys));
    }

    /**
     * count of the records
     *
     * @param connection db connection
     * @return count of the records
     * @throws ServiceException exception
     */
    public long count(Connection connection) throws ServiceException {
        try (PreparedStatement psmt = connection.prepareStatement("select count(1) from " + sourceTableName + ";");
            ResultSet resultSet = psmt.executeQuery()) {
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
            return 0;
        } catch (SQLException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    private List<S> extractResultAsSource(ResultSet resultSet)
        throws ServiceException {
        try {
            List<S> results = new ArrayList<>();
            while (resultSet.next()) {
                S result = sourceClazz.newInstance();
                for (ColumnMapping columnMapping : originFields.values()) {
                    Field field = columnMapping.getSourceField();
                    Object value = getFieldValue(resultSet, columnMapping.getSourceName(),
                        columnMapping.getSourceField().getType());
                    field.set(result, value);
                }
                results.add(result);
            }
            return results;
        } catch (InstantiationException | IllegalAccessException | SQLException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    private List<T> extractResultAsTarget(ResultSet resultSet) throws ServiceException {
        try {
            List<T> results = new ArrayList<>();
            while (resultSet.next()) {
                T result = targetClazz.newInstance();
                for (ColumnMapping columnMapping : originFields.values()) {
                    Field field = columnMapping.getTargetField();
                    Object value = getFieldValue(resultSet, columnMapping.getSourceName(),
                        columnMapping.getTargetField().getType());
                    field.set(result, value);
                }
                results.add(result);
            }
            return results;
        } catch (InstantiationException | IllegalAccessException | SQLException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    private List<CloudDBZoneGenericObject> extractResultAsGenericObject(ResultSet resultSet) throws ServiceException {
        try {
            List<CloudDBZoneGenericObject> results = new ArrayList<>();
            while (resultSet.next()) {
                CloudDBZoneGenericObject genericObject = CloudDBZoneGenericObject.build(targetTableName);
                for (ColumnMapping columnMapping : originFields.values()) {
                    genericObject.addFieldValue(columnMapping.getTargetName(),
                        getFieldValue(resultSet, columnMapping.getSourceName(),
                            columnMapping.getSourceField().getType()));
                }
                results.add(genericObject);
            }
            return results;
        } catch (SQLException | AGConnectCloudDBException ex) {
            throw new ServiceException(ex.getMessage());
        }
    }

    private static Object getFieldValue(ResultSet resultSet, String fieldName, Class<?> valueType) throws SQLException {
        if (valueType == byte[].class) {
            return resultSet.getBytes(fieldName);
        } else if (valueType == Text.class) {
            return new Text(resultSet.getString(fieldName));
        } else if (valueType == Date.class) {
            return new Date(resultSet.getDate(fieldName).getTime());
        } else {
            return resultSet.getObject(fieldName, valueType);
        }
    }

    /**
     * convert the source objects to JSONObject which Cloud DB can process
     *
     * @param zoneName the destiny cloud db zone
     * @param upsertObjects source objects
     * @return JSONObject which Cloud DB can process
     * @throws ServiceException exception
     */
    public JSONObject convert(String zoneName, List<S> upsertObjects) throws ServiceException {
        JSONObject jsonObject = new JSONObject(new LinkedHashMap<>());
        jsonObject.put("cloudDBZoneName", zoneName);
        jsonObject.put("objectTypeName", targetTableName);

        JSONArray recordObjs = new JSONArray();
        try {
            for (AbstractSourceModel upsertObj : upsertObjects) {
                JSONObject recordObj = new JSONObject();
                for (ColumnMapping columnMapping : originFields.values()) {
                    recordObj.put(columnMapping.getTargetName(), upsertObj.getValue(columnMapping.getSourceField()));
                }
                recordObjs.add(recordObj);
            }
        } catch (IllegalAccessException ex) {
            throw new ServiceException(ex.getMessage());
        }

        jsonObject.put("objects", recordObjs);
        return jsonObject;
    }
}
