package com.example.file.stage;

import com.example.file.controller.DirectoryController;
import com.example.file.controller.DiskTableController;
import com.example.file.controller.EditorController;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/16 19:05
 */
public class StageManager {

    // 桌面的Stage (主Stage)
    private static Stage desktopStage;

    // 打开的所有文件夹Stage
    public static ArrayList<DirectoryController> directoryControllers = new ArrayList<>();

    // 打开的所有文件Stage
    public static ArrayList<EditorController> editorControllers = new ArrayList<>();
    public static DiskTableController diskTableController;

    // 打开的所有文本编辑器Stage
    public static ArrayList<Stage> EditStages = new ArrayList<>();

    public static Stage getDesktopStage() {
        return desktopStage;
    }

    public static void setDesktopStage(Stage desktopStage) {
        StageManager.desktopStage = desktopStage;
    }

    public static ArrayList<Stage> getEditStages() {
        return EditStages;
    }

    public static void setEditStages(ArrayList<Stage> editStages) {
        EditStages = editStages;
    }

    public static void refreshAllTreeView()
    {

        for (int i = 0; i < directoryControllers.size(); i++) {
            DirectoryController controller = directoryControllers.get(i);
            controller.refreshIconGridByCurrentFileBlock();
            try {
                controller.updateTreeView();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        if(diskTableController != null)
        {
            try {
                diskTableController.updateTreeView();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void closeAllStage()
    {
        for(EditorController controller : editorControllers)
        {
            controller.closeStage();
        }
        for(DirectoryController directoryController : directoryControllers)
        {
            directoryController.closeStage();
        }
        if(diskTableController != null)
        {
            diskTableController.closeStage();
        }
    }
    public static void refreshTableStage()
    {
        if (diskTableController != null)
        {
            diskTableController.refreshTableView();
            diskTableController.refreshPieChart();
        }
    }

}
