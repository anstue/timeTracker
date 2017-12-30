package com.ti_zero.com.apptime.data.dto;

import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.TimeEntry;

/**
 * Created by anstue on 12/25/17.
 */

public class StartItemDTO {

    private AccountItem item;
    private boolean newItem;
    private TimeEntry timeEntry;

    public StartItemDTO(AccountItem item, boolean newItem, TimeEntry timeEntry) {
        this.item = item;
        this.newItem = newItem;
        this.timeEntry = timeEntry;
    }

    public AccountItem getItem() {
        return item;
    }

    public void setItem(AccountItem item) {
        this.item = item;
    }

    public boolean isNewItem() {
        return newItem;
    }

    public void setNewItem(boolean newItem) {
        this.newItem = newItem;
    }

    public TimeEntry getTimeEntry() {
        return timeEntry;
    }

    public void setTimeEntry(TimeEntry timeEntry) {
        this.timeEntry = timeEntry;
    }
}
