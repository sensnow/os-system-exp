package com.example.file.config;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/16 22:11
 */
public class WindowConfig {
    private static Properties properties = new Properties();
    static {
        try {
            properties.load(WindowConfig.class.getResourceAsStream("WindowConfig.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // win 关闭窗口图片
    public static String windowCloseImage = properties.getProperty("windowCloseImage");
    // win 最小化窗口图片
    public static String windowMinimizeImage = properties.getProperty("windowMinimizeImage");
}
