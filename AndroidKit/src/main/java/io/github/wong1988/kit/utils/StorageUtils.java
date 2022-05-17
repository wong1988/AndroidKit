package io.github.wong1988.kit.utils;

import android.Manifest;
import android.os.Environment;
import android.text.TextUtils;

import androidx.annotation.RequiresPermission;

import java.io.File;

import io.github.wong1988.kit.type.SharedStorage;

public class StorageUtils {

    /**
     * 共享存储空间目录
     * Android 6.0 需动态申请权限：Manifest.permission.WRITE_EXTERNAL_STORAGE
     *
     * @param storage 目录.
     */
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static String sharedStorageFolder(SharedStorage storage) {
        File directory = Environment.getExternalStoragePublicDirectory(storage.type);
        // 防止用户删除共享存储控件目录
        directory.mkdirs();
        return directory.getAbsolutePath();
    }

    /**
     * 共享存储空间目录
     * Android 6.0 需动态申请权限：Manifest.permission.WRITE_EXTERNAL_STORAGE
     * 本方法对不符合的文件夹名称做了处理，并返回正确的路径
     *
     * @param storage   目录.
     * @param subFolder 子文件夹. eg：cache + File.separator + thumbnail(目录/cache/thumbnail下的文件)
     */
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static String sharedStorageFolder(SharedStorage storage, String subFolder) {


        subFolder = StringUtils.trim(subFolder);

        StringBuffer buffer = new StringBuffer();

        if (!TextUtils.isEmpty(subFolder)) {
            // 验证格式是否正确，不正确组装成正确的
            String[] split = subFolder.split(File.separator);

            for (String folderName : split) {
                String newFolderName = RegexUtils.removeFolderSpecialChars(StringUtils.trim(folderName));
                if (newFolderName.length() > 0)
                    buffer.append(File.separator).append(newFolderName);
            }
        }

        File file = new File(sharedStorageFolder(storage) + buffer.toString());
        file.mkdirs();

        return file.getAbsolutePath();
    }

    /**
     * 共享存储空间（Pictures）目录文件
     * 推荐：subFolder不为null
     *
     * @param subFolder eg1: null(根目录下的文件)  eg2: cache + File.separator + thumbnail(根目录/cache/thumbnail下的文件)
     * @param fileName  文件名： 123.jpg
     */
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public static String sharedPicturesFile(String subFolder, String fileName) {
        return new File(sharedStorageFolder(SharedStorage.DIRECTORY_PICTURES, subFolder), fileName).getAbsolutePath();
    }


}
