package com.example.learningenglish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.learningenglish.models.Connections;
import com.example.learningenglish.models.Lists;
import com.example.learningenglish.models.Words;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.prefs.Preferences;

public class PlayGameChoice extends AppCompatActivity {
    TextView word;
    Button var1, var2, var3, var4, next;
    ImageButton someBtnId;
    private DatabaseReference myRef;

//    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
    ArrayList<String> wordsFav = new ArrayList<>();

    private int count = 0;
    FirebaseAuth auth;
    private String uid;
    private ArrayList<Words> wordsList = new ArrayList<>();
    private ArrayList<Lists> listData = new ArrayList<>();
    private ArrayList<Connections> conData = new ArrayList<>();
    private Map<Integer, Integer> conNumber = new HashMap<>();
    private List<Integer> listWordItems = new ArrayList<>();
    private int counter = 0;
    private int counterTrue = 0;
    List usedWordList = new ArrayList();
    int counterTrans = 0;
    String idList;
    int wordItem;

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
        someBtnId = findViewById(R.id.someBtnId);
         idList =(String)getIntent().getSerializableExtra("idList");
        System.out.println("idList "+idList);
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

        switch (id) {
            case R.id.main_menu:
                startActivity(new Intent(PlayGameChoice.this, SecondActivity.class));
                finish();

                return true;
            case R.id.back:
                Intent intent2 = new Intent(PlayGameChoice.this, UserListsActivity.class);
                intent2.putExtra("activity","1");
                startActivity(intent2);
                finish();

                return true;
            case R.id.get_result:
                Intent intent = new Intent(PlayGameChoice.this, ResultActivity.class);
                intent.putExtra("count",counter);
                intent.putExtra("countTrue",counterTrue);
                intent.putExtra("activity","1");
                startActivity(intent);

                finish();
                conNumber.clear();
                usedWordList.clear();
                counter = 0;
                counterTrue = 0;
                counterTrans = 0;

                return true;
            case R.id.exitAcc:
                FirebaseAuth.getInstance().signOut();
                Intent intents = new Intent(PlayGameChoice.this, MainActivity.class);
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
                                            System.out.println(count);
                                            if (count == wordsList.size()) {
                                                if (count >= 4) {
                                                    changeWords();

                                                } else {
                                                    System.out.println("зашла сюда " + count);
                                                    System.out.println("list size" + wordsList.size());
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "Добавьте слова в список или выберите другой",
                                                            Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                }
                                                next.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        changeWords();
                                                    }


                                                });

                                                someBtnId.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        String filename = "newfile";
                                                        String string = word.getText()+" - "+ findByWordName(word.getText())+"\n";                                                        FileOutputStream outputStream;

                                                        try {
                                                            outputStream = openFileOutput(filename, Context.MODE_APPEND);
                                                            outputStream.write(string.getBytes());
                                                            outputStream.close();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
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

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private CharSequence findByWordName(CharSequence s) {
        for (Words w : wordsList) {
            System.out.println("слово "+w.getEngWord());
            System.out.println("ориг "+s);
            if (w.getEngWord().equals(s)) {
                return w.getRusWord();
            }
            if (w.getRusWord().equals(s)) {
                return w.getEngWord();
            }

        }
        return s;
    }

    private void changeWords() {
        final Random random = new Random();
        counterTrans = random.nextInt(10);

        List example = new ArrayList();
        int c1 = random.nextInt(count);
        System.out.println("c1 " + c1);
        example.add(c1);
        int c2 = random.nextInt(count);
        while (example.contains(c2)){
            c2 = random.nextInt(count);
        }
        example.add(c2);
        System.out.println("c2 " + c2);
        int c3 = random.nextInt(count);
        while (example.contains(c3)){
            c3 = random.nextInt(count);
        }
        example.add(c3);
        System.out.println("c3 " + c3);
        int c4 = random.nextInt(count);
        while (example.contains(c4)){
            c4 = random.nextInt(count);
        }
        System.out.println("c4 " + c4);
        example.clear();
        if (counterTrans % 2 == 0) {
            var1.setText(wordsList.get(c1).getEngWord());
            var2.setText(wordsList.get(c2).getEngWord());
            var3.setText(wordsList.get(c3).getEngWord());
            var4.setText(wordsList.get(c4).getEngWord());
        } else {
            var1.setText(wordsList.get(c1).getRusWord());
            var2.setText(wordsList.get(c2).getRusWord());
            var3.setText(wordsList.get(c3).getRusWord());
            var4.setText(wordsList.get(c4).getRusWord());
        }
        conNumber.put(1, c1);
        conNumber.put(2, c2);
        conNumber.put(3, c3);
        conNumber.put(4, c4);

        wordItem = random.nextInt(4)+1;
        int counterItem = count;
        while (usedWordList.contains(conNumber.get(wordItem)) && (counterItem > 0) ){
            wordItem = random.nextInt(4)+1;
            counterItem--;
        }
        if (counterItem > 0){
            System.out.println("wordItem " + wordItem);
            System.out.println("conNumber.get(wordItem) " + conNumber.get(wordItem));
            System.out.println("conNumber.get(wordItem)).getRusWord()" + wordsList.get(conNumber.get(wordItem)).getEngWord());
            usedWordList.add(conNumber.get(wordItem));
            if (counterTrans % 2 == 0) {
                word.setText(wordsList.get(conNumber.get(wordItem)).getRusWord());
            } else {
                word.setText(wordsList.get(conNumber.get(wordItem)).getEngWord());
            }
            final int wordItem1 = wordItem;
            clickButtons(wordItem1);
            counter++;
        } else{
            Intent intent = new Intent(PlayGameChoice.this, ResultActivity.class);
            intent.putExtra("count",counter);
            intent.putExtra("countTrue",counterTrue);
            intent.putExtra("activity","1");
            startActivity(intent);

            finish();
            conNumber.clear();
            usedWordList.clear();
            counter = 0;
            counterTrue = 0;
            counterTrans = 0;

        }
    }

    private void clickButtons(final int wordItem1){
        final int[] item = new int[1];
        item[0] = 0;
        var1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(conNumber.get(wordItem1));
                if (item[0] == 0) {
                    if (wordsList.get(conNumber.get(wordItem1)).getEngWord().equals(var1.getText()) ||
                            wordsList.get(conNumber.get(wordItem1)).getRusWord().equals(var1.getText())) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Правильно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        counterTrue++;
                        item[0]++;
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Не верно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        item[0]++;
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ответ уже был получен",
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
                if (item[0] == 0) {
                    if (wordsList.get(conNumber.get(wordItem1)).getEngWord().equals(var2.getText()) ||
                            wordsList.get(conNumber.get(wordItem1)).getRusWord().equals(var2.getText())) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Правильно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        counterTrue++;
                        item[0]++;
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Не верно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        item[0]++;
                    }
                }else{
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ответ уже был получен",
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
                if (item[0] == 0) {
                    if (wordsList.get(conNumber.get(wordItem1)).getEngWord().equals(var3.getText()) ||
                            wordsList.get(conNumber.get(wordItem1)).getRusWord().equals(var3.getText())) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Правильно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        counterTrue++;
                        item[0]++;
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Не верно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        item[0]++;
                    }
                }else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ответ уже был получен",
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
                if (item[0] == 0) {
                    if (wordsList.get(conNumber.get(wordItem1)).getEngWord().equals(var4.getText()) ||
                            wordsList.get(conNumber.get(wordItem1)).getRusWord().equals(var4.getText())) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Правильно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        counterTrue++;
                        item[0]++;
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Не верно!",
                                Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                        item[0]++;
                    }
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ответ уже был получен",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
            }
        });
    }
}



