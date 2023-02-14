package io.github.kit.example.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

import io.github.kit.example.R;
import io.github.wong1988.kit.entity.FileInfo;
import io.github.wong1988.kit.task.FileAsyncTask;
import io.github.wong1988.kit.utils.FileUtils;
import io.github.wong1988.media.MediaCenter;

public class FileUtilsActivity extends AppCompatActivity {

    private TextView tv1;
    private TextView tv2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_utils);

        tv1 = findViewById(R.id.tv1);
        tv2 = findViewById(R.id.tv2);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void apk(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.fromParts("package", getPackageName(), null));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
        }

        new FileAsyncTask(new FileUtils.FileInfoChanged() {
            @SuppressLint("MissingPermission")
            @Override
            public void change(FileInfo fileInfo) {
                StringBuilder builder = new StringBuilder(tv2.getText().toString());
                builder.append("\n");
                builder.append(fileInfo.getFileName());
                builder.append("：");
                // 此时不开启子线程了
                builder.append(fileInfo.getDescribe());
                builder.append("\n");
                tv2.setText(builder.toString());
            }
        }, new FileAsyncTask.IFileAsyncTask() {
            @Override
            public void start() {
                tv1.setText("开始");
            }

            @SuppressLint("MissingPermission")
            @Override
            public void complete(List<FileInfo> files) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < files.size(); i++) {
                    FileInfo fileInfo = files.get(i);
                    builder.append(fileInfo.getFileName());
                    builder.append("：");
                    // 调用apk会开启子线程
                    builder.append(fileInfo.getDescribe());
                    builder.append("\n");
                }
                tv1.setText(builder.toString());
            }

            @Override
            public void cancel() {
                tv1.setText("取消");
            }
        }).execute(MediaCenter.INSTALL_EXTENSIONS);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void video(View view) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        new FileAsyncTask(new FileUtils.FileInfoChanged() {
            @Override
            public void change(FileInfo fileInfo) {

            }
        }, new FileAsyncTask.IFileAsyncTask() {
            @Override
            public void start() {
                tv1.setText("开始");
            }

            @SuppressLint("MissingPermission")
            @Override
            public void complete(List<FileInfo> files) {
                StringBuilder builder = new StringBuilder();
                for (int i = 0; i < files.size(); i++) {
                    FileInfo fileInfo = files.get(i);
                    builder.append(fileInfo.getFileName());
                    builder.append("：");
                    // 调用apk会开启子线程
                    builder.append(fileInfo.getDescribe());
                    builder.append("\n");
                }
                tv1.setText(builder.toString());
            }

            @Override
            public void cancel() {
                tv1.setText("取消");
            }
        }).execute(MediaCenter.MEDIA_EXTENSIONS);
    }
}