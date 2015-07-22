package com.gcrick.ovj.guesscricket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;


public class SinglePlayerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_player);


        Button bowlerButton = (Button) findViewById(R.id.bowlerButton);
        Button batsmanButton = (Button) findViewById(R.id.batsmanButton);
        final NumberPicker numPickerOvers = (NumberPicker) findViewById(R.id.numberPickerNoOfOvers);
        numPickerOvers.setMaxValue(20);
        numPickerOvers.setMinValue(1);
        numPickerOvers.setValue(1);
        final NumberPicker numPickerPlayers = (NumberPicker) findViewById(R.id.numberPickerNoOfPlayers);
        numPickerPlayers.setMaxValue(11);
        numPickerPlayers.setMinValue(1);
        numPickerPlayers.setValue(1);

        bowlerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameConstants.noOfBatsman=numPickerPlayers.getValue();
                GameConstants.noOfOvers=numPickerOvers.getValue();
                GameConstants.turn=1;
                Intent bowlerIntent = new Intent(SinglePlayerActivity.this, BowlerGame.class);
                SinglePlayerActivity.this.startActivity(bowlerIntent);
            }
        });
        batsmanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameConstants.noOfBatsman=numPickerPlayers.getValue();
                GameConstants.noOfOvers=numPickerOvers.getValue();
                GameConstants.turn=1;
                Intent batsmanIntent = new Intent(SinglePlayerActivity.this, BatsmanGame.class);
                SinglePlayerActivity.this.startActivity(batsmanIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_single_player, menu);
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
