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
                        entity = AccountEntity.class,
                        parentColumns = "groupEntityId",
                        childColumns = "accountEntityId",
                        onDelete = ForeignKey.CASCADE
                )},
        indices = { @Index(value = "groupEntityId")})
public class GroupEntity extends AbstractItemEntity {

    @PrimaryKey
    long groupEntityId;
}
