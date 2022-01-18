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
    implementation 'com.github.wong1988:AndroidKit:0.0.4'
}
```
Step 3. 在Application进行初始化
```
AndroidKit.init(application);
```

### 原生定位 | [查看使用方式](https://github.com/wong1988/AndroidKit/blob/main/原生定位-README.md)


## Change Log

0.0.4:

 * 首个版本发布