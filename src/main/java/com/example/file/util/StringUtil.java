package com.example.file.util;

import com.example.file.domain.PanBlock;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/24 8:28
 */
public class StringUtil {


    // 输入String的剪切 变成一个一个长度为64的字符串片段
    public static String[] splitString(String str, int splitLen,int length)
    {
        char[] chars = null;
        String[] strings = new String[length];
        for (int i = 0; i < length; i++) {
            chars = new char[64];
            str.getChars(i*64, Math.min((i + 1) * 64 - 1, str.length()),chars,0);
            strings[i] = String.valueOf(chars);
        }
        return strings;
    }
}
