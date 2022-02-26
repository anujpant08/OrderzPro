package com.minimaldev.android.orderzpro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.minimaldev.android.orderzpro.model.Mail;
import com.minimaldev.android.orderzpro.viewmodel.MailReadViewModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Mail> list = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //populateList();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        MailRecyclerAdapter mailRecyclerAdapter = new MailRecyclerAdapter(this, list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mailRecyclerAdapter);
        MailReadViewModel mailReadViewModel = new ViewModelProvider(this).get(MailReadViewModel.class);
        mailReadViewModel.getMails().observe(this, new Observer<List<Mail>>() {
            @Override
            public void onChanged(List<Mail> newList) {
                //Log.e(TAG, "Inside onChanged(): " + newList);
                list.clear();
                list.addAll(newList);
                mailRecyclerAdapter.notifyDataSetChanged();
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