package com.sudhanshu.theothellogame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartUpScreen extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up_screen);
        Button startGame = (Button)findViewById(R.id.startGame);
        startGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent();
        i.setClass(this,MainActivity.class);
        startActivity(i);
    }
}
