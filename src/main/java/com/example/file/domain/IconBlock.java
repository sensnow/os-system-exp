package com.example.file.domain;

import com.example.file.config.DesktopConfig;
import com.example.file.manager.FileManager;
import com.example.file.stage.DirectoryStage;
import com.example.file.stage.EditorStage;
import com.example.file.util.GridPaneUtil;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

import static com.example.file.controller.DesktopController.*;
import static com.example.file.controller.DesktopController.preContextMenu;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/26 8:10
 */
public abstract class IconBlock {

    public abstract VBox getIconBlock();

    public abstract IconPoint getIconPoint();

    public abstract void setIconPoint(IconPoint iconPoint);
}
