package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quizapp.Adapter.Subject_Adapter;
import com.example.quizapp.Model.Questions_Model;
import com.example.quizapp.Model.Subject_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import static com.example.quizapp.Subject_Activity.Class_ID;

public class Question2Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView questions;
    private TextView questionCount;
    private TextView timer;
    private int Subject_ID;
    private Button option1,option2,option3,option4;
    private List<Questions_Model> questions_modelList;
    private ProgressDialog progressDialog;
    private int questionNumber;
    private CountDownTimer countDownTimer;

    private int score;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question2);
        questions = (TextView) findViewById(R.id.question2);
        questionCount = (TextView) findViewById(R.id.Question_count);
        timer = (TextView) findViewById(R.id.timer);
        option1 = (Button) findViewById(R.id.button_Option1);
        option2 = (Button) findViewById(R.id.button_Option2);
        option3 = (Button) findViewById(R.id.button_Option3);
        option4 = (Button) findViewById(R.id.button_Option4);


        option1.setOnClickListener(this);
        option2.setOnClickListener(this);
        option3.setOnClickListener(this);
        option4.setOnClickListener(this);
        Subject_ID = getIntent().getIntExtra("Subject_ID",1);


        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(Question2Activity.this);
        progressDialog.setTitle("Loading Questions");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        getQuestionList();
        score = 0;





    }
    private void getQuestionList(){

        questions_modelList = new ArrayList<>();
        /*questions_modelList.add(new Questions_Model("question1","a","b","c","d",2));
        questions_modelList.add(new Questions_Model("question2","a","b","c","d",2));
        questions_modelList.add(new Questions_Model("question3","a","b","c","d",3));
        questions_modelList.add(new Questions_Model("question4","a","b","c","d",3));
        questions_modelList.add(new Questions_Model("question5","a","b","c","d",1));*/

     firebaseFirestore.collection("Quiz").document("Class" + String.valueOf(Class_ID))
               .collection("Subject" + String.valueOf(Subject_ID))
               .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {

               if (task.isSuccessful()) {
                   QuerySnapshot Questions = task.getResult();
                   for (QueryDocumentSnapshot queryDocumentSnapshot : Questions){
                       questions_modelList.add(new Questions_Model(queryDocumentSnapshot.getString("Question"),
                               queryDocumentSnapshot.getString("A"),
                               queryDocumentSnapshot.getString("B"),
                               queryDocumentSnapshot.getString("C"),
                               queryDocumentSnapshot.getString("D"),
                               queryDocumentSnapshot.getString("Answer")));
                   }
                   setQuestion();

               } else {

                   Toast.makeText(Question2Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
               progressDialog.dismiss();
           }
       });


    }

    private void setQuestion() {
        timer.setText(String.valueOf(10));
        questions.setText(questions_modelList.get(0).getQuestions());
        option1.setText(questions_modelList.get(0).getOptionA());
        option2.setText(questions_modelList.get(0).getOptionB());
        option3.setText(questions_modelList.get(0).getOptionC());
        option4.setText(questions_modelList.get(0).getOptionD());
        questionCount.setText(String.valueOf(1) + "/" + String.valueOf(questions_modelList.size()));
        startTimer();
        questionNumber = 0;

    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {

              //if (millisUntilFinished < 58000)
                timer.setText(String.valueOf(millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {

                changeQuestion();
            }
        };
        countDownTimer.start();
    }

    private void changeQuestion() {
        if (questionNumber < questions_modelList.size()-1){
            questionNumber++;

            playAnimation(questions,0,0);
            playAnimation(option1,0,1);
            playAnimation(option2,0,2);
            playAnimation(option3,0,3);
            playAnimation(option4,0,4);
            questionCount.setText(String.valueOf(questionNumber+1)+"/"+ String.valueOf(questions_modelList.size()));
            timer.setText(String.valueOf(100));
            startTimer();
        }else {
            // go to score activity
            Intent intent = new Intent(Question2Activity.this,ScoreActivity.class);
            intent.putExtra("SCORE",String.valueOf(score) + "/" + String.valueOf(questions_modelList.size()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //Question2Activity.this.finish();
        }

    }

    private void playAnimation(final View view, final int value, final int viewNumber) {
        view.animate().alpha(value).scaleX(value).scaleY(value).setDuration(1000)
                .setStartDelay(100).setInterpolator(new DecelerateInterpolator())
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (value == 0){
                            switch (viewNumber){
                                case 0:
                                    ((TextView)view).setText(questions_modelList.get(questionNumber).getQuestions());
                                    break;
                                case 1:
                                    ((Button)view).setText(questions_modelList.get(questionNumber).getOptionA());
                                    break;
                                case 2:
                                    ((Button)view).setText(questions_modelList.get(questionNumber).getOptionB());
                                    break;
                                case 3:
                                    ((Button)view).setText(questions_modelList.get(questionNumber).getOptionC());
                                    break;
                                case 4:
                                    ((Button)view).setText(questions_modelList.get(questionNumber).getOptionD());
                                    break;
                            }
                            if (viewNumber != 0){
                                ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00A5FF")));
                            }
                            playAnimation(view,1,questionNumber);

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
    @Override
    public void onClick(View v) {

        String selectedOption = "";
        switch (v.getId()){
            case R.id.button_Option1:
                selectedOption = "A";
                break;
            case R.id.button_Option2:
                selectedOption = "B";
                break;
            case R.id.button_Option3:
                selectedOption = "C";
                break;
            case R.id.button_Option4:
                selectedOption = "D";
                break;
            default:

        }
        countDownTimer.cancel();
        checkAnswer(selectedOption,v);
    }



     @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void checkAnswer(String selectedOption, View view) {
        if (selectedOption == questions_modelList.get(questionNumber).getCorrectAns()){

            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
            score++;
        }else {

            ((Button)view).setBackgroundTintList(ColorStateList.valueOf(Color.RED));

         /*  switch (questions_modelList.get(questionNumber).getCorrectAns()){
                case 0:
                    option1.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 2:
                    option2.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 3:
                    option3.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
                case 4:
                    option4.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    break;
            }*/
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                changeQuestion();
            }
        },2000);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        countDownTimer.cancel();
    }
}
