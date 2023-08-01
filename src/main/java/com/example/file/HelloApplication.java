package com.example.file;

import com.example.file.config.DesktopConfig;
import com.example.file.controller.DesktopController;
import com.example.file.stage.StageManager;
import com.example.file.util.MathUtil;
import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("desktop.fxml"));
        Parent root = fxmlLoader.load();
        DesktopController desktopController = fxmlLoader.getController();
        desktopController.test();
        stage.setMaximized(true);
        stage.setResizable(false);
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getBounds();
        Scene scene = new Scene(root, DesktopController.screenWidth,DesktopController.screenHeight);
        stage.setTitle("模拟文件系统");
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("/com/example/file/image/systemicon.png"));
        stage.show();

        // 将桌面的Stage存进StageManger里面
        StageManager.setDesktopStage(stage);

    }

    public static void main(String[] args) {
        launch();
    }
}