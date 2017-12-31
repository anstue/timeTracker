package com.ti_zero.com.apptime.helper;

import com.ti_zero.com.apptime.data.objects.TimeEntry;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by anstue on 12/31/17.
 */

public class TimeHelperTest {

    @Test
    public void testGetTimeWithinBoundries() {
        long start=100;
        long end=200;

        TimeEntry timeEntryWithin = new TimeEntry(new Date(150), new Date(160));
        TimeEntry timeEntryBefore = new TimeEntry(new Date(50), new Date(60));
        TimeEntry timeEntryAfter = new TimeEntry(new Date(250), new Date(260));
        TimeEntry timeEntryWholeDay = new TimeEntry(new Date(0), new Date(310));
        TimeEntry timeEntryStartBefore = new TimeEntry(new Date(90), new Date(150));
        TimeEntry timeEntryEndAfter = new TimeEntry(new Date(150), new Date(220));

        assertThat(TimeHelper.getTimeWithinBoundries(start, end, timeEntryWithin),is(10L));
        assertThat(TimeHelper.getTimeWithinBoundries(start, end, timeEntryBefore),is(0L));
        assertThat(TimeHelper.getTimeWithinBoundries(start, end, timeEntryAfter),is(0L));
        assertThat(TimeHelper.getTimeWithinBoundries(start, end, timeEntryWholeDay),is(24*60*60*1000L));
        assertThat(TimeHelper.getTimeWithinBoundries(start, end, timeEntryStartBefore),is(50L));
        assertThat(TimeHelper.getTimeWithinBoundries(start, end, timeEntryEndAfter),is(50L));
    }
}
