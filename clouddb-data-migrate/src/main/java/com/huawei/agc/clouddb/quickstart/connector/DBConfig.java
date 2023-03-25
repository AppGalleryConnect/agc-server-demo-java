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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Parse properties file of source database
 *
 * @since 2022-12-30
 */
public class DBConfig {
    private static final Logger LOGGER = LogManager.getLogger(DBConfig.class);

    private Properties properties;

    private DBConfig() {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("./config/db.properties"))) {
            properties = new Properties();
            properties.load(bufferedReader);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    private static final class SingletonHolder {
        private static final DBConfig INSTANCE = new DBConfig();
    }

    public static DBConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /**
     * get url of source database
     *
     * @return url of source database
     */
    public String getUrl() {
        return properties.getProperty("source.db.url");
    }

    /**
     * get driver of source database
     *
     * @return driver of source database
     */
    public String getDriver() {
        return properties.getProperty("source.db.driver");
    }

    /**
     * get user of source database
     *
     * @return user of source database
     */
    public String getUser() {
        return properties.getProperty("source.db.user");
    }

    /**
     * get password of source database
     *
     * @return password of source database
     */
    public String getPassword() {
        return properties.getProperty("source.db.password");
    }
}
