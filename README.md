# ChinaMap
通过svg文件绘制中国地图
## 涉及技术
* xml文件解析
* svg 路径信息与Path转换
* Canvas绘制path
* 自定义控件手势处理
* RxJava 异步任务处理
* Java8 Lambda 兼容

[APK下载](https://raw.githubusercontent.com/ljying/ChinaMap/master/screenshot/sample.apk)

## 使用方式
```xml
<com.example.nemo.mapdemo.map.ChinaMapView
        android:id="@+id/view_map"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />
```



## 着色方式自由选择，按照简单粗暴的是否着色
```java
mapView.setmColorStrategy(new BooleanColorStrategy());//数据参考//parseDemoData2
```
![效果图](./screenshot/effect2.png)
## 按照权值，着颜色深重
```java
mapView.setmColorStrategy(new ValueColorStrategy());//数据参考//parseDemoData
```
![效果图](./screenshot/effect.png)



## 这个版本将库独立，去掉了lamada表达式，已达到更大兼容

