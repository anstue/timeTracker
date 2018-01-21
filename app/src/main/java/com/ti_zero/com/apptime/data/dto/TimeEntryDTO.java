package com.ti_zero.com.apptime.data.dto;

import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.TimeEntry;

/**
 * Created by anstue on 1/21/18.
 */

public class TimeEntryDTO {
    private TimeEntry timEntry;
    private AccountItem item;

    public TimeEntryDTO(TimeEntry timEntry, AccountItem item) {
        this.timEntry = timEntry;
        this.item = item;
    }

    public TimeEntry getTimEntry() {
        return timEntry;
    }

    public AccountItem getItem() {
        return item;
    }
}
