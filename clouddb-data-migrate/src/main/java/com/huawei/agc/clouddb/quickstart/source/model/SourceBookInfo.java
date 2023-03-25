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
import com.huawei.agc.clouddb.quickstart.target.model.BookInfo;

import java.util.Date;

/**
 * example model of book
 *
 * @since 2022-12-30
 */
@Table(source = "book", target = "BookInfo", targetClass = BookInfo.class)
public class SourceBookInfo extends AbstractSourceModel {
    @Column(source = "id", target = "id", isPrimaryKey = true)
    private Integer id;

    @Column(source = "book_name", target = "bookName")
    private String bookName;

    @Column(source = "author", target = "author")
    private String author;

    @Column(source = "price", target = "price")
    private Double price;

    @Column(source = "publisher", target = "publisher")
    private String publisher;

    @Column(source = "publish_time", target = "publishTime")
    private Date publishTime;

    @Column(source = "shadow_flag", target = "shadowFlag")
    private Boolean shadowFlag;
}
