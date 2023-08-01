package com.example.file.manager;

import com.example.file.domain.FileBlock;
import com.example.file.pre.FileData;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class DataManager {

    public static FileData fileData;
//    把数据记录写入文件里，只能存放一次，因为存放的ArrayList<FileBlock> fileBlocks是同一个对象，默认不会覆盖
    //做完所有操作后，退出系统前才能运行
    public static void OutputStore() {
        String patheFile ="Data";
        FileManager fileManager = FileManager.getInstance();
        PanManager panManager = PanManager.getInstance();
        FileData fileData = new FileData(fileManager.getFileBlocks(),fileManager.getRootFileBlock(),panManager.getPanBlockArrayList());
        File file = new File(patheFile);
        try {
            ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(Paths.get(patheFile)));
            if(!file.exists()){
                file.createNewFile();
            }
//            写入全部
            oos.writeObject(fileData);
            //先清空缓冲区数据,保证缓存清空输出
            oos.flush();
            //关闭此文件输出流并释放与此流有关的所有系统资源
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
//    读取对应的文件,读取数据记录(运行程序static方法默认执行)
    public static void InputStore() {
//        存储文件的路径
        System.out.println(1);
        String patheFile ="Data";
        File file = new File(patheFile);
        try {
            if(file.exists()){
                ObjectInputStream oos = new ObjectInputStream(Files.newInputStream(Paths.get(patheFile)));
                fileData =(FileData)oos.readObject();
                //关闭此文件输出流并释放与此流有关的所有系统资源
                oos.close();
            }else {
                fileData = new FileData(new ArrayList<>(),new FileBlock("",1,2),null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }

}
