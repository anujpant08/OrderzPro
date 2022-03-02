package com.minimaldev.android.orderzpro.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.minimaldev.android.orderzpro.model.Mail;

import java.util.List;

@Dao
public interface MailModelDao {
    @Insert
    public void insertMail(Mail mail);

    @Query("select * from mail_table order by 1 desc")
    public LiveData<List<Mail>> getMails();

    @Query("select * from mail_table where description = :description and ordered_on_date = :orderedOnDate and quantity = :quantity")
    public List<Mail> getMailsByDescOrderedOnDateQty(String description, String orderedOnDate, Integer quantity);

    @Update
    public void updateMail(Mail mail);

//    @Query("select * from mail_table where is_delivered = :isDelivered")
//    public LiveData<List<Mail>> getMailsByDeliveryStatus(boolean isDelivered);
//
//
//    @Update
//    public Integer updateMailsList(List<Mail> mails);

}
