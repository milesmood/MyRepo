package com.example.learningenglish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {
    private ListView listFavor;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> listName = new ArrayList<>();
    private DatabaseReference myRef;
    private String wordToTranslate;

    FirebaseAuth auth;
    private String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        listFavor = findViewById(R.id.list_favor);
        listName = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listName);

        listFavor.setAdapter(adapter);
        putInList();
    }

    private void putInList(){
        wordToTranslate = (String) getIntent().getSerializableExtra("wordToTranslate");
        System.out.println(wordToTranslate);
        listName.add(wordToTranslate);
        adapter.notifyDataSetChanged();
    }
}