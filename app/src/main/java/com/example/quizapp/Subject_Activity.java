package com.example.quizapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.quizapp.Adapter.Class_Adapter;
import com.example.quizapp.Adapter.Subject_Adapter;
import com.example.quizapp.Model.Class_Model;
import com.example.quizapp.Model.Subject_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Subject_Activity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    FirebaseFirestore firebaseFirestore;
    List<Subject_Model> subjectList = new ArrayList<>();
    private String subjectName;
    public static int Class_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_);
         toolbar = findViewById(R.id.Toolbar_SubjectActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        getSupportActionBar().setTitle(intent.getStringExtra("title"));

        Class_ID = intent.getIntExtra("Class_ID", 1);
        recyclerView = findViewById(R.id.recyclerview_SubjectActivity);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Subject_Activity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        firebaseFirestore = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(Subject_Activity.this);
        progressDialog.setTitle("Loading Subject");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
       loadSubjectData();



    }

   private void loadSubjectData() {

       subjectList.clear();


       firebaseFirestore.collection("Quiz").document("Class" + String.valueOf(Class_ID))
               .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
           @Override
           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
               if (task.isSuccessful()) {
                   DocumentSnapshot documentSnapshot = task.getResult();
                   if (documentSnapshot.exists()) {
                       long subject_COUNT = (long) documentSnapshot.get("SUBJECTS");

                       for (int i = 1; i <= subject_COUNT; i++) {
                           subjectName = documentSnapshot.getString("Subject" + String.valueOf(i) + "_Name");

                           subjectList.add(new Subject_Model(subjectName));

                           Subject_Adapter adapter = new Subject_Adapter(subjectList);
                           recyclerView.setAdapter(adapter);
                           progressDialog.dismiss();
                       }
                   } else {
                       Toast.makeText(Subject_Activity.this, "Nothing in Database", Toast.LENGTH_SHORT).show();
                       finish();
                   }

               } else {

                   Toast.makeText(Subject_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
               }
           }
       });
   }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}

