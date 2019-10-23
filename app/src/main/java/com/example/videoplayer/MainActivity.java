package com.example.videoplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.videoplayer.player.PlayerView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by weizheng.huang on 2019-09-25.
 */
public class MainActivity extends Activity {
    private String videoPath;
    private PlayerView mPlayView;
    private VideoView systemVideoView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (verifyPermission(this)) {
            initData();
            initMPlayer();
            initSystemPlayer();
        }
    }
    private boolean verifyPermission(Activity activity){
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,PERMISSIONS_STORAGE,1);
        }
        return permission==PackageManager.PERMISSION_GRANTED;
    }
    private void initSystemPlayer() {
        systemVideoView = findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.show();
        systemVideoView.setMediaController(mediaController);
        systemVideoView.setVideoURI(Uri.fromFile(new File(videoPath)));
        Log.d("tag","initSystemPlayer");
    }
    private void initMPlayer() {
        mPlayView = findViewById(R.id.mPlayerView);

        mPlayView.setVideoFilePath(videoPath);
        Log.d("tag","initMPlayer");
    }
    private void initData() {
        File dir = getFilesDir();

        File path = new File(dir, "shape.mp4");

        final BufferedInputStream in = new BufferedInputStream(getResources().openRawResource(R.raw.shape_of_my_heart));
        final BufferedOutputStream out;
        try {
            out = new BufferedOutputStream(openFileOutput(path.getName(), Context.MODE_PRIVATE));
            byte[] buf = new byte[1024];
            int size = in.read(buf);

            while (size > 0) {
                out.write(buf, 0, size);
                size = in.read(buf);
            }
            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoPath = path.toString();
        Log.d("tag","initData");
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayView != null)
            mPlayView.pause();
        if (systemVideoView != null)
            systemVideoView.pause();
    }
}
