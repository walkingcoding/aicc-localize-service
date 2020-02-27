package com.walkingcoding.aicc.processor.config.task.transcoding;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;
import com.walkingcoding.aicc.processor.util.Im4Java;
import org.apache.commons.io.FilenameUtils;
import org.im4java.core.IM4JavaException;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * PPT 图片生成
 *
 * @author songhuiqing
 */
public class PptImageTranscodingTask extends ProcessTask {
    public PptImageTranscodingTask() {
        super(ProcessTaskType.PPT_IMAGE_TRANSCODING, false);
    }

    private String sourcePath = "ppt";
    private String targetPath = "transcoding/inchen/ppt/";

    private PptComparator pptComparator = new PptComparator();

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) throws InterruptedException, IOException, IM4JavaException {
        // ppt处理，将资源目录/ppt下ppt部分拷贝到转码目录中，并统一设置成jpg格式
        File resourceRoot = getResourceRoot(task, globalConfig);
        File targetDir = new File(resourceRoot.getPath(), targetPath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        File[] ppts = new File(resourceRoot, sourcePath).listFiles();
        if (ppts != null && ppts.length != 0) {
            for (File ppt : ppts) {
                if (ppt.isDirectory()) {
                    String[] images = ppt.list();
                    Arrays.sort(images, pptComparator);
                    new Im4Java().transferToJpg(new File(ppt, images[0]).getPath(), new File(targetDir, getPptName(ppt.getName())).getPath());
                }
            }
        }

    }

    private String getPptName(String sourceName) {
        return String.format("%s.jpg", FilenameUtils.getBaseName(sourceName));
    }

    @Override
    protected boolean taskSuccessExecuted(File resourceRoot, File distRoot) {
        return new File(resourceRoot, targetPath).exists();
    }

    /**
     * ppt图片排序，名称倒序排序
     */
    public static class PptComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            int pre = Integer.parseInt(FilenameUtils.getBaseName(o1));
            int post = Integer.parseInt(FilenameUtils.getBaseName(o2));
            return post - pre;
        }
    }
}