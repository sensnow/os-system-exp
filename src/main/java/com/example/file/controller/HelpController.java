package com.example.file.controller;

import com.example.file.config.DesktopConfig;
import com.example.file.config.DirectoryConfig;
import com.example.file.config.WindowConfig;
import com.example.file.domain.*;
import com.example.file.manager.FileManager;
import com.example.file.manager.PanManager;
import com.example.file.stage.DirectoryStage;
import com.example.file.stage.EditorStage;
import com.example.file.stage.HelpStage;
import com.example.file.util.GridPaneUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.example.file.config.DesktopConfig.desktopIconNumCols;
import static com.example.file.config.DesktopConfig.desktopIconNumRows;
import static com.example.file.controller.DesktopController.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/11/7 21:42
 */
public class HelpController {
    @FXML
    private VBox rootVbox;
    private GridPane gridPane;
    private HBox hBox;
    // 拖动事件的X
    private double xOffset = 0;
    private double yOffset = 0;
    public static double helpWholeWidth = DesktopController.screenWidth * 0.5;
    public static double helpWholeHeight = DesktopController.screenHeight * 0.5;

    // window 状态栏高度
    public static double helpWindowHeight = helpWholeHeight * 0.05;

    public static double helpBottomHboxHeight = helpWholeHeight-helpWindowHeight;
    private Stage stage;


    public void setWholeStage()
    {
        rootVbox.setMaxWidth(helpWholeWidth);
        rootVbox.setPrefWidth(helpWholeWidth);
        rootVbox.setMaxHeight(helpWholeHeight);
        rootVbox.setPrefHeight(helpWholeHeight);
    }

    public void setWindowLayout(Stage stage, MissionIconBlock missionIconBlock)
    {
        // 创建布局
        gridPane = new GridPane();
        gridPane.setPrefWidth(helpWholeWidth);
        gridPane.setMaxWidth(helpWholeWidth);
        gridPane.setPrefHeight(helpWindowHeight);
        gridPane.setMaxHeight(helpWindowHeight);
        gridPane.setMaxSize(helpWholeWidth,helpWindowHeight);
        gridPane.setPrefSize(helpWholeWidth,helpWindowHeight);
        gridPane.setStyle("-fx-background-color: #5cd2d2");
        // 2 列 一边一个Hbox 方便调整
        GridPaneUtil.createGridCol(gridPane,2,helpWholeWidth);
        GridPaneUtil.createGridRow(gridPane,1,helpWindowHeight);


        // 左右布局
        HBox titleHBox = new HBox();
        titleHBox.setAlignment(Pos.CENTER_LEFT);
        titleHBox.setMaxWidth(helpWholeWidth/2);
        titleHBox.setPrefWidth(helpWholeWidth/2);
        titleHBox.setMaxHeight(helpWindowHeight);
        titleHBox.setPrefHeight(helpWindowHeight);

        HBox windowHBox = new HBox();
        windowHBox.setAlignment(Pos.CENTER_RIGHT);
        windowHBox.setMaxWidth(helpWholeWidth/2);
        windowHBox.setPrefWidth(helpWholeWidth/2);
        windowHBox.setPrefHeight(helpWindowHeight);
        windowHBox.setMaxHeight(helpWindowHeight);
//        windowHBox.setStyle("-fx-background-color: blue");
        // 文件名Label
        Label label = new Label();
        label.setPadding(new Insets(0,0,0,14));
        label.setText("相关帮助");
        label.setPrefHeight(helpWindowHeight);
        label.setMaxHeight(helpWindowHeight);
        label.setMaxWidth(helpWholeWidth/2);
        label.setPrefWidth(helpWholeWidth/2);
        titleHBox.getChildren().add(label);
        // 右边的最小化和关闭
        // 最小化
        VBox minimumImageVBox = new VBox();
        minimumImageVBox.setPadding(new Insets(helpWindowHeight*0.15,helpWindowHeight*0.6,helpWindowHeight*0.15,helpWindowHeight*0.6));
        minimumImageVBox.setMaxHeight(helpWindowHeight);
        minimumImageVBox.setPrefHeight(helpWindowHeight);
        Image minimumImage;
        minimumImage= new Image(getClass().getResourceAsStream(WindowConfig.windowMinimizeImage));
        ImageView minimumBtn = new ImageView(minimumImage);
        minimumBtn.setFitWidth(helpWindowHeight*0.7);
        minimumBtn.setFitHeight(helpWindowHeight*0.7);
        minimumImageVBox.getChildren().add(minimumBtn);

        // 设置minimumImageVBox的事件
        minimumImageVBox.setOnMouseClicked(mouseEvent -> {

            stage.hide();
        });
        // 设置minimumImageVbox的动画
        minimumImageVBox.setOnMouseEntered(mouseEvent -> {
            minimumImageVBox.setStyle("-fx-background-color: #34bdbd");
        });
        minimumImageVBox.setOnMouseExited(mouseEvent -> {
            minimumImageVBox.setStyle("-fx-background-color: transparent");
        });
        windowHBox.getChildren().add(minimumImageVBox);


        // 关闭
        VBox closeImageVbox = new VBox();
        closeImageVbox.setPadding(new Insets(helpWindowHeight*0.2,helpWindowHeight*0.6,helpWindowHeight*0.2,helpWindowHeight*0.6));
        closeImageVbox.setMaxHeight(helpWindowHeight);
        closeImageVbox.setPrefHeight(helpWindowHeight);
        Image closeImage;
        closeImage = new Image(getClass().getResourceAsStream(WindowConfig.windowCloseImage));
        ImageView closeBtn = new ImageView(closeImage);
        closeBtn.setFitWidth(helpWindowHeight*0.6);
        closeBtn.setFitHeight(helpWindowHeight*0.6);
        closeImageVbox.getChildren().add(closeBtn);

        // 设置closeImageVbox的事件
        closeImageVbox.setOnMouseClicked(mouseEvent -> {
            HelpStage.stage = null;
            missionIconBlock.closeMissionBlock();
            stage.close();
        });

        // 设置closeImageVbox的动画
        closeImageVbox.setOnMouseEntered(mouseEvent -> {
            closeImageVbox.setStyle("-fx-background-color: red");
        });
        closeImageVbox.setOnMouseExited(mouseEvent -> {
            closeImageVbox.setStyle("-fx-background-color: transparent");
        });

        windowHBox.getChildren().add(closeImageVbox);
        // 放入布局
        gridPane.add(titleHBox,0,0);
        gridPane.add(windowHBox,1,0);

        this.rootVbox.getChildren().add(gridPane);
    }

    public void setBottomLayout() throws FileNotFoundException {
        VBox vBox = new VBox();
        vBox.setPrefHeight(helpBottomHboxHeight);
        vBox.setPrefWidth(helpWholeWidth);

        Image image = new Image(getClass().getResourceAsStream("/com/example/file/image/aboutandhelp.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(helpWholeWidth);
        imageView.setFitHeight(helpBottomHboxHeight);

        vBox.getChildren().add(imageView);
        rootVbox.getChildren().add(vBox);

    }

    public void setWindowAction(Parent root, Stage stage)
    {

        root.setOnMousePressed(event -> {
            xOffset= event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }


    public void setHelp(Parent root,Stage stage,MissionIconBlock missionIconBlock) throws FileNotFoundException {
        this.stage = stage;
        setWholeStage();
        setWindowAction(root,stage);
        setWindowLayout(stage,missionIconBlock);
        setBottomLayout();
    }
    public void closeStage()
    {
        this.stage.close();
    }

}
