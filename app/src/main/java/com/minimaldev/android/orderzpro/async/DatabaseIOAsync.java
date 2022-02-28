package com.minimaldev.android.orderzpro.async;

import android.os.AsyncTask;
import android.util.Log;

import com.minimaldev.android.orderzpro.dao.MailModelDao;
import com.minimaldev.android.orderzpro.model.Mail;

import java.util.List;

public class DatabaseIOAsync extends AsyncTask<Void, Void, Void> {
    private final String TAG = getClass().getSimpleName();
    MailModelDao mailModelDao;
    List<Mail> mails;
    public DatabaseIOAsync(MailModelDao mailModelDao, List<Mail> mails) {
        this.mailModelDao = mailModelDao;
        this.mails = mails;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for(Mail mail: mails){
            mailModelDao.insertMail(mail);
            Log.e(TAG, "Mail added to DB: " + mail);
        }
        return null;
    }
}
