package com.wangwenjun.concurrency.chapter9;

public class PandCRunnableTest {

	public static void main(String[] args) {
		PandCRunnable r = new PandCRunnable();//容器
		Thread produce1 = new Thread("生产者1") {
			@Override
			public void run() {
				for(int i=0;i<40;i++) {
					r.produce();//生产40个
				}
				
			}
		}; 
		produce1.start();
		Thread produce2 = new Thread("生产者2") {
			@Override
			public void run() {
				for(int i=0;i<20;i++) {
					r.produce();//生产20个
				}
				
			}
		}; 
		produce2.start();
		
		Thread consumer1 = new Thread("消费者1") {
			@Override
			public void run() {
				for(int i=0;i<20;i++) {
					r.consume();//消费者20个
				}
				
			}
		}; 
		consumer1.start();
		Thread consumer2 = new Thread("消费者2") {
			@Override
			public void run() {
				for(int i=0;i<10;i++) {
					r.consume();//消费者20个
				}
				
			}
		}; 
		consumer2.start();
		Thread consumer3 = new Thread("消费者3") {
			@Override
			public void run() {
				for(int i=0;i<30;i++) {
					r.consume();//消费者20个
				}
				
			}
		}; 
		consumer3.start();
	}

}
