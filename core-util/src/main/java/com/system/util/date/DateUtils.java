package com.system.util.date;


import org.apache.commons.lang3.time.DateFormatUtils;

import java.util.Date;

public class DateUtils {

    public static final String BASIC_DATA_FORMAT = "dd-MMM-yyyy";

    /**
     * Retrieve todays date as a string
     */
    public static String getToday() {
        return DateFormatUtils.format(new Date(), BASIC_DATA_FORMAT);
    }
}
