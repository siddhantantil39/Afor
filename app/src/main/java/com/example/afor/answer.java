package com.example.afor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class answer extends AppCompatActivity {

    RecyclerView recyclerView;

    //firabase instances
    private DatabaseReference mydbRef;

    //varibles
    private ArrayList<answer_recieve> messagesList;
    private RecyclerAdapter recyclerAdapter;
    private Context mContext;
    private RecyclerAdapter.OnItemClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        //context:this
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //firebase instantiation
        mydbRef = FirebaseDatabase.getInstance().getReference("Questions");
        //using the onclick interface to answer questions
        setOnClickListener();

        //data from reference as Arraylist
        messagesList = new ArrayList<>();

        ClearALL();

        //getting the images
        GetDataFromFirebase();






    }

    private void setOnClickListener() {
        listener = new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent intent = new Intent(getApplicationContext(),write_answer.class);
                intent.putExtra("name",messagesList.get(position).getName());
                startActivity(intent);

            }
        };
    }

    private  void GetDataFromFirebase(){

        //Query query = mydbRef.child("message");
        mydbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                ClearALL();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    answer_recieve messages = new answer_recieve();
                    messages.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                    messages.setName(snapshot.child("name").getValue().toString());
                    //messages.setAnsUrl(snapshot.child("ansUrl").getValue().toString());

                    messagesList.add(messages);
                }
                //very important
                recyclerAdapter = new RecyclerAdapter(getApplicationContext(), messagesList, listener);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });



    }

    private void ClearALL(){
        if (messagesList!=null)
        {
            messagesList.clear();

            if(recyclerAdapter!=null)
                recyclerAdapter.notifyDataSetChanged();

        }

        messagesList = new ArrayList<>();
    }
}