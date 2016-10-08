package com.unity3d.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import java.io.FileInputStream;
import java.io.IOException;

/* renamed from: com.unity3d.player.w */
public final class C0843w extends FrameLayout implements OnBufferingUpdateListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, Callback, MediaPlayerControl {
    private static boolean f231a;
    private final UnityPlayer f232b;
    private final Context f233c;
    private final SurfaceView f234d;
    private final SurfaceHolder f235e;
    private final String f236f;
    private final int f237g;
    private final int f238h;
    private final boolean f239i;
    private final long f240j;
    private final long f241k;
    private final FrameLayout f242l;
    private final Display f243m;
    private int f244n;
    private int f245o;
    private int f246p;
    private int f247q;
    private MediaPlayer f248r;
    private MediaController f249s;
    private boolean f250t;
    private boolean f251u;
    private int f252v;
    private boolean f253w;
    private int f254x;
    private boolean f255y;

    /* renamed from: com.unity3d.player.w.1 */
    final class C08421 implements Runnable {
        final /* synthetic */ C0843w f230a;

        C08421(C0843w c0843w) {
            this.f230a = c0843w;
        }

        public final void run() {
            this.f230a.f232b.hideVideoPlayer();
        }
    }

    static {
        f231a = false;
    }

    protected C0843w(UnityPlayer unityPlayer, Context context, String str, int i, int i2, int i3, boolean z, long j, long j2) {
        super(context);
        this.f250t = false;
        this.f251u = false;
        this.f252v = 0;
        this.f253w = false;
        this.f254x = 0;
        this.f232b = unityPlayer;
        this.f233c = context;
        this.f242l = this;
        this.f234d = new SurfaceView(context);
        this.f235e = this.f234d.getHolder();
        this.f235e.addCallback(this);
        this.f235e.setType(3);
        this.f242l.setBackgroundColor(i);
        this.f242l.addView(this.f234d);
        this.f243m = ((WindowManager) this.f233c.getSystemService("window")).getDefaultDisplay();
        this.f236f = str;
        this.f237g = i2;
        this.f238h = i3;
        this.f239i = z;
        this.f240j = j;
        this.f241k = j2;
        if (f231a) {
            C0843w.m203a("fileName: " + this.f236f);
        }
        if (f231a) {
            C0843w.m203a("backgroundColor: " + i);
        }
        if (f231a) {
            C0843w.m203a("controlMode: " + this.f237g);
        }
        if (f231a) {
            C0843w.m203a("scalingMode: " + this.f238h);
        }
        if (f231a) {
            C0843w.m203a("isURL: " + this.f239i);
        }
        if (f231a) {
            C0843w.m203a("videoOffset: " + this.f240j);
        }
        if (f231a) {
            C0843w.m203a("videoLength: " + this.f241k);
        }
        setFocusable(true);
        setFocusableInTouchMode(true);
        this.f255y = true;
    }

    private void m202a() {
        doCleanUp();
        try {
            this.f248r = new MediaPlayer();
            if (this.f239i) {
                this.f248r.setDataSource(this.f233c, Uri.parse(this.f236f));
            } else if (this.f241k != 0) {
                FileInputStream fileInputStream = new FileInputStream(this.f236f);
                this.f248r.setDataSource(fileInputStream.getFD(), this.f240j, this.f241k);
                fileInputStream.close();
            } else {
                try {
                    AssetFileDescriptor openFd = getResources().getAssets().openFd(this.f236f);
                    this.f248r.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                    openFd.close();
                } catch (IOException e) {
                    FileInputStream fileInputStream2 = new FileInputStream(this.f236f);
                    this.f248r.setDataSource(fileInputStream2.getFD());
                    fileInputStream2.close();
                }
            }
            this.f248r.setDisplay(this.f235e);
            this.f248r.setScreenOnWhilePlaying(true);
            this.f248r.setOnBufferingUpdateListener(this);
            this.f248r.setOnCompletionListener(this);
            this.f248r.setOnPreparedListener(this);
            this.f248r.setOnVideoSizeChangedListener(this);
            this.f248r.setAudioStreamType(3);
            this.f248r.prepare();
            if (this.f237g == 0 || this.f237g == 1) {
                this.f249s = new MediaController(this.f233c);
                this.f249s.setMediaPlayer(this);
                this.f249s.setAnchorView(this);
                this.f249s.setEnabled(true);
                this.f249s.show();
            }
        } catch (Exception e2) {
            if (f231a) {
                C0843w.m203a("error: " + e2.getMessage() + e2);
            }
            onDestroy();
        }
    }

    private static void m203a(String str) {
        Log.v("Video", "VideoPlayer: " + str);
    }

    private void m204b() {
        if (!isPlaying()) {
            if (f231a) {
                C0843w.m203a("startVideoPlayback");
            }
            updateVideoLayout();
            if (!this.f253w) {
                start();
            }
        }
    }

    public final boolean canPause() {
        return true;
    }

    public final boolean canSeekBackward() {
        return true;
    }

    public final boolean canSeekForward() {
        return true;
    }

    protected final void doCleanUp() {
        if (this.f248r != null) {
            this.f248r.release();
            this.f248r = null;
        }
        this.f246p = 0;
        this.f247q = 0;
        this.f251u = false;
        this.f250t = false;
    }

