package com.example.cook01.project1;

/**
 * Created by Perkins on 1/16/2016.
 * cannot instantiate this class
 * access staticly ex.
 * int total_cash = CONSTANTS.STARTUP_CASH;
 */

// DO I need this???


public class Constants {

    //dont allow instatiation
    private Constants() {
    }

    public static final int NUMB_FLOWERS    = 3;
    public static final int STARTUP_CASH    = 5;
    public static final int YOUR_BROKE      = 0;
    public static final int COST_PER_ROLL   = 1;
    public static final int MATCH_2         = 2;
    public static final int MATCH_3         = 3;
}