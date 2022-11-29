/*
 * Copyright (c) Huawei Technologies Co., Ltd. 2021-2021. All rights reserved.
 */

package com.huawei.agconnect.album;

import com.huawei.agconnect.server.commons.AGCClient;
import com.huawei.agconnect.server.commons.AGCParameter;
import com.huawei.agconnect.server.commons.credential.CredentialParser;
import com.huawei.agconnect.server.commons.exception.AGCException;
import com.huawei.agconnect.server.edukit.AGCEdukit;
import com.huawei.agconnect.server.edukit.album.impl.AlbumUpdateRequest;
import com.huawei.agconnect.server.edukit.album.model.Album;
import com.huawei.agconnect.server.edukit.album.model.AlbumCourse;
import com.huawei.agconnect.server.edukit.album.model.AlbumEditLocalizedData;
import com.huawei.agconnect.server.edukit.album.model.AlbumEditMetaData;
import com.huawei.agconnect.server.edukit.album.resp.UpdateAlbumResponse;
import com.huawei.agconnect.server.edukit.common.constant.CommonConstants;
import com.huawei.agconnect.server.edukit.common.errorcode.CommonErrorCode;
import com.huawei.agconnect.server.edukit.common.model.ImageFileInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 更新专辑
 *
 * @author lWX832783
 * @since 2021-03-29
 */
public class UpdateAlbum {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpdateAlbum.class);

    /**
     * 文件上传本地路径
     */
    private static String path = "D:\\education\\";

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
                        .toCredential(UpdateAlbum.class.getClassLoader().getResource("credential.json").getPath()))
                    .build());
        } catch (AGCException e) {
            // 用户可以做记录日志，抛异常等处理
            return;
        }

        AlbumUpdateRequest albumUpdateRequest = AGCEdukit.getInstance(clientName).getAlbumUpdateRequest(buildAlbum());
        UpdateAlbumResponse updateAlbumResponse = albumUpdateRequest.updateAlbum().join();
        LOGGER.info("Update album response:{}", updateAlbumResponse);
        if (CommonErrorCode.SUCCESS != updateAlbumResponse.getResult().getResultCode()) {
            LOGGER.error("update album failed.");
        } else {
            LOGGER.info("update album success.");
        }
    }

    private static Album buildAlbum() {
        AlbumEditMetaData editMetaData = AlbumEditMetaData.builder()
            .recommendFlagSet(false)
            .defaultLangSet("zh-CN")
            .nameSet("SDK专辑")
            .isCombinedAlbumSet(false)
            .build();
        ImageFileInfo cover = ImageFileInfo.builder()
            // 专辑封面 jpg、png格式，图片分辨率为1280*720像素(宽*高)，单张图片最大为2MB
            .pathSet(path + "albumcover.jpg")
            .resourceTypeSet(CommonConstants.ResourceType.COURSE_PACKAGE_ALBUM_HORIZONTAL_COVER)
            .build();
        ImageFileInfo landCover = ImageFileInfo.builder()
            // 专辑封面 jpg、png格式，图片分辨率为1080*360像素(宽*高)，单张图片最大为2MB
            .pathSet(path + "albumlandcover.PNG")
            .resourceTypeSet(CommonConstants.ResourceType.ALBUM_LANDSCAPE_COVER)
            .build();
        AlbumEditLocalizedData editLocalizedData = AlbumEditLocalizedData.builder()
            .coverImgFileInfoSet(cover)
            .landscapeCoverImgFileInfoSet(landCover)
            .nameSet("SDK专辑")
            .fullDescriptionSet("sdk的专辑描述")
            .languageSet("zh-CN")
            .build();
        List<AlbumEditLocalizedData> albumEditLocalizedDatas = new ArrayList<>();
        albumEditLocalizedDatas.add(editLocalizedData);
        AlbumCourse albumCourse = AlbumCourse.builder().contentIdSet("241686060218712064").operTypeSet(1).build();
        List<AlbumCourse> albumCourses = new ArrayList<>();
        albumCourses.add(albumCourse);
        // SubAlbum subAlbum = SubAlbum.builder().albumIdSet(111L).operTypeSet(1).build();
        // List<SubAlbum> subAlbumList = new ArrayList<>();
        // subAlbumList.add(subAlbum);
        Album album = Album.builder()
            .actionSet(2)
            .albumIdSet(599005457503673344L)
            .metaDataSet(editMetaData)
            .localizedDataSet(albumEditLocalizedDatas)
            .courseListSet(albumCourses)
            // .subAlbumListSet(subAlbumList)
            .build();
        return album;
    }
}
