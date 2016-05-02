package com.system.util.date;

import java.util.Date;
import org.apache.commons.lang.time.DateFormatUtils;

public class DateUtils {
    
    public static final String BASIC_DATA_FORMAT = "dd-MMM-yyyy";
    
    /**
     * Retrieve todays date as a string
     */
    public static String getToday() {
        return DateFormatUtils.format(new Date(), BASIC_DATA_FORMAT);
    }
}
