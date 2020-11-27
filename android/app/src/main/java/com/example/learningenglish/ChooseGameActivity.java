package com.example.learningenglish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ChooseGameActivity extends AppCompatActivity {

    private Button oneFour;
    private Button writeAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);
        oneFour = findViewById(R.id.oneFour);
        writeAnswer = findViewById(R.id.writeAnswer);


        oneFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseGameActivity.this, UserListsActivity.class);
                intent.putExtra("activity","1");
                startActivity(intent);
                finish();;
            }
        });

        writeAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseGameActivity.this, UserListsActivity.class);
                intent.putExtra("activity","2");
                startActivity(intent);
                finish();

                Intent intents = new Intent(ChooseGameActivity.this, ResultActivity.class);
                intents.putExtra("activity","2");
            }
        });
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
                startActivity(new Intent(ChooseGameActivity.this, SecondActivity.class));
                finish();;
                return true;
            case R.id.exitAcc:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ChooseGameActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}