package com.wangwenjun.concurrency.chapter7;

public class SychronizedStatic1 {
	//竞争获取类锁（synchronized(SychronizedStatic1.class) 也是需要获取类锁），而不是对象实例锁 
	public synchronized static void m1() {
        System.out.println("SychronizedStatic1 m1 " + Thread.currentThread().getName());
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void m2() {
        System.out.println("SychronizedStatic1 m2 " + Thread.currentThread().getName());
        try {
            Thread.sleep(10_000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void m3() {
    	//下面和加在方法上面一样,证明 public synchronized static void m2() 加锁对象为本类
    	synchronized(SychronizedStatic1.class) {
    		System.out.println("SychronizedStatic1 m3 " + Thread.currentThread().getName());
            try {
                Thread.sleep(10_000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
    	}
        
    }
}
