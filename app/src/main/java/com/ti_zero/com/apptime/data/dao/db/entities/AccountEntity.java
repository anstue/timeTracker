package com.ti_zero.com.apptime.data.dao.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.ti_zero.com.apptime.data.objects.TimeEntry;

import java.util.Date;

/**
 * Created by anstue on 12/23/17.
 */

@Entity(tableName = "accountentity",
        foreignKeys = {
                @ForeignKey(
                        entity = GroupEntity.class,
                        parentColumns = "groupEntityId",
                        childColumns = "groupEntityId",
                        onDelete = ForeignKey.CASCADE
                )},
        indices = { @Index(value = "accountEntityId")})
public class AccountEntity extends AbstractItemEntity {

    @PrimaryKey
    private long accountEntityId;

    private long groupEntityId;

    public AccountEntity(String name, String description, long lastUsage, boolean favorite, long accountEntityId, long groupEntityId) {
        super(name, description, lastUsage, favorite);
        this.accountEntityId = accountEntityId;
        this.groupEntityId = groupEntityId;
    }

    public long getAccountEntityId() {
        return accountEntityId;
    }

    public long getGroupEntityId() {
        return groupEntityId;
    }

    public void setAccountEntityId(long accountEntityId) {
        this.accountEntityId = accountEntityId;
    }

    public void setGroupEntityId(long groupEntityId) {
        this.groupEntityId = groupEntityId;
    }
}
