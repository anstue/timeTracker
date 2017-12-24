package com.ti_zero.com.apptime.data.dao.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.ti_zero.com.apptime.data.objects.TimeEntry;

/**
 * Created by anstue on 12/23/17.
 */

@Entity(tableName = "accountEntities",
        foreignKeys = {
                @ForeignKey(
                        entity = TimeEntry.class,
                        parentColumns = "accountEntityId",
                        childColumns = "timeEntryId",
                        onDelete = ForeignKey.CASCADE
                )},
        indices = { @Index(value = "accountEntityId")})
public class AccountEntity extends AbstractItemEntity {

    @PrimaryKey
    long accountEntityId;

}
