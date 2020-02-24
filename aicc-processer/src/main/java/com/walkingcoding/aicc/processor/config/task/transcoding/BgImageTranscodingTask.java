package com.walkingcoding.aicc.processor.config.task.transcoding;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;
import com.walkingcoding.aicc.processor.enums.TaskState;
import com.walkingcoding.aicc.processor.util.Im4Java;
import org.apache.commons.io.FilenameUtils;

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
        File[] files = new File(resourceRoot, sourceCoverImagePath).listFiles(((dir, name) -> isBgImage(name)));
        if (files == null || files.length == 0) {
            super.state = TaskState.FAILTURE;
            super.errorMessage = "背景图片不存在";
            return;
        }
        try {
            // 去白边，并转换成jpg格式
            new Im4Java().trim(files[0].getPath(), targetDir.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 背景图片规则，文件名为b，不限制类型
     *
     * @param fileName
     * @return
     */
    private boolean isBgImage(String fileName) {
        return "b".equalsIgnoreCase(FilenameUtils.getBaseName(fileName));
    }

    @Override
    public String outputDirectory() {
        return bgImagePath;
    }
}
