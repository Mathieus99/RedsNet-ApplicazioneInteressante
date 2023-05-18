package com.example.applicazioneinteressante;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String operation;
    private double op1=0, op2=0, prevResult=0;
    private TextView current,history;

    private class myOpButton {
        Button b;

        public myOpButton (Button v,String op){
            b=v;
            b.setOnClickListener(v1 -> {
                if(!operation.equals("")){
                    operation=op;

                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //TODO: registra ed invia i dati al server
        initializeComponents();
    }

    //Initialize components in the class
    void initializeComponents(){
        Button zero=findViewById(R.id.number0);
        zero.setOnClickListener(v -> {
            if(current.getText().equals("0"));
            else if(!current.getText().equals("0") && prevResult!=0){ current.setText(zero.getText()); }
            else current.setText(current.getText() + zero.getText().toString());
        });
        Button one=findViewById(R.id.number1);
        one.setOnClickListener(v -> {
            if(current.getText().equals("0")) { current.setText(one.getText()); }
            else if(!current.getText().equals("0") && prevResult!=0) { current.setText(one.getText()); }
            else current.setText(current.getText() + one.getText().toString());
        });
        Button two=findViewById(R.id.number2);
        two.setOnClickListener(v -> {
            if(current.getText().equals("0")) { current.setText(two.getText()); }
            else if(!current.getText().equals("0") && prevResult!=0) { current.setText(two.getText()); }
            else current.setText(current.getText() + two.getText().toString());
        });
        Button three=findViewById(R.id.number3);
        three.setOnClickListener(v -> {
            if(current.getText().equals("0")) { current.setText(three.getText()); }
            else if(!current.getText().equals("0") && prevResult!=0) { current.setText(three.getText()); }
            else current.setText(current.getText() + three.getText().toString());
        });
        Button four=findViewById(R.id.number4);
        four.setOnClickListener(v -> {
            if(current.getText().equals("0")) { current.setText(four.getText()); }
            else if(!current.getText().equals("0") && prevResult!=0) { current.setText(four.getText()); }
            else current.setText(current.getText() + four.getText().toString());
        });
        Button five=findViewById(R.id.number5);
        five.setOnClickListener(v -> {
            if(current.getText().equals("0")) { current.setText(five.getText()); }
            else if(!current.getText().equals("0") && prevResult!=0) { current.setText(five.getText()); }
            else current.setText(current.getText() + five.getText().toString());
        });
        Button six=findViewById(R.id.number6);
        six.setOnClickListener(v -> {
            if(current.getText().equals("0")) { current.setText(six.getText()); }
            else if(!current.getText().equals("0") && prevResult != 0) { current.setText(six.getText()); }
            else current.setText(current.getText() + six.getText().toString());
        });
        Button seven=findViewById(R.id.number7);
        seven.setOnClickListener(v -> {
            if(current.getText().equals("0")) { current.setText(seven.getText()); }
            else if(!current.getText().equals("0") && prevResult != 0) { current.setText(seven.getText()); }
            else current.setText(current.getText() + seven.getText().toString());
        });
        Button eight=findViewById(R.id.number8);
        eight.setOnClickListener(v -> {
            if(current.getText().equals("0")) { current.setText(eight.getText()); }
            else if(!current.getText().equals("0") && prevResult != 0) { current.setText(eight.getText()); }
            else current.setText(current.getText() + eight.getText().toString());
        });
        Button nine=findViewById(R.id.number9);
        nine.setOnClickListener(v -> {
            if(current.getText().equals("0")) { current.setText(nine.getText()); }
            else if(!current.getText().equals("0") && prevResult != 0) { current.setText(nine.getText()); }
            else current.setText(current.getText() + nine.getText().toString());
        });
        Button plus=findViewById(R.id.opPlus);
        plus.setOnClickListener(v -> {
            if(!operation.equals("")){
                if (prevResult == 0) {
                    operation = "+";
                    history.setText(Double.toString(op1) + operation);
                }
                else {
                    operation="+";
                    op1=prevResult;
                    history.setText(Double.toString(op1)+operation);
                    current.setText("0");
                }
            }
            else {
                op1 = Double.parseDouble(current.getText().toString());
                operation = "+";
                history.setText(current + operation);
            }
        });
        Button minus=findViewById(R.id.opMinus);
        minus.setOnClickListener(v -> {
            if(!operation.equals("")){
                if(prevResult == 0){
                    operation="-";
                    history.setText(Double.toString(op1)+operation);
                }
                else {
                    operation="-";
                    op1=prevResult;
                    history.setText(Double.toString(op1)+operation);
                    current.setText("0");
                }
            }
            else {
                op1 = Double.parseDouble(current.getText().toString());
                operation = "-";
                history.setText(current + operation);
            }
        });
        Button muls=findViewById(R.id.opMuls);
        muls.setOnClickListener(v -> {
            if(!operation.equals("")){
                if(prevResult == 0){
                    operation="*";
                    history.setText(Double.toString(op1)+operation);
                }
                else{
                    operation="*";
                    op1=prevResult;
                    history.setText(Double.toString(op1)+operation);
                    current.setText("0");
                }
            }
            else {
                op1 = Double.parseDouble(current.getText().toString());
                operation = "*";
                history.setText(current + operation);
            }
        });
        Button divide=findViewById(R.id.opDivide);
        divide.setOnClickListener(v -> {
            if(!operation.equals("")){
                operation="/";
                history.setText(current.getText()+operation);
            }
            else {
                op1 = Double.parseDouble(current.getText().toString());
                operation = "/";
                history.setText(current + operation);
            }
        });
        Button log=findViewById(R.id.opLog);
        log.setOnClickListener(v -> {
            if(!operation.equals("")){ current.setError("Input error"); }
            else {
                op1 = Double.parseDouble(current.getText().toString());
                history.setText("ln("+current.getText()+")");
                prevResult = Math.log(op1);
                current.setText(Double.toString(prevResult));
            }
        });
        Button sqrt=findViewById(R.id.opSqrt);
        sqrt.setOnClickListener(v -> {
            if(!operation.equals("")){ current.setError("Input error"); }
            else {
                op1 = Double.parseDouble(current.getText().toString());
                history.setText("sqrt("+current.getText()+")");
                prevResult = Math.sqrt(op1);
                current.setText(Double.toString(prevResult));
            }
        });
        Button power=findViewById(R.id.opPower);
        Button point=findViewById(R.id.pointBtn);
        Button equal=findViewById(R.id.opEqual);
        Button ans=findViewById(R.id.ansBtn);
        Button cancel=findViewById(R.id.cancel);
        Button cancellAll=findViewById(R.id.cancelAll);
        current=findViewById(R.id.insertion);
        history=findViewById(R.id.ans);
    }
}