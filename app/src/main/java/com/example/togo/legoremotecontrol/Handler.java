package com.example.togo.legoremotecontrol;

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
    public void kpic_RDFEventHandler(Vector<Vector<String>> vector, Vector<Vector<String>> vector1, String s, String s1) {

        for (int i = 0; i < vector.size(); i++) {
            //temp += "New triple s= " + newTriples.elementAt(i).elementAt(0) + " + predicate " + newTriples.elementAt(i).elementAt(1) + " object = " + newTriples.elementAt(i).elementAt(2) + "\n";
            if (vector.elementAt(i).elementAt(1).equals("blockCount")) {
                new Main.updateFromSmart().execute();
            }

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
