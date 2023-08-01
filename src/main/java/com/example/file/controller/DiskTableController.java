package com.example.file.controller;

import com.example.file.config.DesktopConfig;
import com.example.file.config.DirectoryConfig;
import com.example.file.config.WindowConfig;
import com.example.file.domain.*;
import com.example.file.manager.FileManager;
import com.example.file.manager.PanManager;
import com.example.file.stage.DirectoryStage;
import com.example.file.stage.DiskTableStage;
import com.example.file.stage.EditorStage;
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
import java.util.Date;

import static com.example.file.config.DesktopConfig.desktopIconNumCols;
import static com.example.file.config.DesktopConfig.desktopIconNumRows;
import static com.example.file.controller.DesktopController.desktopLocation;
import static com.example.file.controller.DesktopController.iconGrid;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/11/6 14:55
 */
public class DiskTableController {
    @FXML
    private VBox rootVbox;
    private GridPane gridPane;
    private HBox hBox;
    private VBox treeViewVBox;
    private VBox middleVBox;
    private VBox scrollPaneVBox;
    private ScrollPane scrollPane;
    private TextField textField;
    private VBox orderVBox;
    private VBox chartVBox;
    private PieChart pieChart;

    private TableView<TableData> tableView;
    private PanManager panManager = PanManager.getInstance();
    private FileManager fileManager = FileManager.getInstance();
    // 拖动事件的X
    private double xOffset = 0;

    private double yOffset = 0;
    private TreeView<String> rootTreeView;
    public static double diskTableWholeWidth = DesktopController.screenWidth * 0.65;
    public static double diskTableWholeHeight = DesktopController.screenHeight * 0.65;

    // window 状态栏高度
    public static double diskTableWindowHeight = diskTableWholeHeight * 0.04;

    public static double diskTableBottomHBoxHeight = diskTableWholeHeight-diskTableWindowHeight;

    private static double treeViewVBoxWidth = diskTableWholeWidth * 0.2;

    private static double middleVBoxWidth = diskTableWholeWidth * 0.5;

    private static double tableViewWidth = diskTableWholeWidth * 0.3;

    private PieChart.Data data1;

    private PieChart.Data data2;
    private Stage stage;

    public void setWholeStage()
    {
        rootVbox.setMaxWidth(diskTableWholeWidth);
        rootVbox.setPrefWidth(diskTableWholeWidth);
        rootVbox.setMaxHeight(diskTableWholeHeight);
        rootVbox.setPrefHeight(diskTableWholeHeight);
    }

