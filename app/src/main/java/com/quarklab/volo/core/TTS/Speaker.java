package com.quarklab.volo.core.TTS;

import android.content.Context;
import android.speech.tts.TextToSpeech;


import java.util.Locale;

/**
 * Created by rudy on 2/4/17.
 */
public class Speaker {
    private TextToSpeech myTTS;
    public Speaker(Context context){
        this.myTTS = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS){
                    myTTS.setLanguage(Locale.ENGLISH);
                }
            }
        });
    }

    public void speak(String speech) {
        this.myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void shutdown() {
        this.myTTS.shutdown();
    }
}
