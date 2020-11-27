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
import com.example.learningenglish.models.Words;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayGame extends AppCompatActivity {
    TextView word;
    TextInputEditText translate;
    Button next;
    private DatabaseReference myRef;

    private int count = 0;
    FirebaseAuth auth;
    private String uid;
    private ArrayList<Words> wordsList = new ArrayList<>();
    private ArrayList<Lists> listData = new ArrayList<>();
    private ArrayList<Connections> conData = new ArrayList<>();
    private int counter = 0;
    private int counterTrue = 0;
    List usedWordList = new ArrayList();
    int counterTrans = 0;
    int wordItem = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        word = findViewById(R.id.word);
        translate = findViewById(R.id.translate);
        next = findViewById(R.id.next);

        getDataFromDB();

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

                String idList =(String)getIntent().getSerializableExtra("idList");
                getWordsOneList(idList);


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
                startActivity(new Intent(PlayGame.this, SecondActivity.class));
                finish();;
                return true;
            case R.id.back:
                Intent intent2 = new Intent(PlayGame.this, UserListsActivity.class);
                intent2.putExtra("activity","2");
                startActivity(intent2);
                finish();
                return true;
            case R.id.get_result:
                Intent intent = new Intent(PlayGame.this, ResultActivity.class);
                intent.putExtra("count",counter);
                intent.putExtra("countTrue",counterTrue);
                intent.putExtra("activity","2");
                startActivity(intent);

                finish();
                usedWordList.clear();
                counter = 0;
                counterTrue = 0;
                counterTrans = 0;
                translate.setText("");

                return true;
            case R.id.exitAcc:
                FirebaseAuth.getInstance().signOut();
                Intent intents = new Intent(PlayGame.this, MainActivity.class);
                startActivity(intents);
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
                        if (count == 0){
                            System.out.println("зашла сюда " + count);
                            System.out.println("list size" + wordsList.size());
                            Toast toast = Toast.makeText(getApplicationContext(),
                                    "Добавьте слова в список или выберите другой",
                                    Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                        for (int i = 0; i < conData.size(); i++) {
                            System.out.println(conData.get(i).getId_word());

                            myRef.child("Words/" + conData.get(i).getId_word()).
                                    addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Words words = dataSnapshot.getValue(Words.class);
                                            wordsList.add(words);
                                            System.out.println("count "+count);
                                            if (count == wordsList.size()) {
                                                if ((count > 0)&& (words != null)) {
                                                    changeWords();

                                                    next.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {

                                                            translateWord(wordItem);
                                                            changeWords();
                                                            translate.setText("");
                                                        }


                                                    });
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

    private void changeWords() {
        final Random random = new Random();
        counterTrans = random.nextInt(10);

        wordItem = random.nextInt(count);
        int counterItem = count;
        while (usedWordList.contains(wordItem) && (counterItem > 0) ){
            wordItem = random.nextInt(count);
            counterItem--;
        }
        if (counterItem > 0){
            usedWordList.add(wordItem);

            if (counterTrans % 2 == 0) {
                for (Words t: wordsList){
                    System.out.println("Список"+t.getEngWord());
                }
                System.out.println("номер "+wordItem);
                System.out.println("слово по номеру "+wordsList.get(wordItem).getRusWord());
                word.setText(wordsList.get(wordItem).getRusWord());
            } else {
                word.setText(wordsList.get(wordItem).getEngWord());
            }


            counter++;
        } else{
            Intent intent = new Intent(PlayGame.this, ResultActivity.class);
            intent.putExtra("count",counter);
            intent.putExtra("countTrue",counterTrue);
            intent.putExtra("activity","2");
            startActivity(intent);

            finish();
            usedWordList.clear();
            counter = 0;
            counterTrue = 0;
            counterTrans = 0;
            translate.setText("");

        }
    }

    private void translateWord(final int wordItem){
        System.out.println("переаод" + translate.getText().toString());
        System.out.println("переаод" + wordsList.get(wordItem).getEngWord());
        if (wordsList.get(wordItem).getEngWord().equals(translate.getText().toString()) ||
                wordsList.get(wordItem).getRusWord().equals(translate.getText().toString())) {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Правильно!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            counterTrue++;
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Не верно!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

}