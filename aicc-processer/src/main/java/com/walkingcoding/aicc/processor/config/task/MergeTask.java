package com.walkingcoding.aicc.processor.config.task;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * 课件合并任务
 *
 * @author songhuiqing
 */
public class MergeTask extends ProcessTask {

    private String transcodingPath = "transcoding";

    public MergeTask() {
        super(ProcessTaskType.MERGE, false);
    }

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) throws IOException {
        // TODO 课件合并任务
        // 获取课件模版，替换文件
        File templateDir = super.getTemplateDir(task, globalConfig);
        // 目标文件
        File distRoot = super.getDistRoot(task, globalConfig);
        File resRoot = super.getResourceRoot(task, globalConfig);
        FileUtils.copyDirectory(templateDir, distRoot);
        File[] files = new File(resRoot, transcodingPath).listFiles();
        for (File file : files) {
            File distFile = new File(distRoot, file.getName());
            if (file.isDirectory()) {
                FileUtils.copyDirectory(file, distFile);
            } else {
                FileUtils.copyFile(file, distFile);
            }

        }

    }

    @Override
    protected boolean taskSuccessExecuted(File resourceRoot, File distRoot) {
        return distRoot.exists();
    }
}
