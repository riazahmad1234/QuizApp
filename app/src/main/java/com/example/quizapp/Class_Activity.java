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
import com.example.quizapp.Model.Class_Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Class_Activity extends AppCompatActivity{

    private RecyclerView recyclerView;
    FirebaseFirestore firebaseFirestore;
    List<Class_Model> classList = new ArrayList<>();
    private String className;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_);
        Toolbar toolbar = findViewById(R.id.Toolbar_ClassActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Classes");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_ClassActivity);
       firebaseFirestore = FirebaseFirestore.getInstance();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Class_Activity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        progressDialog = new ProgressDialog(Class_Activity.this);
        progressDialog.setTitle("Loading Classes");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        loadClassesData();

    }

   private void loadClassesData() {
        classList.clear();
        firebaseFirestore.collection("Quiz").document("Classes")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot.exists()){
                        long count = (long)documentSnapshot.get("COUNT");

                        for (int i = 1; i <= count; i++){
                             className = documentSnapshot.getString("Class" + String.valueOf(i));

                             classList.add(new Class_Model(className));

                        }
                        Class_Adapter adapter = new Class_Adapter(classList);
                        recyclerView.setAdapter(adapter);
                        progressDialog.dismiss();
                    }else {
                        Toast.makeText(Class_Activity.this, "Nothing in Database", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }else {

                    Toast.makeText(Class_Activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
