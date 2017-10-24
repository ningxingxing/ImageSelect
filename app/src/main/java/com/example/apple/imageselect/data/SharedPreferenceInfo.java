package com.example.apple.imageselect.data;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by apple on 17/10/23.
 */

public class SharedPreferenceInfo {

    /**
     * 保存编辑的图片的位置置
     * @param mContext
     * @param editPath
     */
    public static void saveEditImage(Context mContext,String editPath){

        SharedPreferences sp = mContext.getSharedPreferences("editPath",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("editPath",editPath);
        editor.commit();
    }

    /**
     * 获取编辑的图片的位置置
     * @param mContext
     * @return
     */
    public static String getEditImage(Context mContext){
        SharedPreferences sp = mContext.getSharedPreferences("editPath",Context.MODE_PRIVATE);
        return sp.getString("editPath","");
    }
}
