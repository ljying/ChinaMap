package com.example.nemo.mapdemo.map;

import android.graphics.Path;

/**
 * Description:  省份绘制信息封装
 * @author nemo
 * @version 2.0
 * @since 16/4/11
 */

public class ProvincePath {

    private Path path;

    private int code;

    private String name;

    public ProvincePath(int code, String name, String pathData) {
        this.code = code;
        this.name = name;
        path = PathParser.createPathFromPathData(pathData);
    }

    public Path getPath() {
        return path;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProvincePath that = (ProvincePath) o;

        if (code != that.code) return false;
        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        int result = code;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ProvincePath{" +
                "name='" + name + '\'' +
                ", code=" + code +
                '}';
    }
}
