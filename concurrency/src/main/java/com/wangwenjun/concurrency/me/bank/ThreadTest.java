package com.wangwenjun.concurrency.me.bank;

import java.util.LinkedList;

public class ThreadTest  extends Thread {
	private final static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();
	//默认设置空闲状态
    private volatile TaskState taskState = TaskState.FREE;
	public TaskState getTaskState() {
		return taskState;
	}
	private enum TaskState {
        FREE, RUNNING, BLOCKED, DEAD
    }
	public static void main(String[] args) {
		ThreadTest tt = new ThreadTest();
		tt.start();
		tt.submit(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("running is doing.");
//				try {
//					Thread.sleep(10_000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				while(true){
					
				}
				//System.out.println("running is done.");
//				if("123".length()==3) {
//					throw new RuntimeException("模拟业务处运行时异常123。");
//				}
			}
		});
		
		
		try {
			Thread.sleep(20_000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		tt.interrupt();
		System.out.println(tt.isInterrupted());
	}
	public void submit(Runnable runnable) {

        synchronized (TASK_QUEUE) {
            TASK_QUEUE.addLast(runnable);//加入到任务队列
            System.out.println("TASK_QUEUE.addLast(runnable) 加入到任务队列----------");
            TASK_QUEUE.notifyAll();//通知唤醒正在等待的监视 对象
            System.out.println("通知唤醒正在等待的监视 对象 ----------");
        }
    }
	@Override
	public void run() {
		OUTER:
			while(this.taskState!=TaskState.DEAD) {
				Runnable runnable;
                synchronized (TASK_QUEUE) {
                    while (TASK_QUEUE.isEmpty()) {
                        try {
                            taskState = TaskState.BLOCKED;
                            System.out.println("taskState set TaskState.BLOCKED");
                            TASK_QUEUE.wait();
                            System.out.println("TASK_QUEUE.wait()被唤醒..");
                        } catch (InterruptedException e) {
                            System.out.println("Closed.");
                            break OUTER;
                        }
                    }
                    runnable = TASK_QUEUE.removeFirst();
                }

                if (runnable != null) {
                    taskState = TaskState.RUNNING;
                    System.out.println("runnable.run() ing.");
                    runnable.run();
                    taskState = TaskState.FREE;
                    System.out.println("taskState set TaskState.FREE");
                }
			}
	}
	
}
