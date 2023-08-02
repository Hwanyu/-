package com.example.ex4_calculator;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText Edit1, Edit2;
    Button BtnAdd, BtnSub, BtnMul, BtnDiv, BtnRem;
    TextView TextAdd, TextSub, TextMul, TextDiv, TextRem;
    String num1, num2;
    Integer result;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Calculator");

        Edit1 = (EditText) findViewById(R.id.Edit1);
        Edit2 = (EditText) findViewById(R.id.Edit2);

        BtnAdd = (Button) findViewById(R.id.BtnAdd);
        BtnSub = (Button) findViewById(R.id.BtnSub);
        BtnMul = (Button) findViewById(R.id.BtnMul);
        BtnDiv = (Button) findViewById(R.id.BtnDiv);
        BtnRem = (Button) findViewById(R.id.BtnRem);

        TextAdd = (TextView) findViewById(R.id.TextAdd);
        TextSub = (TextView) findViewById(R.id.TextSub);
        TextMul = (TextView) findViewById(R.id.TextMul);
        TextDiv = (TextView) findViewById(R.id.TextDiv);
        TextRem = (TextView) findViewById(R.id.TextRem);

        BtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num1 = Edit1.getText().toString();
                num2 = Edit2.getText().toString();
                //num1이나 num2가 비어 있다면
                if (num1.trim().equals("") || num2.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "입력 값이 비었습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    result = Integer.parseInt(num1) + Integer.parseInt(num2);
                    //result = Double.parseDouble(num1) + Double.parseDouble(num2);
                    TextAdd.setText(result.toString());
                }

            }
        });
        BtnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num1 = Edit1.getText().toString();
                num2 = Edit2.getText().toString();
                //num1이나 num2가 비어 있다면
                if (num1.trim().equals("") || num2.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "입력 값이 비었습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    result = Integer.parseInt(num1) - Integer.parseInt(num2);
                    //result = Double.parseDouble(num1) + Double.parseDouble(num2);
                    TextSub.setText(result.toString());
                }

            }
        });
        BtnMul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num1 = Edit1.getText().toString();
                num2 = Edit2.getText().toString();
                //num1이나 num2가 비어 있다면
                if (num1.trim().equals("") || num2.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "입력 값이 비었습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    result = Integer.parseInt(num1) * Integer.parseInt(num2);
                    //result = Double.parseDouble(num1) + Double.parseDouble(num2);
                    TextMul.setText(result.toString());
                }

            }
        });
        BtnDiv.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                num1 = Edit1.getText().toString();
                num2 = Edit2.getText().toString();
                // num1이나 num2가 비어 있다면
                if (num1.trim().equals("") || num2.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "입력 값이 비었습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    // num2가 0이면 나누지 않는다.
                    if (num2.trim().equals("0")) {
                        Toast.makeText(getApplicationContext(),
                                "0으로 나누면 안됩니다!", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        result = Integer.parseInt(num1) / Integer.parseInt(num2);
                        //result = Double.parseDouble(num1) / Double.parseDouble(num2);
                        //result = (int) (result * 10) / 10.0; //소수점 아래 1자리까지만 출력
                        TextDiv.setText(result.toString(result));
                    }
                }
            }
        });
        BtnRem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                num1 = Edit1.getText().toString();
                num2 = Edit2.getText().toString();
                // num1이나 num2가 비어 있다면
                if (num1.trim().equals("") || num2.trim().equals("")) {
                    Toast.makeText(getApplicationContext(), "입력 값이 비었습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    // num2가 0이면 나누지 않는다.
                    if (num2.trim().equals("0")) {
                        Toast.makeText(getApplicationContext(),
                                "0으로 나머지 값 안됩니다!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        result = Integer.parseInt(num1) % Integer.parseInt(num2);
                        //result = Double.parseDouble(num1) % Double.parseDouble(num2);
                        TextRem.setText(result.toString());
                    }
                }
            }
        });
    }
}