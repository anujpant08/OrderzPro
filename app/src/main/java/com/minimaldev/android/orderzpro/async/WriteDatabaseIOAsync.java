package com.minimaldev.android.orderzpro.async;

import android.os.AsyncTask;
import android.util.Log;

import com.minimaldev.android.orderzpro.DatabaseOperationsHelper;
import com.minimaldev.android.orderzpro.dao.MailModelDao;
import com.minimaldev.android.orderzpro.model.Mail;

import java.util.List;

public class WriteDatabaseIOAsync extends AsyncTask<Void, Void, Void> {
    private final String TAG = getClass().getSimpleName();
    DatabaseOperationsHelper databaseOperationsHelper;
    MailModelDao mailModelDao;
    List<Mail> mails;
    public WriteDatabaseIOAsync(MailModelDao mailModelDao, DatabaseOperationsHelper databaseOperationsHelper, List<Mail> mails) {
        this.mailModelDao = mailModelDao;
        this.databaseOperationsHelper = databaseOperationsHelper;
        this.mails = mails;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        for(Mail mail: mails){
            if(!isMailInDB(mail)){
                mailModelDao.insertMail(mail);
                Log.e(TAG, "Mail added to DB: " + mail);
            }else{
                Log.e(TAG, "Row already exists in the DB!");
            }
        }
        return null;
    }

    private boolean isMailInDB(Mail mail) {
        List<Mail> mailHitsInDB = mailModelDao.getMailsByDescOrderedOnDateQty(mail.getDescription(), mail.getOrderedOnDate(), mail.getQuantity());
        return mailHitsInDB != null && !mailHitsInDB.isEmpty();
    }
}
