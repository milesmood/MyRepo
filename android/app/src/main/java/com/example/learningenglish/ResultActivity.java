package com.example.learningenglish;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class ResultActivity extends AppCompatActivity {

    Button playGame;
    TextView viewResult,viewResult2, viewResult1,viewResult3;
    String param;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        int count = (Integer) getIntent().getSerializableExtra("count");
        int countTrue = (Integer) getIntent().getSerializableExtra("countTrue");
        viewResult = findViewById(R.id.result);
        viewResult1 = findViewById(R.id.result1);
        viewResult2 = findViewById(R.id.result2);
        viewResult3 = findViewById(R.id.result3);
        playGame = findViewById(R.id.btnPlayGame);
        viewResult.setText("Ваш результат");
        viewResult1.setText("Всего вопросов: " + count);
        viewResult2.setText("Правильных ответов: " + countTrue);
        viewResult3.setText("Процент выполнения: " + (countTrue * 100 / count) + "%");

        param = (String) getIntent().getSerializableExtra("activity");

        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (param.equals("1")) {
                    Intent intent1 = new Intent(ResultActivity.this, UserListsActivity.class);
                    intent1.putExtra("activity","1");
                    startActivity(intent1);
                } else {
                    Intent intent = new Intent(ResultActivity.this, UserListsActivity.class);
                    intent.putExtra("activity","2");
                    startActivity(intent);
                }
                finish();
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
            case R.id.exitAcc:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case R.id.main_menu:
                startActivity(new Intent(ResultActivity.this, SecondActivity.class));
                finish();
                return true;
            case R.id.chooseGame:
                startActivity(new Intent(ResultActivity.this, ChooseGameActivity.class));
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}