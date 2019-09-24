package com.wangwenjun.concurrency.chapter2;

/***************************************
 * @author:Alex Wang
 * @Date:2017/2/15 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class TicketWindowRunnable implements Runnable {

    private int index = 1;

    private final static int MAX = 50;

    @Override
    public void run() {
        while (index <= MAX) {
        	synchronized(TicketWindowRunnable.class) {
        		System.out.println(Thread.currentThread() + " 的号码是:" + (index++));
        		if(index > MAX) {
        			TicketWindowRunnable.class.notifyAll();
        			System.out.println(Thread.currentThread() + "推出..");
        			break;
        		}
        		
                try {
                    //Thread.sleep(100);
                	TicketWindowRunnable.class.notify();
                	TicketWindowRunnable.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        	}
            
        }
    }
}
