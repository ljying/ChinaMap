package com.brzhang.chainmap.map;

/**
 * Created by hoollyzhang on 2017/10/27.
 */

public class ProvinceBooleanData implements IProvinceData {
    /**
     * 省份id
     */
    private int provinceId;


    public int getProvinceColor() {
        return provinceColor;
    }

    /**
     * 传了用这个颜色，不传默认地图着色
     */
    private int provinceColor;

    private boolean shouldColor;

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public void setProvinceColor(int provinceColor) {
        this.provinceColor = provinceColor;
    }

    public void setShouldColor(boolean shouldColor) {
        this.shouldColor = shouldColor;
    }

    @Override
    public boolean isShouldColor() {
        return shouldColor;
    }

    @Override
    public int getColor() {
        return 0;
    }

    @Override
    public int getPersonNumber() {
        return 0;
    }

    @Override
    public int getProvinceCode() {
        return provinceId;
    }
}
