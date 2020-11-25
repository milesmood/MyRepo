package com.example.learningenglish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.learningenglish.models.Connections;
import com.example.learningenglish.models.Lists;
import com.example.learningenglish.models.User;
import com.example.learningenglish.models.Words;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlayGameChoice extends AppCompatActivity {
    TextView word;
    Button var1, var2, var3,var4;
    private DatabaseReference myRef;

    FirebaseAuth auth;
    private String uid;
    private ArrayList<Words> wordsList= new ArrayList<>();
    private ArrayList<Lists> listData= new ArrayList<>();
    private ArrayList<Connections> conData= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game_choice);

        var1 = findViewById(R.id.var1);
        var2 = findViewById(R.id.var2);
        var3 = findViewById(R.id.var3);
        var4 = findViewById(R.id.var4);
        word = findViewById(R.id.word);
        getDataFromDB();
    }


    private void getDataFromDB(){
        auth = FirebaseAuth.getInstance();
        FirebaseUser cUser = auth.getCurrentUser();

        if (cUser != null) {
            uid = cUser.getUid();
        }

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Lists").orderByChild("usrId").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot listSnapshot: dataSnapshot.getChildren()) {
                    Lists list = listSnapshot.getValue(Lists.class);
                    list.setId_list(listSnapshot.getKey());
                    listData.add(list);

                }
                for (Lists l : listData) {
                    System.out.println(l.getId_list());
                }
                myRef.child("Connections").orderByChild("id_list").equalTo(listData.get(0).getId_list()).
                        addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot listSnapshot: dataSnapshot.getChildren()) {
                            Connections connections = listSnapshot.getValue(Connections.class);
                            conData.add(connections);

                        }
                        for (Connections c : conData) {
                            System.out.println(c.getId_word());

                            myRef.child("Words/" +c.getId_word()).
                                    addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                Words words = dataSnapshot.getValue(Words.class);
                                                wordsList.add(words);
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            for (Words w : wordsList) {
                                                System.out.println(w.getEngWord() + ' ' + w.getRusWord());
                                            }
                                        }

                                    });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.game_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.main_menu:
                startActivity(new Intent(PlayGameChoice.this, SecondActivity.class));
                finish();;
                return true;
            case R.id.back:
                startActivity(new Intent(PlayGameChoice.this, ChooseGameActivity.class));
                finish();;
                return true;
            case R.id.get_result:
                startActivity(new Intent(PlayGameChoice.this, ChooseGameActivity.class));
                finish();;
                return true;
            case R.id.exitAcc:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(PlayGameChoice.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}