package com.walkingcoding.aicc.processor.config.task;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;

/**
 * 课件合并任务
 *
 * @author songhuiqing
 */
public class MergeTask extends ProcessTask {
    public MergeTask() {
        super(ProcessTaskType.MERGE, false);
    }

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) {
        // TODO 课件合并任务
    }

    @Override
    public String outputDirectory() {
        return null;
    }
}
