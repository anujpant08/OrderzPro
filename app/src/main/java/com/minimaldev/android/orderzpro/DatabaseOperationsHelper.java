package com.minimaldev.android.orderzpro;

import androidx.lifecycle.LiveData;

import com.minimaldev.android.orderzpro.async.WriteDatabaseIOAsync;
import com.minimaldev.android.orderzpro.dao.MailModelDao;
import com.minimaldev.android.orderzpro.model.Mail;

import java.util.List;

public class DatabaseOperationsHelper {
    MailModelDao mailModelDao;
    LiveData<List<Mail>> mails;
    public DatabaseOperationsHelper(MailModelDao mailModelDao) {
        this.mailModelDao = mailModelDao;
    }
    public LiveData<List<Mail>> readMails(){
        mails = mailModelDao.getMails();
        return mails;
    }
    public void addMail(List<Mail> mails){
        WriteDatabaseIOAsync writeDatabaseIOAsync = new WriteDatabaseIOAsync(mailModelDao, this, mails);
        writeDatabaseIOAsync.execute();
    }
    public void updateMail(Mail mail){
        mailModelDao.updateMail(mail);
    }
}
