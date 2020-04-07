package com.example.quizapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class
ScoreActivity extends AppCompatActivity {

    private TextView score;
    private Button Done;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        score = (TextView) findViewById(R.id.score_textView);
        Done = (Button) findViewById(R.id.Score_DoneButton);
        String score_Str = getIntent().getStringExtra("SCORE");
        score.setText(score_Str);
        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this,MainActivity.class);
                startActivity(intent);
                ScoreActivity.this.finish();
            }
        });
    }
}
