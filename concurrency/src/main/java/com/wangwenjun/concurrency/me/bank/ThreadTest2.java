package com.wangwenjun.concurrency.me.bank;
/**
 * test wait notify 
 * @author Administrator
 *
 */
public class ThreadTest2 extends Thread {
	private static final Object monitor = new Object();
	public ThreadTest2() {
		
	}
	public ThreadTest2(String name) {
		super(name);
	}
	@Override
	public void run() {
		synchronized (monitor) {
			System.out.println(Thread.currentThread().getName()+" get monitor lock begin.");
			try {
				monitor.wait();
				System.out.println(Thread.currentThread().getName()+",is notify...");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread().getName()+" is doing.");
		}
	}
	public static void main(String[] args) {
		for(int i=0;i<10;i++) {
			new ThreadTest2("threadName"+i) .start();
		}
		try {
			Thread.sleep(10_000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized (monitor) {
			System.out.println("main get monitor lock");
			monitor.notifyAll();
			System.out.println("main get monitor notifyAll");
		}
	}
}
