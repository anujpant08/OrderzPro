package com.minimaldev.android.orderzpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.minimaldev.android.orderzpro.async.UpdateDatabaseIOAsync;
import com.minimaldev.android.orderzpro.dao.MailModelDao;
import com.minimaldev.android.orderzpro.database.MailDatabase;
import com.minimaldev.android.orderzpro.model.Mail;
import com.minimaldev.android.orderzpro.viewmodel.MailReadViewModel;
import com.minimaldev.android.orderzpro.viewmodel.WebEmailReadModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Mail> list = new ArrayList<>();
    private MailRecyclerAdapter mailRecyclerAdapter;
    private final String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MailRecyclerAdapter mailRecyclerAdapter = getMailRecyclerAdapter();
        ImageView refreshImageView = findViewById(R.id.refresh_button);
        refreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Refreshing", Toast.LENGTH_LONG).show();
                readMailsFromWeb(mailRecyclerAdapter);
            }
        });
        //Testing FAB for update operation on DB.
        FloatingActionButton floatingActionButton = findViewById(R.id.update_row_floating_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Mail> mails = mailRecyclerAdapter.getMails();
                mails.get(0).setDeliveredDate("16 Feb, 21");
                mails.get(0).setDelivered(true);
                MailModelDao mailModelDao = MailDatabase.getMailDatabase(getApplicationContext()).mailModelDao();
                UpdateDatabaseIOAsync updateDatabaseIOAsync = new UpdateDatabaseIOAsync(mailModelDao, new DatabaseOperationsHelper(mailModelDao), mails);
                updateDatabaseIOAsync.execute();
            }
        });
        //populateList();
        //readMailsFromWeb(mailRecyclerAdapter);
        //DB Read operation
        readMailsFromDB(mailRecyclerAdapter);
        //DB Write operation

    }

    @NonNull
    private MailRecyclerAdapter getMailRecyclerAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mailRecyclerAdapter = new MailRecyclerAdapter(this, list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mailRecyclerAdapter);
        return mailRecyclerAdapter;
    }

    private void readMailsFromWeb(MailRecyclerAdapter mailRecyclerAdapter) {
        WebEmailReadModel webEmailReadModel = new ViewModelProvider(this).get(WebEmailReadModel.class);
        webEmailReadModel.getMails().observe(this, new Observer<List<Mail>>() {
            @Override
            public void onChanged(List<Mail> newList) {
                Log.e(TAG, "Inside onChanged() from Web: " + newList);
//                mailRecyclerAdapter.clearItems();
//                mailRecyclerAdapter.refreshItems(newList);
            }
        });
    }

    private void readMailsFromDB(MailRecyclerAdapter mailRecyclerAdapter) {
        MailReadViewModel mailReadViewModel = new ViewModelProvider(this).get(MailReadViewModel.class);
        mailReadViewModel.getMails().observe(this, new Observer<List<Mail>>() {
            @Override
            public void onChanged(List<Mail> newList) {
                Log.e(TAG, "Inside onChanged() from DB: " + newList);
                mailRecyclerAdapter.clearItems();
                mailRecyclerAdapter.refreshItems(newList);
            }
        });
    }

    /*private void populateList() {
        list.add("hello");
        list.add("world");
        list.add("name");
        list.add("is");
        list.add("java");
        Log.e(TAG, list.toString());
        MailReadViewModel mailReadViewModel = new MailReadViewModel();

    }*/
}