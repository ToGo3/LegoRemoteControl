package com.example.togo.legoremotecontrol;


import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by ToGo on 18.01.2016.
 */
public class ListOfLastUse {

    private static SharedPreferences pref;
    private static Deque<String> deque;
    private static Context cont;

    public static Deque<String> getList(Context context, String key) {
        pref = context.getSharedPreferences("main", context.MODE_PRIVATE);
        cont = context;
        deque = new LinkedList<String>();
        for (int i = 4; i >= 0; i--) {
            if (!pref.getString(key + i, "").isEmpty())
                deque.addFirst(pref.getString(key + i, ""));
        }
        return deque;
    }

    public static void showList(Deque<String> deque, final ListView lvMain, final EditText editText) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(cont,
                android.R.layout.simple_list_item_1, deque.toArray(new String[deque.size()]));

        // присваиваем адаптер списку
        if (lvMain!=null) {
            lvMain.setAdapter(adapter);
            lvMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    editText.setText((String) lvMain.getItemAtPosition(position));
                    editText.setSelection(editText.getText().length());
                }
            });

            if (!deque.isEmpty()) {
                String ip = deque.getFirst();
                editText.setText(ip);
                editText.setSelection(editText.getText().length());
            }
        }
    }

    public static void setList(String key, String ip) {
        getList(cont, key);
        if (deque.contains(ip)) {
            deque.removeLastOccurrence(ip);
        }
        deque.addFirst(ip);
        if (deque.size() > 5) {
            deque.removeLast();
        }

        SharedPreferences.Editor editPref = pref.edit();
        int dequeSize = deque.size();
        for (int i = 0; i < dequeSize; i++) {
            editPref.putString(key + i, deque.pop());
        }

        editPref.commit();
    }

}
