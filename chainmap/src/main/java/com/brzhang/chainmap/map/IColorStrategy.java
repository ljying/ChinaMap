package com.brzhang.chainmap.map;

import java.util.Collection;
import java.util.List;

/**
 * Created by brzhang on 2017/10/27.
 * 着色策略
 */

public interface IColorStrategy<T> {
    /*着色色质*/
    int[] mColorArray = new int[]{0xFF239BD7, 0xFF30A9E5, 0xFF80CBF1, 0xFFFFFFFF};
    void setMapColor(List<ChinaMapView.ProvinceItem> itemList, Collection<T> dataList);
}
