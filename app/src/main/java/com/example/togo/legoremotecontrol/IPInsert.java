package com.example.togo.legoremotecontrol;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class IPInsert extends AppCompatActivity {

    public static String ip;
    private InputFilter[] filters = new InputFilter[1];
    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipinsert);
        textView = (TextView) findViewById(R.id.insertIp);
        editText = (EditText) findViewById(R.id.ip_address);
        final ListView lvMain = (ListView) findViewById(R.id.lvMain);

        ListOfLastUse.showList(ListOfLastUse.getList(this, "ip_"), lvMain, editText);

        //TODO:тесты

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction()==KeyEvent.ACTION_DOWN){
                    Log.d("KEY ", " "+event.getKeyCode());
                    switch (keyCode){
                        case KeyEvent.KEYCODE_ENTER:
                            connect();
                            Log.d("KEY ", " " + keyCode);
                            return true;
                        case KeyEvent.KEYCODE_NAVIGATE_NEXT:
                            connect();
                            Log.d("KEY ", " " + keyCode);
                            return true;
                        default:
                            Log.d("KEY ", " "+keyCode);
                            break;
                    }
                }
                return false;
            }
        });

        filters[0] = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       android.text.Spanned dest, int dstart, int dend) {
                if (end > start) {
                    String destTxt = dest.toString();
                    String resultingTxt = destTxt.substring(0, dstart)
                            + source.subSequence(start, end)
                            + destTxt.substring(dend);
                    if (!resultingTxt
                            .matches("^\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3}(\\.(\\d{1,3})?)?)?)?)?)?")) {
                        return "";
                    } else {
                        String[] splits = resultingTxt.split("\\.");
                        for (String split : splits) {
                            if (Integer.valueOf(split) > 255) {
                                return "";
                            }
                        }
                    }
                }
                return null;
            }
        };
        editText.setFilters(filters);
    }

    @Override
    public void onBackPressed() {
        openQuitDialog();
    }

    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                IPInsert.this);
        quitDialog.setTitle("Выход: Вы уверены?");

        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        quitDialog.show();
    }

    public void onClick(View view) {
       connect();
    }

    private void connect() {
        if (isOnline()) {
            if (IPAddressValidator.validate(editText.getText().toString())) {
                InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                ip = editText.getText().toString();
                new checkSmart().execute();
            } else
                PD.showToast(IPInsert.this, "Incorrect IP address!");
        } else
            PD.showToast(IPInsert.this, "No internet connection!");
    }


    public void setTextView(String s) {
        textView.setText(s);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public void exit(View view) {
        openQuitDialog();
    }


    class checkSmart extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            return true;//SmartM3.check();
        }

        protected void onPreExecute() {
            PD.showDialog(IPInsert.this, "Connecting...");
        }

        @Override
        protected void onPostExecute(Boolean result) {
            PD.hideDialog();
            if (result) {

                ListOfLastUse.setList("ip_", IPInsert.ip);

                Intent intent = new Intent(IPInsert.this, Main.class).addFlags(
                        Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                PD.showToast(IPInsert.this, "Error! Check your connection!");
            }
        }

    }
}
