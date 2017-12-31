package com.ti_zero.com.apptime.helper;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by anstue on 12/31/17.
 */

public class DurationHelperTest {

    @Test
    public void testPrintDuration() {
        assertThat(DurationPrinter.printDuration(24*60*60*1000), is("1.0 days"));
        assertThat(DurationPrinter.printDuration(24*60*60*1000+ 12*60*60*1000), is("1.5 days"));
        assertThat(DurationPrinter.printDuration(12*60*60*1000), is("12h00m"));
        assertThat(DurationPrinter.printDuration(30*60*1000), is("30m00s"));

    }
}
