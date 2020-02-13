package com.walkingcoding.aicc.processor.config;

import com.walkingcoding.aicc.processor.config.task.Core3SourceExtractTask;
import com.walkingcoding.aicc.processor.config.task.FileSourceExtractTask;
import com.walkingcoding.aicc.processor.config.task.PptSwfSourceExtractTask;
import com.walkingcoding.aicc.processor.constant.TaskConstants;
import com.walkingcoding.aicc.processor.enums.TaskState;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理任务
 *
 * @author songhuiqing
 */
@Data
public class Task {
    /**
     * 任务ID，为源文件aicc目录名称
     */
    private String taskId;

    /**
     * 源文件
     */
    private File originFile;

    /**
     * 任务所处阶段，包括：素材抽取阶段、内容转换阶段、课件生产阶段
     */
    private String stage;

    /**
     * 任务状态
     */
    private TaskState taskState;

    /**
     * 子任务列表
     */
    private List<ProcessTask> processTasks;


    public Task() {
    }

    /**
     * 任务构造器
     *
     * @param taskId     任务ID，为单个课件目录名称
     * @param originFile 课件文件
     */
    public Task(String taskId, File originFile) {
        this.taskId = taskId;
        this.originFile = originFile;
        this.stage = TaskConstants.SOURCE_EXTRACT_STAGE;
        this.taskState = TaskState.WAITING;
        this.processTasks = new ArrayList<>(8);
        addProcessTasks();
    }

    /**
     * 添加子任务
     */
    private void addProcessTasks() {
        // 文件拷贝子任务
        this.processTasks.add(new FileSourceExtractTask());
        // core3.swf内容抽取子任务
        this.processTasks.add(new Core3SourceExtractTask());
        // ppt内容抽取子任务
        this.processTasks.add(new PptSwfSourceExtractTask());
    }

    public void printProcessTasksStateAndUpdateTaskState() {
        // 状态打印并更新任务状态
        boolean finished = true;
        String tpl = "任务: %s, %s, 详细状态: [%s]";
        StringBuilder detailState = new StringBuilder();
        for (ProcessTask processTask : this.processTasks) {
            if (!processTask.finished()) {
                finished = false;
            }
            detailState.append(String.format("{%s=%s}", processTask.code.getName(), processTask.state.getName()));
        }
        if (finished) {
            this.setTaskState(TaskState.FINISH);
        }
        System.out.println(String.format(tpl, this.taskId, taskState.getName(), detailState));
    }


    /**
     * 任务完成
     *
     * @return
     */
    public boolean finished() {
        return TaskState.FINISH.equals(taskState);
    }
}
