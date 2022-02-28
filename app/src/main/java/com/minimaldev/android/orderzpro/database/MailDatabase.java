package com.minimaldev.android.orderzpro.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.minimaldev.android.orderzpro.converter.DateTypeConverter;
import com.minimaldev.android.orderzpro.dao.MailModelDao;
import com.minimaldev.android.orderzpro.model.Mail;

@Database(entities = {Mail.class}, version = 2)
//@TypeConverter({DateTypeConverter.class})
public abstract class MailDatabase extends RoomDatabase {
    private static MailDatabase mailDatabase;
    public abstract MailModelDao mailModelDao();
    static final Migration migration = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            //Empty implementation.
        }
    };

    public static MailDatabase getMailDatabase(Context context) {
        if(mailDatabase != null){
            return mailDatabase;
        }
        //Making sure only one "Instance" of the Room DB is created in a thread.
        mailDatabase = Room.databaseBuilder(context, MailDatabase.class, "mails.db").addMigrations(migration).build(); //building Room Database instance to be used in other areas.
        return mailDatabase;
    }
}

