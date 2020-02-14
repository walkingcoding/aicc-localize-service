package com.walkingcoding.aicc.processor.config.task.transcoding;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;

/**
 * 背景图转码任务
 *
 * @author songhuiqing
 */
public class BgImageTranscodingTask extends ProcessTask {
    public BgImageTranscodingTask() {
        super(ProcessTaskType.BG_IMAGE_TRANSCODING, false);
    }

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) {
        // TODO 背景图转码任务
    }

    @Override
    public String outputDirectory() {
        return null;
    }
}
