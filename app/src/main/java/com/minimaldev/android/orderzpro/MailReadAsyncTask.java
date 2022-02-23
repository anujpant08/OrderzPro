package com.minimaldev.android.orderzpro;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
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

public class MailReadAsyncTask extends AsyncTask<Void, Void, Void> {
    private List<String> mails = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();
    private MailsListInterface mailsListInterface = null;
    public MailReadAsyncTask(MailsListInterface mailsListInterface) {
        this.mailsListInterface = mailsListInterface;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("anujpant.work@gmail.com", "chelsea997");
                }
            });

            Store store = session.getStore("imaps");
            store.connect("imap.gmail.com", "USERNAME", "PASSWORD");

            Folder inbox = store.getFolder("Test");
            Log.e(TAG, "No. of messages: " + inbox.getMessageCount());
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), true));

            FetchProfile fp = new FetchProfile();
            fp.add(FetchProfile.Item.ENVELOPE);
            fp.add(FetchProfile.Item.CONTENT_INFO);
            inbox.fetch(messages, fp);

            for (Message message : messages) {
                //Log.e(TAG, "Message: " + message);
                mails.add(message.getSubject());
            }
            //Log.e(TAG, "List is: " + mails);
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred: ", e);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        mailsListInterface.populateList(mails);
    }
}