    public void setWindowLayout(Stage stage, MissionIconBlock missionIconBlock)
    {
        // 创建布局
        gridPane = new GridPane();
        gridPane.setPrefWidth(diskTableWholeWidth);
        gridPane.setMaxWidth(diskTableWholeWidth);
        gridPane.setPrefHeight(diskTableWindowHeight);
        gridPane.setMaxHeight(diskTableWindowHeight);
        gridPane.setMaxSize(diskTableWholeWidth,diskTableWindowHeight);
        gridPane.setPrefSize(diskTableWholeWidth,diskTableWindowHeight);
        gridPane.setStyle("-fx-background-color: #5cd2d2");
        // 2 列 一边一个Hbox 方便调整
        GridPaneUtil.createGridCol(gridPane,2,diskTableWholeWidth);
        GridPaneUtil.createGridRow(gridPane,1,diskTableWindowHeight);


        // 左右布局
        HBox titleHBox = new HBox();
        titleHBox.setAlignment(Pos.CENTER_LEFT);
        titleHBox.setMaxWidth(diskTableWholeWidth/2);
        titleHBox.setPrefWidth(diskTableWholeWidth/2);
        titleHBox.setMaxHeight(diskTableWindowHeight);
        titleHBox.setPrefHeight(diskTableWindowHeight);

        HBox windowHBox = new HBox();
        windowHBox.setAlignment(Pos.CENTER_RIGHT);
        windowHBox.setMaxWidth(diskTableWholeWidth/2);
        windowHBox.setPrefWidth(diskTableWholeWidth/2);
        windowHBox.setPrefHeight(diskTableWindowHeight);
        windowHBox.setMaxHeight(diskTableWindowHeight);
//        windowHBox.setStyle("-fx-background-color: blue");
        // 文件名Label
        Label label = new Label();
        label.setPadding(new Insets(0,0,0,14));
        label.setText("磁盘管理");
        label.setPrefHeight(diskTableWindowHeight);
        label.setMaxHeight(diskTableWindowHeight);
        label.setMaxWidth(diskTableWholeWidth/2);
        label.setPrefWidth(diskTableWholeWidth/2);
        titleHBox.getChildren().add(label);
        // 右边的最小化和关闭
        // 最小化
        VBox minimumImageVBox = new VBox();
        minimumImageVBox.setPadding(new Insets(diskTableWindowHeight*0.15,diskTableWindowHeight*0.6,diskTableWindowHeight*0.15,diskTableWindowHeight*0.6));
        minimumImageVBox.setMaxHeight(diskTableWindowHeight);
        minimumImageVBox.setPrefHeight(diskTableWindowHeight);
        Image minimumImage;
        minimumImage= new Image(getClass().getResourceAsStream(WindowConfig.windowMinimizeImage));
        ImageView minimumBtn = new ImageView(minimumImage);
        minimumBtn.setFitWidth(diskTableWindowHeight*0.7);
        minimumBtn.setFitHeight(diskTableWindowHeight*0.7);
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
        closeImageVbox.setPadding(new Insets(diskTableWindowHeight*0.2,diskTableWindowHeight*0.6,diskTableWindowHeight*0.2,diskTableWindowHeight*0.6));
        closeImageVbox.setMaxHeight(diskTableWindowHeight);
        closeImageVbox.setPrefHeight(diskTableWindowHeight);
        Image closeImage;
        closeImage = new Image(getClass().getResourceAsStream(WindowConfig.windowCloseImage));
        ImageView closeBtn = new ImageView(closeImage);
        closeBtn.setFitWidth(diskTableWindowHeight*0.6);
        closeBtn.setFitHeight(diskTableWindowHeight*0.6);
        closeImageVbox.getChildren().add(closeBtn);

        // 设置closeImageVbox的事件
        closeImageVbox.setOnMouseClicked(mouseEvent -> {
            DiskTableStage.stage = null;
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

    public void setBottomLayout()
    {
        hBox = new HBox();
        hBox.setMaxWidth(diskTableWholeWidth);
        hBox.setPrefWidth(diskTableWholeWidth);
        hBox.setMaxHeight(diskTableBottomHBoxHeight);
        hBox.setPrefHeight(diskTableBottomHBoxHeight);
        rootVbox.getChildren().add(hBox);

    }

    public void setLeftTreeView() throws FileNotFoundException {
        rootTreeView  = new TreeView();
        rootTreeView.setPrefWidth(treeViewVBoxWidth);
        rootTreeView.setMaxWidth(treeViewVBoxWidth);
        rootTreeView.setMaxHeight(diskTableBottomHBoxHeight);
        rootTreeView.setPrefHeight(diskTableBottomHBoxHeight);
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

    public void setHBoxInteriorLayout()
    {
        treeViewVBox = new VBox();
        treeViewVBox.setMaxWidth(treeViewVBoxWidth);
        treeViewVBox.setPrefWidth(treeViewVBoxWidth);
        treeViewVBox.setMaxHeight(diskTableBottomHBoxHeight);
        treeViewVBox.setPrefHeight(diskTableBottomHBoxHeight);
        hBox.getChildren().add(treeViewVBox);

        middleVBox = new VBox();
        middleVBox.setMaxWidth(middleVBoxWidth);
        middleVBox.setPrefWidth(middleVBoxWidth);
        middleVBox.setMaxHeight(diskTableBottomHBoxHeight);
        middleVBox.setPrefHeight(diskTableBottomHBoxHeight);
        hBox.getChildren().add(middleVBox);
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

    private void setMiddleVBox() {

        orderVBox = new VBox();
        orderVBox.setMaxWidth(middleVBoxWidth);
        orderVBox.setPrefWidth(middleVBoxWidth);
        orderVBox.setMaxHeight(diskTableBottomHBoxHeight*0.5);
        orderVBox.setPrefHeight(diskTableBottomHBoxHeight*0.5);

        scrollPaneVBox = new VBox();
        scrollPaneVBox.setMaxWidth(middleVBoxWidth*0.9);
        scrollPaneVBox.setPrefWidth(middleVBoxWidth*0.9);
        scrollPaneVBox.setMaxHeight(diskTableBottomHBoxHeight*0.5*0.8);
        scrollPaneVBox.setPrefHeight(diskTableBottomHBoxHeight*0.5*0.8);

        scrollPane = new ScrollPane();
        scrollPane.setMaxWidth(middleVBoxWidth);
        scrollPane.setPrefWidth(middleVBoxWidth);
        scrollPane.setMaxHeight(diskTableBottomHBoxHeight*0.5*0.9);
        scrollPane.setPrefHeight(diskTableBottomHBoxHeight*0.5*0.9);

        scrollPane.setContent(scrollPaneVBox);

        textField = new TextField();
        textField.setMaxWidth(middleVBoxWidth);
        textField.setPrefWidth(middleVBoxWidth);
        textField.setMaxHeight(diskTableBottomHBoxHeight*0.5*0.1);
        textField.setPrefHeight(diskTableBottomHBoxHeight*0.5*0.1);

        orderVBox.getChildren().add(scrollPane);
        orderVBox.getChildren().add(textField);

        chartVBox = new VBox();
        chartVBox.setMaxWidth(middleVBoxWidth);
        chartVBox.setPrefWidth(middleVBoxWidth);
        chartVBox.setMaxHeight(diskTableBottomHBoxHeight*0.5);
        chartVBox.setPrefHeight(diskTableBottomHBoxHeight*0.5);

        pieChart = new PieChart();
        pieChart.setMaxWidth(middleVBoxWidth);
        pieChart.setPrefWidth(middleVBoxWidth);
        pieChart.setMaxHeight(diskTableBottomHBoxHeight*0.5);
        pieChart.setPrefHeight(diskTableBottomHBoxHeight*0.5);

        chartVBox.getChildren().add(pieChart);

        middleVBox.getChildren().add(orderVBox);
        middleVBox.getChildren().add(chartVBox);

        int length = panManager.getUsedCharsData();

        data1 = new PieChart.Data("已经使用的容量 " + String.format("%.2f", length / 8192.0 * 100) + "%", length);
        data2 = new PieChart.Data("未使用的容量 " + String.format("%.2f", (8192-length) / 8192.0 * 100) + "%", 8192 - length);
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(data1,data2);
        pieChart.setData(pieChartData);
    }

    public void refreshPieChart() {

        int length = panManager.getUsedCharsData();
        data1.setName("已经使用的容量 " + String.format("%.2f", length / 8192.0 * 100) + "%");
        data1.setPieValue(length);
        data2.setName("未使用的容量 " + String.format("%.2f", (8192-length) / 8192.0 * 100) + "%");
        data2.setPieValue(8192-length);
    }

    private void addSystemRespondLabel(String str)
    {
        Label label = new Label(new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]").format(new Date()) +" System > " + str);
        label.setTextFill(Color.RED);
        label.setStyle("-fx-font-size: 14");
        scrollPaneVBox.getChildren().add(label);
    }

    private void addClientRequestLabel(String str)
    {
        Label label = new Label(new SimpleDateFormat("[yyyy/MM/dd HH:mm:ss]").format(new Date()) +" Client > " + str);
        label.setStyle("-fx-font-size: 14");
        scrollPaneVBox.getChildren().add(label);
        textField.clear();
    }

    public FileBlock searchFileBlock(FileBlock fileBlock,String str,int attribute)
    {
        FileBlock searchFileBlock = null;
        for (FileBlock file : fileBlock.getFileChildrenObjectArrayList())
        {
           if(file.getName().equals(str)&&file.getAttribute() == attribute)
           {
               return file;
           }
        }
        return null;
    }

    public boolean judgeFileBlockName(FileBlock fileBlock,String str)
    {
        FileBlock searchFileBlock = null;
        for (FileBlock file : fileBlock.getFileChildrenObjectArrayList())
        {
            if(file.getName().equals(str))
            {
                return false;
            }
        }
        return true;
    }

    /*
    *
    * @Param attribute 3是文件 8是目录
    * @Param mode 0 是create 1是delete 2是open
    *
    *
    * */
    private boolean splitPathAndFunction(String str,int attribute,int mode)
    {
        String[] strings = str.split("/");
        FileBlock fileBlock = fileManager.getRootFileBlock();
        // 创建文件
        if(mode == 0)
        {
            if(attribute==3)
            {
                for (int i = 0; i < strings.length; i++) {
                   if (fileBlock == null)
                   {
                       addSystemRespondLabel("找不到所输入的文件信息: " + strings[i]);
                       return false;
                   }
                    if(i==strings.length -1)
                    {
                        if(judgeFileBlockName(fileBlock,strings[i]))
                        {
                            FileBlock file = fileManager.createFile(fileBlock, strings[i], 3);
                            if(strings.length==1)
                            {
                                IconPoint iconPoint = GridPaneUtil.searchVerticalEmptyGrid(desktopLocation, desktopIconNumRows, desktopIconNumCols);
                                if (iconPoint != null) {
                                    // 添加桌面图标 并且把fileBlock放入 iconBlock里面
                                    DesktopIconBlock desktopIconBlock = new DesktopIconBlock(file.getName(), iconPoint, file);
                                    GridPaneUtil.addIconBlock(iconGrid, desktopLocation, desktopIconBlock);
                                    // StageManager.refreshAllTreeView();
                                }
                            }
                            addSystemRespondLabel("创建成功");
                            return true;
                        }else {
                            addSystemRespondLabel("已经有相同的文件名存在");
                        }
                    }else
                    {
                        fileBlock = searchFileBlock(fileBlock,strings[i],8);
                    }
                }
            }else
            {
                for (int i = 0; i < strings.length; i++) {
                    if (fileBlock == null)
                    {
                        addSystemRespondLabel("找不到所输入的文件信息: " + strings[i]);
                        return false;
                    }
                    if(i==strings.length -1)
                    {
                        if(judgeFileBlockName(fileBlock,strings[i]))
                        {
                            FileBlock file = fileManager.createFile(fileBlock, strings[i], 8);
                            if(strings.length==1)
                            {
                                IconPoint iconPoint = GridPaneUtil.searchVerticalEmptyGrid(desktopLocation, desktopIconNumRows, desktopIconNumCols);
                                if (iconPoint != null) {
                                    // 添加桌面图标 并且把fileBlock放入 iconBlock里面
                                    DesktopIconBlock desktopIconBlock = new DesktopIconBlock(file.getName(), iconPoint, file);
                                    GridPaneUtil.addIconBlock(iconGrid, desktopLocation, desktopIconBlock);
                                    // StageManager.refreshAllTreeView();
                                }
                            }
                            addSystemRespondLabel("创建成功");
                            return true;
                        }else {
                            addSystemRespondLabel("已经有相同的文件名存在");
                        }
                    }else
                    {
                        fileBlock = searchFileBlock(fileBlock,strings[i],8);
                    }
                }
            }
        }else if(mode == 1)
        {
            if(attribute==3)
            {
                for (int i = 0; i < strings.length; i++) {
                    if(i!=strings.length-1)
                    {
                        fileBlock = searchFileBlock(fileBlock,strings[i],8);
                    }else
                    {
                        fileBlock = searchFileBlock(fileBlock,strings[i],3);
                    }
                    if (fileBlock == null)
                    {
                        addSystemRespondLabel("找不到所输入的文件信息: " + strings[i]);
                        return false;
                    }
                }
                fileManager.deleteFile(fileBlock);
                ArrayList<DesktopIconBlock> desktopIconBlocks = DesktopIconBlock.desktopIconBlocks;
                for (int i = 0; i < desktopIconBlocks.size() ; i++) {
                    DesktopIconBlock desktopIconBlock = desktopIconBlocks.get(i);
                    if(desktopIconBlock.getFileBlock() == fileBlock)
                    {
                        GridPaneUtil.removeIconBlock(iconGrid, desktopLocation, desktopIconBlock);
                    }
                }
                addSystemRespondLabel("删除成功");
                return true;
            }else {
                for (int i = 0; i < strings.length; i++) {
                    fileBlock = searchFileBlock(fileBlock, strings[i], 8);
                    if (fileBlock == null) {
                        addSystemRespondLabel("找不到所输入的文件信息: " + strings[i]);
                        return false;
                    }
                }
                fileManager.deleteFile(fileBlock);
                ArrayList<DesktopIconBlock> desktopIconBlocks = DesktopIconBlock.desktopIconBlocks;
                for (int i = 0; i < desktopIconBlocks.size() ; i++) {
                    DesktopIconBlock desktopIconBlock = desktopIconBlocks.get(i);
                    if(desktopIconBlock.getFileBlock() == fileBlock)
                    {
                        GridPaneUtil.removeIconBlock(iconGrid, desktopLocation, desktopIconBlock);
                    }
                }
                addSystemRespondLabel("删除成功");
                return true;
            }
        }else if(mode == 2)
        {
            if(attribute==3)
            {
                for (int i = 0; i < strings.length; i++) {
                    if(i!=strings.length-1)
                    {
                        fileBlock = searchFileBlock(fileBlock,strings[i],8);
                    }else
                    {
                        fileBlock = searchFileBlock(fileBlock,strings[i],3);
                    }
                    if (fileBlock == null)
                    {
                        addSystemRespondLabel("找不到所输入的文件信息: " + strings[i]);
                        return false;
                    }
                }
                EditorStage editorStage = new EditorStage(fileBlock);
                try {
                    editorStage.getEditorStage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                addSystemRespondLabel("打开成功");
                return true;
            }else {
                for (int i = 0; i < strings.length; i++) {
                    fileBlock = searchFileBlock(fileBlock, strings[i], 8);
                    if (fileBlock == null) {
                        addSystemRespondLabel("找不到所输入的文件信息: " + strings[i]);
                        return false;
                    }
                }
                DirectoryStage directoryStage = new DirectoryStage(fileBlock);
                try {
                    directoryStage.getDirectoryStage();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                addSystemRespondLabel("打开成功");
                return true;
            }
        }
        return true;
    }
    public void setOrderVBox() {
        textField.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode()== KeyCode.ENTER)
            {

                String[] strings = textField.getText().split(" ");
                addClientRequestLabel(textField.getText());
                if(strings.length != 3)
                {
                    addSystemRespondLabel("无效命令");
                    return;
                }
                if(strings[0].equals("create"))
                {
                    if(strings[1].equals("dir"))
                    {
                        splitPathAndFunction(strings[2],8,0);
                    }else if(strings[1].equals("txt")){
                        splitPathAndFunction(strings[2],3,0);
                    }else {
                        addSystemRespondLabel("无效命令");
                    }

                }else if(strings[0].equals("delete"))
                {
                    if(strings[1].equals("dir"))
                    {
                        splitPathAndFunction(strings[2],8,1);
                    }else if(strings[1].equals("txt")){
                        splitPathAndFunction(strings[2],3,1);
                    }else {
                        addSystemRespondLabel("无效命令");
                    }

                }else if(strings[0].equals("open"))
                {
                    if(strings[1].equals("dir"))
                    {
                        splitPathAndFunction(strings[2],8,2);
                    }else if(strings[1].equals("txt")){
                        splitPathAndFunction(strings[2],3,2);
                    }else {
                        addSystemRespondLabel("无效命令");
                    }
                }else{
                    addSystemRespondLabel("无效命令");
                }

            }
        });
    }

    public void setTableView()
    {
        tableView = new TableView();
        tableView.setMaxWidth(tableViewWidth);
        tableView.setPrefWidth(tableViewWidth);
        tableView.setMaxHeight(diskTableBottomHBoxHeight);
        tableView.setPrefHeight(diskTableBottomHBoxHeight);

        TableColumn tab1 = new TableColumn<>("Index");
        tab1.setPrefWidth(tableViewWidth * 1/3 *0.95);
        tab1.setMaxWidth(tableViewWidth *1/3 *0.95);
        tab1.setSortable(false);
        tab1.setCellValueFactory(new PropertyValueFactory<TableData,Integer>("index"));
        tab1.setStyle("-fx-alignment: CENTER;");
        TableColumn tab2 = new TableColumn<>("NextIndex");
        tab2.setPrefWidth(tableViewWidth *1/3 *0.95);
        tab2.setMaxWidth(tableViewWidth *1/3 *0.95);
        tab2.setSortable(false);
        tab2.setCellValueFactory(new PropertyValueFactory<TableData,Integer>("nextIndex"));
        tab2.setStyle("-fx-alignment: CENTER;");
        TableColumn tab3 = new TableColumn<>("Used");
        tab3.setPrefWidth(tableViewWidth *1/3 *0.95);
        tab3.setMaxWidth(tableViewWidth *1/3 *0.95);
        tab3.setSortable(false);
        tab3.setCellValueFactory(new PropertyValueFactory<TableData,Integer>("used"));
        tab3.setStyle("-fx-alignment: CENTER;");
        refreshTableView();
        tableView.getColumns().addAll(tab1,tab2,tab3);

        hBox.getChildren().add(tableView);

    }

    public void refreshTableView()
    {
        ObservableList<TableData> data = FXCollections.observableArrayList();
        ArrayList<PanBlock> panBlockArrayList = panManager.getPanBlockArrayList();
        for (int i = 0; i < panBlockArrayList.size(); i++) {
            PanBlock panBlock = panBlockArrayList.get(i);
            data.add(new TableData(panBlock.getBlockIndex(),panBlock.getNextIndex(),panBlock.isUsed() ? "Y" : "N"));
        }
        tableView.setItems(data);
    }

    public void setDiskTable(Parent root,Stage stage,MissionIconBlock missionIconBlock) throws FileNotFoundException {
        this.stage = stage;
        setWholeStage();
        setWindowAction(root,stage);
        setWindowLayout(stage,missionIconBlock);
        setBottomLayout();
        setHBoxInteriorLayout();
        setLeftTreeView();
        setMiddleVBox();
        setOrderVBox();
        setTableView();
        refreshTableView();
    }

    public void closeStage()
    {
        this.stage.close();
    }



}
