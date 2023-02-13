package io.github.wong1988.kit.task;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.format.Formatter;

import java.util.ArrayList;
import java.util.List;

import io.github.wong1988.kit.AndroidKit;
import io.github.wong1988.kit.R;
import io.github.wong1988.kit.entity.FileInfo;
import io.github.wong1988.kit.utils.FileUtils;

public class FileApkInfoAsyncTask extends AsyncTask<FileInfo, Integer, List<FileInfo>> {

    @Override
    protected List<FileInfo> doInBackground(FileInfo... files) {
        List<FileInfo> infoList = new ArrayList<>();
        PackageManager pm = AndroidKit.getInstance().getAppContext().getPackageManager();
        for (FileInfo fileInfo : files) {
            if (fileInfo != null) {
                PackageInfo pkgInfo = pm.getPackageArchiveInfo(fileInfo.getFilePath(), PackageManager.GET_ACTIVITIES);
                if (pkgInfo != null) {
                    ApplicationInfo appInfo = pkgInfo.applicationInfo;
                    /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
                    appInfo.sourceDir = fileInfo.getFilePath();
                    appInfo.publicSourceDir = fileInfo.getFilePath();
                    String appName = pm.getApplicationLabel(appInfo).toString();// 得到应用名
                    String version = pkgInfo.versionName; // 得到版本信息
                    /* icon1和icon2其实是一样的 */
                    Drawable icon2 = appInfo.loadIcon(pm);
                    fileInfo.setApkThumbnail(icon2);
                    fileInfo.setDescribe(String.format(AndroidKit.getInstance().getAppContext().getResources().getString(R.string.kit_app_describe), appName, Formatter.formatFileSize(AndroidKit.getInstance().getAppContext(), fileInfo.getSize()), version));
                    infoList.add(fileInfo);
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
