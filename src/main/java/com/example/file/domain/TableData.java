package com.example.file.domain;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by IntelliJ IDEA.
 *
 * @Author : 陆炜森
 * @create 2022/11/7 8:45
 */
public class TableData {
    private final SimpleIntegerProperty index;
    private final SimpleIntegerProperty nextIndex;

    private final SimpleStringProperty used;

    public TableData(int index, int nextIndex, String used) {
        this.index = new SimpleIntegerProperty(index);
        this.nextIndex = new SimpleIntegerProperty(nextIndex);
        this.used = new SimpleStringProperty(used);
    }

    public int getIndex() {
        return index.get();
    }

    public SimpleIntegerProperty indexProperty() {
        return index;
    }

    public void setIndex(int index) {
        this.index.set(index);
    }

    public int getNextIndex() {
        return nextIndex.get();
    }

    public SimpleIntegerProperty nextIndexProperty() {
        return nextIndex;
    }

    public void setNextIndex(int nextIndex) {
        this.nextIndex.set(nextIndex);
    }

    public String getUsed() {
        return used.get();
    }

    public SimpleStringProperty usedProperty() {
        return used;
    }

    public void setUsed(String used) {
        this.used.set(used);
    }
}
