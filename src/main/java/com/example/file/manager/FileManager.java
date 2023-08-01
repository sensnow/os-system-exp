package com.example.file.manager;

import com.example.file.domain.FileBlock;
import com.example.file.domain.PanBlock;
import com.example.file.stage.StageManager;
import com.example.file.util.StringUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/15 15:03
 */
public class FileManager {

    // 存着所有FileBlock的的链
    private ArrayList<FileBlock> fileBlocks = DataManager.fileData.getFileBlocks();
    // 桌面的rootFileBlock
    private FileBlock rootFileBlock = DataManager.fileData.getRootFileBlock();
    // 获取磁盘管理器单例
    private PanManager panManager = PanManager.getInstance();

    private static FileManager fileManager;

    public static FileManager getInstance()
    {
        if (fileManager == null)
        {
            fileManager = new FileManager();
        }
        return fileManager;
    }

    // 输入文件
    public String inputFileName(FileBlock fileBlock)
    {

        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("输入窗");
        textInputDialog.setHeaderText("输入文件名(三个字符以内)");
        ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/com/example/file/image/input.png")));
        imageView1.setFitHeight(40);
        imageView1.setFitWidth(40);
        textInputDialog.setGraphic(imageView1);
        Optional<String> result = textInputDialog.showAndWait();
        Image image;
        image = new Image(getClass().getResourceAsStream("/com/example/file/image/warning.png"));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(40);
        imageView.setFitHeight(40);
        if (result.isPresent() && !result.get().equals("") && result.get().length()<=3)
        {
            // 是否有同名文件
            Boolean flag = false;
            ArrayList<FileBlock> fileChildrenObjectArrayList = fileBlock.getFileChildrenObjectArrayList();
            for(FileBlock fileBlock1 : fileChildrenObjectArrayList)
            {
                if(fileBlock1.getName().equals(result.get()))
                {
                    flag = true;
                    break;
                }
            }
            if(!flag)
            {
                return result.get();

            }else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误信息");
                alert.setHeaderText("创建文件错误 : 有相同文件名字的文件存在");
                alert.setGraphic(imageView);
                alert.showAndWait();
            }
        }else if(result.isPresent() &&result.get().equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误信息");
            alert.setHeaderText("创建文件错误 : 文件名字不能为空");
            alert.setGraphic(imageView);
            alert.showAndWait();
        }else if(result.isPresent())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误信息");
            alert.setHeaderText("创建文件错误 : 文件名过长");
            alert.setGraphic(imageView);
            alert.showAndWait();
        }
        return "error";
    }

    public FileBlock createFile(FileBlock parentFileBlock,String filename,int attribute)
    {
        // 搜索空余的盘
        int emptyIndex = panManager.getEmptyBlock();
        // 创建文件Block
        FileBlock fileBlock = new FileBlock(filename,attribute, emptyIndex,parentFileBlock);
        // 加入全部文件list
        this.fileBlocks.add(fileBlock);
        // 设置上一个盘内容更新
        parentFileBlock.getFileChildrenObjectArrayList().add(fileBlock);
        setDirectoryPanBlockChange(parentFileBlock);
        panManager.showPanBlockUsageStatus();
        StageManager.refreshAllTreeView();
        StageManager.refreshTableStage();
        return fileBlock;
    }

    // directory pan增删
    public void setDirectoryPanBlockChange(FileBlock fileBlock)
    {
        int necessaryPanLength =(int)Math.ceil(fileBlock.getFileChildrenObjectArrayList().size()/8.0);
        // 没这个会直接
        necessaryPanLength = necessaryPanLength == 0 ? 1: necessaryPanLength;
        int nextPanBlockIndex = fileBlock.getStartPanBlock();
        int prePanBlockIndex = 0;
        if(fileBlock.getPanBlockLength() > necessaryPanLength)
        {
            // 寻找
            int blockGap = fileBlock.getPanBlockLength() - necessaryPanLength;
            int panBlock = fileBlock.getStartPanBlock();
            for (int i = 0; i < necessaryPanLength; i++) {
                panBlock = panManager.getPanBlockByIndex(panBlock).getNextIndex();
            }
            // 回收
            int panBlock1 = panBlock;
            for (int i = 0; i < blockGap; i++) {
                panBlock1 = panManager.getPanBlockByIndex(panBlock1).getNextIndex();
                panManager.reclaimPanBlock(panBlock);
                panBlock = panBlock1;
            }
        }
        ArrayList<FileBlock> arrayList = fileBlock.getFileChildrenObjectArrayList();
        for (int i = 1; i <= necessaryPanLength; i++) {
            if(i <= fileBlock.getPanBlockLength())
            {
                // 重新写数据
                PanBlock panBlock = panManager.getPanBlockByIndex(nextPanBlockIndex);
                char[] content = panBlock.getContent();
                StringBuilder builder = new StringBuilder();
                // 赋值上一个panBlock
                prePanBlockIndex = nextPanBlockIndex;
                nextPanBlockIndex = panBlock.getNextIndex();
                for (int j = (i-1) * 8; j < Math.min(i*8,arrayList.size()); j++) {
                    FileBlock fileBlock1 = arrayList.get(j);
                    String str = new String(fileBlock1.getName() + "  " + fileBlock1.getAttribute() + '*' + '*');
                    builder.append(str);
                }
                builder.getChars(0,builder.length(),content,0);
            }else
            {
                /*
                 * 原来的panBlock已经不够用了 需要申请新的
                 * */
                //获取新的panBlock
                int emptyPanBlockIndex = panManager.getEmptyBlock();
                PanBlock emptyPanBlock = panManager.getPanBlockByIndex(emptyPanBlockIndex);
                PanBlock prePanBlock = panManager.getPanBlockByIndex(prePanBlockIndex);
                char[] content = emptyPanBlock.getContent();
                StringBuilder builder = new StringBuilder();
                for (int j = (i-1) * 8; j < Math.min(i*8,arrayList.size()); j++) {
                    FileBlock fileBlock1 = arrayList.get(j);
                    String str = new String(fileBlock1.getName() + "  " + fileBlock1.getAttribute() + fileBlock1.getStartPanBlock() + fileBlock1.getPanBlockLength());
                    builder.append(str);
                }
                builder.getChars(0,builder.length(),content,0);
                prePanBlock.setNextIndex(emptyPanBlockIndex);
                prePanBlockIndex = emptyPanBlockIndex;
            }
            if(i == necessaryPanLength)
            {
                PanBlock panBlockByIndex = panManager.getPanBlockByIndex(prePanBlockIndex);
                panBlockByIndex.setNextIndex(-1);

            }
        }
        StageManager.refreshTableStage();
        fileBlock.setPanBlockLength(necessaryPanLength);
    }

    // 读取文件
    public String readFile(FileBlock fileBlock)
    {
        StringBuilder stringBuilder = new StringBuilder();
        PanBlock panBlock = panManager.getPanBlockByIndex(fileBlock.getStartPanBlock());
        for (int i = 0; i < fileBlock.getPanBlockLength(); i++) {
            stringBuilder.append(new String(panBlock.getContent()));
            if(panBlock.getNextIndex()!=-1)
            {
                panBlock = panManager.getPanBlockByIndex(panBlock.getNextIndex());
            }

        }
        return stringBuilder.toString();
    }

    // 修改文件内容
    public void saveFileContent(FileBlock fileBlock,String text)
    {
        /*
        * 先判断输入字段的长度
        * 若大于原来的pan长度则增加
        * 若小于原来的pan长度则释放多余的pan
        * */
        // 需要的pan的数量
        int necessaryPanLength =(int)Math.ceil(text.length()/64.0);
        // 记录上一个盘
        int prePanBlockIndex = 0;
        // 记录下一个盘
        int nextPanBlockIndex = fileBlock.getStartPanBlock();
        // 对输入text进行剪切
        String[] strings = StringUtil.splitString(text,64,necessaryPanLength);
        // 如果需要的小于大于的,收回多余的
        if(fileBlock.getPanBlockLength() > necessaryPanLength)
        {
            // 寻找
            int blockGap = fileBlock.getPanBlockLength() - necessaryPanLength;
            int panBlock = fileBlock.getStartPanBlock();
            for (int i = 0; i < necessaryPanLength; i++) {
                panBlock = panManager.getPanBlockByIndex(panBlock).getNextIndex();
            }
            // 回收
            int panBlock1 = panBlock;
            for (int i = 0; i < blockGap; i++) {
                panBlock1 = panManager.getPanBlockByIndex(panBlock1).getNextIndex();
                panManager.reclaimPanBlock(panBlock);
                panBlock = panBlock1;

            }
        }
        for (int i = 1; i <= necessaryPanLength; i++) {
            if(i <= fileBlock.getPanBlockLength())
            {
                // 重新写数据
                PanBlock panBlock = panManager.getPanBlockByIndex(nextPanBlockIndex);
                // 赋值上一个panBlock
                prePanBlockIndex = nextPanBlockIndex;
                nextPanBlockIndex = panBlock.getNextIndex();
                strings[i-1].getChars(0,strings[i-1].length()-1, panBlock.getContent(), 0);
            }else
            {
              /*
              * 原来的panBlock已经不够用了 需要申请新的
              * */
                //获取新的panBlock
                int emptyPanBlockIndex = panManager.getEmptyBlock();
                PanBlock emptyPanBlock = panManager.getPanBlockByIndex(emptyPanBlockIndex);
                PanBlock prePanBlock = panManager.getPanBlockByIndex(prePanBlockIndex);
                strings[i-1].getChars(0,strings[i-1].length()-1, emptyPanBlock.getContent(), 0);
                prePanBlock.setNextIndex(emptyPanBlockIndex);
                prePanBlockIndex = emptyPanBlockIndex;
            }
        }
        fileBlock.setPanBlockLength(necessaryPanLength);
        StageManager.refreshTableStage();
    }

    private void deleteFilePanBlock(FileBlock fileBlock)
    {
        int prePanBlockIndex = fileBlock.getStartPanBlock();
        int nextPanBlockIndex = 0;
        for (int i = 0; i <fileBlock.getPanBlockLength(); i++) {
            nextPanBlockIndex = panManager.getPanBlockByIndex(prePanBlockIndex).getNextIndex();
            panManager.reclaimPanBlock(prePanBlockIndex);
            prePanBlockIndex = nextPanBlockIndex;
        }
        // 删除
        fileBlock.getParentFileBlock().getFileChildrenObjectArrayList().remove(fileBlock);
        setDirectoryPanBlockChange(fileBlock.getParentFileBlock());
        this.fileBlocks.remove(fileBlock);

    }
    public void deleteFile(FileBlock fileBlock)
    {
        if(fileBlock.getAttribute() == 3)
        {
            deleteFilePanBlock(fileBlock);
        }else if(fileBlock.getAttribute() == 8)
        {
            ArrayList<FileBlock> fileBlocks = fileBlock.getFileChildrenObjectArrayList();
            int size = fileBlocks.size();
            for (int i = 0; i < size; i++) {
                FileBlock tempFileBlock = fileBlocks.get(0);
                if (tempFileBlock.getAttribute() == 3)
                {
                    deleteFilePanBlock(tempFileBlock);
                }else
                {
                    deleteFile(tempFileBlock);
                }
            }
            deleteFilePanBlock(fileBlock);
        }
        StageManager.refreshTableStage();
        StageManager.refreshAllTreeView();
    }

    public FileBlock getRootFileBlock() {
        return rootFileBlock;
    }

    public ArrayList<FileBlock> getFileBlocks() {
        return fileBlocks;
    }
}
