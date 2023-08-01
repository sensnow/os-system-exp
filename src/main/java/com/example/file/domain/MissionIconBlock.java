package com.example.file.domain;

import com.example.file.config.DesktopConfig;
import com.example.file.util.GridPaneUtil;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.example.file.config.DesktopConfig.*;
import static com.example.file.controller.DesktopController.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/11/6 14:01
 */
public class MissionIconBlock {


    public static ArrayList<MissionIconBlock> missionIconBlocks = new ArrayList<>();

    private HBox hBox;
    private Image image;
    private ImageView imageView;
    private IconPoint iconPoint;
    private FileBlock fileBlock;
    private Stage stage;

    private int flag;

    public MissionIconBlock(FileBlock fileBlock, Stage stage) {
        this.fileBlock = fileBlock;
        this.stage = stage;
        setIconLayout();
        setLeftAction();
        enterMissionGrid();
    }

    public  MissionIconBlock(int flag,Stage stage) {
        this.stage = stage;
        this.flag =flag;
        setIconLayout();
        setLeftAction();
        enterMissionGrid();
    }
    public void setIconLayout() {
        hBox = new HBox();
        hBox.setMaxWidth(missionGridPaneWidth);
        hBox.setMaxHeight(missionGridPaneHeight);
        hBox.setPrefWidth(missionGridPaneWidth);
        hBox.setPrefHeight(missionGridPaneHeight);
        hBox.setAlignment(Pos.CENTER);
        // 载入图片
        Image image = null;

        if(fileBlock !=null)
        {
            if (fileBlock.getAttribute() == 3) {
                image = new Image(getClass().getResourceAsStream(desktopTxtImage));
            } else if(fileBlock.getAttribute() == 8) {
                image = new Image(getClass().getResourceAsStream(desktopDirectoryImage));
            }
        }else
        {
            if(flag == 0)
            {
                image = new Image(getClass().getResourceAsStream(desktopHelpImage));

            }else if(flag == 1)
            {
                // 磁盘
                image = new Image(getClass().getResourceAsStream(desktopDiskImage));
            }
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(missionGridPaneHeight * 0.85);
        imageView.setFitHeight(missionGridPaneHeight * 0.85);
        // 放入window中
        hBox.setOnMouseEntered(mouseEvent -> {
            hBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);-fx-border-radius: 25;-fx-border-width: 1");
        });
        hBox.setOnMouseExited(mouseEvent -> {
            hBox.setStyle("");
        });
        hBox.getChildren().add(imageView);

    }

    public void setLeftAction()
    {
        hBox.setOnMouseClicked(mouseEvent -> {
            stage.setAlwaysOnTop(true);
            stage.show();
            stage.setAlwaysOnTop(false);
        });
    }

    public void enterMissionGrid()
    {
        missionIconBlocks.add(this);
        iconPoint = GridPaneUtil.searchHorizontalEmptyGrid(missionLocation,missionIconNumRows,missionIconNumCols);
        // 放入missionGrip
        missionLocation[iconPoint.getRow()][iconPoint.getCol()] = 1;
        missionGrid.add(hBox, iconPoint.getCol(), iconPoint.getRow());
    }

    public void renewMissionGrid()
    {
        missionLocation = new int[missionIconNumRows][missionIconNumCols];
        missionLocation[0][missionIconNumCols - 1] = 1;
        missionLocation[0][0] = 1;
        for (MissionIconBlock missionIconBlock : missionIconBlocks)
        {
            missionGrid.getChildren().remove(missionIconBlock.gethBox());
        }
        missionIconBlocks.remove(this);
        for (MissionIconBlock missionIconBlock : missionIconBlocks)
        {
            iconPoint = GridPaneUtil.searchHorizontalEmptyGrid(missionLocation,missionIconNumRows,missionIconNumCols);
            missionIconBlock.setIconPoint(iconPoint);
            // 放入missionGrip
            missionLocation[iconPoint.getRow()][iconPoint.getCol()] = 1;
            missionGrid.add(missionIconBlock.gethBox(), iconPoint.getCol(), iconPoint.getRow());
        }
    }

    public void closeMissionBlock()
    {
        renewMissionGrid();
    }

    public IconPoint getIconPoint() {
        return iconPoint;
    }

    public void setIconPoint(IconPoint iconPoint) {
        this.iconPoint = iconPoint;
    }

    public HBox gethBox() {
        return hBox;
    }

    public void sethBox(HBox hBox) {
        this.hBox = hBox;
    }
}
