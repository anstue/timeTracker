package com.ti_zero.com.apptime.data.objects;

import android.databinding.BaseObservable;

import java.util.UUID;

/**
 * Created by anstue on 12/24/17.
 */

public class ObservableWithUUID extends BaseObservable {

    public final static long ROOT_UUID = 0;
    //root item has id of ROOT_UUID
    private long uniqueID;

    public ObservableWithUUID(long uniqueID) {
        this.uniqueID = uniqueID;
    }

    public ObservableWithUUID() {
        uniqueID = UUID.randomUUID().getMostSignificantBits();//TODO change to a more secure method
    }

    public long getUniqueID() {
        return uniqueID;
    }

}
