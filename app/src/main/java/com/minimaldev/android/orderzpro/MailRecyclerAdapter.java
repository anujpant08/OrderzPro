package com.minimaldev.android.orderzpro;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.minimaldev.android.orderzpro.model.Mail;

import java.util.List;
import java.util.Objects;

public class MailRecyclerAdapter extends RecyclerView.Adapter<MailRecyclerAdapter.MailViewHolder> {
    private final String TAG = getClass().getSimpleName();
    List<Mail> mails;
    Context context;

    public MailRecyclerAdapter(Context context, List<Mail> mails) {
        this.context = context;
        this.mails = mails;
    }

    @NonNull
    @Override
    public MailRecyclerAdapter.MailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view_holder, parent, false);
        return new MailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MailRecyclerAdapter.MailViewHolder holder, int position) {
        try{
            Mail currentMail = this.mails.get(holder.getAdapterPosition());
            holder.mailDescription.setText(currentMail.getDescription().trim());
            holder.mailSource.setText(currentMail.getSourceName().trim());
            holder.mailPrice.setText(currentMail.getPrice().trim());
            holder.mailQuantity.setText("Qty " + currentMail.getQuantity());
            holder.mailDeliveryDate.setText(currentMail.isDelivered() ? currentMail.getDeliveredDate().trim() : currentMail.getExpectedDeliveryDate().trim());
            holder.mailPaymentMode.setText(currentMail.getPaymentMode() != null && !currentMail.getPaymentMode().equals("") ? currentMail.getPaymentMode().trim() : "Payment Mode");
        } catch(Exception e){
            Log.e(TAG, "An exception has occurred: ", e);
        }
    }

    @Override
    public int getItemCount() {
        return this.mails.size();
    }

    public class MailViewHolder extends RecyclerView.ViewHolder {
        TextView mailDescription;
        TextView mailSource;
        TextView mailPrice;
        TextView mailQuantity;
        TextView mailDeliveryDate;
        TextView mailPaymentMode;
        public MailViewHolder(@NonNull View itemView) {
            super(itemView);
            mailDescription = itemView.findViewById(R.id.mail_description);
            mailSource = itemView.findViewById(R.id.mail_source);
            mailPrice = itemView.findViewById(R.id.mail_price);
            mailQuantity = itemView.findViewById(R.id.mail_quantity);
            mailDeliveryDate= itemView.findViewById(R.id.mail_delivery_date);
            mailPaymentMode = itemView.findViewById(R.id.mail_payment_mode);
        }
    }
}
