package com.example.applicazioneinteressante;

import android.util.Log;

import java.io.*;
import java.net.*;

public class NetworkManager {
    private Socket socket;
    private DataOutputStream out;

    public void openConnection(String address, int port){
        try {
            while(!socket.isConnected()) {
                socket = new Socket(address, port);
                out = new DataOutputStream(socket.getOutputStream());
            }
            Log.i("Network Manager", "Connected to " + address + ":" + port);
        } catch (IOException he) { Log.e("Network Manager", he.toString()); }
    }

    public void sendMessage(String s){
        try {
            out.writeUTF(s);
            if(out.size() != s.length()) { throw new IOException(); }
        } catch (IOException io) { Log.e("Network Manager.sendMessage", io.toString()); }
    }
    public void closeConnection(){
        try {
            out.close();
            socket.close();
        } catch (IOException io) { Log.e("Network Manager", "Error closing connection: " + io); }
    }
}