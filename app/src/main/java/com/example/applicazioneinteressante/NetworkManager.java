package com.example.applicazioneinteressante;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.*;
import java.net.*;

public class NetworkManager {
    private Socket socket;
    private DataOutputStream out;
    private BufferedOutputStream bufferedOutputStream;

    public void openConnection(String address, int port, Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //TODO: GESTIRE IL CASO IN CUI NON SI DIANO I PERMESSI PER CONNETTERSI AD INTERNET
            System.out.println("Mnacano i permessi per Internet");
        } else {
            System.out.println("I permessi ci sono");
            try {
                socket = new Socket(address, port);

                while (!socket.isConnected()) {
                    out = new DataOutputStream(socket.getOutputStream());
                    bufferedOutputStream = new BufferedOutputStream(out);
                }
                Log.i("Network Manager", "Connected to " + address + ":" + port);
            } catch (IOException he) {
                Log.e("Network Manager", he.toString());
            }
        }
    }

    public void checkMessage(String s){
        try {
            out.writeUTF(s);
            if(out.size() != s.length()) { throw new IOException(); }
        } catch (IOException io) { Log.e("Network Manager.sendMessage", io.toString()); }
    }

    public void sendMessage2(String message) {
        try {
            // Converte la stringa del messaggio in un array di byte
            byte[] messageBytes = message.getBytes();

            // Invia i dati al server
            out.write(messageBytes);

            // Chiude la connessione
            closeConnection();

            System.out.println("Messaggio inviato con successo.");
        } catch (IOException e) {
            System.err.println("Si è verificato un errore durante l'invio del messaggio:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Si è verificato un errore imprevisto:");
            e.printStackTrace();
        }
    }
    public void closeConnection(){
        try {
            bufferedOutputStream.close();
            out.close();
            socket.close();
        } catch (IOException io) { Log.e("Network Manager", "Error closing connection: " + io); }
    }
}