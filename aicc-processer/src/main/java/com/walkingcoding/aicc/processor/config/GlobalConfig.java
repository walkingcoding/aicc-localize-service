package com.walkingcoding.aicc.processor.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 全局配置
 *
 * @author songhuiqing
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GlobalConfig {


    /**
     * 工作空间目录
     */
    private String workspace;
    /**
     * 模版文件目录
     */
    private String templateFileFolder;

    /**
     * 生成资源文件目录
     */
    private String resourceFileFolder;

    /**
     * 课件文件输出目录
     */
    private String distFileFolder;

    /**
     * 并发执行任务数量，默认3个
     */
    private int parallelSize;


}
