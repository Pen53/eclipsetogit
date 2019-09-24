package com.wangwenjun.concurrency.chapter9;

public class PandCRunnable {
	final private Object LOCK = new Object();
	private WoTou[] woTouList = new WoTou[10];
	int i = 0;//数组下标
	//生产者生产
    protected void produce() {
        synchronized (LOCK) {
        	while(i>=woTouList.length) {
        		try {
        			//LOCK.notify();//唤醒等待线程
					LOCK.wait();//生产已满，等待消费者消费
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
        	}
        	WoTou wt = new WoTou(Thread.currentThread().getName(),i);
        	woTouList[i] = wt;
            System.out.println("P->" + (i++)+",wt:"+wt);
            LOCK.notify();
        }
    }
    //消费者消费
    protected void consume() {
        synchronized (LOCK) {
        	while((i-1)<0) {
        		//没有生产,唤醒生产者
        		//LOCK.notify(); 因为有i下标所以没必要再的掉唤醒其他等待线程（即不可能所有线程都wait获得LOCK的锁 ）
        		try {
					LOCK.wait();//等待生产者生产
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
        	WoTou wt = woTouList[i-1];
			System.out.println("C->" + (i--)+","+Thread.currentThread().getName()+"消费wt:"+wt );
			LOCK.notify();//已经消费,唤醒一个生产者生产
        }
    }

}
class WoTou{
	private String name;
	private int i;
	public WoTou(String name, int i) {
		this.name = name;
		this.i = i;
	}
	@Override
	public String toString() {
		return "WoTou [name=" + name + ", i=" + i + "]";
	}
}
