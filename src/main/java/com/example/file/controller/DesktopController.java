package com.example.file.controller;

import com.example.file.domain.FileBlock;
import com.example.file.domain.DesktopIconBlock;
import com.example.file.domain.IconPoint;
import com.example.file.manager.DataManager;
import com.example.file.manager.FileManager;
import com.example.file.manager.PanManager;
import com.example.file.stage.StageManager;
import com.example.file.util.GridPaneUtil;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static com.example.file.config.DesktopConfig.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/15 15:03
 */
public class DesktopController {
    static {
        DataManager.InputStore();
    }
    /*
     *  获取屏幕的分辨率
     *  ScreenWidth 宽度
     *  ScreenHeight 高度
     *  preIconWidth 每个Icon的宽度
     *  preIconHeight 每个Icon的高度
     *  emptyVboxHeight 空置层 的高度 (占用preIconHeight的0.6)
     *  missionGridPaneHeight 任务栏 的高度 (占用preIconHeight的 0.4);
     * */
    public static double screenWidth = Screen.getPrimary().getBounds().getWidth();
    public static double screenHeight = Screen.getPrimary().getBounds().getHeight();
    public static double preIconWidth = screenWidth / desktopIconNumCols;
    public static double preIconHeight = screenHeight / desktopIconNumRows;
    public static double emptyVboxHeight = preIconHeight * 0.6;
    public static double missionGridPaneHeight = preIconHeight * 0.4;
    // 任务栏 missionGrid 的每一个grid的宽度大小
    public static double missionGridPaneWidth = screenWidth / missionIconNumCols;
    /*
    桌面的图标控制数量，防止重叠
    0 代表的是没有被占用
    1 代表的是已经被占用
    默认新建都是 0
    */
    public static int[][] desktopLocation = new int[desktopIconNumRows][desktopIconNumCols];

    /*
    任务栏的图标控制数量，防止重叠
    0 代表的是没有被占用
    1 代表的是已经被占用
    默认新建都是 0
    */
    public static int[][] missionLocation = new int[missionIconNumRows][missionIconNumCols];

    // 获取文件管理器
    private static FileManager fileManager = FileManager.getInstance();

    // 获取磁盘管理器
    private static PanManager panManager = PanManager.getInstance();

    //只能出现唯一的桌面右键，此处存的是上一次new的contextmenu对象
    public static ContextMenu preContextMenu;

    // 桌面Icon设置
    // 图标桌面布局GridPane
    public static GridPane iconGrid;

    // 桌面端的任务栏 GridPane
    public static GridPane missionGrid;

    // 获取桌面端root
    @FXML
    private VBox rootVbox;


    // 创建桌面的布局
    public void setDesktopLayout() {
        /*
         *   1. 添加IconGridPane的布局
         *   2. 添加中间的空层，避免与任务栏太过于接近
         *   3. 添加任务栏
         * */

        iconGrid = new GridPane();
        iconGrid.setPrefSize(screenWidth, preIconHeight * 8);
        iconGrid.setMaxSize(screenWidth, preIconHeight * 8);
        GridPaneUtil.createGridCol(iconGrid, desktopIconNumCols, screenWidth);
        GridPaneUtil.createGridRow(iconGrid, desktopIconNumRows, screenHeight);
        //添加到根box
        rootVbox.getChildren().add(iconGrid);

        //中间空置层
        //任务栏占用最后一层preHeight的 6/10
        VBox emptyVbox = new VBox();
        emptyVbox.setMaxHeight(emptyVboxHeight);
        emptyVbox.setPrefHeight(emptyVboxHeight);
        emptyVbox.setPrefWidth(screenWidth);
        emptyVbox.setMaxWidth(screenWidth);
        //添加到根box
        rootVbox.getChildren().add(emptyVbox);

        //添加任务栏
        missionGrid = new GridPane();
        missionGrid.setMaxHeight(missionGridPaneHeight);
        missionGrid.setPrefHeight(missionGridPaneHeight);
        missionGrid.setPrefSize(screenWidth, missionGridPaneHeight);
        missionGrid.setMaxSize(screenWidth, missionGridPaneHeight);
        GridPaneUtil.createGridCol(missionGrid, missionIconNumCols, screenWidth);
        missionGrid.setStyle("-fx-background-color: transparent");
        rootVbox.getChildren().add(missionGrid);


    }

