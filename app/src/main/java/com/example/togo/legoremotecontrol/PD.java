package com.example.togo.legoremotecontrol;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by ToGo on 06.11.2015.
 */
public class PD {
    static ProgressDialog pd;

    public static void showDialog(Context context, String message) {
        pd = new ProgressDialog(context);
        pd.setTitle("Загрузка");
        pd.setMessage(message);
        pd.setCancelable(false);
        pd.show();
    }

    public static void hideDialog() {
        pd.hide();
        pd.dismiss();
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context,
                message,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
