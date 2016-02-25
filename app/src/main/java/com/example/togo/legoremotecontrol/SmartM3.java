package com.example.togo.legoremotecontrol;

import java.util.Vector;

import wrapper.SmartSpaceException;
import wrapper.SmartSpaceKPI;
import wrapper.SmartSpaceTriplet;

/**
 * Created by ToGo on 19.02.2016.
 */
public class SmartM3 {

    private static SmartSpaceKPI smartSpaceKPI;

    public static boolean insert(SmartSpaceTriplet triplet) {
        try {
            smartSpaceKPI = new SmartSpaceKPI("192.168.2.101", 10010, "x");
            if (triplet != null) {
                smartSpaceKPI.insert(triplet);
            }
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

    public static int update() {
        int status = 0;
        try {
            smartSpaceKPI = new SmartSpaceKPI("192.168.2.101", 10010, "x");
            Vector<SmartSpaceTriplet> query = smartSpaceKPI.query(new SmartSpaceTriplet("Robot", "blockCount", null));
            if (!query.isEmpty()) {
                status = Integer.parseInt(query.lastElement().getObject());
                for (int i = 0; i < Integer.parseInt(query.lastElement().getObject()); i++) {
                    status *= 100;
                    Vector<SmartSpaceTriplet> blockQuery = smartSpaceKPI.query(new SmartSpaceTriplet("Block" + i, "has", null));
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
            smartSpaceKPI = new SmartSpaceKPI("192.168.2.101", 10010, "x");
            smartSpaceKPI.subscribe(new SmartSpaceTriplet("Robot", "blockCount", null), new Handler());

        } catch (SmartSpaceException e) {
            e.printStackTrace();
        }
    }

    public static void leave() {
        try {
            smartSpaceKPI.leave();
        } catch (SmartSpaceException e) {
            e.printStackTrace();
        }
    }
}
