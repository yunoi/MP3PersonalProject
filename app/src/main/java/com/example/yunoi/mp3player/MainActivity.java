package com.example.yunoi.mp3player;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import static com.example.yunoi.mp3player.Fragment1.MP3_PATH;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private BottomNavigationView bottomMenu;
    private FrameLayout frameLayout;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private ImageView ivPlay, ivStop, ivFav, ivRewind, ivFoward;
    private TextView tvPlaying, tvTime;
    private SeekBar seekBar;
    private MediaPlayer mediaPlayer;
    private View favoriteView;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
    private String janre;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, MODE_PRIVATE);

        bottomMenu = findViewById(R.id.bottomMenu);
        frameLayout = findViewById(R.id.frameLayout);

        ivPlay = findViewById(R.id.ivPlay);
        ivStop = findViewById(R.id.ivStop);
        ivRewind = findViewById(R.id.ivRewind);
        ivFoward = findViewById(R.id.ivFoward);
        ivFav = findViewById(R.id.ivFav);
        tvPlaying = findViewById(R.id.tvPlaying);
        tvTime = findViewById(R.id.tvTime);
        seekBar = findViewById(R.id.seekBar);

        ivPlay.setOnClickListener(this);
        ivStop.setOnClickListener(this);
        ivFav.setOnClickListener(this);
        ivRewind.setOnClickListener(this);
        ivFoward.setOnClickListener(this);

        mediaPlayer = new MediaPlayer();
        seekBar.setProgress(0);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        // bottomMenu를 변경했을 때 그것을 감지하여 해당된 프래그먼트를 세팅해주는 리스너
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.action_1:
                        setOnChangeFragment(0);
                        break;
                    case R.id.action_2:
                        setOnChangeFragment(1);
                        break;
                }
                return true;
            }
        });
        setOnChangeFragment(0);
    }
    public void playMusic(String path){

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();
        } catch (IOException e) {
            toastDisplay("재생하실 파일을 선택해 주세요.");
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPlay:
                try{
                    if (!mediaPlayer.isPlaying()) {
                        mediaPlayer.start();
                        ivPlay.setImageResource(R.drawable.sharp_pause_black_36dp);
                        tvPlaying.setText("Now Playing ...   " + Fragment1.selectedFile);
                        startUiThread();
                    } else if (mediaPlayer.isPlaying()) {
                        mediaPlayer.pause();
                        tvPlaying.setText("PAUSED");
                        ivPlay.setImageResource(R.drawable.sharp_play_arrow_black_36dp);

                    }
                } catch(Exception e) {
                    toastDisplay("재생하실 파일을 선택해 주세요.");
                }
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        nextSong();
                    }
                });
                break;

            case R.id.ivStop:
                mediaPlayer.stop();
                mediaPlayer.reset();
                ivPlay.setEnabled(true);
                tvPlaying.setText("STOP");
                seekBar.setProgress(0);
                tvTime.setText("00:00");
                ivPlay.setImageResource(R.drawable.sharp_play_arrow_black_36dp);
                break;
            case R.id.ivFav:
                favoriteView = View.inflate(this, R.layout.dialog, null);
                final EditText edtSinger = favoriteView.findViewById(R.id.edtSinger);
                final EditText edtTitle = favoriteView.findViewById(R.id.edtTitle);
                final Spinner spinner = favoriteView.findViewById(R.id.spinner);
                final RatingBar ratingBar = favoriteView.findViewById(R.id.ratingBar);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            janre = "";
                        } else {
                            janre = parent.getItemAtPosition(position).toString();
//                            toastDisplay(janre);
                        }
                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setTitle("좋아요");
                dialog.setView(favoriteView);
                dialog.setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String singer = edtSinger.getText().toString();
                        String title = edtTitle.getText().toString();
                        int rate = (int) ratingBar.getRating();
                        Fragment1.db = Fragment1.dbHelper.getWritableDatabase();
                        if (edtSinger.getText().toString().equals("") || edtTitle.getText().toString().equals("")) {
                            toastDisplay("가수명과 곡명을 입력해 주세요");
                        } else {
                            Fragment1.db.execSQL("INSERT INTO myMusicTBL VALUES ('" + singer + "','" + title + "','" + janre + "'," + rate + ");");
                        }
                        Fragment1.db.close();
                        toastDisplay("즐겨찾는 곡으로 저장되었습니다.");

                    }
                });
                dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        toastDisplay("취소되었습니다.");
                    }
                });
                dialog.show();
                break;

            case R.id.ivRewind :
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    Fragment1.position-=1;
                    if(Fragment1.position<0){
                        Fragment1.position=Fragment1.list.size()-1;
                    }
                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(MP3_PATH + Fragment1.list.get(Fragment1.position));
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    Fragment1.selectedFile = String.valueOf(Fragment1.list.get(Fragment1.position));
                    tvPlaying.setText("Now Playing ...   " + Fragment1.selectedFile);
                    startUiThread();
                }
                break;
            case R.id.ivFoward :
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.stop();
                    Fragment1.position+=1;
                    if(Fragment1.position>=Fragment1.list.size()){
                        Fragment1.position = 0;
                    }
                    try {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(MP3_PATH + Fragment1.list.get(Fragment1.position));
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Fragment1.selectedFile = String.valueOf(Fragment1.list.get(Fragment1.position));
                    tvPlaying.setText("Now Playing ...   " + Fragment1.selectedFile);
                    mediaPlayer.start();
                    startUiThread();
                }
                break;

        }
    }

    private void nextSong() {
        if (++Fragment1.position >= Fragment1.list.size()) {
            // 마지막 곡이 끝나면, 재생할 곡을 초기화.
            Fragment1.position = 0;
        } else {
            // 다음 곡을 재생.
            try {
                mediaPlayer.setDataSource(MP3_PATH + Fragment1.list.get(Fragment1.position));
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void startUiThread() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                if (mediaPlayer == null) {
                    return;
                }
                // 작업스레드 내에서 UI객체를 변경하기 위해
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        seekBar.setMax(mediaPlayer.getDuration());
                        tvTime.setText(simpleDateFormat.format(mediaPlayer.getDuration()));

                    }
                });

                while (mediaPlayer.isPlaying()) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            tvTime.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                        }
                    }); // end of runOnUiThread
                    SystemClock.sleep(100);

                }// end of while
            }
        };
        thread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.stop();
    }

    private void setOnChangeFragment(int position) {
        // 화면 전환 위해서는 프래그먼트 매니저 필요
        fragmentManager = getSupportFragmentManager();
        // 프래그먼트 매니저의 권한을 받아서 화면을 바꾸는 역할의 트랜젝션 필요
        fragmentTransaction = fragmentManager.beginTransaction();
        switch(position){
            case 0:
                fragmentTransaction.replace(R.id.frameLayout, fragment1);
                fragmentTransaction.commit();
                break;
            case 1:
                fragmentTransaction.replace(R.id.frameLayout, fragment2);
                fragmentTransaction.commit();
                break;

        }
    }

    private void toastDisplay(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

}
