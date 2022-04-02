package com.tools.commonservice.common;

import org.openjdk.jol.info.ClassLayout;


/**
 * 查看类对象参数在jvm中内存分配情况， 包括初始化重排序， 节省内存空间
 */
public class Reorder {

    private byte a;

    private int b;

    private boolean c;

    private float d;

    private char e;

    private char f;

    private Object g;

    public static void main(String[] args) {
        System.out.println(ClassLayout.parseClass(Reorder.class).toPrintable());
    }


}

