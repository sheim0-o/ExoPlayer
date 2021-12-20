package com.example.exoplayer;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ClippingMediaSource;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
    String videoURL = "https://ak.picdn.net/shutterstock/videos/1052764631/preview/stock-footage-cute-cat-on-gray-studio-background-fluffy-siberian-cat-looking-up-concept-of-pets-domestic.mp4";
    String videoURL2 = "https://ak.picdn.net/shutterstock/videos/1014175664/preview/stock-footage-attentive-look-of-a-gray-thoroughbred-cat.mp4";
    String videoURL3 = "https://ak.picdn.net/shutterstock/videos/1050468217/preview/stock-footage-british-scottish-fold-cat-is-washing-her-tongue-happy-cat-washes-licks-his-paw-cat-is-lying.mp4";
    RadioGroup mRadioGroup;
    MediaSource clMediaSource1, clMediaSource2, clMediaSource3, loopMediaSource, clipMediaSource, mergeMediaSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exoPlayerView = findViewById(R.id.idExoPlayerVIew);
        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));

            exoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
            Uri videoURI1 = Uri.parse(videoURL);
            Uri videoURI2 = Uri.parse(videoURL2);
            Uri videoURI3 = Uri.parse(videoURL3);

            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            // Default media sources
            clMediaSource1 = new ExtractorMediaSource(videoURI1, dataSourceFactory, extractorsFactory, null, null);
            clMediaSource2 = new ExtractorMediaSource(videoURI2, dataSourceFactory, extractorsFactory, null, null);
            clMediaSource3 = new ExtractorMediaSource(videoURI3, dataSourceFactory, extractorsFactory, null, null);
            // Media sources in one list
            MediaSource concatenatingMediaSource = new ConcatenatingMediaSource(clMediaSource1, clMediaSource2, clMediaSource3);

            // Looping media source
            loopMediaSource = new LoopingMediaSource(clMediaSource3);
            // Clipping media source
            clipMediaSource = new ClippingMediaSource(clMediaSource1, 0, 10000000);
            // Merging media sources
            mergeMediaSource = new MergingMediaSource(concatenatingMediaSource);

            exoPlayerView.setPlayer(exoPlayer);
            RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radioButton1:
                            exoPlayer.prepare(clMediaSource2);
                            break;
                        case R.id.radioButton2:
                            exoPlayer.prepare(loopMediaSource);
                            break;
                        case R.id.radioButton3:
                            exoPlayer.prepare(clipMediaSource);
                            break;
                        case R.id.radioButton4:
                            exoPlayer.prepare(mergeMediaSource);
                            break;
                    }
                }
            });
            exoPlayer.setPlayWhenReady(true);

        } catch (Exception e) {
            Log.e("TAG", "Error : " + e.toString());
        }
    }
}