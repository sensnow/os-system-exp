package com.example.file.controller;

import com.example.file.config.DesktopConfig;
import com.example.file.config.WindowConfig;
import com.example.file.domain.FileBlock;
import com.example.file.domain.MissionIconBlock;
import com.example.file.manager.FileManager;
import com.example.file.stage.StageManager;
import com.example.file.util.GridPaneUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 容子铿
 * @create 2022/10/15
 *
 *
 *
 *
 *  @Author : 陆炜森 (修改)
 *  @create 2022/10/16
 */
public class EditorController {


    // 编辑窗口
    @FXML
    private VBox editorVbox;

    // 默认的字体大小
    private int fontSize=12;
    private String fontStyle = String.valueOf(FontWeight.NORMAL);

    // 文本输入框
    private TextArea textArea;

    // window布局Grid
    private GridPane gridPane;

    private Stage stage;

    // 拖动事件的X
    private double xOffset = 0;

    private double yOffset = 0;

    // 设置对应的FileBlock;
    private FileBlock fileBlock;
    // 获取FileManager
    private static final FileManager fileManager = FileManager.getInstance();
    // 整个编辑器的宽度
    public static double editorWholeWidth = DesktopController.screenWidth * 0.5;
    // 整个编辑器的高度
    public static double editorWholeHeight = DesktopController.screenHeight * 0.5;

    // window 状态栏高度
    public static double editorWindowHeight = editorWholeHeight * 0.06;
    // menu 工具栏高度
    public static double editorToolHeight  = editorWholeHeight * 0.05;
    // textArea 输入栏的高度
    public static double editorTextAreaHeight = editorWholeHeight * 0.89;

