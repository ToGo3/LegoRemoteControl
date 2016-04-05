package com.example.togo.legoremotecontrol;

import android.util.Log;

import java.util.Vector;

import sofia_kp.SSAP_sparql_response;
import sofia_kp.iKPIC_subscribeHandler2;

/**
 * Created by ToGo on 24.02.2016.
 */
public class Handler implements iKPIC_subscribeHandler2 {
    /* private KPICore uKP;

     public Handler(KPICore uKP){
         this.uKP=uKP;

     }*/
    @Override
    public void kpic_RDFEventHandler(Vector<Vector<String>> newTriples, Vector<Vector<String>> vector1, String s, String s1) {

        for (int i = 0; i < newTriples.size(); i++) {
            String temp = "New triple s= " + newTriples.elementAt(i).elementAt(0) + " + predicate " + newTriples.elementAt(i).elementAt(1) + " object = " + newTriples.elementAt(i).elementAt(2) + "\n";
            if (newTriples.elementAt(i).elementAt(1).equals("blockAmount")) {
                Main.handler.sendEmptyMessage(0);
            }
            if (newTriples.elementAt(i).elementAt(1).equals("event")) {
                switch (newTriples.elementAt(i).elementAt(2)) {
                    case "stopped":
                        Main.handler.sendEmptyMessage(1);
                        break;
                    case "rised":
                        Main.handler.sendMessage(Main.handler.obtainMessage(2, Integer.valueOf(newTriples.elementAt(i).elementAt(0).substring(5))));
                        break;
                }
            }
            Log.d("NewTriples= ", temp);

        }
    }

    @Override
    public void kpic_SPARQLEventHandler(SSAP_sparql_response ssap_sparql_response, SSAP_sparql_response ssap_sparql_response1, String s, String s1) {

    }

    @Override
    public void kpic_UnsubscribeEventHandler(String s) {

    }

    @Override
    public void kpic_ExceptionEventHandler(Throwable throwable) {

    }

    //public
}
