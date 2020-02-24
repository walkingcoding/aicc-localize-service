package com.walkingcoding.aicc.processor.handler;

import com.walkingcoding.aicc.processor.config.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 执行器池，用来监听进程执行状态，如果进程空了，从要执行的序列中拿去一个执行
 *
 * @author songhuiqing
 */
public class ExecutorPool {

    public interface TaskExcutor {
        void execute(Task task);
    }

    private ExecutorService executorService;

    /**
     * 最大并行的线程数
     */
    private int maximumPoolSize = 10;

    /**
     * 线程活跃列表
     */
    private List<String> threadActiveList;

    /**
     * 线程数
     */
    private int threadNum;

    public ExecutorPool() {
        executorService = new ThreadPoolExecutor(5, maximumPoolSize,
                10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(512),
                new ThreadPoolExecutor.DiscardPolicy());
        threadActiveList = new ArrayList<>(maximumPoolSize);

    }

    /**
     * 执行任务
     *
     * @param parallelSize 并发数量
     * @param tasks        要执行的任务列表
     */
    public void execute(int parallelSize, List<Task> tasks, TaskExcutor taskExcutor) {
        // 限制最大的并发数
        if (parallelSize > maximumPoolSize) {
            parallelSize = maximumPoolSize;
        }
        // 设置线程数
        threadNum = parallelSize;
        // 开始执行任务
        for (Task task : tasks) {
            executeTask(task, taskExcutor);
        }
    }

    private void executeTask(Task task, TaskExcutor taskExcutor) {

        threadActiveList.add(task.getTaskId());
        executorService.execute(() -> taskExcutor.execute(task));
        threadActiveList.remove(task.getTaskId());
    }
}
