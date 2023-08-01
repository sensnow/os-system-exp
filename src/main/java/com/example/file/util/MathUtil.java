package com.example.file.util;
/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/15 15:03
 */
public class MathUtil {
    public static int getDivisor(int a,int b)
    {
        int temp;                   //整形零时变量
        if(a<b)                     //a<b 则交换
        {
            temp=a;a=b;b=temp;
        }
        while(b!=0)
        {
            temp=a%b;              //a中大数除以b中小数循环取余，直到b及余数为0
            a=b;
            b=temp;
        }
        return a;
    }
}
