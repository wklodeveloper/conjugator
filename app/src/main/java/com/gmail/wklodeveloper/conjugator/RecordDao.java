package com.gmail.wklodeveloper.conjugator;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecordDao {
    @Query("SELECT * FROM record WHERE origin LIKE '%' || :keyword || '%' OR data like '%;' || :keyword || ';%'")
    List<Record> findByKeyword(String keyword);
}
