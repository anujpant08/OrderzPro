package com.minimaldev.android.orderzpro;

import androidx.lifecycle.LiveData;

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
    public void addMail(Mail mail){
        mailModelDao.insertMail(mail);
    }
}
