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

package com.huawei.agc.clouddb.quickstart.connector;

import com.huawei.agc.clouddb.quickstart.except.ServiceException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Provide connection of source database
 *
 * @since 2022-12-30
 */
public class DbConnector {
    /**
     * get db connection
     *
     * @return db connection
     * @throws ServiceException exception
     */
    public static Connection getConnection() throws ServiceException {
        try {
            DBConfig dbConfig = DBConfig.getInstance();
            Class.forName(dbConfig.getDriver());
            return DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword());
        } catch (SQLException e) {
            throw new ServiceException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new ServiceException("Driver not found.");
        }
    }
}
