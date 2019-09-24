package com.wangwenjun.concurrency.chapter10;

/***************************************
 * @author:Alex Wang
 * @Date:2017/2/22 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class SynchronizedProblem2 {

    public static void main(String[] args) throws InterruptedException {

    	Thread annoer = new Thread("annoer") {
            @Override
            public void run() {
                SynchronizedProblem2.run();
               
            }
        };
        annoer.start();

        Thread.sleep(1000);

        Thread t2 = new Thread("t2"){
            @Override
            public void run() {
//                /sdfsdfsd
                SynchronizedProblem2.run();
                //sdfsdfsd
            }
        };
        t2.start();
        Thread.sleep(2000);
        t2.interrupt();
        annoer.interrupt();
        System.out.println("t2.isInterrupted():"+t2.isInterrupted());
    }

    private synchronized static void run() {
        System.out.println(Thread.currentThread());
//        while (true) {
//        	try {
//				Thread.sleep(10);
//				SynchronizedProblem2.class.wait(1000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//        }
        
        try {
        	 while (true) {
        		 Thread.sleep(10);
        	 }
        	 
        }catch (InterruptedException e) {
        	System.out.println("InterruptedException is appeance:"+e.getMessage());
        }
    }
}
