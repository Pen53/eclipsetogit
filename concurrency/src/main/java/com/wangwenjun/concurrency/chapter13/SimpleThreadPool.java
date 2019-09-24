package com.wangwenjun.concurrency.chapter13;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/***************************************
 * @author:Alex Wang
 * @Date:2017/2/25 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class SimpleThreadPool extends Thread {

    private int size;

    private final int queueSize;

    private final static int DEFAULT_TASK_QUEUE_SIZE = 2000;

    private static volatile int seq = 0;

    private final static String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";

    private final static ThreadGroup GROUP = new ThreadGroup("Pool_Group");

    private final static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();

    private final static List<WorkerTask> THREAD_QUEUE = new ArrayList<>();

    private final DiscardPolicy discardPolicy;

    public final static DiscardPolicy DEFAULT_DISCARD_POLICY = () -> {
        throw new DiscardException("Discard This Task.");
    };

    private volatile boolean destroy = false;

    private int min;

    private int max;

    private int active;

    public SimpleThreadPool() {
        this(4, 8, 12, DEFAULT_TASK_QUEUE_SIZE, DEFAULT_DISCARD_POLICY);
    }

    public SimpleThreadPool(int min, int active, int max, int queueSize, DiscardPolicy discardPolicy) {
        this.min = min;
        this.active = active;
        this.max = max;
        this.queueSize = queueSize;
        this.discardPolicy = discardPolicy;
        init();
    }

    private void init() {
        for (int i = 0; i < this.min; i++) {
            createWorkTask();//创建最小个数的线程个数
        }
        this.size = min;//设置线程池线程持有线程大小
        this.start();//开始线程池线程（主要用来检查待执行线程任务是否大于线程池持有线程数量，默认一个线程执行一个线程任务，如果任务大于线程数，则创建至最大线程数量）
    }

    public void submit(Runnable runnable) {
        if (destroy)
            throw new IllegalStateException("The thread pool already destroy and not allow submit task.");

        synchronized (TASK_QUEUE) {
            if (TASK_QUEUE.size() > queueSize) {
                discardPolicy.discard();//当任务队列大小大于设置队列大小时，执行释放策略的释放方法。
            }
            TASK_QUEUE.addLast(runnable);//加入到任务队列
            TASK_QUEUE.notifyAll();//通知唤醒正在等待的监视 对象
        }
    }

    @Override
    public void run() {
    	//如果没有销毁线程池
        while (!destroy) {
            System.out.printf("Pool#Min:%d,Active:%d,Max:%d,Current:%d,QueueSize:%d\n",
                    this.min, this.active, this.max, this.size, TASK_QUEUE.size());
            try {
                Thread.sleep(5_000L);
                if (TASK_QUEUE.size() > active && size < active) {
                    for (int i = size; i < active; i++) {
                        createWorkTask();
                    }
                    System.out.println("The pool incremented to active.");
                    size = active;
                } else if (TASK_QUEUE.size() > max && size < max) {
                    for (int i = size; i < max; i++) {
                        createWorkTask();
                    }
                    System.out.println("The pool incremented to max.");
                    size = max;
                }

                synchronized (THREAD_QUEUE) {
                	//如果没有正在运行的任务时，让线程池的线程大小缩小到活动的线程数量
                    if (TASK_QUEUE.isEmpty() && size > active) {
                        System.out.println("=========Reduce========");
                        int releaseSize = size - active;
                        for (Iterator<WorkerTask> it = THREAD_QUEUE.iterator(); it.hasNext(); ) {
                            if (releaseSize <= 0) {
                            	//缩减线程数完成
                                break;
                            }

                            WorkerTask task = it.next();
                            
//                            task.close();
//                            task.interrupt();
//                            it.remove();
//                            releaseSize--;
                            
                            	/*
                            	 * 检查是否这个线程执行任务已完毕，如果没完成，
                            	 * 则不从线程池去掉这个线程(否则有可能移除正在执行的任务，任务完成后task.getTaskState又设置TaskState.FREE)
                            	 * WorkerTask run方法则会进入死循环，则JVM永远不会退出
                            	 */
                            if(task.getTaskState().equals(TaskState.BLOCKED)) {
                            	task.close();
                                task.interrupt();
                                it.remove();
                                releaseSize--;
                            }
                        }
                        //size = active;
                        size = active + releaseSize;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void createWorkTask() {
        WorkerTask task = new WorkerTask(GROUP, THREAD_PREFIX + (seq++));
        task.start();
        THREAD_QUEUE.add(task);
    }

    public void shutdown() throws InterruptedException {
        while (!TASK_QUEUE.isEmpty()) {
            Thread.sleep(50);
        }

        synchronized (THREAD_QUEUE) {
            int initVal = THREAD_QUEUE.size();
            while (initVal > 0) {
                for (WorkerTask task : THREAD_QUEUE) {
                    if (task.getTaskState() == TaskState.BLOCKED) {
                        task.interrupt();
                        task.close();
                        initVal--;
                    } else {
                        Thread.sleep(10);
                    }
                }
            }
        }

        System.out.println(GROUP.activeCount());

        this.destroy = true;
        System.out.println("The thread pool disposed.");
    }

    public int getQueueSize() {
        return queueSize;
    }

    public int getSize() {
        return size;
    }

    public boolean isDestroy() {
        return this.destroy;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public int getActive() {
        return active;
    }

    private enum TaskState {
        FREE, RUNNING, BLOCKED, DEAD
    }

    public static class DiscardException extends RuntimeException {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public DiscardException(String message) {
            super(message);
        }
    }

    public interface DiscardPolicy {

        void discard() throws DiscardException;
    }

    private static class WorkerTask extends Thread {
    	
    	//默认设置空闲状态
        private volatile TaskState taskState = TaskState.FREE;

        public WorkerTask(ThreadGroup group, String name) {
            super(group, name);
        }

        public TaskState getTaskState() {
            return this.taskState;
        }

        public void run() {
            OUTER:
            while (this.taskState != TaskState.DEAD) {
                Runnable runnable;
                synchronized (TASK_QUEUE) {
                    while (TASK_QUEUE.isEmpty()) {
                        try {
                            taskState = TaskState.BLOCKED;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            System.out.println("Closed.");
                            break OUTER;
                        }
                    }
                    runnable = TASK_QUEUE.removeFirst();
                }

                if (runnable != null) {
                    taskState = TaskState.RUNNING;
                    //runnable.run();
                    try {
                    	runnable.run(); 
                    	//捕获业务逻辑的运行期异常，否则此线程执行的runnable.run()RuntimeException时，线程while将break,从而造成一个无用的线程存在。
                    }catch(Exception e1) {
                    	System.out.println("执行业务逻辑出错了. msg："+e1.getMessage());
                    }
                    taskState = TaskState.FREE;
                }
            }
        }

        public void close() {
            this.taskState = TaskState.DEAD;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool threadPool = new SimpleThreadPool();
        for (int i = 0; i < 40; i++) {
            threadPool.submit(() -> {
                System.out.println("The runnable  be serviced by " + Thread.currentThread() + " start.");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("The runnable be serviced by " + Thread.currentThread() + " finished.");
            });
        }

        Thread.sleep(10000);
        threadPool.shutdown();

       /* Thread.sleep(10000);
        threadPool.shutdown();
        threadPool.submit(() -> System.out.println("======="));*/
    }
}
