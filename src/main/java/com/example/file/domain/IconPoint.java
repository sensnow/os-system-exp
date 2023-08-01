package com.example.file.domain;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/15 15:03
 */
/*
*
* 该类用于搜到gridArray数组 搜索到的就返回坐标
*
* */
public class IconPoint {
    private int row;
    private int col;

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public IconPoint(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
