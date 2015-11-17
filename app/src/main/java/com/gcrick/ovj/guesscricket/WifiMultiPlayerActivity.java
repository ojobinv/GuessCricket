package com.gcrick.ovj.guesscricket;

import android.app.Activity;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.net.ServerSocket;

public class WifiMultiPlayerActivity extends Activity {

    private String SERVICE_NAME = "GuessCricket";
    NsdManager.RegistrationListener mRegistrationListener = new NsdManager.RegistrationListener() {

        @Override
        public void onServiceRegistered(NsdServiceInfo NsdServiceInfo) {

            SERVICE_NAME = NsdServiceInfo.getServiceName();
            Utilities.outPut(SERVICE_NAME + " Registered");
        }

        @Override
        public void onRegistrationFailed(NsdServiceInfo serviceInfo,
                                         int errorCode) {
            // Registration failed! Put debugging code here to determine
            // why.
            Utilities.outPut("NSD Registration failed");
        }

        @Override
        public void onServiceUnregistered(NsdServiceInfo serviceInfo) {
            // Service has been unregistered. This only happens when you
            // call
            // NsdManager.unregisterService() and pass in this listener.
            Utilities.outPut(SERVICE_NAME + " Unregistered");
        }

        @Override
        public void onUnregistrationFailed(NsdServiceInfo serviceInfo,
                                           int errorCode) {
            // Unregistration failed. Put debugging code here to determine
            // why.
            Utilities.outPut("NSD UnRegistration failed");
        }
    };
    private String SERVICE_TYPE = "_http._tcp.";
    private NsdManager mNsdManager;
    private Button joinGameButton;
    private Button hostGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);

        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if(!wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(true);
        }


            hostGameButton = (Button) findViewById(R.id.buttonHost);
            joinGameButton = (Button) findViewById(R.id.buttonJoin);

            hostGameButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        ServerSocket mServerSocket = new ServerSocket(0);
                        // Store the chosen port.
                        int mLocalPort = mServerSocket.getLocalPort();
                        mNsdManager = (NsdManager) getSystemService(Context.NSD_SERVICE);
                        registerService(mLocalPort);
                    }catch (Exception e) {
                        Utilities.outPut("Problem in registering Wifi");
                        e.printStackTrace();
                    }
                }
            });


    }

    public void registerService(int port) {
        // Create the NsdServiceInfo object, and populate it.
        NsdServiceInfo serviceInfo = new NsdServiceInfo();

        // The name is subject to change based on conflicts
        // with other services advertised on the same network.
        serviceInfo.setServiceName(SERVICE_NAME);
        serviceInfo.setServiceType(SERVICE_TYPE);
        serviceInfo.setPort(port);
        mNsdManager.registerService(serviceInfo,
                NsdManager.PROTOCOL_DNS_SD,
                mRegistrationListener);

    }

}
