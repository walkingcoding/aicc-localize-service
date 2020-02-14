package com.walkingcoding.aicc.processor.enums;

import static com.walkingcoding.aicc.processor.constant.TaskConstants.SOURCE_EXTRACT_STAGE;
import static com.walkingcoding.aicc.processor.constant.TaskConstants.CONTENT_TRANSCODING_STAGE;
import static com.walkingcoding.aicc.processor.constant.TaskConstants.COURSE_MERGE_STAGE;

/**
 * 过程中任务枚举
 *
 * @author songhuiqing
 */
public enum ProcessTaskType {
    /**
     * core3.swf文件内容提取
     */
    CORE3_SOURCE_EXTRACT(SOURCE_EXTRACT_STAGE, "core3.swf文件提取"),

    /**
     * swf/*.swf文件内容提取
     */
    PPT_SOURCE_EXTRACT(SOURCE_EXTRACT_STAGE, "ppt文件提取"),

    /**
     * 课件中其他物理文件内容提取
     */
    FILE_SOURCE_EXTRACT(SOURCE_EXTRACT_STAGE, "音视频及core.cor文件提取"),

    /**
     * 音视频转码
     */
    MEDIA_TRANSCODING(CONTENT_TRANSCODING_STAGE, "音视频转码"),

    /**
     * 封面图片合成
     */
    COVER_IMAGE_TRANSCODING(CONTENT_TRANSCODING_STAGE, "封面图片生成"),

    /**
     * 背景图片合成
     */
    BG_IMAGE_TRANSCODING(CONTENT_TRANSCODING_STAGE, "背景图片生成"),

    /**
     * 文件转码
     */
    ENCODING_TRANSCODING(CONTENT_TRANSCODING_STAGE, "文件转码"),

    /**
     * 课件合并
     */
    MERGE(COURSE_MERGE_STAGE, "课件合并");


    private String stage;
    private String name;

    ProcessTaskType(String stage, String name) {
        this.stage = stage;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
