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

import com.huawei.agconnect.server.auth.entity.AuthAccessToken;
import com.huawei.agconnect.server.auth.exception.AGCAuthException;
import com.huawei.agconnect.server.auth.service.AGCAuth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * verify access token demo
 *
 * @since 2020-10-19
 */
public class VerifyAccessTokenDemo extends AbstractDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerifyAccessTokenDemo.class);

    /**
     * token need verify
     */
    private static final String ACCESS_TOKEN = "access_token";

    public static void main(String[] args) {

        init();

        AuthAccessToken authAccessToken = null;

        try {
            authAccessToken = AGCAuth.getInstance(AUTH_CLIENT_NAME).verifyAccessToken(ACCESS_TOKEN, true);
        } catch (AGCAuthException e) {
            // record error log or throw exception
        }

        if (authAccessToken != null) {
            LOGGER.info("verify token success");
            // get user info from AuthAccessToken object, include name/picture/phone/email and so on
        } else {
            LOGGER.error("verify token failed");
        }
    }
}
