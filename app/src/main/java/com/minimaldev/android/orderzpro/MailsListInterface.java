package com.minimaldev.android.orderzpro;

import androidx.lifecycle.MutableLiveData;

import com.minimaldev.android.orderzpro.model.Mail;

import java.util.List;

public interface MailsListInterface {
    void populateList(List<Mail> mails);
}
