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

package com.huawei.agc.clouddb.quickstart.auth;

import com.huawei.agconnect.server.clouddb.service.ClientNameEnum;
import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.constants.Constants;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;

/**
 * init authenticate credential
 *
 * @since 2022-12-30
 */
public class AuthenticateTool {
    /**
     * init authenticate credential
     *
     * @throws AGCException while init authentication credential
     */
    public static void init() throws AGCException {
        AGCClient.initialize(ClientNameEnum.CLOUDDB_CN.getClientName(),
            AGCParameter.builder().setCredential(
                CredentialParser.toCredential("./config/agc-apiclient.json"))
                    .build(),
            Constants.Region.REGION_CN);
    }
}
