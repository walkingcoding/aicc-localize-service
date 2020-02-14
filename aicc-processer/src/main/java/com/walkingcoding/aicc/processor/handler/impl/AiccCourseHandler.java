package com.walkingcoding.aicc.processor.handler.impl;

import com.walkingcoding.aicc.processor.config.GlobalConfig;
import com.walkingcoding.aicc.processor.config.Task;
import com.walkingcoding.aicc.processor.handler.CourseHandler;
import org.apache.log4j.Logger;
import org.apache.log4j.helpers.DateTimeDateFormat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * aicc课件处理
 *
 * @author songhuiqing
 */
public class AiccCourseHandler implements CourseHandler {

    private Logger logger = Logger.getLogger(AiccCourseHandler.class);
    private int maximumPoolSize = 10;
    private ExecutorService executorService;

    public AiccCourseHandler() {
        executorService = new ThreadPoolExecutor(5, maximumPoolSize,
                10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(512),
                new ThreadPoolExecutor.DiscardPolicy());
    }

    @Override
    public void execute(List<Task> tasks, GlobalConfig globalConfig) {

        // 并发任务数
        int parallelSize = globalConfig.getParallelSize();
        // 如果并发任务数大于线程池最大数量，并发任务数按照线程池最大数量执行
        if (parallelSize > maximumPoolSize) {
            parallelSize = maximumPoolSize;
        }
        // 将任务按照并发数顺序进行分组
        List<List<Task>> parallelList = new ArrayList<>(parallelSize);
        // 初始化parallelList
        for (int i = 0; i < parallelSize; i++) {
            parallelList.add(new ArrayList<>(16));
        }

        for (int i = 0; i < tasks.size(); i++) {
            // parallelList 下标
            int idx = i % parallelSize;
            // 将任务添加到对应的分组中
            parallelList.get(idx).add(tasks.get(i));
        }
        // 异步执行
        logger.info(String.format("开始课件任务转换，线程数: %s", parallelList.size()));
        parallelList.forEach(taskList -> {
            // 异步执行
            executorService.execute(() -> executeProcessTask(taskList, globalConfig));

        });
    }

    /**
     * 执行子任务
     *
     * @param tasks        任务列表
     * @param globalConfig 全局配置对象
     */
    private void executeProcessTask(List<Task> tasks, GlobalConfig globalConfig) {

        if (tasks == null || tasks.isEmpty()) {
            return;
        }
        tasks.forEach(task -> {
            logger.info(String.format(">>> 任务[%s]开始执行...", task.getTaskId()));
            long start = System.currentTimeMillis();
            // 执行子任务
            task.getProcessTasks().forEach(processTask -> processTask.execute(task, globalConfig));
            logger.info(String.format(">>> 任务[%s]执行完成，耗时: [%ss].", task.getTaskId(), (System.currentTimeMillis() - start) / 1000));
        });

    }

}
