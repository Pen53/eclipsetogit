package com.wangwenjun.concurrency.chapter7;

/**
 * public synchronized void m1()  需要获取对象实例锁
 * synchronized(this) 需要获取对象实例锁
 * 
 * public synchronized static void m1()
 * 竞争获取类锁（synchronized(SychronizedStatic1.class) 也是需要获取类锁）
 * 
 */
public class SychronizedStaticTest2 {
	public synchronized void m1() {
		System.out.println("thead:"+Thread.currentThread().getName()+",m1 synchronized is doing..");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public synchronized void m2() {
		System.out.println("thead:"+Thread.currentThread().getName()+",m2 synchronized is doing..");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  void m3() {
		System.out.println("thead:"+Thread.currentThread().getName()+",m3 synchronized before is doing..");
		//synchronized SynchronizedTest.class 是获取类锁 不会与m1 m2 方法竞争对象实例锁   
		synchronized(SynchronizedTest.class) {
			System.out.println("thead:"+Thread.currentThread().getName()+",m3 synchronized is doing..");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public  void m4() {
		System.out.println("thead:"+Thread.currentThread().getName()+",m4 synchronized before is doing..");
		//下面的锁会与public synchronized void m1() public synchronized void m2() 竞争实例 对象锁 
		synchronized(this) {
			System.out.println("thead:"+Thread.currentThread().getName()+",m4 synchronized is doing..");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) {
		SychronizedStaticTest2 st = new SychronizedStaticTest2();
		new Thread("t1") {
			@Override
			public void run() {
				System.out.println("t1 running is doing");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				st.m1();
			}
		}.start();
		
		new Thread("t2") {
			@Override
			public void run() {
				System.out.println("t2 running is doing");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				st.m2();
			}
		}.start();
		
		new Thread("t3") {
			@Override
			public void run() {
				System.out.println("t3 running is doing");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				st.m3();
			}
		}.start();
		
		new Thread("t4") {
			@Override
			public void run() {
				System.out.println("t4 running is doing");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				st.m4();
			}
		}.start();
	}
}
