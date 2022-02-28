package com.minimaldev.android.orderzpro.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.minimaldev.android.orderzpro.async.DatabaseIOAsync;
import com.minimaldev.android.orderzpro.async.MailReadAsyncTask;
import com.minimaldev.android.orderzpro.MailsListInterface;
import com.minimaldev.android.orderzpro.dao.MailModelDao;
import com.minimaldev.android.orderzpro.database.MailDatabase;
import com.minimaldev.android.orderzpro.model.Mail;

import java.util.List;

public class MailReadViewModel extends AndroidViewModel implements MailsListInterface {
    private final String TAG = getClass().getSimpleName();
    private MailModelDao mailModelDao;
    private MutableLiveData<List<Mail>> mails;

    public MailReadViewModel(@NonNull Application application) {
        super(application);
        initializeDB(application);
    }


    public LiveData<List<Mail>> getMails(){
        if(mails == null){
            mails = new MutableLiveData<>();
            loadMails();
        }
        return mails;
    }

    private void initializeDB(Application application) {
        // Get instance of MailModelDao using MailDatabase.
        mailModelDao = MailDatabase.getMailDatabase(application).mailModelDao();
    }

    private void loadMails(){
        MailReadAsyncTask mailReadAsyncTask = new MailReadAsyncTask(this);
        mailReadAsyncTask.execute();
    }

    @Override
    public void populateList(List<Mail> mails) {
        DatabaseIOAsync databaseIOAsync = new DatabaseIOAsync(mailModelDao, mails);
        databaseIOAsync.execute();
        this.mails.postValue(mails);
        //Log.e(TAG, "Inside populateList()");
    }
}
