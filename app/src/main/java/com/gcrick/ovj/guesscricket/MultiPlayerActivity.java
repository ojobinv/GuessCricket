package com.gcrick.ovj.guesscricket;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Set;
import java.util.UUID;


public class MultiPlayerActivity extends Activity {

    BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket socket;
    private InputStream is;
    private OutputStreamWriter os;
    private boolean CONTINUE_READ_WRITE = true;

    private ListView listview;
    private ArrayAdapter adapter;
    private ArrayAdapter<String> mPairedDevicesArrayAdapter;
    private ArrayAdapter<String> mNewDevicesArrayAdapter;

    private static final int ENABLE_BT_REQUEST_CODE = 1;
    private static final int DISCOVERABLE_BT_REQUEST_CODE = 2;
    private static final int REQUEST_DISCOVERING_DEVICE = 3;
    private static final int DISCOVERABLE_DURATION = 300;

    UUID uuid = UUID.fromString("8c859610-27c5-4fca-93b4-88840f4fc520");

    Button hostGameButton;
    Button joinGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_player);
        try {


            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) {
                Toast.makeText(getApplicationContext(), "Your Device doesn't support Bluetooth", Toast.LENGTH_SHORT).show();
                Utilities.outPut("Device doesn't support Bluetooth");
                Intent returnIntent = new Intent(MultiPlayerActivity.this, FirstActivity.class);
                MultiPlayerActivity.this.startActivity(returnIntent);
                Utilities.outPut("Returning to initial screen");
            }
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent, ENABLE_BT_REQUEST_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utilities.outPut("Problem in Initializing Bluetooth");
        }

        hostGameButton = (Button) findViewById(R.id.buttonHost);
        joinGameButton = (Button) findViewById(R.id.buttonJoin);

        hostGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.outPut("Hosting a Game, Starting as a Server");
                server();
            }
        });
        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utilities.outPut("Joining a Game, Starting as a Client");
                client();

            }
        });

    }

    public void server() {
        try {
            if (mBluetoothAdapter.getScanMode() !=
                    BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
                Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION);
                startActivity(discoverableIntent);
                startActivityForResult(discoverableIntent, DISCOVERABLE_BT_REQUEST_CODE);
            }
            //UUID uuid = UUID.fromString("8c859610-27c5-4fca-93b4-88840f4fc520");
            //new Thread(server).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void client() {

        //setUpDeviceList();
        Intent discoveringIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(discoveringIntent, REQUEST_DISCOVERING_DEVICE);

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if(D) Log.d(TAG, "onActivityResult " + resultCode);
        switch (requestCode) {
            case REQUEST_DISCOVERING_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    //connectDevice(data, true);
                    Bundle resultBundle = data.getExtras();
                    String address = resultBundle.getString("EXTRA_DEVICE_ADDRESS");
                    Utilities.outPut("Received address:"+ address);

                    BluetoothDevice bluetoothDevice = mBluetoothAdapter.getRemoteDevice(address);

                    // Initiate a connection request in a separate thread
                    ConnectingThread t = new ConnectingThread(bluetoothDevice);
                    t.start();
                }
                break;
            case DISCOVERABLE_BT_REQUEST_CODE:
                //When Discoverable options turns ON successfully
                if (resultCode == DISCOVERABLE_DURATION){
                    Toast.makeText(getApplicationContext(), "Device is now discoverable for " +
                                    DISCOVERABLE_DURATION + " seconds",
                            Toast.LENGTH_SHORT).show();
                    // Start a thread to create a  server socket to listen
                    // for connection request
                    ListeningThread t = new ListeningThread();
                    t.start();
                } else {
                    Toast.makeText(getApplicationContext(), "Fail to enable discoverability.",
                            Toast.LENGTH_SHORT).show();
                }
            case ENABLE_BT_REQUEST_CODE:
                // Bluetooth successfully enabled!
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getApplicationContext(), "Bluetooth ON",
                            Toast.LENGTH_SHORT).show();
                } else { // RESULT_CANCELED as user refused or failed to enable Bluetooth
                    Toast.makeText(getApplicationContext(), "Bluetooth is not enabled.",
                            Toast.LENGTH_SHORT).show();
                }
        }
    }

    private class ListeningThread extends Thread {
        private final BluetoothServerSocket bluetoothServerSocket;

        public ListeningThread() {
            BluetoothServerSocket temp = null;
            try {
                temp = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(getString(R.string.app_name), uuid);

            } catch (IOException e) {
                e.printStackTrace();
            }
            bluetoothServerSocket = temp;
        }

        public void run() {
            BluetoothSocket bluetoothSocket;
            // This will block while listening until a BluetoothSocket is returned
            // or an exception occurs
            while (true) {
                try {
                    bluetoothSocket = bluetoothServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                // If a connection is accepted
                if (bluetoothSocket != null) {


                    runOnUiThread(new Runnable() {
                        public void run() {
                            Utilities.outPut("A connection has been accepted.");
                            Toast.makeText(getApplicationContext(), "A connection has been accepted.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Code to manage the connection in a separate thread

                       manageServerConnection(bluetoothSocket);


                    try {
                        bluetoothServerSocket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }

        public void manageServerConnection(BluetoothSocket bluetoothSocket){
            setContentView(R.layout.fragment_sample);
            FragmentManager fragmentManager = getFragmentManager();
            SampleFragmentActivity samplefragment = new SampleFragmentActivity();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragmentHolder, samplefragment);
            fragmentTransaction.commit();
        }

        // Cancel the listening socket and terminate the thread
        public void cancel() {
            try {
                bluetoothServerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ConnectingThread extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final BluetoothDevice bluetoothDevice;

        public ConnectingThread(BluetoothDevice device) {

            BluetoothSocket temp = null;
            bluetoothDevice = device;

            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                temp = bluetoothDevice.createRfcommSocketToServiceRecord(uuid);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bluetoothSocket = temp;
        }

        public void run() {
            // Cancel discovery as it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // This will block until it succeeds in connecting to the device
                // through the bluetoothSocket or throws an exception
                bluetoothSocket.connect();
            } catch (IOException connectException) {
                connectException.printStackTrace();
                try {
                    bluetoothSocket.close();
                } catch (IOException closeException) {
                    closeException.printStackTrace();
                }
            }

            runOnUiThread(new Runnable() {
                public void run() {
                    Utilities.outPut("Device Connected to a server");
                    Toast.makeText(getApplicationContext(), "Device Connected to a server.",
                            Toast.LENGTH_SHORT).show();
                }
            });


            // Code to manage the connection in a separate thread
            /*
               manageClientConnection(bluetoothSocket);
            */
        }

        // Cancel an open connection and terminate the thread
        public void cancel() {
            try {
                bluetoothSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_multi_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    protected void onDestroy() {
        super.onDestroy();

        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.disable();
        }
    }
}