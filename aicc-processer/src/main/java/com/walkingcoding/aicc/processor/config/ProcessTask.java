package com.walkingcoding.aicc.processor.config;

import com.walkingcoding.aicc.processor.enums.ProcessTaskType;
import com.walkingcoding.aicc.processor.enums.TaskState;

import java.io.File;
import java.nio.file.Paths;

/**
 * 过程中任务，为Task的子对象
 *
 * @author songhuiqing
 */
public abstract class ProcessTask {

    /**
     * 子任务类型
     */
    protected ProcessTaskType code;

    /**
     * 是否可并发执行，咱不实现
     */
    protected boolean isParallel;

    /**
     * 子任务状态
     */
    protected TaskState state;

    /**
     * 错误信息
     */
    protected String errorMessage;


    public ProcessTask() {
    }

    public ProcessTask(ProcessTaskType code, boolean isParallel) {
        this.code = code;
        this.isParallel = isParallel;
        this.state = TaskState.WAITING;
        this.errorMessage = "";
    }


    /**
     * 处理任务
     *
     * @param task         任务对象
     * @param globalConfig 全局配置
     */
    public void execute(Task task, GlobalConfig globalConfig) {
        this.state = TaskState.PROCESSING;
        task.setTaskState(TaskState.PROCESSING);
        if (!taskSuccessExecuted(task, globalConfig)) {
            try {
                executeTask(task, globalConfig);
            } catch (Exception e) {
                e.printStackTrace();
                this.state = TaskState.FAILTURE;
                this.errorMessage = e.getMessage();
            }
        }

        this.state = TaskState.FINISH;
    }

    public boolean finished() {

        return TaskState.FINISH.equals(state) || TaskState.FAILTURE.equals(state);
    }

    /**
     * 输出课件目录
     *
     * @param task         任务对象
     * @param globalConfig 全局配置
     * @return
     */
    public File getCourseRoot(Task task, GlobalConfig globalConfig) {
        return Paths.get(globalConfig.getWorkspace(), globalConfig.getResourceFileFolder(), task.getTaskId()).toFile();
    }

    public File getResourceRoot(Task task, GlobalConfig globalConfig) {
        return Paths.get(globalConfig.getWorkspace(), globalConfig.getResourceFileFolder(), task.getTaskId()).toFile();
    }

    public File getDistRoot(Task task, GlobalConfig globalConfig) {
        return Paths.get(globalConfig.getWorkspace(), globalConfig.getDistFileFolder(), task.getTaskId()).toFile();
    }

    /**
     * 输出的相对目录
     *
     * @return
     */
    public abstract String outputDirectory();

    /**
     * 任务是否已经成功执行过
     *
     * @param task         任务对象
     * @param globalConfig 全局配置
     * @return
     */
    protected boolean taskSuccessExecuted(Task task, GlobalConfig globalConfig) {
        File courseRoot = getCourseRoot(task, globalConfig);
        return new File(courseRoot, outputDirectory()).exists();
    }

    /**
     * 执行子任务
     *
     * @param task         任务对象
     * @param globalConfig 全局配置
     * @throws Exception
     */
    public abstract void executeTask(Task task, GlobalConfig globalConfig) throws Exception;


}
