package com.example.file.domain;

import com.example.file.config.DesktopConfig;
import com.example.file.manager.FileManager;
import com.example.file.stage.*;
import com.example.file.util.GridPaneUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.file.controller.DesktopController.*;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/15 15:03
 */
public class DesktopIconBlock extends IconBlock {
    private VBox vBox = new VBox();
    private Label label = new Label();
    private ImageView imageView = new ImageView();
    private Image image;
    private FileBlock fileBlock;

    private static FileManager fileManager = FileManager.getInstance();
    // 记录在桌面的位置
    private IconPoint iconPoint;

    public static ArrayList<DesktopIconBlock> desktopIconBlocks = new ArrayList<>();
    // IconBlock 构造方法
    public DesktopIconBlock(String fileName, String picturePath, IconPoint iconPoint) {
        this.iconPoint = iconPoint;
        image = new Image(getClass().getResourceAsStream(picturePath));
        desktopIconBlocks.add(this);
        label.setText(fileName);

        // 设置Icon的一系列操作
        // 1.设置能看到的 图片 文字
        setIconBlock();
        // 2.设置打开
        setSystemLeftAction();

    }

    public DesktopIconBlock(String fileName, IconPoint iconPoint, FileBlock fileBlock) {
        this.iconPoint = iconPoint;
        this.fileBlock = fileBlock;
        String picturePath = null;
        if (fileBlock.getAttribute() == 8) {
            picturePath = DesktopConfig.desktopDirectoryImage;
        } else if (fileBlock.getAttribute() == 3) {
            picturePath = DesktopConfig.desktopTxtImage;
        }
        image = new Image(getClass().getResourceAsStream(picturePath));
        desktopIconBlocks.add(this);
        label.setText(fileName);

        // 设置Icon的一系列操作
        // 1.设置能看到的 图片 文字
        setIconBlock();
        // 2.设置右键
        setIconContextMenu();
        // 3.设置左键
        setIconLeftAction();
    }

    public void setSystemLeftAction() {
        vBox.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton().name().equals("PRIMARY")) {
                // 相关帮助
                if (iconPoint.getRow() == 0 && iconPoint.getCol() == 0) {
                    HelpStage helpStage = HelpStage.helpStage;
                    try {
                        helpStage.getDirectoryStage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                // 磁盘信息
                else if (iconPoint.getRow() == 1 && iconPoint.getCol() == 0) {
                    DiskTableStage diskTableStage = DiskTableStage.diskTableStage;
                    try {
                        diskTableStage.getDirectoryStage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }
    private void setIconLeftAction() {
        vBox.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2 && mouseEvent.getButton().name().equals("PRIMARY")) {
                if (fileBlock.getAttribute() == 3) {
                    EditorStage editorStage = new EditorStage(fileBlock);
                    try {
                        editorStage.getEditorStage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (fileBlock.getAttribute() == 8) {
                    DirectoryStage directoryStage = new DirectoryStage(fileBlock);
                    try {
                        directoryStage.getDirectoryStage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    // 初始化图标
    public void setIconBlock() {

        // 图标 Vbox
        vBox.setPadding(new Insets(5));
        vBox.setMaxHeight(70);
        vBox.setMaxWidth(70);
        vBox.setAlignment(Pos.CENTER);
        vBox.setOnMouseEntered(mouseEvent -> {
            vBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.2);-fx-border-radius: 25;-fx-border-width: 1");
        });
        vBox.setOnMouseExited(mouseEvent -> {
            vBox.setStyle("");
        });

        // 图标 图片
        imageView.setImage(image);
        imageView.setFitWidth(50);
        imageView.setFitHeight(50);

        // 图标 名称
        label.setAlignment(Pos.CENTER);
        label.setMaxWidth(70);
        label.setMaxHeight(30);
        label.setTextFill(Color.WHITE);
        label.setPadding(new Insets(5, 0, 0, 0));
        label.setStyle("-fx-text-alignment: center;-fx-font-size: 11;");
        label.autosize();
        vBox.getChildren().addAll(imageView, label);

    }

    /*
     *
     *   3 是普通文件
     *   8 是目录
     *
     * */
    private void setIconContextMenu() {
        vBox.setOnContextMenuRequested(event -> {
            if (preContextMenu != null) {
                preContextMenu.hide();
            }
            preContextMenu = new ContextMenu();
            MenuItem open = new MenuItem("打开");
            open.setOnAction(actionEvent -> {
                if (fileBlock.getAttribute() == 3) {
                    EditorStage editorStage = new EditorStage(fileBlock);
                    try {
                        editorStage.getEditorStage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else if (fileBlock.getAttribute() == 8) {
                    DirectoryStage directoryStage = new DirectoryStage(fileBlock);
                    try {
                        directoryStage.getDirectoryStage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            MenuItem delete = new MenuItem("删除");
            delete.setOnAction(actionEvent -> {
                fileManager.deleteFile(fileBlock);
                GridPaneUtil.removeIconBlock(iconGrid, desktopLocation, this);
                // StageManager.refreshAllTreeView();
            });
            MenuItem rename = new MenuItem("重命名");
            rename.setOnAction(actionEvent -> {
                String result = fileManager.inputFileName(fileManager.getRootFileBlock());
                if (!result.equals("error")) {
                    this.fileBlock.setName(result);
                    this.label.setText(result);
                    fileManager.setDirectoryPanBlockChange(this.fileBlock.getParentFileBlock());
                    StageManager.refreshAllTreeView();
                }
            });
            MenuItem trait = new MenuItem("文件属性");
            trait.setOnAction(actionEvent -> {
                List<String> choices = new ArrayList<>();
                choices.add("只读");
                choices.add("读与写");
                ChoiceDialog<String> dialog = new ChoiceDialog<>("读与写", choices);
                dialog.setTitle("文件属性");
                dialog.setHeaderText("以下选择文件的属性");
                dialog.setContentText("请选择:");
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    if (result.get().equals("只读")) {
                        this.fileBlock.setWrTrait(0);
                    } else {
                        this.fileBlock.setWrTrait(1);
                    }
                }
            });
            if(fileBlock.getAttribute() == 3)
            {
                preContextMenu.getItems().addAll(open, rename,trait, delete);
            }else {
                preContextMenu.getItems().addAll(open, rename, delete);
            }
            preContextMenu.show(vBox, event.getSceneX(), event.getSceneY());
            preContextMenu.setAutoHide(true);
        });

    }


    // 获取图标Vbox
    public VBox getIconBlock() {
        return vBox;
    }

    public IconPoint getIconPoint() {
        return iconPoint;
    }

    public void setIconPoint(IconPoint iconPoint) {
        this.iconPoint = iconPoint;
    }

    public FileBlock getFileBlock() {
        return fileBlock;
    }
}
