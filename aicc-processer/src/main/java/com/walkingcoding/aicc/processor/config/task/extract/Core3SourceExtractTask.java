package com.walkingcoding.aicc.processor.config.task.extract;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;
import com.walkingcoding.aicc.swftools.SwfDecompilerUtils;
import com.walkingcoding.aicc.swftools.exporter.ExporterType;

import java.io.File;

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

        File outputDir = super.getOutPutDirectory(task, globalConfig);
        SwfDecompilerUtils.export(
                new ExporterType[]{ExporterType.image, ExporterType.sprite},
                new File(outputDir, outputDirectory).getPath(),
                core3File);
    }
}
