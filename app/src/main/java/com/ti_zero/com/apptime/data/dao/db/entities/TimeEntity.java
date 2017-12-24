package com.ti_zero.com.apptime.data.dao.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.ti_zero.com.apptime.data.objects.TimeEntry;

/**
 * Created by anstue on 12/23/17.
 */

@Entity(tableName = "timeentity",
        foreignKeys = {
                @ForeignKey(
                        entity = AccountEntity.class,
                        parentColumns = "accountEntityId",
                        childColumns = "accountEntityId",
                        onDelete = ForeignKey.CASCADE
                )},
        indices = { @Index(value = "timeEntityId")})
public class TimeEntity {

    @PrimaryKey
    private long timeEntityId;
    private long accountEntityId;
    private long start;
    private long end;

    public TimeEntity(long timeEntityId, long accountEntityId, long start, long end) {
        this.timeEntityId = timeEntityId;
        this.accountEntityId = accountEntityId;
        this.start = start;
        this.end = end;
    }

    public long getTimeEntityId() {
        return timeEntityId;
    }

    public void setTimeEntityId(long timeEntityId) {
        this.timeEntityId = timeEntityId;
    }

    public long getAccountEntityId() {
        return accountEntityId;
    }

    public void setAccountEntityId(long accountEntityId) {
        this.accountEntityId = accountEntityId;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }
}
