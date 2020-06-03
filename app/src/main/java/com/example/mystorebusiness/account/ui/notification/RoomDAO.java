package com.example.mystorebusiness.account.ui.notification;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RoomDAO {


    @Insert
    void Insert(Reminders... reminders);

    @Update
    void Update(Reminders... reminders);

    @Delete
    void Delete(Reminders reminders);

    @Query("Select * from reminder order by remindDate")
    List<Reminders> orderTheTable();

    @Query("Select * from reminder Limit 1")
    Reminders getRecentEnteredData();

    @Query("Select * from reminder")
    List<Reminders> getAll();

}
