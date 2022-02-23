package com.minimaldev.android.orderzpro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {
    List<String> list = new ArrayList<>();
    private final String TAG = getClass().getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        populateList();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        MailRecyclerAdapter mailRecyclerAdapter = new MailRecyclerAdapter(this, list);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mailRecyclerAdapter);
    }

    private void populateList() {
        list.add("hello");
        list.add("world");
        list.add("name");
        list.add("is");
        list.add("java");
        Log.e(TAG, list.toString());
    }
}