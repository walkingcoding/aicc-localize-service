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
        File targetFile = new File(resourceRoot.getPath(), coverImagePath);
        // 查找封面图或封面图上层图片
        File[] files = new File(resourceRoot, sourceCoverImagePath).listFiles();
        File coverImage = null;
        File overImage = null;
        for(File file : files) {
            if (isCoverImage(file.getName())) {
                coverImage = file;
            }
            if (isOverImage(file.getName())) {
                overImage = file;
            }
        }
        // 每门课件必须有封面图
        if(coverImage == null) {
            super.state = TaskState.FAILTURE;
            super.errorMessage = "封面图片不存在";
            return;
        }
        try {
            // 先将封面图去除白边
            new Im4Java().trim(files[0].getPath(), targetFile.getPath());
            // 如果存在上层图片，则覆盖上层图片到封面图上
            if(overImage != null) {
                new Im4Java().over(overImage.getPath(), targetFile.getPath(), targetFile.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 封面图规则，文件名为c，不限制类型
     *
     * @param fileName
     * @return
     */
    private boolean isCoverImage(String fileName) {
        return "c".equalsIgnoreCase(FilenameUtils.getBaseName(fileName));
    }

    /**
     * 封面图上层图片规则，文件名为o，不限制类型
     *
     * @param fileName
     * @return
     */
    private boolean isOverImage(String fileName) {
        return "o".equalsIgnoreCase(FilenameUtils.getBaseName(fileName));
    }

    @Override
    public String outputDirectory() {
        return coverImagePath;
    }
}
