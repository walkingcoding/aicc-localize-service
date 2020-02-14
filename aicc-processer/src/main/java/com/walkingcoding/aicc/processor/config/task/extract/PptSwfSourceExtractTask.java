package com.walkingcoding.aicc.processor.config.task.extract;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;
import com.walkingcoding.aicc.swftools.SwfDecompilerUtils;
import com.walkingcoding.aicc.swftools.exporter.ExporterType;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * swf/*.swf 提取任务
 *
 * @author songhuiqing
 */
public class PptSwfSourceExtractTask extends ProcessTask {

    private String pptFileDirectory = "inchen/swf";
    private String outputDirectory = "ppt";


    public PptSwfSourceExtractTask() {
        super(ProcessTaskType.PPT_SOURCE_EXTRACT, false);
    }

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) {

        String outputDir = super.getCourseRoot(task, globalConfig).getPath();

        // 获取swf列表
        File directory = new File(task.getOriginFile(), pptFileDirectory);
        File[] files = directory.listFiles((dir, name) -> name.endsWith(".swf"));
        Arrays.stream(files).forEach(swf -> {
            SwfDecompilerUtils.export(ExporterType.frame,
                    Paths.get(outputDir, outputDirectory, FilenameUtils.getBaseName(swf.getName())).toString(),
                    swf);
        });
    }

    @Override
    public String outputDirectory() {
        return outputDirectory;
    }
}
