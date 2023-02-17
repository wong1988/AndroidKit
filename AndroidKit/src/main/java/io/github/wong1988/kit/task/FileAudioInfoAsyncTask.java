package io.github.wong1988.kit.task;

import android.Manifest;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import androidx.annotation.RequiresPermission;

import java.util.ArrayList;
import java.util.List;

import io.github.wong1988.kit.entity.FileInfo;
import io.github.wong1988.kit.utils.FileUtils;

/**
 * 必须有读写权限才可解析音乐
 */
public class FileAudioInfoAsyncTask extends AsyncTask<FileInfo, Integer, List<FileInfo>> {

    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public FileAudioInfoAsyncTask() {
    }

    @Override
    protected List<FileInfo> doInBackground(FileInfo... files) {
        List<FileInfo> infoList = new ArrayList<>();
        for (FileInfo fileInfo : files) {
            if (fileInfo != null) {
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                try {
                    // 这行也很容易crash
                    mediaMetadataRetriever.setDataSource(fileInfo.getFilePath());
                    final byte[] coverImage = mediaMetadataRetriever.getEmbeddedPicture();
                    fileInfo.setMusicThumbnail(BitmapFactory.decodeByteArray(coverImage, 0, coverImage.length));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return infoList;
    }

    @Override
    protected void onPostExecute(List<FileInfo> files) {
        super.onPostExecute(files);
        for (int i = 0; i < files.size(); i++) {
            FileInfo fileInfo = files.get(i);
            FileUtils.FileInfoChanged changedListener = fileInfo.getChangedListener();
            if (changedListener != null)
                changedListener.change(fileInfo);
        }
    }

}
