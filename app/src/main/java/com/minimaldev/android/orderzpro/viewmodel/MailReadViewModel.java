package com.minimaldev.android.orderzpro.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.minimaldev.android.orderzpro.MailReadAsyncTask;
import com.minimaldev.android.orderzpro.MailsListInterface;
import com.minimaldev.android.orderzpro.model.Mail;

import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

public class MailReadViewModel extends ViewModel implements MailsListInterface {
    private final String TAG = getClass().getSimpleName();
    private MutableLiveData<List<Mail>> mails;
    public LiveData<List<Mail>> getMails(){
        if(mails == null){
            mails = new MutableLiveData<>();
            loadMails();
        }
        return mails;
    }
    private void loadMails(){
        MailReadAsyncTask mailReadAsyncTask = new MailReadAsyncTask(this);
        mailReadAsyncTask.execute();
    }

    @Override
    public void populateList(List<Mail> mails) {
        this.mails.postValue(mails);
        //Log.e(TAG, "Inside populateList()");
    }
}
