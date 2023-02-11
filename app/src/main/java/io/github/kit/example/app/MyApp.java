package io.github.kit.example.app;

import android.app.Application;

import io.github.wong1988.kit.AndroidKit;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidKit.init(this);
    }
}
