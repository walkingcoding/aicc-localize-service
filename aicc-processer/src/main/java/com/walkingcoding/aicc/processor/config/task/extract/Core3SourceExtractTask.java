package com.walkingcoding.aicc.processor.config.task.extract;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;
import com.walkingcoding.aicc.processor.util.ImageUtils;
import com.walkingcoding.aicc.swftools.SwfDecompilerUtils;
import com.walkingcoding.aicc.swftools.exporter.ExporterType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * core3.swf提取任务
 *
 * @author songhuiqing
 */
public class Core3SourceExtractTask extends ProcessTask {

    private String core3FilePath = "inchen/core3.swf";
    private String outputDirectory = "image";


    public Core3SourceExtractTask() {
        super(ProcessTaskType.CORE3_SOURCE_EXTRACT, false);
    }

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) {
        //TODO core3.swf 处理
        File core3File = new File(task.getOriginFile(), core3FilePath);

        File imageDir = new File(super.getCourseRoot(task, globalConfig), outputDirectory);
        SwfDecompilerUtils.export(
                new ExporterType[]{ExporterType.image, ExporterType.sprite},
                imageDir.getPath(),
                core3File);
        // 处理图片，将小图片删除，并将所有目录中的图片放在同一个目录中
        List<File> imageList = ImageUtils.filterImages(imageDir.getPath(), image -> image.getWidth() >= 500 && image.getHeight() >= 500);
        // 拷贝图片到最外层目录
        try {
            for (File image : imageList) {
                Files.copy(image.toPath(), Paths.get(imageDir.getPath(), image.getName()));
            }
            // 删除原始图片？
            File swfImagesDir = new File(imageDir, "images");
            File swfSpritesDir = new File(imageDir, "sprites");
            Files.deleteIfExists(swfImagesDir.toPath());
            Files.deleteIfExists(swfSpritesDir.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public String outputDirectory() {
        return outputDirectory;
    }
}
