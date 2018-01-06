package com.ti_zero.com.apptime.data.objects.factories;

import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;
import com.ti_zero.com.apptime.data.objects.ObservableWithUUID;

import java.util.Date;

/**
 * Created by uni on 12/22/17.
 */

public class ObjectFactory {

    public AccountItem getNewAccountItem() {
        return new AccountItem("New", "", new Date(), false);
    }

    public AccountItem getNewAccountItem(String name, long uuid) {
        return new AccountItem(name, "", new Date(), false, uuid);
    }

    public GroupItem getNewGroupItem() {
        return new GroupItem("New Group", "", new Date(), false);
    }

    public GroupItem getNewGroupItem(String name, long uuid) {
        return new GroupItem(name,"", new Date(), false, uuid);
    }

    public GroupItem getRootItem() {
        return new GroupItem("Root", "", new Date(), false, ObservableWithUUID.ROOT_UUID);
    }
}
