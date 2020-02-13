package com.walkingcoding.aicc.processor.main;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.handler.impl.AiccCourseHandler;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * aicc课件处理过程配置对象
 *
 * @author songhuiqing
 */
public class AiccCourseProcessor {

    /**
     * 待处理文件清单
     */
    private List<File> originFiles;

    /**
     * 文件转化为的任务清单
     */
    private List<Task> tasks;

    /**
     * 工作空间目录
     */
    private String workspace = "/Users/songhuiqing/docker/nginx/html/workspace";

    /**
     * 源文件目录
     */
    private String originFileFolder = "origin";
    /**
     * 模版文件目录
     */
    private String templateFileFolder = "templates/aicc01";

    /**
     * 生成资源文件目录
     */
    private String resourceFileFolder = "resource";

    /**
     * 课件文件输出目录
     */
    private String distFileFolder = "dist";

    /**
     * 并发执行任务数量，默认3个
     */
    private int parallelSize = 3;


    /**
     * 0. 设置参数
     *
     * @param args 参数数组
     */
    private void setArgs(String[] args) {
        // TODO 接收输入参数
    }

    /**
     * 1. 加载课件文件
     */
    private void loadFiles() {
        originFiles = Arrays.asList(
                new File(workspace, originFileFolder)
                        .listFiles(((dir, name) -> new File(dir, name).isDirectory()))
        );
    }


    /**
     * 2. 初始化任务
     */
    private void initTask() {

        if (originFiles == null || originFiles.isEmpty()) {
            throw new RuntimeException("课件源文件列表为空");
        }
        tasks = originFiles.stream().map(file -> new Task(file.getName(), file)).collect(Collectors.toList());

    }

    /**
     * 3. 开始执行课件处理操作
     */
    private void execute() {
        new AiccCourseHandler().execute(this.tasks,
                new GlobalConfig(workspace, templateFileFolder, resourceFileFolder, distFileFolder, parallelSize));
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();

        AiccCourseProcessor processor = new AiccCourseProcessor();
        processor.setArgs(args);
        processor.loadFiles();
        processor.initTask();
        processor.execute();

        while (true) {

            List<Task> tasks = processor.getTasks();
            boolean flag = printProgress(tasks);
            if (flag) {
                System.out.println(String.format("花费时间: %s", (System.currentTimeMillis() - start) / 1000));
                System.exit(0);
            } else {
                Thread.sleep(5 * 1000);
            }

        }
    }

    public static boolean printProgress(List<Task> tasks) {
        boolean finished = true;
        for (Task task : tasks) {
            task.printProcessTasksStateAndUpdateTaskState();
            if (!task.finished()) {
                finished = false;
            }
        }

        return finished;
    }

}
