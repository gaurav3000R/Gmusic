 package com.example.gmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {




    TextView textView;
    ImageView play,previous,next;
    ArrayList<File> Songs;
    MediaPlayer mediaPlayer;
    String TextContent;
    int position;
    SeekBar seekBar ;
    Thread updates;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updates.interrupt();

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView=findViewById(R.id.textView);
        play =findViewById(R.id.play);
        previous=findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekBar =findViewById(R.id.seekBar);
        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();

        Songs =(ArrayList)bundle.getParcelableArrayList("songList");

        TextContent = intent.getStringExtra("currentSong");
        textView.setText(TextContent);

        textView.setSelected(true);
        position = intent.getIntExtra("position",0);
        Uri uri = Uri.parse(Songs.get(position).toString());
        mediaPlayer = MediaPlayer.create(this,uri);
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        updates = new Thread(){

            @Override
            public void run() {
                int currentPosition =0;
                try{
                   while (currentPosition<mediaPlayer.getDuration()){
                    currentPosition= mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                    sleep(800);
                }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        updates.start();

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    play.setImageResource(R.drawable.ic_baseline_play_circle_outline_24);
                    mediaPlayer.pause();
                }
                else {
                    play.setImageResource(R.drawable.ic_baseline_pause_24);
                    mediaPlayer.start();

                }
            }
        });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=0){
                    position=position-1;


            }
                else {
                    position=Songs.size()-1;

                }

                Uri uri = Uri.parse(Songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                TextContent=Songs.get(position).getName().toString();
                textView.setText(TextContent);
            }

        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                if(position!=Songs.size()-1){
                    position=position+1;


                }
                else {
                    position=0;

                }

                Uri uri = Uri.parse(Songs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(),uri);
                mediaPlayer.start();
                seekBar.setMax(mediaPlayer.getDuration());
                TextContent=Songs.get(position).getName().toString();
                textView.setText(TextContent);
            }

        });



    }
}