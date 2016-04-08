package com.example.togo.legoremotecontrol;

import android.util.Log;

import java.util.Calendar;
import java.util.Vector;

import wrapper.SmartSpaceException;
import wrapper.SmartSpaceKPI;
import wrapper.SmartSpaceTriplet;

/**
 * Created by ToGo on 19.02.2016.
 */
public class SmartM3 {

    private static SmartSpaceKPI smartSpaceKPI;
    private static SmartSpaceKPI smartSubs;
    private static String imp = "192.168.1.1";

    public static boolean check() {
        try {
            smartSpaceKPI = new SmartSpaceKPI(IPInsert.ip, 10010, "x");
            return true;
        } catch (SmartSpaceException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (smartSpaceKPI != null) {
                try {
                    smartSpaceKPI.leave();
                } catch (SmartSpaceException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static boolean insert(SmartSpaceTriplet triplet) {
        try {
            smartSpaceKPI = new SmartSpaceKPI(IPInsert.ip, 10010, "x");
            if (triplet != null) {
                smartSpaceKPI.insert(triplet);
            }
            Log.d("insertTime", " " + Calendar.getInstance().getTime());
            return true;
        } catch (SmartSpaceException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (smartSpaceKPI != null) {
                try {
                    smartSpaceKPI.leave();
                } catch (SmartSpaceException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //TODO insert update to handler
    public static int update() {
        int status = 0;
        try {
            smartSpaceKPI = new SmartSpaceKPI(IPInsert.ip, 10010, "x");
            Vector<SmartSpaceTriplet> query = smartSpaceKPI.query(new SmartSpaceTriplet("robot", "blockAmount", null));
            if (!query.isEmpty()) {
                status = Integer.parseInt(query.lastElement().getObject());
                for (int i = Integer.parseInt(query.lastElement().getObject()) - 1; i >= 0; i--) {
                    status *= 100;
                    Vector<SmartSpaceTriplet> blockQuery = smartSpaceKPI.query(new SmartSpaceTriplet("block" + i, "has", null));
                    if (!blockQuery.isEmpty()) {
                        for (SmartSpaceTriplet triplet : blockQuery) {
                            if (triplet.getObject().equals("liftEngine"))
                                status += 10;
                            if (triplet.getObject().equals("moveEngine"))
                                status += 1;
                        }
                    }
                }
            }

        } catch (SmartSpaceException e) {
            e.printStackTrace();
        } finally {
            if (smartSpaceKPI != null) {
                try {
                    smartSpaceKPI.leave();
                } catch (SmartSpaceException e) {
                    e.printStackTrace();
                }
            }
            return status;
        }
    }

    public static void subscribe() {
        try {
            Handler handler = new Handler();
            smartSubs = new SmartSpaceKPI(IPInsert.ip, 10010, "x");
            smartSubs.subscribe(new SmartSpaceTriplet("robot", "blockAmount", null), handler);
            smartSubs.subscribe(new SmartSpaceTriplet(null, "event", null), handler);
            Log.d("Phone", " subscribe");

        } catch (SmartSpaceException e) {
            Log.d("Phone", " Problems with subscribing");
            e.printStackTrace();
        }
    }

    public static void leave() {
        try {
            if (smartSubs != null)
                smartSubs.leave();
            Log.d("Phone", " unsubscribe");
        } catch (SmartSpaceException e) {
            Log.d("Phone", " Problems with unsubscribing");
            Log.d("Phone", e.getMessage());
        }
    }

    public static void remove(String obj, String pred, String subj) {
        try {
            if (smartSubs != null)
                smartSubs.remove(new SmartSpaceTriplet(subj, pred, obj));
        } catch (SmartSpaceException e) {
            e.printStackTrace();
        }

    }
}
