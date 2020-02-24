package com.walkingcoding.aicc.processor.config.task.transcoding;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;
import com.walkingcoding.aicc.processor.util.Im4Java;

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
    private String coverImagePath = "transcoding/core/index_bg.jpg";

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) {

        File resourceRoot = getResourceRoot(task, globalConfig);
        File targetDir = new File(resourceRoot.getPath(), coverImagePath);
        // TODO 封面图转码任务
        File[] files = new File(resourceRoot, sourceCoverImagePath).listFiles(((dir, name) -> name.contains("index_bg")));
        if (files != null && files.length != 0) {
            try {
                new Im4Java().trim(files[0].getPath(), targetDir.getPath());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public String outputDirectory() {
        return coverImagePath;
    }
}
