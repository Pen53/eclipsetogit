package com.wangwenjun.concurrency.chapter9;

import java.util.stream.Stream;

/***************************************
 * @author:Alex Wang
 * @Date:2017/2/19 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class ProduceConsumerVersion2 {

    private int i = 0;

    final private Object LOCK = new Object();

    private volatile boolean isProduced = false;

    public void produce() {
        synchronized (LOCK) {
            if (isProduced) {
                try {
                	System.out.println(Thread.currentThread().getName()+" P->LOCK.wait()" );
                    LOCK.wait();
                    System.out.println(Thread.currentThread().getName()+" P->被唤醒，进入runnable status");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                i++;
                System.out.println(Thread.currentThread().getName()+" P->" + i);
                //LOCK.notify();
                LOCK.notifyAll(); 
                isProduced = true;
            }
        }
    }

    public void consume() {
        synchronized (LOCK) {
            if (isProduced) {
                System.out.println(Thread.currentThread().getName()+" C->" + i);
                //LOCK.notify();
                LOCK.notifyAll();
                isProduced = false;
            } else {
                try {
                	System.out.println(Thread.currentThread().getName()+" C->LOCK.wait()");
                    LOCK.wait();
                    System.out.println(Thread.currentThread().getName()+" C->被唤醒，进入runnable status");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ProduceConsumerVersion2 pc = new ProduceConsumerVersion2();
        Stream.of("P1", "P2").forEach(n ->
                new Thread(n) {
                    @Override
                    public void run() {
                        while (true)
                            pc.produce();
                    }
                }.start()
        );
        Stream.of("C1", "C2").forEach(n ->
                new Thread(n) {
                    @Override
                    public void run() {
                        while (true)
                            pc.consume();
                    }
                }.start()
        );
    }
}