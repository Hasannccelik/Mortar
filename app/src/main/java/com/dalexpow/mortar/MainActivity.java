package com.dalexpow.mortar;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;

import static com.dalexpow.mortar.R.id;
import static com.dalexpow.mortar.R.layout;
import static com.dalexpow.mortar.R.raw;


public class MainActivity extends AppCompatActivity {
    public Button press;
    public SoundPool soundPool;
    private int sound1,sound2,sound3,steal1,steal2,steal3,steal4;//değişkenler
    Boolean isSelected = false;
    long seconds=0;
    long millis=0;
    public int MCount=0;
    Timer timer;
    TextView timeflg;
    public Button random;
    int[] sounds={raw.stealth1, raw.stealth2, raw.stealth3, raw.stealth4};//dizi değişkeni
    static int[] sm;// soundpool dizi tanımlaması
    MediaPlayer mp1,mp2;// media player tanımlaması
    final int[] steals= new int[4]; // 4 kapasiteli dizi değişkeni



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        press = (Button) findViewById(id.press);
        random =(Button) findViewById(id.random);
        timeflg = (TextView)findViewById(id.timeflg);
        timer = new Timer();





        //random butonu ses çalma rastgele mediaPlayer ile kullanımı
        random.setOnClickListener(new View.OnClickListener() {
            @Override
            //rastgele ses çaldırma
            public void onClick(View v) {
                Random r = new Random();
                int Low = 0;
                int High = 4;
                int rndm = r.nextInt(High-Low) + Low;
                mp1 = MediaPlayer.create(getApplicationContext(),sounds[rndm]);
               mp1.start();


               //soundpool rastgele çalma denemesi
                soundPool.play(sounds[r.nextInt(4)], 1.0f, 1.0f, 0, 0, 1.0f);

            }
        });
        //durdurma kodları

        timeflg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp1 != null && mp1.isPlaying()){
                    mp1.stop();
                    mp1.reset();
                    mp1.release();
                    mp1 = null;



            }
        }});

        {


};

        //minigun sesi kodları
        press.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){

                    switch (event.getAction() ) {
                        case MotionEvent.ACTION_DOWN:
                            soundPool.play(sound1, 1, 1, 1, 0, 1);
                            //soundPool.play(sound2, 1, 1, 2, 1, 1) // sonsuz döngü

                            break;
                        case MotionEvent.ACTION_UP:
                            soundPool.autoPause();
                            //soundPool.setVolume(sound1,0,0);
                            soundPool.play(sound3, 1, 1, 2, 0, 1);
                            //soundPool.autoPause();

                            break;
                    }


                return true;
            }

            });


//soundpool tanımlama
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(7)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            soundPool = new SoundPool(7, AudioManager.STREAM_MUSIC, 0);
        }
        //soundpool id tanımlama
        sound1=soundPool.load(this, raw.minigun_spinup,0);
        sound2=soundPool.load(this, raw.minigun_spin,0);
        sound3=soundPool.load(this, raw.minigun_spindown,0);

        steal1=soundPool.load(this,raw.stealth1,0);
        steal2=soundPool.load(this,raw.stealth2,0);
        steal3=soundPool.load(this,raw.stealth3,0);
        steal4=soundPool.load(this,raw.stealth4,0);

        final Random r = new Random();
        sm= new int[3];
        

    }
    //ses çal metodu
    public  void  playSound(View v){
        if (id.press == v.getId()) {
            soundPool.play(sound2, 1, 1, 0, 1, 1);
        }
    }
    //hata olursa durdurma kodları soundpool
@Override
    protected void onDestroy(){
        super.onDestroy();
        soundPool.release();
        soundPool=null;
}

}

