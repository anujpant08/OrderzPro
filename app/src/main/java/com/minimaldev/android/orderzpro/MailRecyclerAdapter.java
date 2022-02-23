package com.minimaldev.android.orderzpro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MailRecyclerAdapter extends RecyclerView.Adapter<MailRecyclerAdapter.MailViewHolder> {
    List<String> mails;
    Context context;

    public MailRecyclerAdapter(Context context, List<String> mails) {
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
        holder.textView.setText(this.mails.get(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return this.mails.size();
    }

    public class MailViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MailViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.mail_text);
        }
    }
}
