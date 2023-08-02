package com.example.myapplicationafnjlaksfnalsf;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SecondActivity extends AppCompatActivity {

    private TextView aa;
    EditText bb;
    Button cc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        aa = (TextView) findViewById(R.id.aa);
        bb = (EditText) findViewById(R.id.bb);
        cc = (Button) findViewById(R.id.cc);

        Intent intent = getIntent();
        String reseive2 = intent.getStringExtra("send1");

        aa.setText(String.valueOf(reseive2));

        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String send2 = bb.getText().toString();
                Intent outintent = new Intent(getApplicationContext(), MainActivity.class);
                outintent.putExtra("send2", send2);
                startActivityForResult(outintent,0);
            }
        });


    }
}