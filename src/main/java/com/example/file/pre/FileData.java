package com.example.file.pre;

import com.example.file.domain.FileBlock;
import com.example.file.domain.PanBlock;
import com.example.file.manager.FileManager;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/11/7 19:47
 */
public class FileData implements Serializable {


    private static final long serialVersionUID=353561978771842198L;
    public ArrayList<FileBlock> fileBlocks;
    public FileBlock rootFileBlock;
    public ArrayList<PanBlock> panBlockArrayList;

    public FileData(ArrayList<FileBlock> fileBlocks, FileBlock rootFileBlock, ArrayList<PanBlock> panBlockArrayList) {
        this.fileBlocks = fileBlocks;
        this.rootFileBlock = rootFileBlock;
        this.panBlockArrayList = panBlockArrayList;
    }

    public ArrayList<FileBlock> getFileBlocks() {
        return fileBlocks;
    }

    public void setFileBlocks(ArrayList<FileBlock> fileBlocks) {
        this.fileBlocks = fileBlocks;
    }

    public FileBlock getRootFileBlock() {
        return rootFileBlock;
    }

    public void setRootFileBlock(FileBlock rootFileBlock) {
        this.rootFileBlock = rootFileBlock;
    }

    public ArrayList<PanBlock> getPanBlockArrayList() {
        return panBlockArrayList;
    }

    public void setPanBlockArrayList(ArrayList<PanBlock> panBlockArrayList) {
        this.panBlockArrayList = panBlockArrayList;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
