package io.github.wong1988.kit.utils;

import android.Manifest;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.RequiresPermission;

import java.util.ArrayList;
import java.util.List;

import io.github.wong1988.kit.AndroidKit;
import io.github.wong1988.kit.entity.FileInfo;
import io.github.wong1988.kit.type.SortMode;

public class FileUtils {

    /**
     * 搜索手机存储文件（指定扩展名）
     * Android10（targetSdkVersion = 29）只有存储权限，部分可以搜索，如：图片、视频、音频 ；apk、文档等不能搜索
     * 清单文件加入 android:requestLegacyExternalStorage="true"
     * Android11（targetSdkVersion = 30）需要用到所有文件访问权限
     *
     * @param extension       筛选扩展名          如：new String[]{".png",".jpg"}
     * @param sortColumn      根据列名排序        如：MediaStore.Files.FileColumns.DATE_MODIFIED 根据修改时间进行排序
     * @param sortMode        排序模式           如：升序、降序
     * @param changedListener 数据变动的监听器     如：apk图标以及描述都为子线程获取，如果有变动请单独刷新某个item
     *
     *                       todo /storage/emulated/0/Android/data/com.huawei.pcassistant/cache/.dragfile_template/2.docx
     *                        todo 应该忽略其它包下的
     */
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static List<FileInfo> queryMediaStoreFiles(String[] extension, String sortColumn, SortMode sortMode, FileInfoChanged changedListener) {

        int targetSdkVersion = AndroidKit.getInstance().getAppContext().getApplicationInfo().targetSdkVersion;

        if (targetSdkVersion == Build.VERSION_CODES.Q && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            Log.e("NOTICE", "queryMediaStoreFiles()", new Throwable("targetSdkVersion=29且当前运行系统>=29，请在清单文件加入 android:requestLegacyExternalStorage=\"true\"，否则非媒体文件搜索不到，如以加入忽略即可"));
        } else if (targetSdkVersion == Build.VERSION_CODES.R && android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            Log.e("NOTICE", "queryMediaStoreFiles()", new Throwable("targetSdkVersion=30且当前运行系统>=30，请申请权限：android.permission.MANAGE_EXTERNAL_STORAGE，如以申请忽略即可"));
        }

        if (extension == null)
            extension = new String[0];

        List<FileInfo> fileInfoList = new ArrayList<FileInfo>();

        // 数据库表Uri(external表)
        Uri fileUri = MediaStore.Files.getContentUri("external");
        // 指定查询的列名：文件路径、文件名、文件修改日期、文件大小
        String[] projection = new String[]{
                MediaStore.Files.FileColumns.DATA, MediaStore.Files.FileColumns.TITLE,
                MediaStore.Files.FileColumns.DATE_MODIFIED, MediaStore.Files.FileColumns.SIZE,
                MediaStore.Files.FileColumns.WIDTH,
                MediaStore.Files.FileColumns.HEIGHT
        };

        // 指定where的约束条件
        StringBuilder selection = new StringBuilder();
        for (int i = 0; i < extension.length; i++) {
            if (i != 0)
                selection.append(" OR ");
            selection.append(MediaStore.Files.FileColumns.DATA).append(" LIKE '%").append(extension[i]).append("'");
        }

        // sortOrder 指定查询结果的排列方式
        Cursor cursor = AndroidKit.getInstance().getAppContext().getContentResolver().query(fileUri, projection, selection.toString(), null, sortColumn + (sortMode == null ? "" : " " + sortMode.name()));

        if (cursor != null) {
            while (cursor.moveToNext()) {
                try {
                    // 地址
                    String path = cursor.getString(0);
                    // 名称
                    String name = cursor.getString(1);
                    // 时间
                    long time = cursor.getLong(2) * 1000;
                    // 大小
                    long size = cursor.getLong(3);
                    // 宽
                    int width = cursor.getInt(4);
                    // 高
                    int height = cursor.getInt(5);
                    // 添加到文件集合
                    fileInfoList.add(new FileInfo(fileInfoList.size(), name, path, size, time, width, height, changedListener));
                } catch (Exception e) {
                    Log.e("FileUtils", "searchExternalFiles()", e);
                }
            }
            cursor.close();
        }

        return fileInfoList;
    }

    public interface FileInfoChanged {
        void change(FileInfo fileInfo);
    }
}
