package com.ti_zero.com.apptime.data.dao.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.ti_zero.com.apptime.data.dao.db.entities.TimeEntity;

import java.util.List;

/**
 * Created by anstue on 12/24/17.
 */


@Dao
public interface TimeEntityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addTimeEntity(TimeEntity timeEntity);

    @Query("select * from timeentity")
    List<TimeEntity> getAllTimeEntities();

    @Query("select * from timeEntity where timeEntityId = :timeEntityId")
    List<TimeEntity> getTimeEntity(long timeEntityId);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTimeEntity(TimeEntity timeEntity);

    @Query("delete from timeentity where timeEntityId=:timeEntityId")
    void deleteTimeEntity(long timeEntityId);

    @Query("select * from timeEntity where accountEntityId = :accountEntityId order by start desc")
    List<TimeEntity> getTimeEntities(long accountEntityId);
}
