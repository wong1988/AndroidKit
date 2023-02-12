package io.github.wong1988.kit.entity;

public class FileInfo {

    private final String fileName;
    private final String filePath;
    private final long size;
    private final long time;
    private String describe;
    private int type;

    public FileInfo(String fileName, String filePath, long size, long time) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.size = size;
        this.time = time;
        this.describe = "";
        this.type = -1;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getSize() {
        return size;
    }

    public long getTime() {
        return time;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
