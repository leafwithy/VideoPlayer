package com.example.videoplayer.player;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;

/**
 * Created by weizheng.huang on 2019-09-25.
 */
public class PlayerView extends SurfaceView implements MediaController.MediaPlayerControl,IPlayerCallBack {
    private double aspectRadio;
    private MediaController mMediaController;
    private VideoPlayer mVideoPlayer;

    public PlayerView(Context context) {
        super(context);

    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mVideoPlayer  = new VideoPlayer(this.getHolder().getSurface());
        mVideoPlayer.setCallBack(this);
        mMediaController = new MediaController(getContext());
        mMediaController.setMediaPlayer(this);
    }

    private void attackMediaController(){
        View anchorView = this.getParent() instanceof  View?(View)this.getParent():this;
        mMediaController.setAnchorView(anchorView);
        mMediaController.setEnabled(true);
    }

    public MediaController getmMediaController()
    {
        return mMediaController;
    }
    public void setVideoFilePath(String videoFilePath){

        mVideoPlayer.setFilePath(videoFilePath);
    }
    public void setAspect(double aspect){
        if(aspect>0){
            this.aspectRadio=aspect;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (aspectRadio > 0) {
            int initialWidth = MeasureSpec.getSize(widthMeasureSpec);
            int initialHeight = MeasureSpec.getSize(heightMeasureSpec);

            final int horizPadding = getPaddingLeft() + getPaddingRight();
            final int vertPadding = getPaddingTop() + getPaddingBottom();
            initialWidth -= horizPadding;
            initialHeight -= vertPadding;

            final double viewAspectRatio = (double) initialWidth / initialHeight;
            final double aspectDiff = aspectRadio / viewAspectRatio - 1;

            if (Math.abs(aspectDiff) > 0.01) {
                if (aspectDiff > 0) {
                    initialHeight = (int) (initialWidth / aspectRadio);
                } else {
                    initialWidth = (int) (initialHeight * aspectRadio);
                }
                initialWidth += horizPadding;
                initialHeight += vertPadding;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(initialWidth, MeasureSpec.EXACTLY);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(initialHeight, MeasureSpec.EXACTLY);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    @Override
    public void start() {
        mVideoPlayer.play();
    }

    @Override
    public void pause() {
        mVideoPlayer.stop();
    }

    @Override
    public int getDuration() {
        return 0;
    }

    @Override
    public int getCurrentPosition() {
        return 0;
    }

    @Override
    public void seekTo(int pos) {
    }

    @Override
    public boolean isPlaying() {
        return mVideoPlayer.isPlaying();
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 1;
    }

    @Override
    public void videoAspect(final int width, final int height, float time) {
        post(new Runnable() {
            @Override
            public void run() {
                setAspect((float) width/height);
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attackMediaController();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN){
            if(mMediaController!=null){
                if(!mMediaController.isShowing()){
                    mMediaController.show();
                }else{
                    mMediaController.hide();
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
