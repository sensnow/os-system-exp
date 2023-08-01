package com.example.file.stage;

import com.example.file.HelloApplication;
import com.example.file.controller.DirectoryController;
import com.example.file.controller.DiskTableController;
import com.example.file.domain.MissionIconBlock;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/11/6 16:47
 */
public class DiskTableStage {

    public static Stage stage;

    private MissionIconBlock missionIconBlock;

    public static DiskTableStage diskTableStage = new DiskTableStage();

    public void getDirectoryStage() throws IOException {
        if(stage == null)
        {
            stage = new Stage();
            missionIconBlock = new MissionIconBlock(1,stage);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("disktable.fxml"));
            Parent root = fxmlLoader.load();
            DiskTableController diskTableController= fxmlLoader.getController();
            StageManager.diskTableController = diskTableController;
            diskTableController.setDiskTable(root,stage,missionIconBlock);
            Scene scene = new Scene(root, DiskTableController.diskTableWholeWidth,DiskTableController.diskTableWholeHeight);
            stage.initOwner(StageManager.getDesktopStage());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("磁盘管理器");
            stage.setScene(scene);
            stage.show();
        }else{
            stage.show();
        }

    }
}
