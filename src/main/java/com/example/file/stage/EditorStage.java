package com.example.file.stage;

import com.example.file.HelloApplication;
import com.example.file.controller.DesktopController;
import com.example.file.controller.DirectoryController;
import com.example.file.controller.EditorController;
import com.example.file.domain.FileBlock;
import com.example.file.domain.MissionIconBlock;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/16 16:57
 */
public class EditorStage {

    private FileBlock fileBlock;

    private MissionIconBlock missionIconBlock;

    public void getEditorStage() throws IOException {
        if (fileBlock.getStage() == null)
        {
            Stage stage = new Stage();
            fileBlock.setStage(stage);

            missionIconBlock = new MissionIconBlock(fileBlock,stage);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("editor.fxml"));
            Parent root = fxmlLoader.load();
            EditorController editorController = fxmlLoader.getController();
            StageManager.editorControllers.add(editorController);
            editorController.setEditorVbox(root,stage,fileBlock,missionIconBlock);
            Scene scene = new Scene(root, EditorController.editorWholeWidth,EditorController.editorWholeHeight);
            stage.initOwner(StageManager.getDesktopStage());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("文件夹");
            stage.setScene(scene);
            stage.show();
        }else
        {
            fileBlock.getStage().show();
        }
    }

    public EditorStage(FileBlock fileBlock) {
        this.fileBlock = fileBlock;
    }

}
