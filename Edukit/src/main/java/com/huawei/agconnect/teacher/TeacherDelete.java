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

package com.huawei.agconnect.teacher;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.CommonResponse;
import com.huawei.agconnect.server.edukit.teacher.impl.TeacherDeleteRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 新建状态的教师，不存在在架版本或者曾经在架版本，会直接删除教师数据
 * 审核中状态的教师，不允许删除
 * 可编辑状态的教师，删除操作会提交审核，审核通过后予以删除
 *
 * 
 * @since 2020-11-24
 */
public class TeacherDelete {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherCreate.class);

    public static void main(String[] args) {
        /**
         * 请求客户端名称，自定义
         */
        String clientName = "edukit";

        try {
            AGCClient.initialize(clientName,
                AGCParameter.builder()
                    .setCredential(CredentialParser
                        .toCredential(TeacherDelete.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        TeacherDeleteRequest teacherDeleteRequest =
            AGCEdukit.getInstance(clientName).getTeacherDeleteRequest("457048347694399488", "这里填写删除的原因");

        CommonResponse teacherDeleteResponse = teacherDeleteRequest.delete();

        if (teacherDeleteResponse.getResult().getResultCode() == CommonErrorCode.SUCCESS) {
            LOGGER.info("Teacher deleted successfully.");
        } else {
            LOGGER.info("Delete teacher failed.");
        }
    }
}
