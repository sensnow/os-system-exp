package com.example.file.domain;


import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/15 15:03
 */
// 盘块类
public class PanBlock implements Serializable {

    // 盘块编号
    private int blockIndex;

    // 下一盘盘块的编号
    private int nextIndex;

    // 盘块属于哪个文件
    private String fileName;

    // 存储数据
    private char[] content = new char[64];

    // 当前目录类数量
    private int directoryLength;

    // 是否已经被使用
    private boolean isUsed = false;

    public PanBlock(int blockIndex, boolean isUsed) {
        this.blockIndex = blockIndex;
        this.isUsed = isUsed;
        this.nextIndex = -1;
        this.fileName = "";
        this.directoryLength = 0;
    }

    public int getBlockIndex() {
        return blockIndex;
    }

    public int getNextIndex() {
        return nextIndex;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex = nextIndex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getDirectoryLength() {
        return directoryLength;
    }

    public void setDirectoryLength(byte directoryLength) {
        this.directoryLength = directoryLength;
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public char[] getContent() {
        return content;
    }

    public void setContent(char[] content) {
        this.content = content;
    }
}
