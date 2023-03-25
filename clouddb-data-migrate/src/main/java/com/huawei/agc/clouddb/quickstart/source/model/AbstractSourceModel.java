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

package com.huawei.agc.clouddb.quickstart.source.model;

import java.lang.reflect.Field;

/**
 * Base class for model objects, used to constrain model types and behavior
 *
 * @since 2022-12-30
 */
public abstract class AbstractSourceModel {
    /**
     * get the value of the field
     *
     * @param field filed object
     * @return value of the field
     * @throws IllegalAccessException exception
     */
    public Object getValue(Field field) throws IllegalAccessException {
        return field.get(this);
    }
}
