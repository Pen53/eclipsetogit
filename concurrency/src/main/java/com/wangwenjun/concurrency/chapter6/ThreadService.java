package com.wangwenjun.concurrency.chapter6;

/***************************************
 * @author:Alex Wang
 * @Date:2017/2/19 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class ThreadService {

    private Thread executeThread;
    //强制关闭非守护线程（开启执行业务逻辑的守护线程），使执行业务runnable的守护线程结束

    private boolean finished = false;

    public void execute(Runnable task) {
        executeThread = new Thread() {
            @Override
            public void run() {
                Thread runner = new Thread(task);
                runner.setDaemon(true);

                runner.start();
                try {
                    runner.join();
                    //executeThread 线程等待 runner线程执行完毕,
                    //当executeThread线程InterruptedException时,此方法不会阻塞等待
                    finished = true;
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
            }
        };

        executeThread.start();
    }

    public void shutdown(long mills) {
        long currentTime = System.currentTimeMillis();
        while (!finished) {
        	//如果业务线程没有执行完成，但是超过规定预期的时间时，强制结束线程
            if ((System.currentTimeMillis() - currentTime) >= mills) {
                System.out.println("任务超时，需要结束他!");
                executeThread.interrupt();//注意打断方法只能打断线程正在等待的方法执行,让其不在阻塞
                //runner.join();将不会阻塞等待,此时线程将执行完成,守护线程也将结束
                break;
            }

            try {
                executeThread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("执行线程被打断!");
                break;
            }
        }

        finished = false;
    }
}