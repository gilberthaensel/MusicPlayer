package umn.ac.id.musicplayer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gauravk.audiovisualizer.visualizer.BarVisualizer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class MusicPlayerActivity extends AppCompatActivity {
    Button btnplay, btnnext, btnprev, btnff, btnfr;
    TextView txtname, txtstart, txtstop;
    SeekBar seekmusic;
    BarVisualizer visualizer;

    String sname;
    public static final String EXTRA_NAME = "song_name";
    static MediaPlayer mediaPlayer;
    int position;
    ArrayList<String> mySongs;
    ImageView imageView;
    Thread updateseekbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(visualizer != null){
            visualizer.release();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setTitle("Now Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnprev = findViewById(R.id.btnprev);
        btnnext = findViewById(R.id.btnnext);
        btnplay = findViewById(R.id.playbtn);
        btnff = findViewById(R.id.btnff);
        btnfr = findViewById(R.id.btnfr);
        txtname = findViewById(R.id.txtsn);
        txtstart = findViewById(R.id.txtstart);
        txtstop = findViewById(R.id.txtstop);
        seekmusic = findViewById(R.id.seekbar);
        visualizer = findViewById(R.id.blast);
        imageView = findViewById(R.id.imageview);


        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        mySongs = (ArrayList) bundle.getParcelableArrayList("songs");
        String songName = i.getStringExtra("songname");
        position = bundle.getInt("pos", 0);

        sname = songName;
        txtname.setSelected(true);
        txtname.setText(sname);

        Log.d("uri", songName);

        Uri uri = Uri.parse(mySongs.get(position).toString());

        mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
        mediaPlayer.start();

        updateseekbar = new Thread(){
            @Override
            public void run(){
                int totalDuration = mediaPlayer.getDuration();
                int currentposition = 0;

                while(currentposition<totalDuration){
                    try{
                        sleep(500);
                        currentposition = mediaPlayer.getCurrentPosition();
                        seekmusic.setProgress(currentposition);
                    }catch (InterruptedException | IllegalStateException e){
                        e.printStackTrace();
                    }
                }
            }
        };
        seekmusic.setMax(mediaPlayer.getDuration());
        updateseekbar.start();
        seekmusic.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        seekmusic.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        seekmusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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


        String endTime = createTime(mediaPlayer.getDuration());
        txtstop.setText(endTime);

        Log.d("endtime", endTime);

        final Handler handler = new Handler();
        final int delay = 1000;

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String currentTime = createTime(mediaPlayer.getCurrentPosition());
                txtstart.setText(currentTime);
                handler.postDelayed(this, delay);
            }
        }, delay);

        btnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    btnplay.setBackgroundResource(R.drawable.ic_play);
                    mediaPlayer.pause();
                }else{
                    btnplay.setBackgroundResource(R.drawable.ic_pause);
                    mediaPlayer.start();
                }
            }
        });
        //next listener



        int audiosessionId = mediaPlayer.getAudioSessionId();
        if(audiosessionId != 1){
            visualizer.setAudioSessionId(audiosessionId);
        }

        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position+1)%mySongs.size());
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                sname = mySongs.get(position).substring(mySongs.get(position).lastIndexOf("/")+1).replace(".mp3", "").replace(".wav", "");
                txtname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.ic_pause);
                startAnimation(imageView);
                String endTime = createTime(mediaPlayer.getDuration());
                txtstop.setText(endTime);
                seekmusic.setMax(mediaPlayer.getDuration());
                seekmusic.setProgress(0);
                int audiosessionId = mediaPlayer.getAudioSessionId();
                if(audiosessionId != 1){
                    visualizer.setAudioSessionId(audiosessionId);
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Log.d("complete", "MASUK WOI");
                        btnnext.performClick();
                    }
                });
            }
        });

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                position = ((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u = Uri.parse(mySongs.get(position).toString());
                mediaPlayer = MediaPlayer.create(getApplicationContext(), u);
                sname = mySongs.get(position).substring(mySongs.get(position).lastIndexOf("/")+1).replace(".mp3", "").replace(".wav", "");
                txtname.setText(sname);
                mediaPlayer.start();
                btnplay.setBackgroundResource(R.drawable.ic_pause);
                startAnimation(imageView);
                String endTime = createTime(mediaPlayer.getDuration());
                txtstop.setText(endTime);
                seekmusic.setMax(mediaPlayer.getDuration());
                seekmusic.setProgress(0);
                int audiosessionId = mediaPlayer.getAudioSessionId();
                if(audiosessionId != 1){
                    visualizer.setAudioSessionId(audiosessionId);
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        Log.d("complete", "MASUK WOI");
                        btnnext.performClick();
                    }
                });
            }
        });

        btnff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+10000);
                    seekmusic.setProgress(mediaPlayer.getCurrentPosition()+1000);
                }


            }
        });

        btnfr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-10000);
                    seekmusic.setProgress(mediaPlayer.getCurrentPosition()-1000);
                }
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Log.d("complete", "MASUK WOI");
                btnnext.performClick();
            }
        });
    }

    public void startAnimation(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0f, 360f);
        animator.setDuration(1000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animator);
        animatorSet.start();
    }

    public String createTime(int duration){
        String time = "";
        int min = duration/1000/60;
        int sec = duration/1000%60;

        time+=min+":";

        if(sec<10){
            time+="0";
        }
        time+=sec;

        return time;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
    }
}
