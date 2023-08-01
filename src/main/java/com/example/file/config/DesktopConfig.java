package com.example.file.config;

import javafx.stage.Screen;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/15 15:03
 */
public class DesktopConfig {

    private static Properties properties = new Properties();
    static {
        try {
            properties.load(DesktopConfig.class.getResourceAsStream("DesktopConfig.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 桌面有多少列
    public static int desktopIconNumCols = Integer.parseInt(properties.getProperty("desktopIconNumCols"));
    // 桌面有多少行
    public static int desktopIconNumRows = Integer.parseInt(properties.getProperty("desktopIconNumRows"));

    // 任务栏Icon设置
    // 任务栏有多少列
    public static int missionIconNumCols = Integer.parseInt(properties.getProperty("missionIconNumCols"));
    // 任务栏列数 设置只有一行
    public static int missionIconNumRows = Integer.parseInt(properties.getProperty("missionIconNumRows"));
    // 桌面壁纸图片地址
    public static String desktopPictureImage = properties.getProperty("desktopPictureImage");
    // 桌面-关于我们的图片地址
    public static String desktopAboutUsImage = properties.getProperty("desktopAboutUsImage");
    // 桌面-磁盘信息图片地址
    public static String desktopDiskImage = properties.getProperty("desktopDiskImage");
    // 桌面-帮助图片地址
    public static String desktopHelpImage = properties.getProperty("desktopHelpImage");
    // 桌面-Window图片地址
    public static String desktopWindowImage = properties.getProperty("desktopWindowImage");

    // 文件
    public static String desktopTxtImage = properties.getProperty("desktopTxtImage");

    // 文件夹
    public static String desktopDirectoryImage = properties.getProperty("desktopDirectoryImage");

}