    // 设置基础
    public void setEditorLayout()
    {
        //总面板的宽高
        editorVbox.setPrefWidth(editorWholeWidth);
        editorVbox.setMaxWidth(editorWholeWidth);
        editorVbox.setPrefHeight(editorWholeHeight);
        editorVbox.setMaxHeight(editorWindowHeight);

        //上面部分的设置
        HBox hBox = new HBox();
        hBox.setPrefWidth(editorWholeWidth);
        hBox.setMaxWidth(editorWholeWidth);
        hBox.setPrefHeight(editorToolHeight);
        hBox.setMaxHeight(editorToolHeight);
        hBox.setAlignment(Pos.CENTER_LEFT);

        //菜单栏


        MenuBar menuBar = new MenuBar();
        menuBar.setPrefHeight(editorToolHeight);
        menuBar.setMaxHeight(editorToolHeight);

        Menu fileMenu = new Menu("文件");
        Menu fontMenu = new Menu("字体");
        Menu fontsizeMenu = new Menu("大小");
        Menu fontstyleMenu = new Menu("style");
        menuBar.getMenus().addAll(fileMenu,fontMenu);  //把两个菜单放进菜单栏中
        fontMenu.getItems().addAll(fontsizeMenu,fontstyleMenu);
        hBox.getChildren().add(menuBar);

        //定义两个互斥数组，分别是用于调节字体大小，字体粗细
        final ToggleGroup fontsizeGroup = new ToggleGroup();
        final ToggleGroup fontstyleGroup = new ToggleGroup();
        //定义RadioMenuItem的子菜单
        MenuItem saveMenuItem = new MenuItem("保存");
        RadioMenuItem size12MenuItem = new RadioMenuItem("12");
        RadioMenuItem size14MenuItem = new RadioMenuItem("14");
        RadioMenuItem size16MenuItem = new RadioMenuItem("16");
        RadioMenuItem size18MenuItem = new RadioMenuItem("18");
        RadioMenuItem size20MenuItem = new RadioMenuItem("20");
        RadioMenuItem normalMenuItem = new RadioMenuItem("正常");
        RadioMenuItem cutiMenuItem = new RadioMenuItem("粗体");

        //把子菜单放进不同的两个互斥数组中
        size12MenuItem.setToggleGroup(fontsizeGroup);
        size14MenuItem.setToggleGroup(fontsizeGroup);
        size16MenuItem.setToggleGroup(fontsizeGroup);
        size18MenuItem.setToggleGroup(fontsizeGroup);
        size20MenuItem.setToggleGroup(fontsizeGroup);
        normalMenuItem.setToggleGroup(fontstyleGroup);
        cutiMenuItem.setToggleGroup(fontstyleGroup);

        //把子菜单挂载到两个菜单“大小”和“style”中
        fontsizeMenu.getItems().addAll(size12MenuItem,size14MenuItem,size16MenuItem,size18MenuItem,size20MenuItem);
        fontstyleMenu.getItems().addAll(normalMenuItem,cutiMenuItem);
        fileMenu.getItems().add(saveMenuItem);


        //
        textArea = new TextArea();
        textArea.setPrefWidth(editorWholeWidth);
        textArea.setMaxWidth(editorWholeWidth);
        textArea.setPrefHeight(editorTextAreaHeight);
        textArea.setMaxHeight(editorTextAreaHeight);
        textArea.setFont(Font.font("宋体", FontWeight.valueOf(fontStyle),fontSize));

        saveMenuItem.setAccelerator(KeyCombination.valueOf("ctrl+s"));//保存快捷键设置
        saveMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                fileManager.saveFileContent(fileBlock,textArea.getText());
            }
        });
        size12MenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.setFont(Font.font("宋体",FontWeight.valueOf(fontStyle),12));
                fontSize = 12;
            }
        });
        size14MenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.setFont(Font.font("宋体",FontWeight.valueOf(fontStyle),14));
                fontSize = 14;
            }
        });
        size16MenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.setFont(Font.font("宋体",FontWeight.valueOf(fontStyle),16));
                fontSize = 16;
            }
        });
        size18MenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.setFont(Font.font("宋体",FontWeight.valueOf(fontStyle),18));
                fontSize = 18;
            }
        });
        size20MenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.setFont(Font.font("宋体", FontWeight.valueOf(fontStyle),20));
                fontSize = 20;
            }
        });
        normalMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.setFont(Font.font("宋体",FontWeight.NORMAL,fontSize));
                fontStyle = String.valueOf(FontWeight.NORMAL);
            }
        });
        cutiMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.setFont(Font.font("宋体",FontWeight.BOLD,fontSize));
                fontStyle = String.valueOf(FontWeight.BOLD);
            }
        });
        editorVbox.getChildren().add(gridPane);
        editorVbox.getChildren().add(hBox); //把vBox放进面板中
        editorVbox.getChildren().add(textArea);
    }

    // 设置窗口的布局
    public void setEditorWindowLayout(Stage stage,MissionIconBlock missionIconBlock)
    {
        // 创建布局
        gridPane = new GridPane();
        gridPane.setPrefWidth(editorWholeWidth);
        gridPane.setMaxWidth(editorWholeWidth);
        gridPane.setPrefHeight(editorWindowHeight);
        gridPane.setMaxHeight(editorWindowHeight);
        gridPane.setMaxSize(editorWholeWidth,editorWindowHeight);
        gridPane.setPrefSize(editorWholeWidth,editorWindowHeight);
        gridPane.setStyle("-fx-background-color: #5cd2d2");
        // 2 列 一边一个Hbox 方便调整
        GridPaneUtil.createGridCol(gridPane,2,editorWholeWidth);
        GridPaneUtil.createGridRow(gridPane,1,editorWindowHeight);


        // 左右布局
        HBox titleHBox = new HBox();
        titleHBox.setAlignment(Pos.CENTER_LEFT);
        titleHBox.setMaxWidth(editorWholeWidth/2);
        titleHBox.setPrefWidth(editorWholeWidth/2);
        titleHBox.setMaxHeight(editorWindowHeight);
        titleHBox.setPrefHeight(editorWindowHeight);

        HBox windowHBox = new HBox();
        windowHBox.setAlignment(Pos.CENTER_RIGHT);
        windowHBox.setMaxWidth(editorWholeWidth/2);
        windowHBox.setPrefWidth(editorWholeWidth/2);
        windowHBox.setPrefHeight(editorWindowHeight);
        windowHBox.setMaxHeight(editorWindowHeight);
//        windowHBox.setStyle("-fx-background-color: blue");
        // 文件名Label
        Label label = new Label();
        label.setPadding(new Insets(0,0,0,14));
        label.setText(fileBlock.getName());
        label.setPrefHeight(editorWindowHeight);
        label.setMaxHeight(editorWindowHeight);
        label.setMaxWidth(editorWholeWidth/2);
        label.setPrefWidth(editorWholeWidth/2);
        titleHBox.getChildren().add(label);
        // 右边的最小化和关闭
        // 最小化

        VBox minimumImageVBox = new VBox();
        minimumImageVBox.setPadding(new Insets(editorToolHeight*0.15,editorToolHeight*0.6,editorToolHeight*0.15,editorToolHeight*0.6));
        minimumImageVBox.setMaxHeight(editorWindowHeight);
        minimumImageVBox.setPrefHeight(editorWindowHeight);
        Image minimumImage;
        minimumImage= new Image(getClass().getResourceAsStream(WindowConfig.windowMinimizeImage));
        ImageView minimumBtn = new ImageView(minimumImage);
        minimumBtn.setFitWidth(editorWindowHeight*0.7);
        minimumBtn.setFitHeight(editorWindowHeight*0.7);
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
        closeImageVbox.setPadding(new Insets(editorToolHeight*0.2,editorToolHeight*0.6,editorToolHeight*0.2,editorToolHeight*0.6));
        closeImageVbox.setMaxHeight(editorWindowHeight);
        closeImageVbox.setPrefHeight(editorWindowHeight);
        Image closeImage;
        closeImage = new Image(getClass().getResourceAsStream(WindowConfig.windowCloseImage));
        ImageView closeBtn = new ImageView(closeImage);
        closeBtn.setFitWidth(editorWindowHeight*0.6);
        closeBtn.setFitHeight(editorWindowHeight*0.6);
        closeImageVbox.getChildren().add(closeBtn);

        // 设置closeImageVbox的事件
        closeImageVbox.setOnMouseClicked(mouseEvent -> {

            missionIconBlock.closeMissionBlock();
            StageManager.editorControllers.remove(this);
            fileBlock.setStage(null);
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
    }

    // 打开的时候 读取File的内容
    public void setTextAreaContentOnReading()
    {
        String str =fileManager.readFile(fileBlock);
        textArea.setText(str);
        textArea.setWrapText(true);
        textArea.positionCaret(str.length());
        if(fileBlock.getWrTrait() == 0)
        {
            textArea.setEditable(false);
        }
    }

    // 设置窗口的事件
    public void setEditorWindowAction(Parent root,Stage stage)
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

    public void setEditorVbox(Parent parent, Stage stage, FileBlock fileBlock, MissionIconBlock missionIconBlock)
    {
        this.stage = stage;
        this.fileBlock = fileBlock;
        setEditorWindowLayout(stage,missionIconBlock);
        setEditorLayout();
        setEditorWindowAction(parent,stage);
        setTextAreaContentOnReading();
    }

    public void closeStage()
    {
        this.stage.close();
        fileBlock.setStage(null);
    }

}