    // 设置桌面鼠标右键打开菜单事件
    public void setDesktopMouseAction() {
        iconGrid.setOnContextMenuRequested(event -> {
            if (event.getTarget() == iconGrid) {
                if (preContextMenu != null) {
                    preContextMenu.hide();
                }
                preContextMenu = new ContextMenu();
                MenuItem createFile = new MenuItem("新建文件");
                MenuItem createDirectory = new MenuItem("新建文件夹");
                createFile.setOnAction(actionEvent -> {
                    //获取桌面没有被占用的坐标
                    String result = fileManager.inputFileName(fileManager.getRootFileBlock());
                    if (!result.equals("error")) {
                        // 创建文件
                        IconPoint iconPoint = GridPaneUtil.searchVerticalEmptyGrid(desktopLocation, desktopIconNumRows, desktopIconNumCols);
                        if (iconPoint != null) {
                            FileBlock fileBlock = fileManager.createFile(fileManager.getRootFileBlock(), result, 3);
                            // 添加桌面图标 并且把fileBlock放入 iconBlock里面
                            DesktopIconBlock desktopIconBlock = new DesktopIconBlock(fileBlock.getName(), iconPoint, fileBlock);
                            GridPaneUtil.addIconBlock(iconGrid, desktopLocation, desktopIconBlock);
                            panManager.showPanBlockUsageStatus();
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
                    //获取桌面没有被占用的坐标
                    String result = fileManager.inputFileName(fileManager.getRootFileBlock());
                    if (!result.equals("error")) {
                        // 创建文件
                        IconPoint iconPoint = GridPaneUtil.searchVerticalEmptyGrid(desktopLocation, desktopIconNumRows, desktopIconNumCols);
                        if (iconPoint != null) {
                            FileBlock fileBlock = fileManager.createFile(fileManager.getRootFileBlock(), result, 8);
                            // 添加桌面图标 并且把fileBlock放入 iconBlock里面
                            DesktopIconBlock desktopIconBlock = new DesktopIconBlock(fileBlock.getName(), iconPoint, fileBlock);
                            GridPaneUtil.addIconBlock(iconGrid, desktopLocation, desktopIconBlock);
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
                preContextMenu.getItems().addAll(createFile, createDirectory);
                preContextMenu.show(iconGrid, event.getSceneX(), event.getSceneY());
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
    }

    //默认的桌面图标布局设置
    public void setDesktopIconLocationInit() {

        desktopLocation[0][0] = 1;
        desktopLocation[1][0] = 1;
        //桌面壁纸
        rootVbox.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream(desktopPictureImage)), BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
        // 相关帮助
        GridPaneUtil.addIconBlock(iconGrid, desktopLocation, new DesktopIconBlock("相关帮助", desktopHelpImage, new IconPoint(0, 0)));
        GridPaneUtil.addIconBlock(iconGrid, desktopLocation, new DesktopIconBlock("磁盘信息", desktopDiskImage, new IconPoint(1, 0)));

    }

    public void setMissionGridPaneLayout() {

        // 设置占用
        // 占用第一个
        missionLocation[0][0] = 1;
        // 占用最后一个
        missionLocation[0][missionIconNumCols - 1] = 1;
        //设置window
        HBox window = new HBox();
        window.setMaxWidth(missionGridPaneWidth);
        window.setMaxHeight(missionGridPaneHeight);
        window.setPrefWidth(missionGridPaneWidth);
        window.setPrefHeight(missionGridPaneHeight);
        window.setAlignment(Pos.CENTER);
        // 载入图片
        Image image = null;
        image = new Image(getClass().getResourceAsStream(desktopWindowImage));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(missionGridPaneHeight * 0.85);
        imageView.setFitHeight(missionGridPaneHeight * 0.85);
        imageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("关机提示框");
                alert.setHeaderText("请确认是否关机！");
                alert.setContentText("请选择：");
                Image image1 = new Image(getClass().getResourceAsStream("/com/example/file/image/closepc.png"));
                ImageView imageView1 = new ImageView(image1);
                imageView1.setFitWidth(40);
                imageView1.setFitHeight(40);
                alert.setGraphic(imageView1);
                ButtonType buttonTypeOne = new ButtonType("保存并关机");
                ButtonType buttonTypeTwo = new ButtonType("关机");
                ButtonType buttonTypeCancel = new ButtonType("取消", ButtonBar.ButtonData.CANCEL_CLOSE);

                alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeCancel);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == buttonTypeOne){
                    StageManager.closeAllStage();
                    DataManager.OutputStore();
                    StageManager.getDesktopStage().close();
                    // ... user chose "保存并关机"
                } else if (result.get() == buttonTypeTwo) {
                    StageManager.closeAllStage();
                    StageManager.getDesktopStage().close();
                    // user chose "关机"
                } else {
                    // user chose CANCEL or closed the dialog
                }
            }
        });

        // 放入window中
        window.getChildren().add(imageView);
        // 放入missionGrip
        missionGrid.add(window, 0, 0);

        VBox vBox = new VBox();
        vBox.setPrefHeight(missionGridPaneHeight);
        vBox.setMaxHeight(missionGridPaneHeight);
        vBox.setPrefWidth(missionGridPaneWidth);
        vBox.setMaxWidth(missionGridPaneWidth);
        Label timeLabel = new Label();
        timeLabel.setTextFill(Color.WHITE);
        timeLabel.setPrefWidth(missionGridPaneWidth);
        timeLabel.setMaxWidth(missionGridPaneWidth);
        timeLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 12");
        timeLabel.setMaxHeight(missionGridPaneHeight / 2);
        timeLabel.setPrefHeight(missionGridPaneHeight / 2);
        timeLabel.setAlignment(Pos.CENTER);
        Label dateLabel = new Label();
        dateLabel.setTextFill(Color.WHITE);
        dateLabel.setStyle("-fx-font-weight: bold;-fx-font-size: 12");
        dateLabel.setMaxHeight(missionGridPaneHeight / 2);
        dateLabel.setPrefHeight(missionGridPaneHeight / 2);
        dateLabel.setPrefWidth(missionGridPaneWidth);
        dateLabel.setMaxWidth(missionGridPaneWidth);
        dateLabel.setAlignment(Pos.CENTER);

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        //设置时间更新
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), actionEvent -> {
            Date date = new Date();
            dateLabel.setText(dateFormat.format(date));
            timeLabel.setText(timeFormat.format(date));
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        vBox.getChildren().addAll(timeLabel, dateLabel);
        missionGrid.add(vBox, missionIconNumCols - 1, 0);


    }

    public void initDesktopIconBlock()
    {
        FileBlock rootFileBlock = fileManager.getRootFileBlock();
        for(FileBlock fileBlock: rootFileBlock.getFileChildrenObjectArrayList())
        {
            IconPoint iconPoint = GridPaneUtil.searchVerticalEmptyGrid(desktopLocation, desktopIconNumRows, desktopIconNumCols);
            DesktopIconBlock desktopIconBlock = new DesktopIconBlock(fileBlock.getName(), iconPoint, fileBlock);
            GridPaneUtil.addIconBlock(iconGrid, desktopLocation, desktopIconBlock);
        }
    }

    public void test() {

        // 创建桌面布局
        setDesktopLayout();
        // 设置桌面右键事件
        setDesktopMouseAction();
        // 设置桌面的默认软件
        setDesktopIconLocationInit();
        // 任务栏布局
        setMissionGridPaneLayout();
        // 生成桌面
        initDesktopIconBlock();
        // 磁盘管理器初始化
        panManager.initDickPan();

    }

}
