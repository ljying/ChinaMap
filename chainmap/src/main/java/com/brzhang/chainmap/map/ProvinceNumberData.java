package com.brzhang.chainmap.map;

/**
 * Description: 省份数据实体
 *
 * @author nemo
 * @version 2.0
 * @since 2016/4/15
 */
public class ProvinceNumberData implements IProvinceData {
    /**
     * 省份Id
     */
    private int provinceId;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 该省内人数
     */
    private int number;

    @Override
    public int getPersonNumber() {
        return number;
    }

    @Override
    public int getProvinceCode() {
        return provinceId;
    }

    @Override
    public boolean isShouldColor() {
        return false;
    }

    @Override
    public int getColor() {
        return 0;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "ProvinceNumberData{" +
                "provinceId=" + provinceId +
                ", provinceName='" + provinceName + '\'' +
                ", number=" + number +
                '}';
    }
}
