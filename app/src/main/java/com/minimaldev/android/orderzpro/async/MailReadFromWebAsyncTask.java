package com.minimaldev.android.orderzpro.async;

import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;

import com.minimaldev.android.orderzpro.MailsListInterface;
import com.minimaldev.android.orderzpro.model.Mail;

import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;

public class MailReadFromWebAsyncTask extends AsyncTask<Void, Void, Void> {
    private final List<Mail> mails = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();
    static Pattern msgSubjectPattern = null;
    static Matcher msgSubjectMatcher = null;
    static Pattern myntraOrderDetailsPattern = null;
    static Matcher myntraOrderDetailsMatcher = null;
    static Pattern flipkartOrderDetailsPattern = null;
    static Matcher flipkartOrderDetailsMatcher = null;
    static Pattern deliveryExpectedDatePattern = null;
    static Matcher deliveryExpectedDateMatcher = null;
    static Pattern paymentModeDeliverAddressPattern = null;
    static Matcher paymentModeDeliverAddressMatcher = null;
    static Pattern sourceNamePattern = null;
    static Matcher sourceNameMatcher = null;
    private MailsListInterface mailsListInterface = null;
    public MailReadFromWebAsyncTask(MailsListInterface mailsListInterface) {
        this.mailsListInterface = mailsListInterface;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            loadPatterns();
            Properties props = getProperties();
            Session session = getSession(props);
            Store store = getStore(session);
            Folder ordersFolder = getFolder(store);
            searchMessages(ordersFolder);
        } catch (Exception e) {
            Log.e(TAG, "An exception occurred: ", e);
        }
        return null;
    }

    private void searchMessages(Folder ordersFolder) throws MessagingException {
        ordersFolder.search(new SearchTerm() {
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
                    }
                } catch (Exception e) {
                    Log.e(TAG, "An exception has occurred: ", e);
                }
                return false;
            }
        });
        Log.e(TAG, "Mail extracted: " + mails);
    }

    @NonNull
    private Folder getFolder(Store store) throws MessagingException {
        Folder inbox = store.getFolder("Test");
        Log.e(TAG, "No. of messages: " + inbox.getMessageCount());
        inbox.open(Folder.READ_ONLY);
        return inbox;
    }

    @NonNull
    private Store getStore(Session session) throws MessagingException {
        Store store = session.getStore("imaps");
        store.connect("imap.gmail.com", "u", "p");
        return store;
    }

    private Session getSession(Properties props) {
        return Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("Uname", "Pwd");
            }
        });
    }

    @NonNull
    private Properties getProperties() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        return props;
    }

    private void loadPatterns() {
        msgSubjectPattern = Pattern.compile("(Order.+placed)|(Order.+Confirmation)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
        myntraOrderDetailsPattern = Pattern.compile("-\\s+-[^\\w]+([^\\.-]+)[^-]+-\\s+Size\\s+(\\w+)[^\\w]+Qty\\s+(\\w+)\\s+-\\s+([^\\s]+)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
        flipkartOrderDetailsPattern = Pattern.compile("Delivery\\s+Address\\s+(.+)\\s+SMS.+]\\s+([\\w\\s]+)\\s+Rs\\.\\s+([^\\s]+)\\s+.+Qty:\\s+(\\w+)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
        deliveryExpectedDatePattern = Pattern.compile("Delivery\\s+by\\s+(\\w+,\\s+\\w+\\s+\\w+)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
        paymentModeDeliverAddressPattern = Pattern.compile("Paid\\s+by\\s+(.+)Delivering\\s+at\\s+(.+\\d{6}+)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
        sourceNamePattern = Pattern.compile("@([^\\.]+)", Pattern.DOTALL|Pattern.CASE_INSENSITIVE);
    }

    private void applyRegexExtractData(Message msg, String mailBody) throws Exception{
        String orderedOnDate = msg.getHeader("Date")[0];
        String sourceName = msg.getHeader("From")[0].toLowerCase(Locale.ROOT);
        sourceNameMatcher = sourceNamePattern.matcher(sourceName);
        if(sourceNameMatcher.find()){
            sourceName = sourceNameMatcher.group(1);
            if (sourceName != null) {
                switch(sourceName){
                    case "gmail": //TODO: Remove "gmail" case after testing.
                        getMyntraOrder(mailBody, orderedOnDate);
                        getFlipkartOrder(mailBody, orderedOnDate);
                        break;
                    case "myntra":
                        getMyntraOrder(mailBody, orderedOnDate);
                        break;
                    case "flipkart":
                        getFlipkartOrder(mailBody, orderedOnDate);
                        break;
                }
            }else{
                Log.e(TAG, "Source name match not found!");
            }
        }
    }

    private void getMyntraOrder(String mailBody, String orderedOnDate) {
        String expectedDeliveryDate = "";
        String paymentMode = "";
        String deliveryAddress = "";
        myntraOrderDetailsMatcher = myntraOrderDetailsPattern.matcher(mailBody);
        deliveryExpectedDateMatcher = deliveryExpectedDatePattern.matcher(mailBody);
        paymentModeDeliverAddressMatcher = paymentModeDeliverAddressPattern.matcher(mailBody);
        if(deliveryExpectedDateMatcher.find()){
            expectedDeliveryDate = deliveryExpectedDateMatcher.group(1);
        }
        if(paymentModeDeliverAddressMatcher.find()){
            paymentMode = paymentModeDeliverAddressMatcher.group(1);
            deliveryAddress = paymentModeDeliverAddressMatcher.group(2);
        }
        while(myntraOrderDetailsMatcher.find()){
            Mail extractedMail = new Mail();
            extractedMail.setOrderedOnDate(orderedOnDate);
            extractedMail.setDescription(myntraOrderDetailsMatcher.group(1));
            extractedMail.setProductSize(myntraOrderDetailsMatcher.group(2));
            extractedMail.setQuantity(Integer.parseInt(Objects.requireNonNull(myntraOrderDetailsMatcher.group(3))));
            extractedMail.setPrice(myntraOrderDetailsMatcher.group(4));
            extractedMail.setExpectedDeliveryDate(expectedDeliveryDate);
            extractedMail.setPaymentMode(paymentMode);
            extractedMail.setDeliveryAddress(deliveryAddress);
            extractedMail.setSourceName("Myntra"); //TODO: Remove hardcoded source. Tip: Try using from email address for the source. For Amazon, we can use auto-confirm.amazon.in
            mails.add(extractedMail);
        }
    }
    private void getFlipkartOrder(String mailBody, String orderedOnDate) {
        String expectedDeliveryDate = "";
        flipkartOrderDetailsMatcher = flipkartOrderDetailsPattern.matcher(mailBody);
        deliveryExpectedDateMatcher = deliveryExpectedDatePattern.matcher(mailBody);
        if(deliveryExpectedDateMatcher.find()){
            expectedDeliveryDate = deliveryExpectedDateMatcher.group(1);
        }
        while(flipkartOrderDetailsMatcher.find()){
            Mail extractedMail = new Mail();
            extractedMail.setOrderedOnDate(orderedOnDate);
            extractedMail.setDescription(flipkartOrderDetailsMatcher.group(2));
            extractedMail.setProductSize("");
            extractedMail.setQuantity(Integer.parseInt(Objects.requireNonNull(flipkartOrderDetailsMatcher.group(4))));
            extractedMail.setPrice(flipkartOrderDetailsMatcher.group(3));
            extractedMail.setExpectedDeliveryDate(expectedDeliveryDate);
            extractedMail.setPaymentMode("");
            extractedMail.setDeliveryAddress(flipkartOrderDetailsMatcher.group(1));
            extractedMail.setSourceName("Flipkart");
            mails.add(extractedMail);
        }
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        mailsListInterface.populateList(mails);
    }
    private String getTextFromMimeMultipart(MimeMultipart mimeMultipart)  throws Exception {
        StringBuilder result = new StringBuilder();
        int count = mimeMultipart.getCount();
        for (int i = 0; i < count; i++) {
            BodyPart bodyPart = mimeMultipart.getBodyPart(i);
            if (bodyPart.isMimeType("text/plain")) {
                result.append("\n").append(bodyPart.getContent());
                break;
            } else if (bodyPart.isMimeType("text/html")) {
                String html = (String) bodyPart.getContent();
                result.append("\n").append(html); //org.jsoup.Jsoup.parse(html).text();
            } else if (bodyPart.getContent() instanceof MimeMultipart){
                result.append(getTextFromMimeMultipart((MimeMultipart) bodyPart.getContent()));
            }
        }
        return result.toString();
    }
    private String html2text(String html) {
        return Jsoup.parse(html).text();
    }
}
