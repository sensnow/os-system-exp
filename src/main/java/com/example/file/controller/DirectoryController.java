package com.example.file.controller;

import com.example.file.config.DesktopConfig;
import com.example.file.config.DirectoryConfig;
import com.example.file.config.WindowConfig;
import com.example.file.domain.*;
import com.example.file.manager.FileManager;
import com.example.file.stage.StageManager;
import com.example.file.util.GridPaneUtil;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import static com.example.file.config.DesktopConfig.desktopIconNumCols;
import static com.example.file.config.DesktopConfig.desktopIconNumRows;
import static com.example.file.controller.DesktopController.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/25 21:28
 */
public class DirectoryController {


    // 拖动事件的X
    private double xOffset = 0;

    private double yOffset = 0;

    // 记录 windowX
    private double windowX;
    // 记录 windowY
    private double windowY;
    // 打开的文件夹
    private FileBlock startFileBlock;
    // 上一个的FileBlock
    private FileBlock preFileBlock;
    // 现在的FileBlock
    private FileBlock currentFileBlock;
    // 下一个的FileBlock
    private FileBlock nextFileBlock;
    // 获取文件管理器
    private FileManager fileManager = FileManager.getInstance();
    //

    // 整个文件夹的宽度
    public static final double directoryWholeWidth = DesktopController.screenWidth * 0.65;
    // 整个文件夹的高度
    public static final double directoryWholeHeight = DesktopController.screenHeight * 0.65;
    // 左边树状Vbox的宽度
    private static final double treeViewVBoxWidth =  directoryWholeWidth * 0.25;
    // 右边目录的Vbox的宽度
    public static final double iconGridPaneWidth = directoryWholeWidth * 0.75;
    // window 状态栏高度
    private static final double directoryWindowHeight = directoryWholeHeight * 0.04;
    // bottomHBox的高度
    private static final double directoryBottomHBoxHeight = directoryWholeHeight*0.96;
    // toolGridPane 的高度
    private static final double toolGridPaneHeight = 25;
    // iconGridPane 的高度
    public static final double iconGridPaneHeight = directoryBottomHBoxHeight - toolGridPaneHeight;
    // 获取根目录
    @FXML
    private VBox rootVBox;
    // 下面的HBox
    private HBox bottomHBox;
    // 上面的任务栏
    // 下面左边TreeView的Vbox
    private VBox treeViewVBox;
    // 下面右边的 VBox
    private VBox showVBox;
    // 下面右边的左右返回按钮行
    private GridPane toolGridPane;
    // 下面右边的图标的GridPane
    private GridPane iconGridPane;
    // window布局Grid
    private GridPane windowGridPane;
    // 左边的TreeView
    private TreeView<String> rootTreeView;

    private  Label label;
    //

    /*
   桌面的图标控制数量，防止重叠
   0 代表的是没有被占用
   1 代表的是已经被占用
   默认新建都是 0
   */
    public int[][] directoryLocation = new int[DirectoryConfig.directoryIconNumRows][DirectoryConfig.directoryIconNumCols];
    private Stage stage;


    // 设置窗口的事件
    public void setDirectoryWindowAction(Parent root, Stage stage)
    {
        windowX = DesktopController.screenWidth/2-directoryWholeWidth/2;
        windowY = DesktopController.screenHeight/2 -directoryWholeHeight/2;
        stage.setX(windowX);
        stage.setY(windowY);
        root.setOnMousePressed(event -> {
            xOffset= event.getSceneX();
            yOffset = event.getSceneY();
        });

        root.setOnMouseDragged(event -> {
            windowX = event.getScreenX() - xOffset;
            windowY = event.getScreenY() - yOffset;
            stage.setX(windowX);
            stage.setY(windowY);
        });
    }

