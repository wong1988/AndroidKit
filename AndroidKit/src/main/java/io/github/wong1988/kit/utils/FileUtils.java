package io.github.wong1988.kit.utils;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresPermission;

import java.util.ArrayList;
import java.util.List;

import io.github.wong1988.kit.AndroidKit;
import io.github.wong1988.kit.entity.FileInfo;

public class FileUtils {

    /**
     * 按时间排序搜索手机存储文件
     */
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public static List<FileInfo> searchExternalFiles(String[] extension) {
        return searchExternalFiles(extension, MediaStore.Files.FileColumns.DATE_MODIFIED);
    }

    /**
     * 搜索手机存储文件（指定扩展名）
     *
     * @param extension 筛选扩展名 如：new String[]{".png",".jpg"}
     * @param sort      排序      如：MediaStore.Files.FileColumns.DATE_MODIFIED
     */
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public static List<FileInfo> searchExternalFiles(String[] extension, String sort) {

        if (extension == null)
            extension = new String[0];

        List<FileInfo> fileInfoList = new ArrayList<FileInfo>();

        // 内存卡文件的Uri
        Uri fileUri = MediaStore.Files.getContentUri("external");
        // 筛选列，这里筛选了：文件路径、文件名、文件修改日期、文件大小
        String[] projection = new String[]{
                MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.SIZE
        };

        // 构造筛选条件语句
        StringBuilder selection = new StringBuilder();
        for (int i = 0; i < extension.length; i++) {
            if (i != 0)
                selection.append(" OR ");
            selection.append(MediaStore.Files.FileColumns.DATA).append(" LIKE '%").append(extension[i]).append("'");
        }

        Cursor cursor = AndroidKit.getInstance().getAppContext().getContentResolver().query(fileUri, projection, selection.toString(), null, sort);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    // 地址
                    String path = cursor.getString(0);
                    // 名称
                    String name = cursor.getString(1);
                    // 时间
                    long time = cursor.getLong(2);
                    // 大小
                    long size = cursor.getLong(3);
                    fileInfoList.add(new FileInfo(name, path, size, time));
                } catch (Exception e) {
                    Log.e("FileUtils", "searchExternalFiles()", e);
                }
            }
        }
        return fileInfoList;
    }
}
