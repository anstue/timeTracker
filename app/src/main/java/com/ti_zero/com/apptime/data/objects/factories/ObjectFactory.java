package com.ti_zero.com.apptime.data.objects.factories;

import com.ti_zero.com.apptime.data.objects.AbstractItem;
import com.ti_zero.com.apptime.data.objects.AccountItem;
import com.ti_zero.com.apptime.data.objects.GroupItem;

import java.util.Date;

/**
 * Created by uni on 12/22/17.
 */

public class ObjectFactory {

    public AbstractItem getNewAccountItem() {
        return new AccountItem("New", "", new Date(), false);
    }

    public AbstractItem getNewGroupItem() {
        return new GroupItem("New Group", "", new Date(), false);
    }
}
