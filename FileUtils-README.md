# FileUtils

文件相关的工具类

## Method

```
// 查询数据库文件，推荐使用 QueryMediaStoreAsyncTask 对象来查询
// Android10（targetSdkVersion = 29）清单文件加入 android:requestLegacyExternalStorage="true"
// Android11（targetSdkVersion = 30）需要用到所有文件访问权限
queryMediaStoreFiles()
```
