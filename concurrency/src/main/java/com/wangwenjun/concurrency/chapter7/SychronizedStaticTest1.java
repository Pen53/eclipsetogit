package com.wangwenjun.concurrency.chapter7;

public class SychronizedStaticTest1 {
	public static void main(String[] args) {
		new Thread("T1") {
            @Override
            public void run() {
                SychronizedStatic1.m1();
            }
        }.start();

        new Thread("T2") {
            @Override
            public void run() {
                SychronizedStatic1.m2();
            }
        }.start();

        new Thread("T3") {
            @Override
            public void run() {
                SychronizedStatic1.m3();
            }
        }.start();
	}
}
