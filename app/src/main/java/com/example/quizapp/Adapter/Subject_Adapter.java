package com.example.quizapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quizapp.Model.Class_Model;
import com.example.quizapp.Model.Subject_Model;
import com.example.quizapp.Question2Activity;
import com.example.quizapp.Questions_Activity;
import com.example.quizapp.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Subject_Adapter extends RecyclerView.Adapter<Subject_Adapter.viewHolder> {

    private List<Subject_Model> Subject_modelList;

    public Subject_Adapter(List<Subject_Model> subject_modelList) {
        Subject_modelList = subject_modelList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_item,parent,false);
        return  new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {

        holder.setData(Subject_modelList.get(position).getSubjectTitle(),position);
    }

    @Override
    public int getItemCount() {
        return Subject_modelList.size();
    }

    class viewHolder extends RecyclerView.ViewHolder{

        private TextView title;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title =itemView.findViewById(R.id.subject_title);
        }
        private void setData(String title , final int position) {
            this.title.setText(title);
          itemView.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  Intent intent = new Intent(itemView.getContext(), Question2Activity.class);
                  intent.putExtra("Subject_ID",position+1);
                  itemView.getContext().startActivity(intent);
              }
          });

        }

    }
}
