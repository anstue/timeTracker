package com.ti_zero.com.apptime.data.dao.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ti_zero.com.apptime.data.dao.db.entities.GroupEntity;

import java.util.List;

/**
 * Created by anstue on 12/24/17.
 */


@Dao
public interface GroupEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addGroupEntity(GroupEntity groupEntity);

    @Query("select * from groupentity order by lastUsage desc")
    List<GroupEntity> getAllGroupEntities();

    @Query("select * from groupentity where groupEntityId = :groupEntityId")
    List<GroupEntity> getGroupEntity(long groupEntityId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateGroupEntity(GroupEntity groupEntity);

    @Query("delete from groupentity where groupEntityId=:groupEntityId")
    void deleteGroupEntity(long groupEntityId);

    @Query("delete from groupentity")
    void removeAll();

    @Query("select * from groupentity where parentGroupEntityId = :groupEntityId  order by lastUsage desc")
    List<GroupEntity> getGroupEntities(long groupEntityId);
}
