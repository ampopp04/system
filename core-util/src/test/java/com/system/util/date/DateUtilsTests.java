package com.system.util.date;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * The <class>DateUtilsTests</class> defines
 * date related tests
 *
 * @author Andrew
 */
public class DateUtilsTests {

    @Test
    public void testGetToday() {
        String actual = DateUtils.getToday();
        String expected = new SimpleDateFormat("dd-MMM-yyyy")
                .format(new Date());
        assertEquals(expected, actual);
    }
}