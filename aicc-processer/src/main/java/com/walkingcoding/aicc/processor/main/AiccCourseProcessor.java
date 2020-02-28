package com.walkingcoding.aicc.processor.main;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.enums.TaskState;
import com.walkingcoding.aicc.processor.handler.impl.AiccCourseHandler;
import com.walkingcoding.aicc.processor.util.PrintColorUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
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
    private String workspace;

    /**
     * 源文件目录
     */
    private String originFileFolder = "origin";
    /**
     * 模版文件目录
     */
    private String templateFileFolder = "templates/default";

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
        if (args != null || args.length != 0) {
            this.workspace = args[0];
            if (args.length > 1) {
                this.parallelSize = Integer.parseInt(args[2]);
            }
            if (args.length > 2) {
                this.templateFileFolder = args[2];
            }
        }

        if (this.workspace == null || "".equalsIgnoreCase(this.workspace)) {
            System.err.println("工作空间不能为空");
            System.exit(0);
        } else if (!new File(this.workspace).exists()) {
            System.err.println(String.format("目录%s不存在", this.workspace));
            System.exit(0);
        }

        System.setProperty("workspace", this.workspace);

    }

    private void setArgs() throws InterruptedException {
        MainFrame mainFrame = new MainFrame();
        while (true) {
            Thread.sleep(10 * 1000);
            if (mainFrame.getArgsSuccess()) {
                break;
            }
        }
        this.workspace = mainFrame.getWorkspace();
        this.templateFileFolder = mainFrame.getTemplateFileFolder();
        this.parallelSize = mainFrame.getParallelSize();
        System.setProperty("workspace", this.workspace);
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
        System.out.println("********** aicc课件加工程序 **********");

        long start = System.currentTimeMillis();

        AiccCourseProcessor processor = new AiccCourseProcessor();
        System.out.print("通过界面设置参数，请输入1：");
        String argsType = scanner();
        // 设置参数
        if ("1".equalsIgnoreCase(argsType)) {
            processor.setArgs();
        } else {
            processor.setArgs(args);
        }


        // 加载课件
        processor.loadFiles();
        // 初始化任务
        processor.initTask();

        int courseSize = processor.originFiles.size();
        int preMinutes = 10;
        int parallelSize = processor.parallelSize;
        // 预计消耗总时间
        int total = (courseSize * preMinutes) / parallelSize;

        System.out.println(String.format("工作空间：%s", processor.workspace));
        System.out.println(String.format("课件数量：%s", courseSize));
        System.out.println(String.format("并发线程数：%s", parallelSize));
        System.out.println(String.format("每门课件预计消耗时间[%s]分钟", preMinutes));
        System.out.println(String.format("预计消耗总时间[%s]分钟", total));
        System.out.println("10秒后开始进行课件加工...");
        Thread.sleep(10 * 1000);
        // 开始执行课件加工
        processor.execute();

        while (true) {

            List<Task> tasks = processor.getTasks();
            boolean flag = printProgress(tasks);
            if (flag) {
                break;
            } else {
                Thread.sleep(30 * 1000);
            }

        }
        System.out.println(String.format("********** 程序结束, 共计花费时间: [%s]分钟 **********", (System.currentTimeMillis() - start) / 1000 / 60));

    }

    /**
     * 控制台输入
     *
     * @return
     */
    public static String scanner() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    /**
     * 控制台输入
     *
     * @return
     */
    public static boolean continueOrNot() {
        System.out.print("********** 执行请输入y, 取消请输入n: ");
        String nextLine = scanner();
        while (true) {
            if ("y".equalsIgnoreCase(nextLine) || "n".equalsIgnoreCase(nextLine)) {
                break;
            }
            System.out.println("********** 请输入: Y/y - 开始执行, N/n - 不执行");
            nextLine = scanner();
        }
        return "y".equalsIgnoreCase(nextLine);
    }


    public static boolean printProgress(List<Task> tasks) {
        boolean finished = true;
        int total = tasks.size();
        int doneNum = 0, processNum = 0, noneNum = 0;

        for (Task task : tasks) {
            task.printProcessTasksStateAndUpdateTaskState();
            if (!task.finished()) {
                finished = false;
            }
            if (TaskState.FINISH.equals(task.getTaskState())) {
                ++doneNum;
            } else if (TaskState.PROCESSING.equals(task.getTaskState())) {
                ++processNum;
            } else {
                ++noneNum;
            }
        }
        String progress = ((doneNum * 100) / total) + "%";
        System.out.println(String.format("*** 课件总数: %s, 完成进度: %s, 已完成: %s, 进行中: %s, 未开始: %s ***", total, progress, doneNum, processNum, noneNum));

        return finished;
    }

}
