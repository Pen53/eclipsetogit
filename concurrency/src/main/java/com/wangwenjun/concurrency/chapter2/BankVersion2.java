package com.wangwenjun.concurrency.chapter2;

/***************************************
 * @author:Alex Wang
 * @Date:2017/2/15 QQ:532500648
 * QQ交流群:286081824
 ***************************************/
public class BankVersion2 {

    private final static int MAX = 50;

    public static void main(String[] args) {

        //final TicketWindowRunnable ticketWindow = new TicketWindowRunnable();
    	
    	Runnable ticketWindow =new Runnable() {
    		public int index = 1; 
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
		}; 
    			
		
		  final Runnable ticketWindow2 = () -> { 
				  int index = 1; 
				  while (index <= MAX) {
				  System.out.println(Thread.currentThread() + " 的号码是:" + (index++)); try {
				  Thread.sleep(100); 
				  } catch (InterruptedException e) { 
					  e.printStackTrace(); 
				  }
			  }
		  };
		 

        Thread windowThread1 = new Thread(ticketWindow, "一号窗口") {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				System.out.println("1号窗口ticketWindow:"+ticketWindow);
			}
        	
        };
        Thread windowThread2 = new Thread(ticketWindow, "二号窗口"){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				System.out.println("二号窗口ticketWindow:"+ticketWindow);
			}
        	
        };
        Thread windowThread3 = new Thread(ticketWindow, "三号窗口"){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				super.run();
				System.out.println("3号窗口ticketWindow:"+ticketWindow);
			}
        	
        };
        windowThread1.start();
        windowThread2.start();
        windowThread3.start();
    }
}
