package com.system.util.date;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

public class DateUtilsTest {

        @Test
        public void testGetToday() {
                String actual = DateUtils.getToday();
                String expected = new SimpleDateFormat("dd-MMM-yyyy")
                                .format(new Date());
                assertEquals(expected, actual);
        }
}