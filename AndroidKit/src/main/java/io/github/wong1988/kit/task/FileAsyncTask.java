package io.github.wong1988.kit.task;

import android.Manifest;
import android.os.AsyncTask;
import android.provider.MediaStore;

import androidx.annotation.RequiresPermission;

import java.util.List;

import io.github.wong1988.kit.entity.FileInfo;
import io.github.wong1988.kit.type.SortMode;
import io.github.wong1988.kit.utils.FileUtils;

public class FileAsyncTask extends AsyncTask<String, Integer, List<FileInfo>> {

    private final IFileAsyncTask mTaskListener;

    private final String mSortColumn;
    private final SortMode mSortMode;
    private final FileUtils.FileInfoChanged mChangedListener;


    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public FileAsyncTask(FileUtils.FileInfoChanged changedListener, IFileAsyncTask taskListener) {
        this.mTaskListener = taskListener;
        this.mSortColumn = MediaStore.Files.FileColumns.DATE_MODIFIED;
        this.mSortMode = SortMode.DESC;
        this.mChangedListener = changedListener;
    }

    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public FileAsyncTask(String sortColumn, SortMode sortMode, FileUtils.FileInfoChanged changedListener, IFileAsyncTask taskListener) {
        this.mTaskListener = taskListener;
        this.mSortColumn = sortColumn;
        this.mSortMode = sortMode;
        this.mChangedListener = changedListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mTaskListener != null)
            mTaskListener.start();
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    protected List<FileInfo> doInBackground(String... extensions) {
        return FileUtils.queryMediaStoreFiles(extensions, mSortColumn, mSortMode, mChangedListener);
    }

    @Override
    protected void onPostExecute(List<FileInfo> files) {
        super.onPostExecute(files);
        if (mTaskListener != null)
            mTaskListener.complete(files);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mTaskListener != null)
            mTaskListener.cancel();
    }

    public interface IFileAsyncTask {
        void start();

        void complete(List<FileInfo> files);

        void cancel();
    }
}
