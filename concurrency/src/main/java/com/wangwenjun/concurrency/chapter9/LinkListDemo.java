package com.wangwenjun.concurrency.chapter9;

import java.util.LinkedList;


public class LinkListDemo {
	final static private LinkedList<Control> CONTROLS = new LinkedList<>();
	public static void main(String[] args) {
		Control c1 = null;
		for(int i=0;i<5;i++) {
			Control c =new Control();
			c.setName("name_"+i);
			CONTROLS.addLast(c );
			if(i==3) {
				c1 = c;
			}
		}
		System.out.println("size1:"+CONTROLS.size());
		boolean c2 = CONTROLS.remove(c1);
		System.out.println("c1 obj:"+c1+",c2:"+c2);
		System.out.println("size2:"+CONTROLS.size());
		for(int i=0;i<CONTROLS.size();i++) {
			Control c =CONTROLS.get(i);
			
			System.out.println("c:"+c);
		}
	}
}
class Control{
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Control [name=" + name + "]";
	}
	
}