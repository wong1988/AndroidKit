package io.github.wong1988.kit.entity;

import android.graphics.drawable.Drawable;
import android.text.format.DateFormat;
import android.text.format.Formatter;

import io.github.wong1988.kit.AndroidKit;
import io.github.wong1988.kit.task.FileApkInfoAsyncTask;
import io.github.wong1988.kit.utils.FileUtils;
import io.github.wong1988.media.MediaCenter;

public class FileInfo {

    private final int index;
    private final String fileName;
    private final String filePath;
    private final long size;
    private final long time;
    private final int width;
    private final int height;
    private String describe;
    private MediaCenter.FileClassify fileType;
    private Drawable apkThumbnail;
    private final FileUtils.FileInfoChanged changedListener;

    public FileInfo(String fileName, String filePath, long size, long time, int width, int height, FileUtils.FileInfoChanged changedListener) {
        this.index = -1;
        this.fileName = fileName;
        this.filePath = filePath;
        this.size = size;
        this.time = time;
        this.width = width;
        this.height = height;
        this.changedListener = changedListener;
    }

    public int getIndex() {
        return index;
    }

    public FileInfo(int index, String fileName, String filePath, long size, long time, int width, int height, FileUtils.FileInfoChanged changedListener) {
        this.index = index;
        this.fileName = fileName;
        this.filePath = filePath;
        this.size = size;
        this.time = time;
        this.width = width;
        this.height = height;
        this.changedListener = changedListener;
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

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getDescribe() {

        // 子线程获取
        getFileType();

        if (describe == null)
            // 未获取过，先取默认值
            describe = Formatter.formatFileSize(AndroidKit.getInstance().getAppContext(), size) + "   " + DateFormat.format("yyyy年MM月dd日", time);

        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public MediaCenter.FileClassify getFileType() {

        if (fileType == null) {
            // 还未获取过type
            fileType = MediaCenter.getMediaFileType(filePath).classify;
            if (fileType == MediaCenter.FileClassify.APK)
                // apk进行特殊处理，处理完更新
                new FileApkInfoAsyncTask().execute(this);
        }

        return fileType;
    }

    public Drawable getApkThumbnail() {
        // 子线程去处理
        getFileType();
        return apkThumbnail;
    }

    public void setApkThumbnail(Drawable apkThumbnail) {
        this.apkThumbnail = apkThumbnail;
    }

    public FileUtils.FileInfoChanged getChangedListener() {
        return changedListener;
    }
}