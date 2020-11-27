package com.example.learningenglish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.learningenglish.models.Lists;
import com.example.learningenglish.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListsActivity<onListItemClick> extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<Lists> listData = new ArrayList<>();
    private ArrayList<String> listName = new ArrayList<>();
    private DatabaseReference myRef;

    FirebaseAuth auth;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_lists);


        listView = findViewById(R.id.list_view);
        listName = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listName);

        listView.setAdapter(adapter);

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
                for (DataSnapshot listSnapshot : dataSnapshot.getChildren()) {
                    Lists list = listSnapshot.getValue(Lists.class);
                    list.setId_list(listSnapshot.getKey());
                    listData.add(list);
                    listName.add(listData.get(listData.size()-1).getListName());
                    adapter.notifyDataSetChanged();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                int position, long id) {
                            String param = (String)getIntent().getSerializableExtra("activity");
                            if(param.equals("1")) {
                                Intent intent = new Intent(UserListsActivity.this, PlayGameChoice.class);
                                intent.putExtra("idList", listData.get(position).getId_list());
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(UserListsActivity.this, PlayGame.class);
                                intent.putExtra("idList", listData.get(position).getId_list());
                                startActivity(intent);
                            }
                        }
                    });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.result_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.main_menu:
                startActivity(new Intent(UserListsActivity.this, SecondActivity.class));
                finish();;
                return true;
            case R.id.exitAcc:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserListsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.chooseGame:
                startActivity(new Intent(UserListsActivity.this, ChooseGameActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}