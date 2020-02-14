package com.walkingcoding.aicc.processor.config.task.transcoding;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;

/**
 * 封面图转码任务
 *
 * @author songhuiqing
 */
public class CoverImageTranscodingTask extends ProcessTask {
    public CoverImageTranscodingTask() {
        super(ProcessTaskType.COVER_IMAGE_TRANSCODING, false);
    }

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) {

        // TODO 封面图转码任务
    }

    @Override
    public String outputDirectory() {
        return null;
    }
}
