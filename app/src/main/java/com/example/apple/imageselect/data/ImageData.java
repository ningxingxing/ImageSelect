package com.example.apple.imageselect.data;

/**
 * Created by apple on 17/10/9.
 */

public class ImageData {

    private String path;//文件路径
    private boolean isSelect;//文件是否选中

    public ImageData() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String toString() {
        return "ImageData{" +
                "path='" + path + '\'' +
                ", isSelect=" + isSelect +
                '}';
    }
}
