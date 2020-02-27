package com.walkingcoding.aicc.processor.config;

import com.walkingcoding.aicc.processor.config.task.MergeTask;
import com.walkingcoding.aicc.processor.config.task.extract.Core3SourceExtractTask;
import com.walkingcoding.aicc.processor.config.task.extract.FileSourceExtractTask;
import com.walkingcoding.aicc.processor.config.task.extract.PptSwfSourceExtractTask;
import com.walkingcoding.aicc.processor.config.task.transcoding.*;
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
        /**第一阶段任务 - 素材抽取**/
        // 1. 文件拷贝子任务
        this.processTasks.add(new FileSourceExtractTask());
        // 2. core3.swf内容抽取子任务
        this.processTasks.add(new Core3SourceExtractTask());
        // 3. ppt内容抽取子任务
        this.processTasks.add(new PptSwfSourceExtractTask());
        /**第二阶段任务 转码**/

        // 1. 文件编码转换任务
        this.processTasks.add(new EncodingTranscodingTask());
        // 2. 媒体文件转码任务
        this.processTasks.add(new MediaTranscodingTask());
        // 3. 背景图转换
        this.processTasks.add(new BgImageTranscodingTask());
        // 4.封面图转换
        this.processTasks.add(new CoverImageTranscodingTask());
        // 5.ppt转换
        this.processTasks.add(new PptImageTranscodingTask());

        /**第三阶段任务 课件合并**/
        this.processTasks.add(new MergeTask());
    }

    public void printProcessTasksStateAndUpdateTaskState() {
        if (TaskState.FINISH.equals(this.taskState)) {
            return;
        }
        // 状态打印并更新任务状态
        boolean finished = true;
        boolean failture = false;
        String tpl = "%s %s [%s]";
        StringBuilder detailState = new StringBuilder();
        for (ProcessTask processTask : this.processTasks) {
            if (!processTask.finished()) {
                finished = false;
            }
            if(TaskState.FAILTURE.equals(processTask.getState())) {
                failture = true;
            }
            detailState.append(String.format("%s: %s, ", processTask.code.getName(), processTask.state.getName()));
        }
        if (finished) {
            this.setTaskState(TaskState.FINISH);
        }
        if(failture) {
            this.setTaskState(TaskState.FAILTURE);
        }
        System.out.println(String.format(tpl, this.taskId, taskState.getName(), detailState));
    }


    /**
     * 任务完成
     *
     * @return
     */
    public boolean finished() {
        return TaskState.FINISH.equals(taskState) || TaskState.FAILTURE.equals(taskState);
    }
}
