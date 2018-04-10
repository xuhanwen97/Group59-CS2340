package com.example.xu.group59.Utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 */

public class ToastUtils {
    /**
     * A method that makes the popup when a popup is needed
     * @param context a context of the context of why the toast is needed
     * @param text a CharSequence of the text in the toast
     * @return the toast popup
     */
    public static Toast shortToastCenter(Context context, CharSequence text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);

        //show() is not called because the purpose of this method is to return an unshown toast
        return toast;
    }
}
