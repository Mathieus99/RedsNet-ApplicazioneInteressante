package com.example.applicazioneinteressante;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private String operation;
    private double op1=0, op2=0, prevResult=0;
    private TextView current,history;
    private class MyNumberButton{
        Button b;
        @SuppressLint("SetTextI18n")
        public MyNumberButton(Button v){
            b=v;
            b.setOnClickListener(v1 -> {
                if(current.getText().equals("0") && b.getText().equals("0"));
                else if (current.getText().equals("0") && !b.getText().equals("0")) current.setText(b.getText());
                else if(!current.getText().equals("0") && prevResult != 0) { current.setText(b.getText()); }
                else current.setText(current.getText() + b.getText().toString());
            });
        }
    }

    private class MyBaseOpButton {
        Button b;

        @SuppressLint("SetTextI18n")
        public MyBaseOpButton (Button v, String op){
            b=v;
            b.setOnClickListener(v1 -> {
                if(!operation.equals("")){
                    if(prevResult==0){
                        operation = op;
                        history.setText(op1 + operation);
                        current.setText("0");
                    }
                    else {
                        operation = op;
                        op1 = prevResult;
                        history.setText(op1 + operation);
                        current.setText("0");
                    }
                }
                else {
                    op1 = Double.parseDouble(current.getText().toString());
                    operation = op;
                    history.setText(op1 + operation);
                    current.setText("0");
                }
            });
        }
    }

    private class MyAdvOpButton{

        Button b;
        @SuppressLint("SetTextI18n")
        public MyAdvOpButton(Button v, String op){
            b=v;
            b.setOnClickListener(v1 -> {
                if(!operation.equals("")) current.setError("Input error");
                else {
                    double opy = Double.parseDouble(current.getText().toString());
                    switch (op){
                        case "log":
                                if (opy <= 0) {
                                    current.setError("Input Error");
                                    current.setText("0");
                                }
                                else {
                                    op1 = opy;
                                    prevResult = Math.log(op1);
                                    history.setText("ln(" + op1 + ")");
                                    current.setText(Double.toString(prevResult));
                                }
                            break;
                        case "sqrt":
                                if (op1 < 0) {
                                    current.setError("Input Error");
                                    current.setText("0");
                                }
                                else {
                                    op1 = opy;
                                    prevResult = Math.sqrt(op1);
                                    history.setText("sqrt("+op1+")");
                                    current.setText(Double.toString(prevResult));
                                }
                            break;
                        case "pow":
                            op1 = opy;
                            operation = op;
                            history.setText(op1+"^");
                            break;
                    }
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
    @SuppressLint("SetTextI18n")
    void initializeComponents(){
        //Add listener on number buttons with inner class MyNumberButton
        MyNumberButton zero = new MyNumberButton((Button) findViewById(R.id.number0));
        MyNumberButton one = new MyNumberButton((Button) findViewById(R.id.number1));
        MyNumberButton two = new MyNumberButton((Button) findViewById(R.id.number2));
        MyNumberButton three = new MyNumberButton((Button) findViewById(R.id.number3));
        MyNumberButton four = new MyNumberButton((Button) findViewById(R.id.number4));
        MyNumberButton five = new MyNumberButton((Button) findViewById(R.id.number5));
        MyNumberButton six = new MyNumberButton((Button) findViewById(R.id.number6));
        MyNumberButton seven = new MyNumberButton((Button) findViewById(R.id.number7));
        MyNumberButton eight = new MyNumberButton((Button) findViewById(R.id.number8));
        MyNumberButton nine = new MyNumberButton((Button) findViewById(R.id.number9));

        //Add listener on simple operation's button with inner class MyBaseOpButton
        MyBaseOpButton plus = new MyBaseOpButton((Button) findViewById(R.id.opPlus),"+");
        MyBaseOpButton minus = new MyBaseOpButton((Button) findViewById(R.id.opMinus),"-");
        MyBaseOpButton muls = new MyBaseOpButton((Button) findViewById(R.id.opMuls),"*");
        MyBaseOpButton divide = new MyBaseOpButton((Button) findViewById(R.id.opDivide),"/");

        //Add listener on advanced operation's button with inner class MyAdvOpButton
        MyAdvOpButton log = new MyAdvOpButton((Button) findViewById(R.id.opLog),"log");
        MyAdvOpButton sqrt = new MyAdvOpButton((Button) findViewById(R.id.opSqrt),"sqrt");
        MyAdvOpButton pow = new MyAdvOpButton((Button) findViewById(R.id.opPower),"pow");

        this.<Button>findViewById(R.id.pointBtn).setOnClickListener(v -> {
                int integerPart = (int) Double.parseDouble(current.getText().toString());
                if ((Double.parseDouble(current.getText().toString())-integerPart) != 0);
                else current.setText(current.getText() + ".");
        });

        Button equal=findViewById(R.id.opEqual);    //TODO
        Button ans=findViewById(R.id.ansBtn);       //TODO

        this.<Button>findViewById(R.id.cancel).setOnClickListener(v -> current.setText("0"));
        this.<Button>findViewById(R.id.cancelAll).setOnClickListener(v -> {
            history.setText("");
            current.setText("0");
            prevResult=0;
        });
        current=findViewById(R.id.insertion);
        history=findViewById(R.id.ans);
    }
}