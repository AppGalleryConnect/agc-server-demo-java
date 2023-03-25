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

import com.huawei.agc.clouddb.quickstart.annotation.Column;
import com.huawei.agc.clouddb.quickstart.annotation.Table;
import com.huawei.agc.clouddb.quickstart.target.model.alltypetable;

/**
 * example model of alltypetable
 *
 * @since 2022-12-30
 */
@Table(source = "alltypetable", target = "alltypetable", targetClass = alltypetable.class)
public class SourceAllTypeTable extends AbstractSourceModel {
    @Column(source = "id", target = "ID", isPrimaryKey = true)
    private Integer id;

    @Column(source = "integert", target = "IntegerT")
    private Integer integerT;

    @Column(source = "doublet", target = "DoubleT")
    private Double doubleT;

    @Column(source = "floatt", target = "FloatT")
    private Float floatT;

    @Column(source = "shortt", target = "ShortT")
    private Short shortT;

    @Column(source = "longt", target = "LongT")
    private Long longT;

    @Column(source = "stringt", target = "StringT")
    private String stringT;

    @Column(source = "booleant", target = "BooleanT")
    private Boolean booleanT;

    @Column(source = "bytearrayt", target = "ByteArrayT")
    private byte[] bytearrayT;

    @Column(source = "textt", target = "TextT")
    private String textT;
}
