package com.wangwenjun.concurrency.me.bank;

public class BankWindow extends Thread{
	private String windowName;
	private static int ticketPost = 1;
	private static final int ticketPostMax = 100;
	
	public BankWindow(String windowName) {
		this.windowName = windowName;
	}
	public static void main(String[] args) {
		BankWindow b1 = new BankWindow("w1");
		b1.start();
		BankWindow b2 = new BankWindow("w2");
		b2.start();
		BankWindow b3 = new BankWindow("w3");
		b3.start();
		BankWindow b4 = new BankWindow("w4");
		b4.start();
		BankWindow b5 = new BankWindow("w5");
		b5.start();
		StringBuffer buffer = new StringBuffer();
		buffer.append("123");
		StringBuilder build = new StringBuilder();
		build.append("32");
	}
	@Override
	public void run() {
		
			while(ticketPost<ticketPostMax) {
				synchronized(BankWindow.class) {
					System.out.println("windowName:"+windowName+",get ticketPost:"+(ticketPost++));
					if(ticketPost>=ticketPostMax) {
						System.out.println("windowName:"+windowName+",break it.");
						BankWindow.class.notifyAll();
						break;
					}
					try {
						BankWindow.class.notify();//唤醒一个等待线程
						BankWindow.class.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
	}
	
	
}
