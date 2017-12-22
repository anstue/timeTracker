package com.ti_zero.com.apptime.data.objects.helper;

import com.ti_zero.com.apptime.data.objects.AccountItem;

import java.util.Date;

/**
 * Created by uni on 12/22/17.
 */

public class ObjectHelper {

    public static AccountItem getNewAccountItem() {
        return new AccountItem("New", "", new Date(), false);
    }
}
