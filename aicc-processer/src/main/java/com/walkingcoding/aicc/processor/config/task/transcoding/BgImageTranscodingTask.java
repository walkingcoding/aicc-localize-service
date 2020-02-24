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
 * 背景图转码任务
 *
 * @author songhuiqing
 */
public class BgImageTranscodingTask extends ProcessTask {
    public BgImageTranscodingTask() {
        super(ProcessTaskType.BG_IMAGE_TRANSCODING, false);
    }

    private String sourceCoverImagePath = "image";
    private String bgImagePath = "transcoding/core/frame_bg.jpg";

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) {
        // TODO 背景图转码任务
        File resourceRoot = getResourceRoot(task, globalConfig);
        File targetDir = new File(resourceRoot.getPath(), bgImagePath);
        File[] files = new File(resourceRoot, sourceCoverImagePath).listFiles(((dir, name) -> name.contains("frame_bg")));
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
        return bgImagePath;
    }
}