    // 设置toolGriPaneLayout
    public void setToolGridPaneLayout() throws FileNotFoundException {
        toolGridPane = new GridPane();
        toolGridPane.setPrefWidth(iconGridPaneWidth);
        toolGridPane.setMaxWidth(iconGridPaneWidth);
        toolGridPane.setPrefHeight(toolGridPaneHeight);
        toolGridPane.setMaxHeight(toolGridPaneHeight);
        toolGridPane.setMaxSize(iconGridPaneWidth,toolGridPaneHeight);
        toolGridPane.setPrefSize(iconGridPaneWidth,toolGridPaneHeight);
        // 2 列 一边一个Hbox 方便调整
        GridPaneUtil.createGridCol(toolGridPane, 2,iconGridPaneWidth);
        GridPaneUtil.createGridRow(toolGridPane,1,toolGridPaneHeight);
        toolGridPane.setStyle("-fx-border-width: 0 0 1 0;-fx-border-color: Grey; -fx-border-style: solid");

        // 左右布局
        HBox imageButtonHBox = new HBox();
        imageButtonHBox.setAlignment(Pos.CENTER_LEFT);
        imageButtonHBox.setMaxWidth(iconGridPaneWidth/2);
        imageButtonHBox.setPrefWidth(iconGridPaneWidth/2);
        imageButtonHBox.setMaxHeight(toolGridPaneHeight);
        imageButtonHBox.setPrefHeight(toolGridPaneHeight);

        HBox searchHBox = new HBox();
        searchHBox.setAlignment(Pos.CENTER_RIGHT);
        searchHBox.setMaxWidth(iconGridPaneWidth/2);
        searchHBox.setPrefWidth(iconGridPaneWidth/2);
        searchHBox.setPrefHeight(toolGridPaneHeight);
        searchHBox.setMaxHeight(toolGridPaneHeight);

        // 详细设置
        // 向左 向右
        Image toLeftGreyImage = new Image(getClass().getResourceAsStream(DirectoryConfig.toLeftGreyImage));
        Image toLeftBlueImage = new Image(getClass().getResourceAsStream(DirectoryConfig.toLeftBlueImage));
        Image toRightGreyImage = new Image(getClass().getResourceAsStream(DirectoryConfig.toRightGreyImage));
        Image toRightBlueImage = new Image(getClass().getResourceAsStream(DirectoryConfig.toRightBlueImage));
        Image toHomeGreyImage = new Image(getClass().getResourceAsStream(DirectoryConfig.toHomeGreyImage));
        Image toHomeGreenImage = new Image(getClass().getResourceAsStream(DirectoryConfig.toHomeGreenImage));

        ArrayList<ImageView> imageViews = new ArrayList<>();
        ImageView toLeftImageView = new ImageView(toLeftGreyImage);
        ImageView toHomeImageView = new ImageView(toHomeGreyImage);
        ImageView toRightImageView = new ImageView(toRightGreyImage);

        imageViews.add(toHomeImageView);
        imageViews.add(toLeftImageView);
        imageViews.add(toRightImageView);
        for(ImageView imageView:imageViews)
        {
            HBox.setMargin(imageView,new Insets(0,5,0,5));
            imageView.setFitWidth(toolGridPaneHeight);
            imageView.setFitHeight(toolGridPaneHeight);
        }
        toLeftImageView.setOnMouseEntered(mouseEvent -> {
            toLeftImageView.setImage(toLeftBlueImage);
        });
        toLeftImageView.setOnMouseExited(mouseEvent -> {
            toLeftImageView.setImage(toLeftGreyImage);
        });
        toRightImageView.setOnMouseEntered(mouseEvent -> {
            toRightImageView.setImage(toRightBlueImage);
        });
        toRightImageView.setOnMouseExited(mouseEvent -> {
            toRightImageView.setImage(toRightGreyImage);
        });
        toHomeImageView.setOnMouseEntered(mouseEvent -> {
            toHomeImageView.setImage(toHomeGreenImage);
        });
        toHomeImageView.setOnMouseExited(mouseEvent -> {
            toHomeImageView.setImage(toHomeGreyImage);
        });

        toHomeImageView.setOnMouseClicked(mouseEvent -> {
            preFileBlock = currentFileBlock;
            nextFileBlock = startFileBlock;
            currentFileBlock = startFileBlock;
            refreshIconGrid(currentFileBlock);
        });

        toLeftImageView.setOnMouseClicked(mouseEvent -> {
            nextFileBlock = currentFileBlock;
            currentFileBlock = preFileBlock;
            refreshIconGrid(currentFileBlock);
        });

        toRightImageView.setOnMouseClicked(mouseEvent -> {
            preFileBlock = currentFileBlock;
            currentFileBlock = nextFileBlock;
            refreshIconGrid(currentFileBlock);
        });


        imageButtonHBox.getChildren().addAll(toLeftImageView,toHomeImageView,toRightImageView);

        label = new Label();
        label.setPrefWidth(iconGridPaneWidth/2 * 0.7);
        label.setMaxWidth(iconGridPaneWidth/2* 0.7);
        HBox.setMargin(label,new Insets(0,10,0,0));
        label.setText("当前文件夹: "+currentFileBlock.getName());
        searchHBox.getChildren().add(label);


        

        toolGridPane.add(imageButtonHBox,0,0);
        toolGridPane.add(searchHBox,1,0);



    }

