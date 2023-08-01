package com.example.file.stage;

import com.example.file.HelloApplication;
import com.example.file.controller.DirectoryController;
import com.example.file.controller.EditorController;
import com.example.file.domain.FileBlock;
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
 * @create 2022/10/25 21:24
 */
public class DirectoryStage {
    private FileBlock fileBlock;

    private MissionIconBlock missionIconBlock;
    public void getDirectoryStage() throws IOException {
        if (fileBlock.getStage() == null)
        {
            Stage stage = new Stage();
            fileBlock.setStage(stage);
            missionIconBlock = new MissionIconBlock(fileBlock,stage);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("directory.fxml"));
            Parent root = fxmlLoader.load();
            DirectoryController directoryController = fxmlLoader.getController();
            StageManager.directoryControllers.add(directoryController);
            directoryController.setDirectoryStage(root,fileBlock,stage,missionIconBlock);
            Scene scene = new Scene(root, DirectoryController.directoryWholeWidth,DirectoryController.directoryWholeHeight);
            stage.initOwner(StageManager.getDesktopStage());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("文本编辑器");
            stage.setScene(scene);
            stage.show();
        }else
        {
            fileBlock.getStage().show();
        }
    }

    public DirectoryStage(FileBlock fileBlock) {
        this.fileBlock = fileBlock;
    }

}
