/*
 * Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
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

package com.huawei.agconnect.server.demo.auth;

import com.huawei.agconnect.server.auth.exception.AGCAuthException;
import com.huawei.agconnect.server.auth.service.AGCAuth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * revoke demo
 *
 * @since 2020-10-19
 */
public class RevokeDemo extends AbstractDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(RevokeDemo.class);

    /**
     * revoke token user id
     */
    private static final String UID = "uidDemo";

    public static void main(String[] args) {

        init();

        try {
            AGCAuth.getInstance(AUTH_CLIENT_NAME).revokeRefreshTokens(UID);
        } catch (AGCAuthException e) {
            // record error log or throw exception
        }
    }
}