    // 左右基础布局
    private void setRightLeftBaseLayout() throws FileNotFoundException {

        // 设置整个Width 和 Height
        rootVBox.setMaxWidth(directoryWholeWidth);
        rootVBox.setPrefWidth(directoryWholeWidth);
        rootVBox.setMaxHeight(directoryWholeHeight);
        rootVBox.setPrefHeight(directoryWholeHeight);


        bottomHBox = new HBox();
        bottomHBox.setPrefWidth(directoryWholeWidth);
        bottomHBox.setMaxWidth(directoryWholeWidth);
        bottomHBox.setPrefHeight(directoryBottomHBoxHeight);
        bottomHBox.setMaxHeight(directoryBottomHBoxHeight);

        // 开始设置左边VBox
        treeViewVBox = new VBox();
        treeViewVBox.setMaxWidth(treeViewVBoxWidth);
        treeViewVBox.setPrefWidth(treeViewVBoxWidth);

        // 开始设置右边VBox
        showVBox = new VBox();
        showVBox.setMaxHeight(directoryBottomHBoxHeight);
        showVBox.setPrefHeight(directoryBottomHBoxHeight);
        showVBox.setMaxWidth(iconGridPaneWidth);
        showVBox.setPrefWidth(iconGridPaneWidth);
        // 开始设置右边的toolGridPane
        setToolGridPaneLayout();
        // 开始设置右边GridPane
        iconGridPane = new GridPane();
        iconGridPane.setPrefWidth(iconGridPaneWidth);
        iconGridPane.setMaxWidth(iconGridPaneWidth);
        iconGridPane.setPrefHeight(iconGridPaneHeight);
        iconGridPane.setMaxHeight(iconGridPaneHeight);
        iconGridPane.setMaxSize(iconGridPaneWidth,iconGridPaneHeight);
        iconGridPane.setPrefSize(iconGridPaneWidth,iconGridPaneHeight);
        iconGridPane.setPadding(new Insets(5,0,0,0));
        GridPaneUtil.createGridCol(iconGridPane,DirectoryConfig.directoryIconNumCols,iconGridPaneWidth);
        GridPaneUtil.createGridRow(iconGridPane,DirectoryConfig.directoryIconNumRows,iconGridPaneHeight);

        iconGridPane.setStyle("-fx-background-color: White");

        //加入到rootHBox
        showVBox.getChildren().addAll(toolGridPane,iconGridPane);
        rootVBox.getChildren().add(windowGridPane);
        bottomHBox.getChildren().addAll(treeViewVBox,showVBox);
        rootVBox.getChildren().add(bottomHBox);
    }

