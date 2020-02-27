package com.walkingcoding.aicc.processor.config.task.transcoding;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;
import com.walkingcoding.aicc.processor.enums.TaskState;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 文件转码任务
 *
 * @author songhuiqing
 */
public class EncodingTranscodingTask extends ProcessTask {

    private String sourceFilePath = "file/core.cor";
    private String transcodingFilePath = "transcoding/core/core.cor";

    public EncodingTranscodingTask() {
        super(ProcessTaskType.ENCODING_TRANSCODING, false);
    }

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) throws Exception {

        File resourceRoot = getResourceRoot(task, globalConfig);

        List<String> lines = IOUtils.readLines(new FileInputStream(new File(resourceRoot, sourceFilePath)), UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.contains("<?xml") && line.contains("encoding")) {
                lines.set(i, "<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                break;
            }
        }
        File output = new File(resourceRoot, transcodingFilePath);
        File outputDir = output.getParentFile();
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        IOUtils.writeLines(lines, IOUtils.LINE_SEPARATOR_UNIX, new FileOutputStream(output), UTF_8);


    }

    @Override
    protected boolean taskSuccessExecuted(File resourceRoot, File distRoot) {
        return new File(resourceRoot, transcodingFilePath).exists();
    }
}
