package com.gmail.wklodeveloper.conjugator;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Record.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract RecordDao recordDao();
}
