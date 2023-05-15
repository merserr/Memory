package com.example.memory;

import static com.example.memory.MainActivity.MESSAGEOUTPUTCONTROL;
import static com.example.memory.MainActivity.MESSAGEOUTPUTFR1;
import static com.example.memory.MainActivity.MESSAGEOUTPUTFR2;
import static com.example.memory.MainActivity.MESSAGEOUTPUTFR3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

//import com.jcraft.jsch.ChannelExec;
//import com.jcraft.jsch.JSch;
//import com.jcraft.jsch.JSchException;
//import com.jcraft.jsch.Session;

import java.io.IOException;
import java.io.InputStream;

public class GetARPTable extends Service {

    String LOG_TAG ="==GetARPTable==" ;
    String fragment;
    String hostIP;
    String port;
    String username;
    String password;
    String command;
    int intport;
 //   Session session;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent!=null) {
            fragment = intent.getStringExtra("fragment");
            hostIP = intent.getStringExtra("hostIP");
            port = intent.getStringExtra("port");
            username = intent.getStringExtra("username");
            password = intent.getStringExtra("password");
            command = intent.getStringExtra("command");
        }

        new get_data_from_cisco(hostIP, port, username, password, command).execute();

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private class get_data_from_cisco {
        public get_data_from_cisco(String hostIP, String port, String username, String password, String command) {

            try {
                intport = Integer.parseInt(port);
            } catch (NumberFormatException nfe) {}

            //start execution of ssh commands
            Thread thread = new Thread(() -> {

            });
         //   thread.start();

        }


        public void execute() {
        }
    }
}
