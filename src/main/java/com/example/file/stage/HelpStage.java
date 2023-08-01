package com.example.file.stage;

import com.example.file.HelloApplication;
import com.example.file.controller.DiskTableController;
import com.example.file.controller.HelpController;
import com.example.file.domain.MissionIconBlock;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

import static com.example.file.controller.HelpController.helpWholeHeight;
import static com.example.file.controller.HelpController.helpWholeWidth;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/11/8 0:22
 */
public class HelpStage {

    public static Stage stage;

    private MissionIconBlock missionIconBlock;

    public static HelpStage helpStage = new HelpStage();

    public void getDirectoryStage() throws IOException {
        if(stage == null)
        {
            stage = new Stage();
            missionIconBlock = new MissionIconBlock(0,stage);
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("help.fxml"));
            Parent root = fxmlLoader.load();
            HelpController controller = fxmlLoader.getController();
            controller.setHelp(root,stage,missionIconBlock);
            Scene scene = new Scene(root, helpWholeWidth,helpWholeHeight);
            stage.initOwner(StageManager.getDesktopStage());
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("关于我们");
            stage.setScene(scene);
            stage.show();
        }else{
            stage.show();
        }

    }
}
