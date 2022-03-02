package com.minimaldev.android.orderzpro.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.minimaldev.android.orderzpro.DatabaseOperationsHelper;
import com.minimaldev.android.orderzpro.dao.MailModelDao;
import com.minimaldev.android.orderzpro.database.MailDatabase;
import com.minimaldev.android.orderzpro.model.Mail;

import java.util.List;

public class MailReadViewModel extends AndroidViewModel {
    private final String TAG = getClass().getSimpleName();
    private LiveData<List<Mail>> mails;
    private MailModelDao mailModelDao;
    DatabaseOperationsHelper databaseOperationsHelper;
    public MailReadViewModel(@NonNull Application application) {
        super(application);
        initializeDB(application);
        databaseOperationsHelper = new DatabaseOperationsHelper(mailModelDao);
    }
    private void initializeDB(Application application) {
        // Get instance of MailModelDao using MailDatabase.
        mailModelDao = MailDatabase.getMailDatabase(application).mailModelDao();
    }
    public LiveData<List<Mail>> getMails(){
        if(mails == null){
            mails = new MutableLiveData<>();
            loadMails();
        }
        return mails;
    }
    private void loadMails(){
        mails = databaseOperationsHelper.readMails();
    }
}
