package com.example.file.domain;


import javafx.stage.Stage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/15 15:03
 */
// 文件类
public class FileBlock implements Serializable {


    //识别唯一文件的ID
    private String FileID;

    //文件名字 限制为 3 个
    private String name;

    //文件属性
    /*0为只读,1为系统,3为普通文件,8为目录*/
    private int attribute;

    // 文件的读写情况
    // 0 是只读
    // 1 是读和写都可以
    private int wrTrait;

    //起始的盘块号
    private int startPanBlock;

    //文件长度 （用多少个盘)
    private int panBlockLength;

    // 存进去stage的信息，避免打开第二个
    private Stage stage;

    // 当前目录存了多少个文件

    //所在的目录
    private FileBlock parentFileBlock;

    //文件的子文件 (存对象)
    private ArrayList<FileBlock> fileChildrenObjectArrayList = new ArrayList();

    //文件的子目录 (存UUID)
    private ArrayList<String> fileChildrenUuidArrayList = new ArrayList<>();

    public FileBlock(String name, int attribute, int startPanBlock,FileBlock parentFileBlock) {
        this.FileID = UUID.randomUUID().toString().replace("-","");;
        this.name = name;
        this.attribute = attribute;
        this.startPanBlock = startPanBlock;
        this.panBlockLength = 1;
        this.parentFileBlock = parentFileBlock;
        this.wrTrait = 1;
    }

    public FileBlock(String name, int attribute, int startPanBlock) {
        this.FileID = UUID.randomUUID().toString().replace("-","");;
        this.name = name;
        this.attribute = attribute;
        this.startPanBlock = startPanBlock;
        this.parentFileBlock = null;
        this.panBlockLength = 1;
        this.wrTrait = 1;
    }


    public String getFileID() {
        return FileID;
    }

    public void setFileID(String fileID) {
        FileID = fileID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAttribute() {
        return attribute;
    }

    public void setAttribute(int attribute) {
        this.attribute = attribute;
    }

    public int getStartPanBlock() {
        return startPanBlock;
    }

    public void setStartPanBlock(int startPanBlock) {
        this.startPanBlock = startPanBlock;
    }

    public int getPanBlockLength() {
        return panBlockLength;
    }

    public void setPanBlockLength(int panBlockLength) {
        this.panBlockLength = panBlockLength;
    }

    public FileBlock getParentFileBlock() {
        return parentFileBlock;
    }

    public void setParentFileBlock(FileBlock parentFileBlock) {
        this.parentFileBlock = parentFileBlock;
    }

    public ArrayList<FileBlock> getFileChildrenObjectArrayList() {
        return fileChildrenObjectArrayList;
    }

    public void setFileChildrenObjectArrayList(ArrayList<FileBlock> fileChildrenObjectArrayList) {
        this.fileChildrenObjectArrayList = fileChildrenObjectArrayList;
    }

    public ArrayList<String> getFileChildrenUuidArrayList() {
        return fileChildrenUuidArrayList;
    }

    public void setFileChildrenUuidArrayList(ArrayList<String> fileChildrenUuidArrayList) {
        this.fileChildrenUuidArrayList = fileChildrenUuidArrayList;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public int getWrTrait() {
        return wrTrait;
    }

    public void setWrTrait(int wrTrait) {
        this.wrTrait = wrTrait;
    }
}
