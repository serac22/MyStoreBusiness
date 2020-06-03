package com.example.mystorebusiness.account.ui.notification;

import androidx.room.TypeConverter;

import java.util.Date;


public class DateTypeConverter {

    @TypeConverter
    public Date Long_toDateConverter(Long date){
        return new Date(date);
    }

    @TypeConverter
    public Long Date_toLongConverter(Date date){
        return date.getTime();
    }

}
