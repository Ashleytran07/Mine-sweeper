package com.example.minesweeper_project1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.minesweeper_project1.MainActivity;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);

        Intent intent = getIntent();
        String message = intent.getStringExtra("com.example.sendmessage.MESSAGE");
        message = "Used " + message + " seconds.";

        TextView textView = (TextView) findViewById(R.id.textView7);
        textView.setText(message);

        String winCondition = intent.getStringExtra("com.example.sendmessage.WINLOSE");
        TextView WLView = (TextView)findViewById(R.id.textView8);
        WLView.setText(winCondition);
        TextView encouragement = (TextView)findViewById(R.id.textView9);

        if (winCondition.equals("You won!")) {
            encouragement.setText("Good job!");
        } else {
            encouragement.setText("Sorry. Try again!");
        }
    }

    public void playAgain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}