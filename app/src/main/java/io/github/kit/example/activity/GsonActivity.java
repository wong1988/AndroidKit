package io.github.kit.example.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

import io.github.kit.example.R;
import io.github.kit.example.bean.Pet;
import io.github.wong1988.kit.utils.GsonUtils;

public class GsonActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gson);
        tv = findViewById(R.id.tv);
    }

    public void serialization(View view) {

        Pet pet = new Pet();
        pet.setName("小黑");
        pet.setPhotoUrls(Arrays.asList("http://www.baidu.com/1", "http://www.baidu.com/2"));

        Gson gson = GsonUtils.gsonSerializeNulls();
        tv.setText(gson.toJson(pet));

    }

    public void serialization2(View view) {
        Pet pet = new Pet();
        pet.setName("小黑");
        pet.setPhotoUrls(Arrays.asList("http://www.baidu.com/1", "http://www.baidu.com/2"));

        Gson gson = GsonUtils.gson();
        tv.setText(gson.toJson(pet));
    }

    public void deserialization(View view) {
        serialization(view);
        String s = tv.getText().toString();

        Pet pet = GsonUtils.gson().fromJson(s, Pet.class);
        Pet.CategoryBean category = pet.getCategory();
        String name = pet.getName();
        List<String> photoUrls = pet.getPhotoUrls();
        List<Pet.TagsBean> tags = pet.getTags();
        String status = pet.getStatus();

        s = s + "\n\n取值\n" + "category[Object]:" + category + "\nstatus[String]:" + status + "\nname[String]:" + name + "\nphotoUrls[Array]:" + photoUrls.toString() + "\ntags[Array]:" + tags;
        tv.setText(s);
    }

    public void deserialization2(View view) {

        serialization(view);
        String s = tv.getText().toString();

        Pet pet = GsonUtils.gsonNullsByDefault()
                .fromJson(s, Pet.class);
        Pet.CategoryBean category = pet.getCategory();
        String name = pet.getName();
        List<String> photoUrls = pet.getPhotoUrls();
        List<Pet.TagsBean> tags = pet.getTags();
        String status = pet.getStatus();

        s = s + "\n\n取值\n" + "category[Object]:" + category + "\nstatus[String]:" + status + "\nname[String]:" + name + "\nphotoUrls[Array]:" + photoUrls.toString() + "\ntags[Array]:" + tags;
        tv.setText(s);
    }

    public void serialization3(View view) {

        Pet pet = new Pet();
        pet.setName("小黑");
        pet.setPhotoUrls(Arrays.asList("http://www.baidu.com/1", "http://www.baidu.com/2"));

        Gson gson = GsonUtils.gsonNullsByDefault();
        tv.setText(gson.toJson(pet));

    }
}