package com.example.file.manager;

import com.example.file.domain.FileBlock;
import com.example.file.domain.PanBlock;
import com.example.file.pre.FileData;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/10/15 15:03
 */
public class PanManager {

    private final int panNum = 127;

    private static PanManager panManager;
    private ArrayList<PanBlock> panBlockArrayList = DataManager.fileData.getPanBlockArrayList();

    // 获取单例磁盘管理器
    public static PanManager getInstance() {
        if (panManager == null) {
            panManager = new PanManager();
        }
        return panManager;
    }


    // 如果没有存储，则初始化磁盘
    public void initDickPan() {
        if (panBlockArrayList == null) {
            panBlockArrayList = new ArrayList<>();
            for (int i = 0; i <= panNum; i++) {
                // 前 3 个盘被占用
                if (i == 0) {
                    PanBlock panBlock = new PanBlock(i, true);
                    char[] content = panBlock.getContent();
                    for (int j = 0; j < content.length; j++) {
                        content[j] = 1;
                    }
                    panBlock.setNextIndex(1);
                    panBlockArrayList.add(panBlock);

                } else if (i == 1) {
                    PanBlock panBlock = new PanBlock(i, true);
                    char[] content = panBlock.getContent();
                    for (int j = 0; j < content.length; j++) {
                        content[j] = 1;
                    }
                    panBlockArrayList.add(panBlock);
                }else if( i == 2)
                {
                    panBlockArrayList.add(new PanBlock(i, true));
                }
                else {
                    panBlockArrayList.add(new PanBlock(i, false));
                }
            }
            //设置系统盘的 0 和 1 号盘为已经占用
            char[] content = panManager.getPanBlockByIndex(0).getContent();
            content[0] = '1';
            content[1] = '1';
            content[2] = '1';
        }
    }

    public void reclaimPanBlock(int index) {
        PanBlock panBlock = panBlockArrayList.get(index);
        panBlock.setUsed(false);
        panBlock.setNextIndex(-1);
        panBlock.setContent(new char[64]);
        panBlock.setDirectoryLength((byte) 0);
    }

    private int searchEmptyPanBlock() {
        int i;
        for (i = 0; i < 128; i++) {
            PanBlock panBlock = panBlockArrayList.get(i);
            if (!panBlock.isUsed()) {
                panBlock.setUsed(true);
                return i;
            }
        }
        return -1;
    }

    public PanBlock getPanBlockByIndex(int index) {
        return panBlockArrayList.get(index);
    }

    /*
     * 设置下一个空余盘
     * 并且返回空域盘
     *
     * */
    public int setNextPanBlock(int index) {
        int i = searchEmptyPanBlock();
        PanBlock panBlock = panBlockArrayList.get(index);
        PanBlock nextPanBlock = panBlockArrayList.get(i);
        panBlock.setNextIndex(i);
        nextPanBlock.setUsed(true);
        return i;
    }

    public int getUsedCharsData() {
        int length = 0;
        for (int i = 0; i < 128; i++) {
            int temp = 0;
            PanBlock panBlock = panBlockArrayList.get(i);
            char[] content = panBlock.getContent();
            for (int j = 0; j < content.length; j++) {
                if (content[j] != '\u0000') {
                    temp++;
                }
            }
            length += temp;
        }
        return length;
    }

    // 获取空余盘
    public int getEmptyBlock() {
        return searchEmptyPanBlock();
    }

    // 设置盘的使用状态
    public void setPanBlockUsed(int index, boolean bool) {
        PanBlock panBlock = panBlockArrayList.get(index);
        panBlock.setUsed(bool);
    }



    /*
     *   测试函数
     *   下面是测试状态
     *
     * */

    public void showPanBlockUsageStatus() {
        for (int i = 0; i <= 20; i++) {
            System.out.println(i + ": " + panBlockArrayList.get(i).isUsed());
        }
    }

    public ArrayList<PanBlock> getPanBlockArrayList() {
        return panBlockArrayList;
    }
}
