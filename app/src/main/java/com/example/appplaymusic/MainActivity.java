package com.example.appplaymusic;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    TextView tv_time_bd, tv_time_kt, tv_ten_bai;
    CircleImageView imageButtonBack, imageButtonPlay, imageButtonStop, imageButtonNext,imageViewMiss;
    ArrayList<Song> songList;
    int position = 0;
    MediaPlayer mediaPlayer;
    SeekBar skSong;
    ImageView imageViewMusic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Nhớ người yêu");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEven();
        addSong();
        CreateMediaPlayer();
        updateTime();
        startAnimation();
        stopAnimation();
    }
    private void stopAnimation() {//dừng hiệu ứng
        imageViewMusic.animate().cancel();
    }

    private void startAnimation() {//hiệu ứng quanh đĩa

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                imageViewMusic.animate().rotationBy(360).withEndAction(this)
                        .setDuration(10000).setInterpolator(new LinearInterpolator()).start();
            }
        };
        imageViewMusic.animate().rotationBy(360).withEndAction(runnable)
                .setDuration(10000).setInterpolator(new LinearInterpolator()).start();
    }

    private void updateTime() {//dùng để xét thời gian bài hát đang chạy
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dinhDangTime = new SimpleDateFormat("mm:ss");
                tv_time_bd.setText(dinhDangTime.format(mediaPlayer.getCurrentPosition()));// vị trí hiện tại mediaPlayer đang phát
                //update Progess sbSong
                skSong.setProgress(mediaPlayer.getCurrentPosition());
                //kiem tra  thoi gian bao hat -> neu het bai se next bai moi
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        position++;
                        if (position > songList.size() - 1) {
                            position = 0;
                        }
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        CreateMediaPlayer();
                        mediaPlayer.start();
                        imageButtonPlay.setImageResource(R.drawable.ic_pause);
                        skSong.setProgress(0);
                        startAnimation();

                    }
                });
                handler.postDelayed(this, 500);
            }
        }, 100);

    }

    private void CreateMediaPlayer() {//khởi tạo 1 mediaPlayer với vị trí bài hát thứ position trong songList
        mediaPlayer = MediaPlayer.create(MainActivity.this, songList.get(position).getIdBai());
        tv_ten_bai.setText(songList.get(position).getTenBai());
        setTimeTotal();

    }

    private void addSong() {
        songList = new ArrayList<>();
        songList.add(new Song("Tềnh yêu màu hường ", R.raw.tinh_yeu_mau_hong));
        songList.add(new Song("Có một nơi như thế :)", R.raw.co_1_noi_nhu_the));
        songList.add(new Song("Đêm trăng tình yêu :)", R.raw.dem_trang_tinh_yeu));
        songList.add(new Song("Hạc giấy:)", R.raw.hac_giay));
        songList.add(new Song("Vợ luôn đúng,ck cãi đã là sai :)", R.raw.dung_cung_thanh_sai));
        songList.add(new Song("Stand by Vũ đz", R.raw.stand_by_your_man));
        songList.add(new Song("Thay lòng,ứ phải vũ", R.raw.thay_long));
        songList.add(new Song("Yêu đừng sợ đau,vì đã có vũ", R.raw.yeu_dung_so_dau));
        songList.add(new Song("Yêu là 2-4 năm nữa cưới haha", R.raw.yeu_la_cuoi));
        songList.add(new Song("Ngỡ như giấc mơ", R.raw.ngo_nhu_giac_mo));
        songList.add(new Song("Suy nghĩ trong anh", R.raw.suy_nghi_trong_anh));
        songList.add(new Song("Vâng Trang yêu Vũ :))", R.raw.vang_em_yeu_anh));
        songList.add(new Song("Goắt a guâ", R.raw.what_are_words));
        songList.add(new Song("Gio men ", R.raw.you_man));
        songList.add(new Song("Nhớ vợ :(( ", R.raw.nho));


    }

    private void initEven() {
        // Animation animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate);

        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {//nếu đang hát->pause-> đổi hình nút pause
                    mediaPlayer.pause();
                    imageButtonPlay.setImageResource(R.drawable.ic_play);
                    stopAnimation();

                } else {// nếu đang dừng hát
                    mediaPlayer.start();
                    imageButtonPlay.setImageResource(R.drawable.ic_pause);
                    startAnimation();
                    btnStartService();
                }
                //imageViewMusic.startAnimation(animRotate);
            }
        });
        imageButtonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.release();
                imageButtonPlay.setImageResource(R.drawable.ic_play);
                CreateMediaPlayer();
                skSong.setProgress(0);
                stopAnimation();

            }
        });
        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position++;//tăng position nên.
                if (position > songList.size() - 1) {//nếu songList có 6 bài position quay về 0
                    position = 0;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                CreateMediaPlayer();
                imageButtonPlay.setImageResource(R.drawable.ic_pause);
                skSong.setProgress(0);
                startAnimation();
                mediaPlayer.start();
                btnStartService();

            }
        });
        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position = position - 1;
                if (position < 0) {
                    position = songList.size() - 1;
                }
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                CreateMediaPlayer();
                imageButtonPlay.setImageResource(R.drawable.ic_pause);
                skSong.setProgress(0);
                mediaPlayer.start();
                startAnimation();
                btnStartService();

            }
        });
        skSong.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {// hàm dùng để bắt sự kiện ng dùng
            //tác động nên Seek Bar
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(skSong.getProgress());
            }
        });
        imageViewMiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
                position=songList.size()-1;//chuyen den bai hat muon phat
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                CreateMediaPlayer();
                mediaPlayer.start();
                imageButtonPlay.setImageResource(R.drawable.ic_pause);
                skSong.setProgress(0);
                startAnimation();
            }
        });
    }

    private void btnStartService() {
        Intent intent = new Intent(this,MusicService.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key",songList.get(position));
        intent.putExtras(bundle);
        startService(intent);
    }

    private void sendNotification() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_miss);
        Uri sound = Uri.parse("android.resource://"+ getPackageName() + "/" + R.raw.jingle_bells_sms_523);

        Notification notification = new NotificationCompat.Builder(this,MyApplication.CHANNEL_ID)
                .setContentTitle("Vợ nhớ ck rồi phải không?")
                .setContentText("Nhớ thì mau nên với ck đi nhé")
                .setSmallIcon(R.drawable.hearticon)
                .setLargeIcon(bitmap)
                .setSound(sound)
                .setColor(getResources().getColor(R.color.design_default_color_error))
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify(getNotificationId(),notification);
        }
    }

    private int getNotificationId() {
        return (int) new Date().getTime();
    }

    private void setTimeTotal() {//dùng để gán progress max của seekbar bằng tgian bài hát Duration.
        SimpleDateFormat dinhDangTime = new SimpleDateFormat("mm:ss");
        tv_time_kt.setText(dinhDangTime.format(mediaPlayer.getDuration()));
        // gán giá trị max của seekbar skSong bằng với thời gian bài hát
        skSong.setMax(mediaPlayer.getDuration());
    }

    private void initView() {
        tv_time_bd = findViewById(R.id.time_bd);
        tv_ten_bai = findViewById(R.id.ten_bai);
        tv_time_kt = findViewById(R.id.time_kt);
        imageButtonBack = findViewById(R.id.imageButton_back);
        imageButtonPlay = findViewById(R.id.imageButton3_play);
        imageButtonStop = findViewById(R.id.imageButton4_stop);
        imageButtonNext = findViewById(R.id.imageButton2_next);
        skSong = findViewById(R.id.seekBar_song);
        imageViewMusic = findViewById(R.id.item_music);
        imageViewMiss = findViewById(R.id.img_miss);
    }
}