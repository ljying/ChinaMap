package com.brzhang.chainmap.map;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hoollyzhang on 2017/10/27.
 * 根据boolean值判断是否着色
 */

public class BooleanColorStrategy implements IColorStrategy<ProvinceBooleanData> {

    @Override
    public void setMapColor(List<ChinaMapView.ProvinceItem> itemList, Collection<ProvinceBooleanData> dataList) {
        Map<Integer, Boolean> map = new HashMap<>();
        if (dataList != null) {
            for (IProvinceData data : dataList) {
                map.put(data.getProvinceCode(), data.isShouldColor());
            }
        }

        for (ChinaMapView.ProvinceItem item : itemList) {
            int code = item.getProvinceCode();
            boolean shouldColor = false;
            if (map.containsKey(code)) {
                shouldColor = map.get(code);
            }
            if (shouldColor) {
                item.setDrawColor(mColorArray[0]);
            }
        }
    }
}
