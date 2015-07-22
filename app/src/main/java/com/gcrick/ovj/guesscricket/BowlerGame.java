package com.gcrick.ovj.guesscricket;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Random;


public class BowlerGame extends Activity {

    int currentWicket = 0;
    int totalRun = 0;
    int ballSelected = 0;
    int currentOver = 0;
    int batsManGuess = 0;
    String overDisplay;
    int currentBall = -1;

    int endFlag =0;
    int switchFlag=0;

    TextView bowlerOversTv;
    TextView bowlerWicketsTv;
    TextView batsManOversTv;
    TextView batsManWicketsTv;
    TextView batsManRunsTv;
    TextView batsManGuessTv;
    TextView resultTv;

    Button bowlButton;

    private RadioGroup radioGroupRuns;
    private RadioButton radioButtonRuns;

    private int noOfOvers = GameConstants.noOfOvers;
    private int noOfPlayers = GameConstants.noOfBatsman;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bowler_game);

        bowlerOversTv = (TextView) findViewById(R.id.textViewBowlerOvers);
        bowlerWicketsTv = (TextView) findViewById(R.id.textViewBowlerWickets);
        batsManOversTv = (TextView) findViewById(R.id.textViewBatsManOvers);
        batsManWicketsTv = (TextView) findViewById(R.id.textViewBatsManWickets);
        batsManRunsTv = (TextView) findViewById(R.id.textViewBatsManRuns);
        radioGroupRuns = (RadioGroup) findViewById(R.id.radioGroup);
        batsManGuessTv = (TextView) findViewById(R.id.textViewBatsManGuess);
        resultTv = (TextView) findViewById(R.id.textViewResult);

        bowlButton = (Button) findViewById(R.id.buttonBowl);

        nextBall();
        refreshDisplay();

        bowlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outPut("bowl Button Clicked");

                if (endFlag == 1)
                    switcher();
                else{

                    int selectedId = radioGroupRuns.getCheckedRadioButtonId();
                    radioButtonRuns = (RadioButton) findViewById(selectedId);
                    ballSelected = Integer.parseInt(radioButtonRuns.getText().toString());
                    outPut(" Ball selected:" + ballSelected);

                    Random randomGenerator = new Random();
                    batsManGuess = randomGenerator.nextInt(7);
                    while (batsManGuess == 5) {
                        outPut("Rejecting 5");
                        batsManGuess = randomGenerator.nextInt(7);
                    }
                    batsManGuessTv.setText(batsManGuess + "");
                    outPut("BatsMan Guess:" + batsManGuess);

                    if (batsManGuess == ballSelected) {
                        //Wicket Gone
                        currentWicket++;
                        outPut("Wicket Gone:" + currentWicket);
                        resultTv.setText("Wicket");
                        if (currentWicket >= noOfPlayers) {
                            //Batting End
                            outPut("All Wickets gone!!");
                            endGame();

                        }
                    } else {
                        totalRun = totalRun + batsManGuess;
                        if (batsManGuess == 0)
                            resultTv.setText("Blocked");
                        if (batsManGuess == 1)
                            resultTv.setText("Single");
                        if (batsManGuess == 2)
                            resultTv.setText("Double");
                        if (batsManGuess == 3)
                            resultTv.setText("Triple");
                        if (batsManGuess == 4)
                            resultTv.setText("Four");
                        if (batsManGuess == 6)
                            resultTv.setText("Six");

                        if(GameConstants.turn==2){
                            if(totalRun>GameConstants.firstBattingRun){
                                outPut("Scored more runs, Game end");
                                endGame();
                            }
                        }

                    }
                    nextBall();
                    if (currentOver == GameConstants.noOfOvers) {
                        outPut("Overs over, Game end");
                        endGame();

                    }
                    refreshDisplay();
                }
            }
        });
    }


    public void endGame() {
        endFlag=1;
        if (GameConstants.turn == 1) {
            GameConstants.turn++;
            GameConstants.firstBattingRun = totalRun;

            switchFlag=1;
            bowlButton.setText("Start Batting");
            //Start bowling activity
//            Intent bowlerIntent = new Intent(BatsmanGame.this, BlowerGame.class);
//            BatsmanGame.this.startActivity(bowlerIntent);
//            outPut("Starting Bowling Activity..");

        } else if (GameConstants.turn >= 2) {
            outPut("Turns Overs, Game End!!");

            switchFlag=2;
            bowlButton.setText("End Game");
//            setContentView(R.layout.game_end);
//            TextView finalResultTv = (TextView) findViewById(R.id.textViewFinalResult);
//            //check Runs and Set Final Result
//            if (totalRun > GameConstants.secondBattingRun) {
//                finalResultTv.setText("Won");
//            } else {
//                finalResultTv.setText("Lose");
//            }

        }
    }

    public void switcher(){
        if(switchFlag==1){
            //Start batting activity
            Intent battingIntent = new Intent(BowlerGame.this, BatsmanGame.class);
            BowlerGame.this.startActivity(battingIntent);
            outPut("Starting Batting Activity..");
        }else if(switchFlag==2){
            setContentView(R.layout.game_end);
            TextView finalResultTv = (TextView) findViewById(R.id.textViewFinalResult);
            //check Runs and Set Final Result
            if (totalRun > GameConstants.secondBattingRun) {
                finalResultTv.setText("Won");
            } else if(totalRun==GameConstants.secondBattingRun) {
                finalResultTv.setText("Tie");
            }
            else{
                finalResultTv.setText("Lose");
            }
        }
    }

    public void nextBall() {

        if (currentBall == 5) {
            currentBall = 0;
            outPut("current Ball Reset:" + currentBall);
            currentOver++;
            outPut("current over Incremented:" + currentOver);
            overDisplay = "" + currentOver + "." + currentBall;
            outPut("current overDisp:" + overDisplay);
        } else {
            currentBall++;
            outPut("current Ball Incremented:" + currentBall);
            overDisplay = "" + currentOver + "." + currentBall;

            outPut("current overDisp:" + overDisplay);
        }

    }

    public void refreshDisplay() {
        bowlerOversTv.setText(overDisplay);
        bowlerWicketsTv.setText(Integer.toString(currentWicket));
        batsManOversTv.setText(overDisplay);
        batsManWicketsTv.setText(Integer.toString(currentWicket));
        batsManRunsTv.setText(Integer.toString(totalRun));


    }

    public void outPut(String s) {
        //System.out.println("----------------------------------------------");
        System.out.println(s);
        //System.out.println("----------------------------------------------");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bowler_game, menu);
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
