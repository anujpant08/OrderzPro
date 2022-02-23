package com.minimaldev.android.orderzpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;

public class MainActivity extends AppCompatActivity {
    List<String> list = new ArrayList<>();
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
        mailReadViewModel.getMails().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> newList) {
                //Log.e(TAG, "Inside onChanged(): " + newList);
                list.clear();
                list.addAll(newList);
                mailRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }

    private void populateList() {
        list.add("hello");
        list.add("world");
        list.add("name");
        list.add("is");
        list.add("java");
        Log.e(TAG, list.toString());
        MailReadViewModel mailReadViewModel = new MailReadViewModel();

    }
}