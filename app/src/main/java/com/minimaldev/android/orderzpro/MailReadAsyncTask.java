package com.minimaldev.android.orderzpro;

import android.os.AsyncTask;
import android.util.Log;

import com.minimaldev.android.orderzpro.model.Mail;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

public class MailReadAsyncTask extends AsyncTask<Void, Void, Void> {
    private final List<String> mails = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();
    static Pattern msgSubjectPattern = null;
    static Matcher msgSubjectMatcher = null;
    static Pattern msgBodyPattern = null;
    static Matcher msgBodyMatcher = null;
    private MailsListInterface mailsListInterface = null;
    public MailReadAsyncTask(MailsListInterface mailsListInterface) {
        this.mailsListInterface = mailsListInterface;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            msgSubjectPattern = Pattern.compile("(Order\\s+)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
            msgBodyPattern = Pattern.compile("Order no\\.:\\s+[^\\s]+\\s([\\w\\s]+)Size:\\s+([^\\s]+)\\s+Qty:\\s+([^\\s]+).+Delivery\\s+Address\\s+(.+)\\s+Billing.+Total\\s+(.+)\\s+Mode\\s+of\\s+Payment\\s+(\\w+)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
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
            store.connect("imap.gmail.com", "UName", "Pwd");

            Folder inbox = store.getFolder("Test");
            Log.e(TAG, "No. of messages: " + inbox.getMessageCount());
            inbox.open(Folder.READ_ONLY);

            Message[] messages = inbox.search(new SearchTerm() {
                @Override
                public boolean match(Message msg) {
                    //TODO: Add search criteria regex (or some other way) to search for online orders.
                    try {
                        msgSubjectMatcher = msgSubjectPattern.matcher(msg.getSubject());
                        if(msgSubjectMatcher.find()){
                            Mail extractedMail = new Mail();
                            mails.add(msg.getSubject());
                            if(msg.isMimeType("text/plain")){
                                //TODO: Apply regex to message body
                                String body = msg.getContent().toString();
                            }else if(msg.isMimeType("multipart/*")) {
                                MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
                                String messageBody = getTextFromMimeMultipart(mimeMultipart);
                                //TODO: Apply regex to message body
                                String textFromHtml = html2text(messageBody);
                                msgBodyMatcher = msgBodyPattern.matcher(textFromHtml);
                                if(msgBodyMatcher.find()){
                                    extractedMail.setDescription(msgBodyMatcher.group(1));
                                    extractedMail.setProductSize(msgBodyMatcher.group(2));
                                    extractedMail.setQuantity(Integer.parseInt(Objects.requireNonNull(msgBodyMatcher.group(3))));
                                    extractedMail.setDeliveryAddress(msgBodyMatcher.group(4));
                                    extractedMail.setPrice(msgBodyMatcher.group(5));
                                    extractedMail.setPaymentMode(msgBodyMatcher.group(6));
                                }
                            }
                            Log.e(TAG, "Mail extracted: " + extractedMail);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "An exception has occurred: ", e);
                    }
                    return false;
                }
            });

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
    private String getTextFromMimeMultipart(
            MimeMultipart mimeMultipart)  throws MessagingException, IOException {
        String result = "";
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result = result + "\n" + bodyPart.getContent();
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result = result + "\n" + html; //org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
            }
        }
        return result;
    }
    private String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}
