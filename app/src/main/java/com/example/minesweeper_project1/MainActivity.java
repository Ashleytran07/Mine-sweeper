package com.example.minesweeper_project1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.content.Intent;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int COLUMN_COUNT = 10;
    private static final int ROW_COUNT = 12;

    // save the TextViews of all cells in an array, so later on,
    // when a TextView is clicked, we know which cell it is
    private ArrayList<TextView> cell_tvs;
    public Board board;

    //stopwatch
    private int clock = 0;
    private boolean running = true;

    public boolean mineMode = true;
    public int flagCount = 4;
    public boolean revealPage = false;
    boolean won = false;



    private int dpToPixel(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return Math.round(dp * density);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        board = new Board();

        cell_tvs = new ArrayList<TextView>();

        GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
        // Method (3): add four dynamically created cells with LayoutInflater
        LayoutInflater li = LayoutInflater.from(this);
        for (int i = 0; i<12; i++) {
            for (int j=0; j<10; j++) {
                TextView tv = (TextView) li.inflate(R.layout.custom_cell_layout, grid, false);
                tv.setText("" + board.mines[i][j]);
                tv.setTextColor(Color.GREEN);
                tv.setBackgroundColor(Color.GREEN);
                tv.setOnClickListener(this::onClickTV);

                GridLayout.LayoutParams lp = (GridLayout.LayoutParams) tv.getLayoutParams();
                lp.rowSpec = GridLayout.spec(i);
                lp.columnSpec = GridLayout.spec(j);

                grid.addView(tv, lp);

                cell_tvs.add(tv);
            }
        }
        // mode changes
        TextView mode = (TextView) findViewById(R.id.textView12);
        mode.setOnClickListener(this::onClickMode);

        // flag text
        TextView flagText = (TextView) findViewById(R.id.textView2);
        flagText.setText(""+flagCount);

        // stopwatch
        if (savedInstanceState != null) {
            clock = savedInstanceState.getInt("clock");
            running = savedInstanceState.getBoolean("running");
        }

        runTimer();

    }

    public void sendMessage(boolean didWin){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra("com.example.sendmessage.MESSAGE", "" + clock);

        if (didWin) {
            intent.putExtra("com.example.sendmessage.WINLOSE", "You won!");
        } else {
            intent.putExtra("com.example.sendmessage.WINLOSE", "You lost.");
        }

        startActivity(intent);
    }


    private int findIndexOfCellTextView(TextView tv) {
        for (int n=0; n<cell_tvs.size(); n++) {
            if (cell_tvs.get(n) == tv)
                return n;
        }
        return -1;
    }

    public void onClickTV(View view){
        TextView tv = (TextView) view;
        // flag text
        TextView flagText = (TextView) findViewById(R.id.textView2);

        int n = findIndexOfCellTextView(tv);
        int i = n / COLUMN_COUNT;
        int j = n % COLUMN_COUNT;

        //END
        if(revealPage) {
            if(won) {
                sendMessage(true);
            }else {
                sendMessage(false);
            }
        }


        if(!mineMode){ // if in flag mode
            // if not revealed, leave flag
            if(!board.revealed[i][j]) {

                // if there are enough flags
                if(flagCount != 0 && flagCount <= 4 && !board.flagged[i][j]) {
                    tv.setText(R.string.flag);
                    board.flagged[i][j] = true;
                    flagCount--;
                    flagText.setText(""+flagCount);
                    return;
                }

                // if clicked on again,
                //add another flag + turn to green/ mark flagged to false
                if(board.flagged[i][j]){
                    board.flagged[i][j] = false;
                    tv.setTextColor(Color.GREEN);
                    tv.setText("");
                    flagCount++;
                    flagText.setText(""+flagCount);
                }

            }
        }
        // reveal adj mines
        else if(board.mines[i][j] == 0){
            GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
            // Method (3): add four dynamically created cells with LayoutInflater
            LayoutInflater li = LayoutInflater.from(this);
            board.revealADJ(i,j);
            for(int r = 0; r < 12; r++){
                for(int c = 0; c < 10; c++){
                    if(board.revealed[r][c] && board.mines[r][c] == 0){
                        TextView cell = (TextView) grid.getChildAt(r * grid.getColumnCount() + c);
                        cell.setText("");
                        cell.setTextColor(Color.GRAY);
                        cell.setBackgroundColor(Color.LTGRAY);
                    } else if(board.revealed[r][c]){
                        TextView cell = (TextView) grid.getChildAt(r * grid.getColumnCount() + c);
                        cell.setTextColor(Color.GRAY);
                        cell.setBackgroundColor(Color.LTGRAY);
                    }
                }
            }
            if(board.revealed[i][j]){
                tv.setTextColor(Color.GRAY);
                tv.setBackgroundColor(Color.LTGRAY);
            }
        }
        else {
            // mark as visited
            board.revealed[i][j] = true;

            tv.setTextColor(Color.GRAY);
            tv.setBackgroundColor(Color.LTGRAY);
        }

        //if clicked on bomb
        if(board.mines[i][j] == -1){
            GridLayout grid = (GridLayout) findViewById(R.id.gridLayout01);
            LayoutInflater li = LayoutInflater.from(this);
            for(int r = 0; r < 12; r++) {
                for (int c = 0; c < 10; c++) {
                    if(board.mines[r][c] == -1){
                        TextView cell = (TextView) grid.getChildAt(r * grid.getColumnCount() + c);
                        cell.setText(R.string.mine);
                        cell.setBackgroundColor(Color.RED);

                    }

                }
            }
            revealPage = true;
        }

        //if clicked on all without bombs
        if(revealedCounter(board.revealed) == 116){
            won = true;
            revealPage = true;
        }



        // if I win -> sendMessage(true);

//        if (tv.getCurrentTextColor() == Color.GREEN) {
//            tv.setTextColor(Color.GREEN);
//            tv.setBackgroundColor(Color.parseColor("lime"));
//        }
//        else {
//            tv.setTextColor(Color.GRAY);
//            tv.setBackgroundColor(Color.LTGRAY);
//        }
    }

    public int revealedCounter(boolean[][] openedSquares){

        int numOpened = 0;

        for(int i = 0; i < 12; i++){
            for(int j = 0; j < 10; j++){
                if (openedSquares[i][j] == true){
                    numOpened++;
                }
            }
        }

        return numOpened;

    }

    public void onClickMode(View modeView){
        TextView mode = (TextView) modeView;
        int pick = R.string.pick;
        int flag = R.string.flag;

        //if pick turn to flag ; change boolean
        if(mineMode){
            mineMode = false;
            ((TextView) modeView).setText(flag);

        }else{
            //if flag, turn to pick ; change boolean
            mineMode = true;
            ((TextView) modeView).setText(pick);
        }




    }

    // stopwatch
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("clock", clock);
        savedInstanceState.putBoolean("running", running);
    }

    public void onClickStart(View view) {
        running = true;
    }

    public void onClickStop(View view) {
        running = false;
    }
    public void onClickClear(View view) {
        running = false;
        clock = 0;
    }

    private void runTimer() {
        final TextView timeView = (TextView) findViewById(R.id.clockText);
        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
//                int hours =clock/3600;
//                int minutes = (clock%3600) / 60;
                int seconds = clock;
                String time = String.format("%02d", seconds);
                timeView.setText(time);

                if (running) {
                    clock++;
                }
                handler.postDelayed(this, 1000);
            }
        });
    }


}