    public final int getBufferPercentage() {
        return this.f239i ? this.f252v : 100;
    }

    public final int getCurrentPosition() {
        return this.f248r == null ? 0 : this.f248r.getCurrentPosition();
    }

    public final int getDuration() {
        return this.f248r == null ? 0 : this.f248r.getDuration();
    }

    public final boolean isPlaying() {
        boolean z = this.f251u && this.f250t;
        if (this.f248r == null) {
            if (z) {
                return false;
            }
        } else if (!this.f248r.isPlaying() && z) {
            return false;
        }
        return true;
    }

    public final void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        if (f231a) {
            C0843w.m203a("onBufferingUpdate percent:" + i);
        }
        this.f252v = i;
    }

    public final void onCompletion(MediaPlayer mediaPlayer) {
        if (f231a) {
            C0843w.m203a("onCompletion called");
        }
        onDestroy();
    }

    public final void onControllerHide() {
    }

    protected final void onDestroy() {
        onPause();
        doCleanUp();
        UnityPlayer.m74a(new C08421(this));
    }

    public final boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4 && (this.f237g != 2 || i == 0 || keyEvent.isSystem())) {
            return this.f249s != null ? this.f249s.onKeyDown(i, keyEvent) : super.onKeyDown(i, keyEvent);
        } else {
            onDestroy();
            return true;
        }
    }

    protected final void onPause() {
        if (f231a) {
            C0843w.m203a("onPause called");
        }
        if (!this.f253w) {
            pause();
            this.f253w = false;
        }
        if (this.f248r != null) {
            this.f254x = this.f248r.getCurrentPosition();
        }
        this.f255y = false;
    }

    public final void onPrepared(MediaPlayer mediaPlayer) {
        if (f231a) {
            C0843w.m203a("onPrepared called");
        }
        this.f251u = true;
        if (this.f251u && this.f250t) {
            m204b();
        }
    }

    protected final void onResume() {
        if (f231a) {
            C0843w.m203a("onResume called");
        }
        if (!(this.f255y || this.f253w)) {
            start();
        }
        this.f255y = true;
    }

    public final boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (this.f237g != 2 || (action & MotionEventCompat.ACTION_MASK) != 0) {
            return this.f249s != null ? this.f249s.onTouchEvent(motionEvent) : super.onTouchEvent(motionEvent);
        } else {
            onDestroy();
            return true;
        }
    }

    public final void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        if (f231a) {
            C0843w.m203a("onVideoSizeChanged called " + i + "x" + i2);
        }
        if (i != 0 && i2 != 0) {
            this.f250t = true;
            this.f246p = i;
            this.f247q = i2;
            if (this.f251u && this.f250t) {
                m204b();
            }
        } else if (f231a) {
            C0843w.m203a("invalid video width(" + i + ") or height(" + i2 + ")");
        }
    }

    public final void pause() {
        if (this.f248r != null) {
            this.f248r.pause();
            this.f253w = true;
        }
    }

    public final void seekTo(int i) {
        if (this.f248r != null) {
            this.f248r.seekTo(i);
        }
    }

    public final void start() {
        if (this.f248r != null) {
            this.f248r.start();
            this.f253w = false;
        }
    }

    public final void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        if (f231a) {
            C0843w.m203a("surfaceChanged called " + i + " " + i2 + "x" + i3);
        }
        if (this.f244n != i2 || this.f245o != i3) {
            this.f244n = i2;
            this.f245o = i3;
            updateVideoLayout();
        }
    }

    public final void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (f231a) {
            C0843w.m203a("surfaceCreated called");
        }
        m202a();
        seekTo(this.f254x);
    }

    public final void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (f231a) {
            C0843w.m203a("surfaceDestroyed called");
        }
        doCleanUp();
    }

    protected final void updateVideoLayout() {
        if (f231a) {
            C0843w.m203a("updateVideoLayout");
        }
        if (this.f244n == 0 || this.f245o == 0) {
            WindowManager windowManager = (WindowManager) this.f233c.getSystemService("window");
            this.f244n = windowManager.getDefaultDisplay().getWidth();
            this.f245o = windowManager.getDefaultDisplay().getHeight();
        }
        int i = this.f244n;
        int i2 = this.f245o;
        float f = ((float) this.f246p) / ((float) this.f247q);
        float f2 = ((float) this.f244n) / ((float) this.f245o);
        if (this.f238h == 1) {
            if (f2 <= f) {
                i2 = (int) (((float) this.f244n) / f);
            } else {
                i = (int) (((float) this.f245o) * f);
            }
        } else if (this.f238h == 2) {
            if (f2 >= f) {
                i2 = (int) (((float) this.f244n) / f);
            } else {
                i = (int) (((float) this.f245o) * f);
            }
        } else if (this.f238h == 0) {
            i = this.f246p;
            i2 = this.f247q;
        }
        if (f231a) {
            C0843w.m203a("frameWidth = " + i + "; frameHeight = " + i2);
        }
        this.f242l.updateViewLayout(this.f234d, new LayoutParams(i, i2, 17));
    }
}
