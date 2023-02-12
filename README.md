# AndroidKit [![](https://www.jitpack.io/v/wong1988/AndroidKit.svg)](https://www.jitpack.io/#wong1988/AndroidKit)

Android开发工具包

Step 1. Add it in your root build.gradle at the end of repositories:

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Step 2. Add the dependency

```
dependencies {
    implementation 'com.github.wong1988:AndroidKit:1.1.2'
    implementation 'com.google.code.gson:gson:2.9.0'
    implementation 'com.github.wong1988:MediaCenter:0.0.2'
}
```

Step 3. 在Application进行初始化

```
AndroidKit.init(application);
```

### UiUtils(Ui相关工具类) | [查看使用方式](https://github.com/wong1988/AndroidKit/blob/main/UiUtils-README.md)

### EditTextUtils(输入框相关工具类) | [查看使用方式](https://github.com/wong1988/AndroidKit/blob/main/EditTextUtils-README.md)

### 原生定位 | [查看使用方式](https://github.com/wong1988/AndroidKit/blob/main/原生定位-README.md)

### SpannableStringUtils(文字样式的工具类) | [查看使用方式](https://github.com/wong1988/AndroidKit/blob/main/SpannableStringUtils-README.md)

## Change Log

1.1.2:

* 加入了搜索内部存储的功能

1.1.1:

* 加入了EditText添加移除长度、字符过滤器的功能

1.1.0:

* 加入了SpannableStringUtils，可设置 左侧图片 + 文字 排版方式

1.0.6:

* 加入了EditText输入现金格式以及汉字格式

1.0.2:

* 加入了gson序列化、反序列化的处理，String = null 处理成“” List = null 处理成 []，详细可查看使用demo

1.0.1:

* 首个版本发布
