package com.example.quizapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.animation.Animator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quizapp.Model.Questions_Model;

import java.util.ArrayList;
import java.util.List;

public class Questions_Activity extends AppCompatActivity {

    private TextView questions;
    private TextView no_indicator;
    private LinearLayout Questions_options;
    private Button Next_btn;
    private int count = 0;
    private List<Questions_Model> questions_modelsList;
    private int position = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions_);
        Toolbar toolbar = findViewById(R.id.Question_Activity_Toolbar);
        setSupportActionBar(toolbar);
        questions = findViewById(R.id.questions);
        no_indicator = findViewById(R.id.no_indicator);
        Questions_options = findViewById(R.id.Question_options);
        Next_btn= findViewById(R.id.next_btn);

       final List<Questions_Model> questions_modelsList = new ArrayList<>();
       /*questions_modelsList.add(new Questions_Model("question1","a","b","c","d","b"));
       questions_modelsList.add(new Questions_Model("question2","a","b","c","d","d"));
       questions_modelsList.add(new Questions_Model("question3","a","b","c","d","b"));
       questions_modelsList.add(new Questions_Model("question4","a","b","c","d","c"));
       questions_modelsList.add(new Questions_Model("question5","a","b","c","d","a"));*/

        for (int i = 0; i < 4; i++){
            Questions_options.getChildAt(i).setOnClickListener(new View.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onClick(View v) {
                   checkAnswer((Button) v);
                }
            });
        }

       playAnimation(questions,0,questions_modelsList.get(position).getQuestions());

        Next_btn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Next_btn.setEnabled(false);
                Next_btn.setAlpha(0.7f);
                enableOption(true);
                position++;
                if (position == questions_modelsList.size()){
                    return;
                }
                count = 0;
                playAnimation(questions,0,questions_modelsList.get(position).getQuestions());
            }
        });

    }
    private void playAnimation(final View view, final int value, final String data ){
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(500).setStartDelay(100)
                .setInterpolator(new DecelerateInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

                if (value == 0 && count < 4){
                    String option = "";
                    if (count == 0){
                        option = questions_modelsList.get(position).getOptionA();
                    }else if (count == 1){
                        option = questions_modelsList.get(position).getOptionB();
                    }else if (count == 2){
                        option = questions_modelsList.get(position).getOptionC();
                    }else if (count == 3){
                        option = questions_modelsList.get(position).getOptionD();
                    }
                    playAnimation(Questions_options.getChildAt(count),0,option);
                    count++;
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                if (value == 0){
                    try {
                        ((TextView)view).setText(data);
                        no_indicator.setText(position+1+"/"+questions_modelsList.size());
                    }catch (ClassCastException ex){
                        ((Button)view).setText(data);
                    }
                    view.setTag(data);
                    playAnimation(view,1,data);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }
  @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
  private void checkAnswer(Button selectedOption){

        enableOption(false);
        Next_btn.setEnabled(true);
        Next_btn.setAlpha(1);
       if (selectedOption.getText().toString().equals(questions_modelsList.get(position).getCorrectAns())){
            // correct
            score++;
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));
        }else {

            //incorrect
            selectedOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#ff0000")));

            Button correctOption = (Button) Questions_options.findViewWithTag(questions_modelsList.get(position).getCorrectAns());
            correctOption.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4CAF50")));

        }
    }
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void enableOption(boolean enable){


        for (int i = 0; i < 4; i++) {
            Questions_options.getChildAt(i).setEnabled(enable);
            if (enable){
                Questions_options.getChildAt(i).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("@color/primaryTextColor")));
            }
        }
    }

}

