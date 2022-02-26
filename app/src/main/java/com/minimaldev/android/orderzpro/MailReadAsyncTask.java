package com.minimaldev.android.orderzpro;

import android.os.AsyncTask;
import android.util.Log;

import com.minimaldev.android.orderzpro.model.Mail;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

public class MailReadAsyncTask extends AsyncTask<Void, Void, Void> {
    private final List<Mail> mails = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();
    static Pattern msgSubjectPattern = null;
    static Matcher msgSubjectMatcher = null;
    static Pattern orderDetailsPattern = null;
    static Matcher orderDetailsMatcher = null;
    static Pattern deliveryExpectedDatePattern = null;
    static Matcher deliveryExpectedDateMatcher = null;
    static Pattern paymentModeDeliverAddressPattern = null;
    static Matcher paymentModeDeliverAddressMatcher = null;
    private MailsListInterface mailsListInterface = null;
    public MailReadAsyncTask(MailsListInterface mailsListInterface) {
        this.mailsListInterface = mailsListInterface;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            loadPatterns();
            Properties props = new Properties();
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "465");

            Session session = Session.getDefaultInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("UNAME", "PWD");
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
                    try {
                        msgSubjectMatcher = msgSubjectPattern.matcher(msg.getSubject());
                        if(msgSubjectMatcher.find()){
                            if(msg.isMimeType("text/plain")){
                                String body = msg.getContent().toString();
                                applyRegexExtractData(msg, body);
                            }else if(msg.isMimeType("multipart/*")) {
                                MimeMultipart mimeMultipart = (MimeMultipart) msg.getContent();
                                String messageBody = getTextFromMimeMultipart(mimeMultipart);
                                String textFromHtml = html2text(messageBody);
                                applyRegexExtractData(msg, textFromHtml);
                            }
                            Log.e(TAG, "Mail extracted: " + mails);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "An exception has occurred: ", e);
                    }
                    return false;
                }
            });

//            FetchProfile fp = new FetchProfile();
//            fp.add(FetchProfile.Item.ENVELOPE);
//            fp.add(FetchProfile.Item.CONTENT_INFO);
//            inbox.fetch(messages, fp);
//
//            for (Message message : messages) {
//                //Log.e(TAG, "Message: " + message);
//                mails.add(message.getSubject());
//            }
            //Log.e(TAG, "List is: " + mails);
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred: ", e);
        }
        return null;
    }

    private void loadPatterns() {
        msgSubjectPattern = Pattern.compile("(Order\\s+)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
        orderDetailsPattern = Pattern.compile("-\\s+-[^\\w]+([^\\.-]+)[^-]+-\\s+Size\\s+(\\w+)[^\\w]+Qty\\s+(\\w+)\\s+-\\s+([^\\s]+)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
        deliveryExpectedDatePattern = Pattern.compile("Delivery\\s+by\\s+(\\w+,\\s+\\w+\\s+\\w+)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
        paymentModeDeliverAddressPattern = Pattern.compile("Paid\\s+by\\s+(.+)Delivering\\s+at\\s+(.+\\d{6}+)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
    }

    private void applyRegexExtractData(Message msg, String mailBody) throws Exception{
        String orderedOnDate = msg.getHeader("Date")[0];
        String expectedDeliveryDate = "";
        orderDetailsMatcher = orderDetailsPattern.matcher(mailBody);
        deliveryExpectedDateMatcher = deliveryExpectedDatePattern.matcher(mailBody);
        paymentModeDeliverAddressMatcher = paymentModeDeliverAddressPattern.matcher(mailBody);
        if(deliveryExpectedDateMatcher.find()){
            expectedDeliveryDate = deliveryExpectedDateMatcher.group(1);
        }
        while(orderDetailsMatcher.find()){
            Mail extractedMail = new Mail();
            extractedMail.setOrderedOnDate(orderedOnDate);
            extractedMail.setDescription(orderDetailsMatcher.group(1));
            extractedMail.setProductSize(orderDetailsMatcher.group(2));
            extractedMail.setQuantity(Integer.parseInt(Objects.requireNonNull(orderDetailsMatcher.group(3))));
            extractedMail.setPrice(orderDetailsMatcher.group(4));
            extractedMail.setExpectedDeliveryDate(expectedDeliveryDate);
            extractedMail.setSourceName("Myntra"); // TODO: Remove hardcoding source
            if(paymentModeDeliverAddressMatcher.find()){
                extractedMail.setPaymentMode(paymentModeDeliverAddressMatcher.group(1));
                extractedMail.setDeliveryAddress(paymentModeDeliverAddressMatcher.group(2));
            }
            mails.add(extractedMail);
        }
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        mailsListInterface.populateList(mails);
    }
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws Exception {
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
