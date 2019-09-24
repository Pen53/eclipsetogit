
package com.wangwenjun.concurrency.chapter9;

import java.util.stream.Stream;

/***************************************
 * @author:Alex Wang
 * @Date:2017/2/20 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class DifferenceOfWaitAndSleep {

    private final static Object LOCK = new Object();

    public static void main(String[] args) {
        Stream.of("T1", "T2").forEach(name ->
                new Thread(name) {
                    @Override
                    public void run() {
                        m2();
                    }
                }.start()
        );
    }

    public static void m1() {
        synchronized (LOCK) {
            try {
                System.out.println("The Thread " + Thread.currentThread().getName() + " enter.");
                Thread.sleep(20000);//不会释放对象锁
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void m2() {
        synchronized (LOCK) {
            try {
                System.out.println("The Thread " + Thread.currentThread().getName() + " enter.");
                LOCK.wait();//会释放对象锁，但是会一直阻塞，等待被唤醒。
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}