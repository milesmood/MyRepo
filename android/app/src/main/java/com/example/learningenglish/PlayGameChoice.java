package com.example.learningenglish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningenglish.models.Connections;
import com.example.learningenglish.models.Lists;
import com.example.learningenglish.models.RandomNumb;
import com.example.learningenglish.models.User;
import com.example.learningenglish.models.Words;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class PlayGameChoice<listItem> extends AppCompatActivity {
    TextView word;
    Button var1, var2, var3, var4, next;
    private DatabaseReference myRef;

    private int count = 0;
    FirebaseAuth auth;
    private String uid;
    private ArrayList<Words> wordsList = new ArrayList<>();
    private ArrayList<Lists> listData = new ArrayList<>();
    private ArrayList<Connections> conData = new ArrayList<>();
    private Map<Integer, Integer> conNumber = new HashMap<>();
    private List<Integer> listWordItems = new ArrayList<>();
    private int counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game_choice);

        var1 = findViewById(R.id.var1);
        var2 = findViewById(R.id.var2);
        var3 = findViewById(R.id.var3);
        var4 = findViewById(R.id.var4);
        next = findViewById(R.id.next);
        word = findViewById(R.id.word);
        getDataFromDB();
        for (int f: listWordItems){
            System.out.println("В списке" +f);
        }
    }


    private void getDataFromDB() {
        auth = FirebaseAuth.getInstance();
        FirebaseUser cUser = auth.getCurrentUser();

        if (cUser != null) {
            uid = cUser.getUid();
        }

        myRef = FirebaseDatabase.getInstance().getReference();
        myRef.child("Lists").orderByChild("usrId").equalTo(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot listSnapshot : dataSnapshot.getChildren()) {
                    Lists list = listSnapshot.getValue(Lists.class);
                    list.setId_list(listSnapshot.getKey());
                    listData.add(list);

                }
                for (Lists l : listData) {
                    System.out.println(l.getId_list());
                }

                getWordsOneList(listData.get(0).getId_list());

                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        counter --;
                        if (counter == 0){
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Игра окончена",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        } else{
                            varClick();
                        }
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

        switch (id) {
            case R.id.main_menu:
                startActivity(new Intent(PlayGameChoice.this, SecondActivity.class));
                finish();
                ;
                return true;
            case R.id.back:
                startActivity(new Intent(PlayGameChoice.this, ChooseGameActivity.class));
                finish();
                ;
                return true;
            case R.id.get_result:
                startActivity(new Intent(PlayGameChoice.this, ChooseGameActivity.class));
                finish();
                ;
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

    private void getWordsOneList(String id_list) {
        myRef.child("Connections").orderByChild("id_list").equalTo(id_list).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot listSnapshot : dataSnapshot.getChildren()) {
                            Connections connections = listSnapshot.getValue(Connections.class);
                            conData.add(connections);
                        }
                        count = conData.size();
                        counter = count;
                        for (int i = 0; i < conData.size(); i++) {
                            System.out.println(conData.get(i).getId_word());

                            myRef.child("Words/" + conData.get(i).getId_word()).
                                    addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Words words = dataSnapshot.getValue(Words.class);
                                            wordsList.add(words);
                                            System.out.println(count);
                                            if (count == wordsList.size()) {
                                                if (count >= 4) {
                                                    varClick();
                                                } else {
                                                    System.out.println("зашла сюда " + count);
                                                    System.out.println("list size" + wordsList.size());
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "Добавьте слова в список или выберите другой",
                                                            Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                }
                                               
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }

                                    });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void varClick() {

        RandomNumb rand = new RandomNumb(count);


        int c1 = rand.generate();
        var1.setText(wordsList.get(c1).getEngWord());
        int c2 = rand.generate();
        var2.setText(wordsList.get(c2).getEngWord());
        int c3 = rand.generate();
        var3.setText(wordsList.get(c3).getEngWord());
        int c4 = rand.generate();
        var4.setText(wordsList.get(c4).getEngWord());

        conNumber.put(1, c1);
        conNumber.put(2, c2);
        conNumber.put(3, c3);
        conNumber.put(4, c4);

        final Random random = new Random();
        int variable = 0;
        int wordItem = rand.generate();

        while (listWordItems.contains(conNumber.get(wordItem)) || (variable != count)){
            variable++;
            wordItem = rand.generate();
        }

        if (variable != count) {
            listWordItems.add(conNumber.get(wordItem));
            System.out.println("wordItem" + wordItem);
            word.setText(wordsList.get(conNumber.get(wordItem)).getRusWord());
            final int wordItem1 = wordItem;
            var1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(conNumber.get(wordItem1));
                    if (wordsList.get(conNumber.get(wordItem1)).getEngWord().equals(var1.getText())) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Правильно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Не верно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }

                }
            });
            var2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(conNumber.get(wordItem1));

                    if (wordsList.get(conNumber.get(wordItem1)).getEngWord().equals(var2.getText())) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Правильно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Не верно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            });
            var3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(conNumber.get(wordItem1));

                    if (wordsList.get(conNumber.get(wordItem1)).getEngWord().equals(var3.getText())) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Правильно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Не верно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            });
            var4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(conNumber.get(wordItem1));

                    if (wordsList.get(conNumber.get(wordItem1)).getEngWord().equals(var4.getText())) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Правильно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Не верно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                }
            });
        } else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Выход",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


}



