package com.ti_zero.com.apptime.data.dto;

import com.ti_zero.com.apptime.data.objects.AccountItem;

/**
 * Created by anstue on 12/25/17.
 */

public class StartItemDTO {

    private AccountItem item;
    private boolean newItem;

    public StartItemDTO(AccountItem item, boolean newItem) {
        this.item = item;
        this.newItem = newItem;
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
}
