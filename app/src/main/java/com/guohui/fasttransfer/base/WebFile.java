package com.guohui.fasttransfer.base;

import java.io.Serializable;

/**
 * Created by Dikaros on 2016/5/29.
 */
public class WebFile implements Serializable{
    String name;
    String type;
    String length;

    long size;
    long readed;

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getReaded() {
        return readed;
    }

    public void setReaded(long readed) {
        this.readed = readed;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    String progress;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    @Override
    public boolean equals(Object o) {

        return ((WebFile)o).getName().equals(name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
