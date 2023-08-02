package com.example.myapplicationafnjlaksfnalsf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView a;
    EditText b;
    Button c;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        a = (TextView) findViewById(R.id.a);
        b = (EditText) findViewById(R.id.b);
        c = (Button) findViewById(R.id.c);

        Intent intent = getIntent();
        String reseive1 = intent.getStringExtra("send2");

        a.setText(String.valueOf(reseive1));

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String send1 = b.getText().toString();
                Intent outintent = new Intent(getApplicationContext(), SecondActivity.class);
                outintent.putExtra("send1", send1);
                startActivityForResult(outintent,0);
            }
        });
    }
}