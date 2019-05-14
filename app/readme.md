


> 默认的缓存路径： /sdcard/Android/data/（应用包名）/cache/diskcache

该工具类使用了DiskLruCache，所以要在build.gradle中添加：

```
dependencies {

implementation 'com.jakewharton:disklrucache:2.0.2'
}
```