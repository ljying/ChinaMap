package com.brzhang.chainmap.map;

/**
 * Description: 省份数据方法接口
 *
 * @author nemo
 * @version 2.0
 * @since 16/4/12
 */
interface IProvinceData {

    /**
     * 获取省份人数
     *
     * @return 人数
     */
    int getPersonNumber();

    /**
     * 获取省份编码
     *
     * @return 省份编码
     */
    int getProvinceCode();

    /**
     * 获取省份是否应该着色
     *
     * @return 省份编码
     */
    boolean isShouldColor();

    /**
     * 获取省份应该着色的颜色
     *
     * @return 人数
     */
    int getColor();


}
