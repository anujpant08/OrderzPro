package com.minimaldev.android.orderzpro.async;

import android.os.AsyncTask;
import android.util.Log;

import com.minimaldev.android.orderzpro.DatabaseOperationsHelper;
import com.minimaldev.android.orderzpro.dao.MailModelDao;
import com.minimaldev.android.orderzpro.model.Mail;

import java.util.List;

public class UpdateDatabaseIOAsync extends AsyncTask<Void, Void, Void> {
    private final String TAG = getClass().getSimpleName();
    DatabaseOperationsHelper databaseOperationsHelper;
    MailModelDao mailModelDao;
    List<Mail> mails;
    public UpdateDatabaseIOAsync(MailModelDao mailModelDao, DatabaseOperationsHelper databaseOperationsHelper, List<Mail> mails) {
        this.mailModelDao = mailModelDao;
        this.databaseOperationsHelper = databaseOperationsHelper;
        this.mails = mails;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        for(Mail mail : mails){
            databaseOperationsHelper.updateMail(mail);
            Log.e(TAG, "Updated mail in the DB: " + mail);
        }
        return null;
    }
}
