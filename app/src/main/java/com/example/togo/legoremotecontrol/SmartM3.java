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
    private static SmartSpaceKPI smartCheck;
    private static String imp = "192.168.1.1";
    // TODO: 19.04.2017 to singleton 

    public static boolean check() {
        try {
            smartCheck = new SmartSpaceKPI(IPInsert.ip, 10010, "x");
            return true;
        } catch (SmartSpaceException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (smartCheck != null) {
                try {
                    smartCheck.leave();
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
            Log.d("insertTime", " ");
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
        int count = 0;
        try {
            smartSpaceKPI = new SmartSpaceKPI(IPInsert.ip, 10010, "x");
            Vector<SmartSpaceTriplet> query = smartSpaceKPI.query(new SmartSpaceTriplet("robot1", "blockAmount", null));
            if (!query.isEmpty()) {
                count = Integer.parseInt(query.lastElement().getObject());
                for (int i = count - 1; i >= 0; i--) {
                    count *= 100;
                    Vector<SmartSpaceTriplet> blockQuery = smartSpaceKPI.query(new SmartSpaceTriplet("block" + i, "hasPart", null));
                    if (!blockQuery.isEmpty()) {
                        for (SmartSpaceTriplet triplet : blockQuery) {
                            if (triplet.getObject().equals("liftEngine"))
                                count += 10;
                            if (triplet.getObject().equals("moveEngine"))
                                count += 1;
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
            return count;
        }
    }

    public static void subscribe() {
        try {
            Handler handler = new Handler();
            smartSubs = new SmartSpaceKPI(IPInsert.ip, 10010, "x");
            smartSubs.subscribe(new SmartSpaceTriplet("robot1", "blockAmount", null), handler);
            smartSubs.subscribe(new SmartSpaceTriplet(null, "event", null), handler);
            smartSubs.subscribe(new SmartSpaceTriplet(null, "task", null), handler);
            Log.d("Phone", " subscribe");

        } catch (SmartSpaceException e) {
            Log.d("Phone", " Problems with subscribing");
            Log.d("Phone ", e.getMessage());
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
