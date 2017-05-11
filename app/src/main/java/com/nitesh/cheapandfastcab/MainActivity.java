package com.nitesh.cheapandfastcab;

import android.content.Intent;
import android.media.MediaPlayer;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, View.OnClickListener {

    private Button tapBtn;
    private MediaPlayer mp;
    private TextToSpeech tts;
    private Button record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tapBtn = (Button) findViewById(R.id.tapBtn);
        record = (Button) findViewById(R.id.respond);
        mp = new MediaPlayer();
        tts = new TextToSpeech(this, this);
        record.setOnClickListener(this);
        tapBtn.setOnClickListener(this);
    }

    @Override
    public void onInit(int success) {
        if (success == TextToSpeech.SUCCESS) {
            int res = tts.setLanguage(Locale.getDefault());
            if (res == TextToSpeech.LANG_NOT_SUPPORTED || res == TextToSpeech.LANG_MISSING_DATA) {
                Log.e("Nitesh", "Please check");
            }
        } else {
            Log.e("Nitesh", "Not initialized");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tapBtn:
                tts.speak("Welcome to Cab Booking Assistant. Please tell the destination where you want to go", TextToSpeech.QUEUE_FLUSH, null);
                break;
            case R.id.respond:
                showSpeechDialog();
                break;

        }
    }

    private void showSpeechDialog() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplication().getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "say something");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 2);
        intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
        intent.putExtra("android.speech.extra.GET_AUDIO", true);
        startActivityForResult(intent, 900);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 900) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
//            playIv.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.play));
                ArrayList<String> matches = bundle.getStringArrayList(RecognizerIntent.EXTRA_RESULTS);
                Log.e("OnActivityResult", matches.get(0));
                tts.speak("your destination is identified as "+matches.get(0)+". Are you sure you want to go to "+matches.get(0),TextToSpeech.QUEUE_FLUSH,null);
            }
        }
    }
}
