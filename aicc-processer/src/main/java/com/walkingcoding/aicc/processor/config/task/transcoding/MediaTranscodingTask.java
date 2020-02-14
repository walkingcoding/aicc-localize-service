package com.walkingcoding.aicc.processor.config.task.transcoding;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;

/**
 * 音视频转码任务
 *
 * @author songhuiqing
 */
public class MediaTranscodingTask extends ProcessTask {
    public MediaTranscodingTask() {
        super(ProcessTaskType.MEDIA_TRANSCODING, false);
    }

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) {

        // TODO 音视频转码
    }

    @Override
    public String outputDirectory() {
        return null;
    }
}