    // 设置window的烂
    public void setEditorWindowLayout(Stage stage,MissionIconBlock missionIconBlock)
    {
        // 创建布局
        windowGridPane = new GridPane();
        windowGridPane.setPrefWidth(directoryWholeWidth);
        windowGridPane.setMaxWidth(directoryWholeWidth);
        windowGridPane.setPrefHeight(directoryWindowHeight);
        windowGridPane.setMaxHeight(directoryWindowHeight);
        windowGridPane.setMaxSize(directoryWholeWidth,directoryWindowHeight);
        windowGridPane.setPrefSize(directoryWholeWidth,directoryWindowHeight);
        windowGridPane.setStyle("-fx-background-color: #5cd2d2");
        // 2 列 一边一个Hbox 方便调整
        GridPaneUtil.createGridCol(windowGridPane, 2,directoryWholeWidth);
        GridPaneUtil.createGridRow(windowGridPane,1,directoryWindowHeight);


        // 左右布局
        HBox titleHBox = new HBox();
        titleHBox.setAlignment(Pos.CENTER_LEFT);
        titleHBox.setMaxWidth(directoryWholeWidth/2);
        titleHBox.setPrefWidth(directoryWholeWidth/2);
        titleHBox.setMaxHeight(directoryWindowHeight);
        titleHBox.setPrefHeight(directoryWindowHeight);

        HBox windowHBox = new HBox();
        windowHBox.setAlignment(Pos.CENTER_RIGHT);
        windowHBox.setMaxWidth(directoryWholeWidth/2);
        windowHBox.setPrefWidth(directoryWholeWidth/2);
        windowHBox.setPrefHeight(directoryWindowHeight);
        windowHBox.setMaxHeight(directoryWindowHeight);
//        windowHBox.setStyle("-fx-background-color: blue");
        // 文件名Label
        Label label = new Label();
        label.setPadding(new Insets(0,0,0,14));
        label.setText(startFileBlock.getName());
        label.setPrefHeight(directoryWindowHeight);
        label.setMaxHeight(directoryWindowHeight);
        label.setMaxWidth(directoryWholeWidth/2);
        label.setPrefWidth(directoryWholeWidth/2);
        titleHBox.getChildren().add(label);
        // 右边的最小化和关闭
        // 最小化

        VBox minimumImageVBox = new VBox();
        minimumImageVBox.setPadding(new Insets(directoryWindowHeight*0.15,directoryWindowHeight*0.6,directoryWindowHeight*0.15,directoryWindowHeight*0.6));
        minimumImageVBox.setMaxHeight(directoryWindowHeight);
        minimumImageVBox.setPrefHeight(directoryWindowHeight);
        Image minimumImage;
        minimumImage= new Image(getClass().getResourceAsStream(WindowConfig.windowMinimizeImage));
        ImageView minimumBtn = new ImageView(minimumImage);
        minimumBtn.setFitWidth(directoryWindowHeight*0.7);
        minimumBtn.setFitHeight(directoryWindowHeight*0.7);
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
        closeImageVbox.setPadding(new Insets(directoryWindowHeight*0.2,directoryWindowHeight*0.6,directoryWindowHeight*0.2,directoryWindowHeight*0.6));
        closeImageVbox.setMaxHeight(directoryWindowHeight);
        closeImageVbox.setPrefHeight(directoryWindowHeight);
        Image closeImage;
        closeImage = new Image(getClass().getResourceAsStream(WindowConfig.windowCloseImage));
        ImageView closeBtn = new ImageView(closeImage);
        closeBtn.setFitWidth(directoryWindowHeight*0.6);
        closeBtn.setFitHeight(directoryWindowHeight*0.6);
        closeImageVbox.getChildren().add(closeBtn);
        // 设置closeImageVbox的事件
        closeImageVbox.setOnMouseClicked(mouseEvent -> {
            missionIconBlock.closeMissionBlock();
            startFileBlock.setStage(null);
            StageManager.directoryControllers.remove(this);
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
        windowGridPane.add(titleHBox,0,0);
        windowGridPane.add(windowHBox,1,0);

    }

    // 设置右边Vbox的内容
    public void setRightGridPaneContent()
    {
        currentFileBlock = startFileBlock;
        refreshIconGrid(startFileBlock);
    }

    public void refreshIconGridByCurrentFileBlock()
    {
        refreshIconGrid(currentFileBlock);
    }
    // 刷新IconGrid
    public void refreshIconGrid(FileBlock fileBlock0)
    {
        iconGridPane.getChildren().clear();
        directoryLocation = new int[DirectoryConfig.directoryIconNumRows][DirectoryConfig.directoryIconNumCols];
        ArrayList<FileBlock> fileBlocks = fileBlock0.getFileChildrenObjectArrayList();
        for (FileBlock fileBlock: fileBlocks) {
            if (fileBlock.getAttribute() == 3)
            {
                IconPoint iconPoint = GridPaneUtil.searchHorizontalEmptyGrid(directoryLocation,DirectoryConfig.directoryIconNumRows,DirectoryConfig.directoryIconNumCols);
                DirectoryIconBlock directoryIconBlock = new DirectoryIconBlock(fileBlock.getName(),iconPoint,fileBlock,this);
                GridPaneUtil.addIconBlock(iconGridPane,directoryLocation,directoryIconBlock);

            }else if(fileBlock.getAttribute()== 8)
            {
                IconPoint iconPoint = GridPaneUtil.searchHorizontalEmptyGrid(directoryLocation,DirectoryConfig.directoryIconNumRows,DirectoryConfig.directoryIconNumCols);
                DirectoryIconBlock directoryIconBlock = new DirectoryIconBlock(fileBlock.getName(),iconPoint,fileBlock,this);
                GridPaneUtil.addIconBlock(iconGridPane,directoryLocation,directoryIconBlock);
            }
        }
        label.setText("当前文件夹: "+ currentFileBlock.getName());
    }

    // 设置右键创建文件
    private void setRightGridPaneMouseAction() {
        iconGridPane.setOnContextMenuRequested(event -> {
            if (event.getTarget() == iconGridPane) {
                if (preContextMenu != null) {
                    preContextMenu.hide();
                }
                preContextMenu = new ContextMenu();
                MenuItem createFile = new MenuItem("新建文件");
                MenuItem createDirectory= new MenuItem("新建文件夹");
                createFile.setOnAction(actionEvent -> {

                    String result = fileManager.inputFileName(this.currentFileBlock);
                    if (!result.equals("error")) {
                        //获取桌面没有被占用的坐标
                        IconPoint iconPoint = GridPaneUtil.searchHorizontalEmptyGrid(directoryLocation,DirectoryConfig.directoryIconNumRows,DirectoryConfig.directoryIconNumCols);
                        if (iconPoint != null) {
                            FileBlock fileBlock = fileManager.createFile(this.currentFileBlock, result, 3);
                            // 添加桌面图标 并且把fileBlock放入 iconBlock里面
                            DirectoryIconBlock directoryIconBlock = new DirectoryIconBlock(fileBlock.getName(),iconPoint,fileBlock,this);
                            GridPaneUtil.addIconBlock(iconGridPane,directoryLocation, directoryIconBlock);
                            // StageManager.refreshAllTreeView();
                        }else
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("错误信息");
                            alert.setHeaderText("创建文件错误 : 桌面没有位置啦");
                            alert.showAndWait();
                        }
                    }
                });
                createDirectory.setOnAction(actionEvent -> {
                    String result = fileManager.inputFileName(this.currentFileBlock);
                    if (!result.equals("error")) {
                        //获取桌面没有被占用的坐标
                        IconPoint iconPoint = GridPaneUtil.searchHorizontalEmptyGrid(directoryLocation,DirectoryConfig.directoryIconNumRows,DirectoryConfig.directoryIconNumCols);
                        if (iconPoint != null) {
                            FileBlock fileBlock = fileManager.createFile(this.currentFileBlock, result, 8);
                            // 添加桌面图标 并且把fileBlock放入 iconBlock里面
                            DirectoryIconBlock directoryIconBlock = new DirectoryIconBlock(fileBlock.getName(),iconPoint,fileBlock,this);
                            GridPaneUtil.addIconBlock(iconGridPane,directoryLocation, directoryIconBlock);
                            // StageManager.refreshAllTreeView();
                        }else
                        {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("错误信息");
                            alert.setHeaderText("创建文件错误 : 桌面没有位置啦");
                            alert.showAndWait();
                        }
                    }
                });
                preContextMenu.getItems().addAll(createFile,createDirectory);
                preContextMenu.show(iconGridPane, getWindowX()+event.getSceneX(),getWindowY()+event.getSceneY());
                preContextMenu.setAutoHide(true);
            }
        });

        // 全局设置 点击任务栏或者桌面 就会关闭preContextMenu
        iconGrid.setOnMouseClicked(mouseEvent -> {
            if (preContextMenu != null) {
                preContextMenu.hide();
            }
        });
        missionGrid.setOnMouseClicked(mouseEvent -> {
            if (preContextMenu != null) {
                preContextMenu.hide();
            }
        });
        iconGridPane.setOnMouseClicked(MouseEvent ->{
            if (preContextMenu != null) {
                preContextMenu.hide();
            }
        });
    }

    public void setLeftTreeView() throws FileNotFoundException {
        rootTreeView  = new TreeView();
        rootTreeView.setPrefWidth(treeViewVBoxWidth);
        rootTreeView.setMaxWidth(treeViewVBoxWidth);
        rootTreeView.setMaxHeight(directoryBottomHBoxHeight);
        rootTreeView.setPrefHeight(directoryBottomHBoxHeight);
        rootTreeView.setShowRoot(true);
        updateTreeView();
    }

    public ImageView getTreeItemImageView(FileBlock fileBlock) throws FileNotFoundException {
        Image image;
        if(fileBlock.getAttribute() == 3)
        {
            image = new Image(getClass().getResourceAsStream(DesktopConfig.desktopTxtImage));
        }else if (fileBlock.getAttribute() == 8)
        {
            image = new Image(getClass().getResourceAsStream(DesktopConfig.desktopDirectoryImage));
        }else
        {
            image = new Image(getClass().getResourceAsStream(DirectoryConfig.rootDirectoryImage));
        }
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        return imageView;
    }

    private void updateTreeViewWhile(FileBlock fileBlock, TreeItem<String> preTreeItem) throws FileNotFoundException {
        ArrayList<FileBlock> arrayList = fileBlock.getFileChildrenObjectArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            TreeItem<String> objectTreeItem = null;
            FileBlock tempFileBlock = arrayList.get(i);
            if (tempFileBlock.getAttribute() == 3)
            {
                objectTreeItem = new TreeItem<>(tempFileBlock.getName(),getTreeItemImageView(tempFileBlock));
            }else if(tempFileBlock.getAttribute() == 8)
            {
                objectTreeItem = new TreeItem<>(tempFileBlock.getName(),getTreeItemImageView(tempFileBlock));
                updateTreeViewWhile(tempFileBlock,objectTreeItem);
            }
            objectTreeItem.setExpanded(true);
            preTreeItem.getChildren().add(objectTreeItem);
        }
    };

