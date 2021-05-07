package com.dalexpow.mortar;
import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.media.SoundPool.OnLoadCompleteListener;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.os.CountDownTimer.*;
import static android.view.View.*;
import static com.dalexpow.mortar.R.id;
import static com.dalexpow.mortar.R.layout;
import static com.dalexpow.mortar.R.raw;


public class MainActivity extends AppCompatActivity {
    public Button press;
    public SoundPool soundPool;
    private int sound1,sound2,sound3,steal1,steal2,steal3,steal4;//değişkenler
    public Button tap;
    Boolean isSelected = false;
    long seconds=0;
    long millis=0;
    public int MCount=0;
    Timer timer;
    TextView timeflg;
    public Button random;
    int[] sounds={raw.stealth1, raw.stealth2, raw.stealth3, raw.stealth4};//dizi değişkeni
    int[] twsound={raw.t1,raw.t2,raw.t3,raw.t4,raw.t5,raw.t6};
    static int[] sm;// soundpool dizi tanımlaması
    MediaPlayer mp1,mp2;// media player tanımlaması
    final int[] steals= new int[4]; // 4 kapasiteli dizi değişkeni
    SeekBar change;
    TextView display;
    Handler handler;
    long startTime=0;
    Button start,stop;
    long time,now,init;





    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_main);
        press = findViewById(id.press);
        random = findViewById(id.random);
        timeflg = findViewById(id.timeflg);
        timer = new Timer();
        final SeekBar change =(SeekBar) findViewById(R.id.change);
        tap= findViewById(id.tap);
        handler = new Handler();//handlerımız
        display = (TextView) findViewById(id.display);
        final ToggleButton passTog = (ToggleButton) findViewById(R.id.onoff);



        //yepyeni timer denemesi
     final Runnable timeRunnable=new Runnable() {
         @Override
         public void run() {
             long millis = System.currentTimeMillis() - startTime;
             int seconds = (int) (millis / 1000);
             int minutes = seconds / 60;
             seconds = seconds % 60;

             display.setText(String.format("%d:%02d", minutes, seconds));

             display.postDelayed((Runnable) this, 500);

         }

     };

//yeni timer denemesi
        start=findViewById(R.id.start);
        start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Button start=(Button)view;
                if (start.getText().equals("stop")){
                    handler.removeCallbacks(timeRunnable);
                    start.setText("working");
                }
                else{
                    startTime=System.currentTimeMillis();
                    handler.postDelayed(timeRunnable,0);
                    timeflg.setText("stopping");
                }

            }
        });
        stop=findViewById(id.stop);
        stop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                handler.removeCallbacks(timeRunnable);

            }
        });
//        @Override
//         public void onPause(){
//            super.onPause();
//            handler.removeCallbacks(timeRunnable);
//            Button start =(Button)findViewById(R.id.start);
//            timeflg.setText("wtf");
//        }





        final Runnable updater = new Runnable() {
            @Override
            public void run() {
                 {
                    now=System.currentTimeMillis();
                    time=now-init;
                    display.setText("" + time);
                    handler.postDelayed(this, 30);
                }
            }


        };

//tap butonu on click kullanmı timer butona basınca çalışıyor
        tap.setOnClickListener(new OnClickListener() {
         @Override
        public void onClick(View view) {


         }

       });


        //tap butonu touch listener kullanımı
       tap.setOnTouchListener(new OnTouchListener() {
           @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
           @Override
           public boolean onTouch(View view, MotionEvent event) {
               switch (event.getAction() ) {
                   case MotionEvent.ACTION_DOWN:
                       //eğer tap butonuna basılmaya devam edilirse timer ı başlat milisaniye cinsinden

                       init = System.currentTimeMillis(); // init değeri anlık alınan milisaniyeyi gösteriyor
                       handler.post(updater); //handler a updater runnable metoduna post gönderiyor yani init i tanımlanan yere

                       timeflg.setText("start");//stop yazdırma

                       break; //durmak için switch case için
                   case MotionEvent.ACTION_UP:
                       //eğer butondan parmağımı çekersem yapılacaklar kodu


                       handler.removeCallbacks(updater);//handlerı durdur updater runnable metodunu
                       timeflg.setText("stop");

                       break;
               }

               return false;
           }
       });










//seekbar kullanımı alanı
        change.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                change.setMax(5);
                progressChangedValue = progress;
                if (progress == 0){


                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(MainActivity.this, "Seek bar progress is :" + progressChangedValue,
                        Toast.LENGTH_SHORT).show();

            }
        });
        //random butonu ses çalma rastgele mediaPlayer ile kullanımı
        random.setOnClickListener(new OnClickListener() {
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

        timeflg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mp1 != null && mp1.isPlaying()){
                    mp1.stop();
                    mp1.reset();
                    mp1.release();
                    mp1 = null;
                    soundPool.pause(sound1);
                    soundPool.pause(sound2);
                    soundPool.stop(sound1);
                    soundPool.stop(sound2);
                    soundPool.autoPause();




            }
        }});






        //minigun sesi kodları
        press.setOnTouchListener(new OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){

                    switch (event.getAction() ) {
                        case MotionEvent.ACTION_DOWN:

                            soundPool.play(sound1, 1, 1, 1, 0, 1);

                            soundPool.play(sound2, 1, 1, 2, -1, 1) ;// sonsuz döngü


                            break;
                        case MotionEvent.ACTION_UP:


                            soundPool.play(sound3, 1, 1, 2, 0, 1);


                            soundPool.autoPause();

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



