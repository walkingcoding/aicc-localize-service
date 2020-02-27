package com.walkingcoding.aicc.processor.config.task.transcoding;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;
import com.walkingcoding.aicc.processor.enums.TaskState;
import com.walkingcoding.aicc.processor.util.Im4Java;
import org.apache.commons.io.FilenameUtils;
import org.im4java.core.IM4JavaException;

import java.io.File;
import java.io.IOException;

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
    private String coverImagePath = "transcoding/core/index-bg.jpg";

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) throws InterruptedException, IOException, IM4JavaException {

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
        if(overImage == null) {
            new Im4Java().trim(coverImage.getPath(), targetFile.getPath());
        } else {
            // 如果存在上层图片，则覆盖上层图片到封面图上
            new Im4Java().trim(coverImage.getPath(), coverImage.getPath());
            new Im4Java().over(overImage.getPath(), coverImage.getPath(), targetFile.getPath());

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
    protected boolean taskSuccessExecuted(File resourceRoot, File distRoot) {
        return new File(resourceRoot, coverImagePath).exists();
    }
}
