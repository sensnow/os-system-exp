package test;

import com.example.file.config.DesktopConfig;

import java.util.Arrays;

public class testApp {
    public static void main(String[] args) {
        char[] chars = new char[64];
        chars[0]= '2';
        chars[3] = '3';
        System.out.println(new String(chars));
    }
}
