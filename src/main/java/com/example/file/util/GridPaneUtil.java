package com.example.file.util;

import com.example.file.domain.DesktopIconBlock;
import com.example.file.domain.IconBlock;
import com.example.file.domain.IconPoint;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import static com.example.file.controller.DesktopController.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/15 15:03
 */
public class GridPaneUtil {

    // 创建GridPane的列
    public static void createGridCol(GridPane gridPane,int cols,double width)
    {
        for (int i = 0; i < cols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setMaxWidth(width/cols);
            colConst.setPrefWidth(width/cols);
            gridPane.getColumnConstraints().add(colConst);
        }
    }

    // 创建GridPane的行
    public static void createGridRow(GridPane gridPane,int rows,double height)
    {
        for (int i = 0; i < rows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setMaxHeight(height/rows);
            rowConst.setPrefHeight(height/rows);
            gridPane.getRowConstraints().add(rowConst);
        }
    }

    // 搜索没有使用的Grid 垂直
    public static IconPoint searchVerticalEmptyGrid(int[][] gridArray, int rows, int cols)
    {
        int row = -1;
        int col = -1;
        IconPoint result = null;
        for (col = 0; col < cols; col++) {
            for (row = 0; row < rows; row++) {
                if (gridArray[row][col] == 0)
                {
                    return result = new IconPoint(row,col);
                }
            }
        }
        return null;
    }

    // 搜索没有使用的Grid 水平
    public static IconPoint searchHorizontalEmptyGrid(int[][] gridArray, int rows, int cols)
    {
        int row = -1;
        int col = -1;
        IconPoint result = null;
        for (row = 0; row < rows; row++) {
            for (col = 0; col < cols; col++) {
                if (gridArray[row][col] == 0)
                {
                    return result = new IconPoint(row,col);
                }
            }
        }
        return null;
    }

    //  gridpane 增加 IconBlock 垂直
    public static void addIconBlock(GridPane gridPane, int[][] location, IconBlock IconBlock)
    {
        location[IconBlock.getIconPoint().getRow()][IconBlock.getIconPoint().getCol()] = 1;
        gridPane.add(IconBlock.getIconBlock(), IconBlock.getIconPoint().getCol(), IconBlock.getIconPoint().getRow());
    }

    // gripane 删除 IconBlock 垂直
    public static void removeIconBlock(GridPane gridPane, int[][] location, IconBlock IconBlock)
    {
        location[IconBlock.getIconPoint().getRow()][IconBlock.getIconPoint().getCol()] = 0;
        gridPane.getChildren().remove(IconBlock.getIconBlock());
    }

}
