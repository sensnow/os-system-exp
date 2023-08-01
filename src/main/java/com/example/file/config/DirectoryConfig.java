package com.example.file.config;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/25 23:03
 */
public class DirectoryConfig {
    private static Properties properties = new Properties();
    static {
        try {
            properties.load(DirectoryConfig.class.getResourceAsStream("DirectoryConfig.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // 文件夹里面放多少列
    public static int directoryIconNumCols = Integer.parseInt(properties.getProperty("directoryIconNumCols"));
    // 文件夹里面放多少行
    public static int directoryIconNumRows = Integer.parseInt(properties.getProperty("directoryIconNumRows"));
    // 主目录文件
    public static String rootDirectoryImage = properties.getProperty("rootDirectoryImage");

    // 朝左的图标(灰色)
    public static String toLeftGreyImage  = properties.getProperty("toLeftGreyImage");
    // 朝左的图标(灰色)
    public static String toLeftBlueImage  =  properties.getProperty("toLeftBlueImage");
    // 朝右的图标(灰色)
    public static String toRightGreyImage = properties.getProperty("toRightGreyImage");
    // 朝右的图标(蓝色)
    public static String toRightBlueImage = properties.getProperty("toRightBlueImage");
    // 回到主界面的图标(灰色)
    public static String toHomeGreyImage = properties.getProperty("toHomeGreyImage");
    // 回到主界面的图标(蓝色)
    public static String toHomeGreenImage = properties.getProperty("toHomeGreenImage");

}
