package com.walkingcoding.aicc.processor.config.task.extract;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 课件中文件抽取子任务 <br>
 * <p>
 * 抽取的文件包括： <br>
 * 1. inchen/media/video.wmv ->  file/video.wmv  <br>
 * 2. inchen/media/audio.wma ->  file/audio.wma  <br>
 * 3. core/core.cor          ->  file/core.cor   <br>
 *
 * @author songhuiqing
 */
public class FileSourceExtractTask extends ProcessTask {

    public FileSourceExtractTask() {
        super(ProcessTaskType.FILE_SOURCE_EXTRACT, true);
    }

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) throws Exception {

        // 文件处理
        File videoFile = new File(task.getOriginFile(), "inchen/media/video.wmv");
        File audioFile = new File(task.getOriginFile(), "inchen/media/audio.wma");
        File coreFile = new File(task.getOriginFile(), "core/core.cor");

        File outputDir = Paths.get(globalConfig.getWorkspace(), globalConfig.getResourceFileFolder(), task.getTaskId()).toFile();
        // 文件复制
        File copyVideoFile = new File(outputDir, "file/video.wmv");
        File copyAudioFile = new File(outputDir, "file/audio.wma");
        File copyCoreFile = new File(outputDir, "file/core.cor");
        // 创建上级目录
        copyVideoFile.getParentFile().mkdirs();

        if (videoFile.exists() && !copyVideoFile.exists()) {
            Files.copy(videoFile.toPath(), copyVideoFile.toPath());
        }
        if (audioFile.exists() && !copyAudioFile.exists()) {
            Files.copy(audioFile.toPath(), copyAudioFile.toPath());
        }
        if (coreFile.exists() && !copyCoreFile.exists()) {
            Files.copy(coreFile.toPath(), copyCoreFile.toPath());
        }

    }

    @Override
    public String outputDirectory() {
        return "file";
    }
}
