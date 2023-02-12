package io.github.wong1988.kit.task;

import android.Manifest;
import android.os.AsyncTask;

import androidx.annotation.RequiresPermission;

import java.util.List;

import io.github.wong1988.kit.entity.FileInfo;
import io.github.wong1988.kit.utils.FileUtils;

public class FileAsyncTask extends AsyncTask<String, Integer, List<FileInfo>> {

    private final String[] mExtension;
    private String mSort;
    private final IFileAsyncTask mTaskListener;

    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public FileAsyncTask(String[] extension, IFileAsyncTask taskListener) {
        this.mExtension = extension;
        this.mTaskListener = taskListener;
    }

    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    public FileAsyncTask(String[] extension, String sort, IFileAsyncTask taskListener) {
        this.mExtension = extension;
        this.mSort = sort;
        this.mTaskListener = taskListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mTaskListener != null)
            mTaskListener.start();
    }

    @Override
    @RequiresPermission(allOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    protected List<FileInfo> doInBackground(String... strings) {
        if (mSort == null)
            return FileUtils.searchExternalFiles(mExtension);
        else
            return FileUtils.searchExternalFiles(mExtension, mSort);
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
