package com.ti_zero.com.apptime.data.objects;

import java.util.UUID;

/**
 * Created by anstue on 12/24/17.
 */

public class ObjWithUUID {

    public final static long ROOT_UUID = 0;
    //root item has id of ROOT_UUID
    private long uniqueID;

    public ObjWithUUID(long uniqueID) {
        this.uniqueID = uniqueID;
    }

    public ObjWithUUID() {
        uniqueID = UUID.randomUUID().getMostSignificantBits();//TODO change to a more secure method
    }

    public long getUniqueID() {
        return uniqueID;
    }

}
