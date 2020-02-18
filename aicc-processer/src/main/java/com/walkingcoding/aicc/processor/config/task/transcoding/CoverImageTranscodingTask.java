package com.walkingcoding.aicc.processor.config.task.transcoding;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 封面图转码任务
 *
 * @author songhuiqing
 */
public class CoverImageTranscodingTask extends ProcessTask {
    public CoverImageTranscodingTask() {
        super(ProcessTaskType.COVER_IMAGE_TRANSCODING, false);
    }

    private String sourceCoverImagePath = "image";
    private String coverImagePath = "transcoding/core";

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) {

        File resourceRoot = getResourceRoot(task, globalConfig);
        File targetDir = new File(resourceRoot.getPath(), coverImagePath);
        // TODO 封面图转码任务
        File[] files = new File(resourceRoot, sourceCoverImagePath).listFiles(((dir, name) -> name.contains("index_bg")));
        if (files != null && files.length != 0) {
            try {
                Files.copy(files[0].toPath(), Paths.get(targetDir.getPath(), files[0].getName()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String outputDirectory() {
        return coverImagePath + "/index_bg.jpg";
    }
}
