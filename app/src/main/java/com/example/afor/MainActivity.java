package com.example.afor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button ask;
    Button answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ask=(Button)findViewById(R.id.asker);
        answer=(Button)findViewById(R.id.answer);


        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int1 = new Intent(view.getContext(),asker.class);
                startActivity(int1);
            }
        });

        answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent int2 = new Intent(view.getContext(),answer.class);
                startActivity(int2);
            }
        });


    }
}