package com.walkingcoding.aicc.processor.handler;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.Task;

import java.util.List;


/**
 * 课件处理接口
 *
 * @author songhuiqing
 */
public interface CourseHandler {

    /**
     * 课件处理
     *
     * @param tasks        任务清单
     * @param globalConfig 全局配置对象
     */
    void execute(List<Task> tasks, GlobalConfig globalConfig);
}
