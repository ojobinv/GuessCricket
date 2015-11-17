package com.gcrick.ovj.guesscricket;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class FirstActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        Button singlePlayerButton = (Button) findViewById(R.id.buttonSinglePlayer);
        Button multiPlayerButton = (Button) findViewById(R.id.buttonMultiPlayer);

        singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent singlePlayerIntent = new Intent(FirstActivity.this, SinglePlayerActivity.class);
                FirstActivity.this.startActivity(singlePlayerIntent);
                Utilities.outPut("Starting Single Player Activity..");

            }
        });

        multiPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMultiPlayer();
            }
        });

    }

    private void startMultiPlayer() {
        setContentView(R.layout.multiplayer_chooser);
        Button bluetoothChooserButton = (Button) findViewById(R.id.button_bluetooth);
        Button wlanChooserButton = (Button) findViewById(R.id.button_wlan);
        bluetoothChooserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent multiPlayerIntent = new Intent(FirstActivity.this, BluetoothMultiPlayerActivity.class);
                FirstActivity.this.startActivity(multiPlayerIntent);
                Utilities.outPut("Starting BlueTooth multi Player Activity..");
            }
        });

        wlanChooserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent multiPlayerIntent = new Intent(FirstActivity.this, WifiMultiPlayerActivity.class);
                FirstActivity.this.startActivity(multiPlayerIntent);
                Utilities.outPut("Starting Wifi multi Player Activity..");
            }
        });
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        //System.exit(0);
                    }
                }).setNegativeButton("No", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_first, menu);
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
}


