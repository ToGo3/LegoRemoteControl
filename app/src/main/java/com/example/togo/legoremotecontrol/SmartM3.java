package com.example.togo.legoremotecontrol;

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
}
