package com.walkingcoding.aicc.processor.enums;

/**
 * 任务状态
 *
 * @author songhuiqing
 */
public enum TaskState {

    /**
     * 等待中
     */
    WAITING("等待中"),
    /**
     * 处理中
     */
    PROCESSING("处理中"),
    /**
     * 已完成
     */
    FINISH("已完成"),
    /**
     * 失败
     */
    FAILTURE("失败");

    private String name;

    TaskState(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
