package io.github.kit.example;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import io.github.kit.example.activity.GsonActivity;
import io.github.kit.example.activity.SpannableStringUtilsActivity;
import io.github.kit.example.activity.StringUtilsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void stringUtils(View view) {
        startActivity(new Intent(this, StringUtilsActivity.class));
    }

    public void Gson(View view) {
        startActivity(new Intent(this, GsonActivity.class));
    }

    public void SpannableStringUtils(View view) {
        startActivity(new Intent(this, SpannableStringUtilsActivity.class));
    }
}