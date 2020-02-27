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
        File resourceRoot = getResourceRoot(task, globalConfig);
        File distRoot = getDistRoot(task, globalConfig);
        if (!taskSuccessExecuted(resourceRoot, distRoot)) {
            try {
                executeTask(task, globalConfig);
                if(TaskState.PROCESSING.equals(this.state)) {
                    this.state = TaskState.FINISH;
                }
            } catch (Exception e) {
                e.printStackTrace();
                this.state = TaskState.FAILTURE;
                this.errorMessage = e.getMessage();
            }
        }else {
            this.state = TaskState.FINISH;
        }
    }

    public boolean finished() {

        return TaskState.FINISH.equals(state) || TaskState.FAILTURE.equals(state);
    }

    /**
     * 课件输出资源文件目录
     *
     * @param task         任务对象
     * @param globalConfig 全局配置
     * @return
     */
    public File getResourceRoot(Task task, GlobalConfig globalConfig) {
        return Paths.get(globalConfig.getWorkspace(), globalConfig.getResourceFileFolder(), task.getTaskId()).toFile();
    }

    /**
     * 获取课件输出目录
     *
     * @param task         任务
     * @param globalConfig 全局配置
     * @return
     */
    public File getDistRoot(Task task, GlobalConfig globalConfig) {
        return Paths.get(globalConfig.getWorkspace(), globalConfig.getDistFileFolder(), task.getTaskId()).toFile();
    }

    /**
     * 获取课件模版目录
     *
     * @param task         任务
     * @param globalConfig 全局配置
     * @return
     */
    public File getTemplateDir(Task task, GlobalConfig globalConfig) {
        return Paths.get(globalConfig.getWorkspace(), globalConfig.getTemplateFileFolder()).toFile();
    }


    /**
     * 任务是否已经成功执行过
     *
     * @param resourceRoot 课件资源文件路名
     * @param distRoot     课件目标文件路径
     * @return
     */
    protected abstract boolean taskSuccessExecuted(File resourceRoot, File distRoot);

    /**
     * 执行子任务
     *
     * @param task         任务对象
     * @param globalConfig 全局配置
     * @throws Exception
     */
    public abstract void executeTask(Task task, GlobalConfig globalConfig) throws Exception;


    public TaskState getState() {
        return state;
    }

    public ProcessTaskType getCode() {
        return code;
    }
}
