package com.minimaldev.android.orderzpro.async;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.minimaldev.android.orderzpro.DatabaseOperationsHelper;
import com.minimaldev.android.orderzpro.MailsListInterface;
import com.minimaldev.android.orderzpro.dao.MailModelDao;
import com.minimaldev.android.orderzpro.model.Mail;

import java.util.List;

public class ReadDatabaseIOAsync extends AsyncTask<Void, Void, Void> {
    private final String TAG = getClass().getSimpleName();
    DatabaseOperationsHelper databaseOperationsHelper;
    Application application;
    List<Mail> mails;
    private MailsListInterface mailsListInterface = null;
    public ReadDatabaseIOAsync(Application application, DatabaseOperationsHelper databaseOperationsHelper, MailsListInterface mailsListInterface) {
        this.application = application;
        this.databaseOperationsHelper = databaseOperationsHelper;
        this.mailsListInterface = mailsListInterface;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        databaseOperationsHelper.readMails();
        Log.e(TAG, "Mail read from DB: " + mails);
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        mailsListInterface.populateList(mails);
    }
}
