package com.example.learningenglish;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.learningenglish.models.Connections;
import com.example.learningenglish.models.Lists;
import com.example.learningenglish.models.Words;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WordsOneListActivity extends AppCompatActivity {

    private static final String TAG = "Ошибка удаления";
    private ListView listView;
    private Button add;
    private ArrayAdapter<String> adapter;
    private ArrayList<Lists> listData = new ArrayList<>();
    private ArrayList<String> listName = new ArrayList<>();
    private DatabaseReference myRef;
    private ArrayList<Connections> conData = new ArrayList<>();
    private Map<Integer, Integer> conNumber = new HashMap<>();
    TextInputEditText rusWord,engWord;

    FirebaseAuth auth;
    private String uid;

    private String idList;
    private int position;
    private int count;
    private ArrayList<Words> wordsList  = new ArrayList<>();
    private ArrayList wordName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_words_one_list);
        listView = findViewById(R.id.list_view);
        add = findViewById(R.id.btnAdd);
        rusWord = findViewById(R.id.rusWord);
        engWord = findViewById(R.id.engWord);

        wordsList  = new ArrayList<>();
        wordName = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, wordName);

        registerForContextMenu(listView);


        listView.setAdapter(adapter);


        position = (Integer) getIntent().getSerializableExtra("position");
        System.out.println("her " + position);
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
                idList = listData.get(position).getId_list();

                getWordsOneList(idList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

                        for (int i = 0; i < conData.size(); i++) {
                            System.out.println(conData.get(i).getId_word());

                            myRef.child("Words/" + conData.get(i).getId_word()).
                                    addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Words words = dataSnapshot.getValue(Words.class);
                                            wordsList.add(words);
                                            System.out.println(count);
                                            wordName.add(wordsList.get(wordsList.size()-1).getRusWord()+ " - " +
                                                    wordsList.get(wordsList.size()-1).getEngWord());
                                            adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }

                                    });
                        }

                        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                               // wordName.remove(i);
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                Query applesQuery = ref.child("Connections").orderByChild("id_word").equalTo(conData.get(i).getId_word());

                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Log.e(TAG, "onCancelled", databaseError.toException());
                                    }
                                });
                                return true;
                            }
                        });

                        add.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                changeListWords();
                                rusWord.setText("");
                                engWord.setText("");
                            }


                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void changeListWords() {
        final String rusWord1 = rusWord.getText().toString();
        if (!rusWord1.equals("") || !engWord.getText().toString().equals("")) {
            /* for (DataSnapshot listSnapshot : dataSnapshot.getChildren()) {
                                Words words = listSnapshot.getValue(Words.class);
                                words.setId_word(listSnapshot.getKey());
                                System.out.println("зашел " + words.getId_word());
                                if (words != null) {
                                    Connections con = new Connections();
                                    con.setId_list(idList);
                                    con.setId_word(words.getId_word());
                                    myRef.child("Connection").push()
                                            .setValue(con)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast toast = Toast.makeText(getApplicationContext(),
                                                            "Добавлено",
                                                            Toast.LENGTH_SHORT);
                                                    toast.setGravity(Gravity.CENTER, 0, 0);
                                                    toast.show();
                                                }

                                            });
                                }

                            }
                            -
             */
            Words word1 = new Words();
            //if (check[0] == 0) {
                word1.setRusWord(rusWord1);
                word1.setEngWord(engWord.getText().toString());
                DatabaseReference db_ref = myRef.child("Words").push();

                db_ref.setValue(word1)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Добавлено",
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                        });
                String wordId = db_ref.getKey();
                System.out.println("слово " + wordId);
                word1.setId_word(wordId);
                //  }else{
                // System.out.println("сюда не должно зайти"+ words.getId_word());
                //    }
                String idWord;
//                            if (words != null){
//                                idWord = words.getId_word();
//                                System.out.println("слово ад"+idWord);
//                            } else{
                idWord = word1.getId_word();
                System.out.println("то что записывается " + idWord);
                // }
                Connections con = new Connections();
                con.setId_list(idList);
                con.setId_word(idWord);
                myRef.child("Connections").push()
                        .setValue(con)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Добавлено",
                                        Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                            }

                        });
           // }
        }else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Введите слово и перевод",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
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
                startActivity(new Intent(WordsOneListActivity.this, SecondActivity.class));
                finish();;
                return true;
            case R.id.exitAcc:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(WordsOneListActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}