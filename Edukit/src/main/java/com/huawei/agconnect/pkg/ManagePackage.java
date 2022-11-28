/*
 * Copyright 2021. Huawei Technologies Co., Ltd. All rights reserved.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.huawei.agconnect.pkg;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.pkg.impl.PackageManageRequest;
import com.huawei.agconnect.server.edukit.pkg.model.ManagePkgStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 管理会员包
 *
 * 
 * @since 2021-03-29
 */
public class ManagePackage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagePackage.class);

    public static void main(String[] args) {
        /**
         * 用户名，自定义
         */
        String clientName = "edukit";
        /**
         * 初始化
         */
        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(ManagePackage.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        String pkgId = "pkg_599168332478209024";
        ManagePkgStatus status = ManagePkgStatus.builder()
            .actionSet(CommonConstants.PkgManageAction.SUBMIT_FOR_DISABLING)
            .removalReasonSet("管理会员包")
            .build();
        PackageManageRequest packageManageRequest =
            AGCEdukit.getInstance(clientName).getPackageManageRequest(pkgId, status);
        CommonResponse commonResponse = packageManageRequest.managePkg();
        LOGGER.info("Manage package response:{}", commonResponse);
        if (CommonErrorCode.SUCCESS != commonResponse.getResult().getResultCode()) {
            LOGGER.error("manage pkg failed.");
        } else {
            LOGGER.info("manage pkg success.");
        }
    }
}
