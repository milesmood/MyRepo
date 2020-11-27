package com.example.learningenglish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        //wordToTranslate = (String) getIntent().getSerializableExtra("wordsFav");
       // String wordToTranslate = sharedPreferences.getString("wordFav", "unknown");
        FileInputStream fin = null;
        try {
            fin = openFileInput("newfile");
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String (bytes);
            Pattern pattern = Pattern.compile("\n");
            String[] strings = pattern.split(text);
            for (String s : strings) {
                listName.add(s);
                adapter.notifyDataSetChanged();
            }



        }
        catch(IOException ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally{

            try{
                if(fin!=null)
                    fin.close();
            }
            catch(IOException ex){

                Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        System.out.println(wordToTranslate);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.back:
                startActivity(new Intent(FavoriteActivity.this, SecondActivity.class));
                finish();;
                return true;
            case R.id.exitAcc:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}