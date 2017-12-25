package com.ti_zero.com.apptime.data.dao.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;


/**
 * Created by anstue on 12/23/17.
 */

@Entity(tableName = "groupentity",
        foreignKeys = {
            @ForeignKey(
                    entity = GroupEntity.class,
                    parentColumns = "groupEntityId",
                    childColumns = "parentGroupEntityId",
                    onDelete = ForeignKey.CASCADE
            )},
        indices = { @Index(value = "groupEntityId")})
public class GroupEntity extends AbstractItemEntity {

    @PrimaryKey
    private long groupEntityId;

    private Long parentGroupEntityId;

    public GroupEntity(String name, String description, long lastUsage, boolean favorite, long groupEntityId, Long parentGroupEntityId) {
        super(name, description, lastUsage, favorite);
        this.groupEntityId = groupEntityId;
        this.parentGroupEntityId = parentGroupEntityId;
    }

    public long getGroupEntityId() {
        return groupEntityId;
    }

    public void setGroupEntityId(long groupEntityId) {
        this.groupEntityId = groupEntityId;
    }

    public Long getParentGroupEntityId() {
        return parentGroupEntityId;
    }

    public void setParentGroupEntityId(Long parentGroupEntityId) {
        this.parentGroupEntityId = parentGroupEntityId;
    }
}