    public void updateTreeView() throws FileNotFoundException {
        treeViewVBox.getChildren().clear();
        FileBlock startFileBlock = FileManager.getInstance().getRootFileBlock();
        TreeItem<String> rootTreeItem = null;
        try {
            rootTreeItem = new TreeItem<>("主目录",getTreeItemImageView(startFileBlock));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        updateTreeViewWhile(startFileBlock,rootTreeItem);
        rootTreeItem.setExpanded(true);
        rootTreeView.setRoot(rootTreeItem);
        treeViewVBox.getChildren().add(rootTreeView);
    }

    // 总和所有方法
    public void setDirectoryStage(Parent root, FileBlock fileBlock, Stage stage, MissionIconBlock missionIconBlock) throws FileNotFoundException {
        this.stage = stage;
        this.startFileBlock = fileBlock;
        currentFileBlock = startFileBlock;
        setDirectoryWindowAction(root,stage);
        setEditorWindowLayout(stage,missionIconBlock);
        setRightLeftBaseLayout();
        setRightGridPaneContent();
        setRightGridPaneMouseAction();
        setLeftTreeView();
    }

    public void closeStage()
    {
        this.stage.close();
        startFileBlock.setStage(null);
    }
    public double getWindowX() {
        return windowX;
    }

    public double getWindowY() {
        return windowY;
    }

    public GridPane getIconGridPane() {
        return iconGridPane;
    }

    public FileBlock getPreFileBlock() {
        return preFileBlock;
    }

    public void setPreFileBlock(FileBlock preFileBlock) {
        this.preFileBlock = preFileBlock;
    }

    public FileBlock getCurrentFileBlock() {
        return currentFileBlock;
    }

    public void setCurrentFileBlock(FileBlock currentFileBlock) {
        this.currentFileBlock = currentFileBlock;
    }
}
