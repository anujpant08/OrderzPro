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
    List<Mail> mails;
    public WriteDatabaseIOAsync(DatabaseOperationsHelper databaseOperationsHelper, List<Mail> mails) {
        this.databaseOperationsHelper = databaseOperationsHelper;
        this.mails = mails;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for(Mail mail: mails){
            databaseOperationsHelper.addMail(mail);
            Log.e(TAG, "Mail added to DB: " + mail);
        }
        return null;
    }
}
