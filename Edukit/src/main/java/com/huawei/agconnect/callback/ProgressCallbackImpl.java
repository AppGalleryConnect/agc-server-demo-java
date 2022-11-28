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

package com.huawei.agconnect.callback;

import com.huawei.agconnect.server.edukit.common.model.ProgressCallback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件上传进度回调通知实现示例
 *
 * @since 2020-07-14
 */
public class ProgressCallbackImpl implements ProgressCallback {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProgressCallbackImpl.class);

    @Override
    public void onProgressChanged(double v) {
        System.out.printf("file upload progress %.2f %%", v * 100);
        LOGGER.info("File upload notify, current progress is {}", v);
    }
}
