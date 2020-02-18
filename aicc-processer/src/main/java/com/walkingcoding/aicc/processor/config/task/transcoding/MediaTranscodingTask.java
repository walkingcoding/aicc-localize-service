package com.walkingcoding.aicc.processor.config.task.transcoding;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.ProcessTask;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.ProcessTaskType;
import ws.schild.jave.*;

import java.io.File;

/**
 * 音视频转码任务
 *
 * @author songhuiqing
 */
public class MediaTranscodingTask extends ProcessTask {

    private String wmvSourceFilePath = "file/video.wmv";
    private String wmaSourceFilePath = "file/audio.wma";
    private String mediaTranscodingPath = "transcoding/inchen/media/";

    public MediaTranscodingTask() {
        super(ProcessTaskType.MEDIA_TRANSCODING, false);
    }

    @Override
    public void executeTask(Task task, GlobalConfig globalConfig) throws Exception {
        // TODO 音视频转码
        File resourceRoot = getResourceRoot(task, globalConfig);
        File wmvFile = new File(resourceRoot, wmvSourceFilePath);
        File wmaFile = new File(resourceRoot, wmaSourceFilePath);
        File targetDir = new File(resourceRoot.getPath(), mediaTranscodingPath);
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        if (wmvFile.exists()) {
            File target = new File(targetDir, String.format("%s.mp4", task.getTaskId()));
            // 读取源文件信息
            MultimediaObject vwmObject = new MultimediaObject(wmvFile);
            MultimediaInfo wmvInfo = vwmObject.getInfo();
            // wmv转码开始
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("aac");
            audio.setBitRate(wmvInfo.getAudio().getBitRate());
            VideoAttributes video = new VideoAttributes();
            video.setCodec("h264");
            video.setBitRate(wmvInfo.getVideo().getBitRate());
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp4");
            attrs.setVideoAttributes(video);
            attrs.setAudioAttributes(audio);
            Encoder encoder = new Encoder();
            encoder.encode(vwmObject, target, attrs);
        }

        if (wmaFile.exists()) {
            // wma转码开始
            File target = new File(targetDir, String.format("%s.mp3", task.getTaskId()));

            MultimediaObject wmaObject = new MultimediaObject(wmaFile);
            MultimediaInfo wmaInfo = wmaObject.getInfo();
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("mp3");
            audio.setBitRate(wmaInfo.getAudio().getBitRate());
            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);
            Encoder encoder = new Encoder();
            encoder.encode(wmaObject, target, attrs);

        }
    }

    @Override
    public String outputDirectory() {
        return mediaTranscodingPath;
    }

}
