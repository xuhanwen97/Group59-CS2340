package com.example.xu.group59.Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by xuhan on 2/18/2018.
 */

public class ToastUtils {
    public static Toast shortToastCenter(Context context, String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);

        //show() is not called because the purpose of this method is to return an unshown toast
        return toast;
    }
}
