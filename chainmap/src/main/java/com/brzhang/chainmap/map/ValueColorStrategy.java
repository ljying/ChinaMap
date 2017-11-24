package com.brzhang.chainmap.map;

import android.graphics.Color;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brzhang on 2017/10/27.
 * 根据数值大小着色
 */

public class ValueColorStrategy implements IColorStrategy<ProvinceNumberData> {
    /**
     * 设置地图区域颜色，根据所占比例来绘制区域颜色深浅即初始化区域绘制信息
     *
     * @param itemList 省份区域集合
     * @param dataList 实际解析数据集合
     */
    @Override
    public void setMapColor(List<ChinaMapView.ProvinceItem> itemList, Collection<ProvinceNumberData> dataList) {
        int totalNumber = 0;
        Map<Integer, Integer> map = new HashMap<>();
        if (dataList != null) {
            for (IProvinceData data : dataList) {
                totalNumber += data.getPersonNumber();
                map.put(data.getProvinceCode(), data.getPersonNumber());
            }
        }

        for (ChinaMapView.ProvinceItem item : itemList) {
            int code = item.getProvinceCode();
            int number = 0;
            if (map.containsKey(code)) {
                number = map.get(code);
            }
            item.setPersonNumber(number);

            int color = Color.WHITE;
            if (totalNumber > 0) {
                double flag = (double) number / totalNumber;
                if (flag > 0.2) {
                    color = mColorArray[0];
                } else if (flag > 0.1) {
                    color = mColorArray[1];
                } else if (flag > 0) {
                    color = mColorArray[2];
                } else {
                    color = Color.WHITE;
                }
            }
            item.setDrawColor(color);
        }
    }

}
