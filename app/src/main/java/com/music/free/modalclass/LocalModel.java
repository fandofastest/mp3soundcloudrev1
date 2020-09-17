package com.music.free.modalclass;

public class LocalModel {
    String filename,filepath,type,size,duration;

    public LocalModel() {
    }

    public LocalModel(String filename, String filepath, String type, String size, String duration) {
        this.filename = filename;
        this.filepath = filepath;
        this.type = type;
        this.size = size;
        this.duration = duration;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
