package com.example.quizapp.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.quizapp.Class_Activity;
import com.example.quizapp.Model.Class_Model;
import com.example.quizapp.R;
import com.example.quizapp.Subject_Activity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Class_Adapter extends RecyclerView.Adapter<Class_Adapter.ViewHolder> {

    private List<Class_Model> class_modelList;


    public Class_Adapter(){

    }
    public Class_Adapter(List<Class_Model> class_modelList) {
        this.class_modelList = class_modelList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        holder.setData(class_modelList.get(position).getName(),position);
    }

    @Override
    public int getItemCount() {
        return class_modelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.class_title);


        }
       private void setData(final String title, final int position){
           this.title.setText(title);

           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), Subject_Activity.class);
                    intent.putExtra("title",title);
                    intent.putExtra("Class_ID", position +1);
                    itemView.getContext().startActivity(intent);

                }
            });
       }



    }


}
