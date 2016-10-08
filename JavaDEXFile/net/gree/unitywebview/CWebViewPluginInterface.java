package net.gree.unitywebview;

import android.webkit.JavascriptInterface;
import com.unity3d.player.UnityPlayer;

class CWebViewPluginInterface {
    private String mGameObject;
    private CWebViewPlugin mPlugin;

    /* renamed from: net.gree.unitywebview.CWebViewPluginInterface.1 */
    class C11661 implements Runnable {
        final /* synthetic */ String val$message;
        final /* synthetic */ String val$method;

        C11661(String str, String str2) {
            this.val$method = str;
            this.val$message = str2;
        }

        public void run() {
            if (CWebViewPluginInterface.this.mPlugin.IsInitialized()) {
                UnityPlayer.UnitySendMessage(CWebViewPluginInterface.this.mGameObject, this.val$method, this.val$message);
            }
        }
    }

    public CWebViewPluginInterface(CWebViewPlugin cWebViewPlugin, String str) {
        this.mPlugin = cWebViewPlugin;
        this.mGameObject = str;
    }

    @JavascriptInterface
    public void call(String str) {
        call("CallFromJS", str);
    }

    public void call(String str, String str2) {
        UnityPlayer.currentActivity.runOnUiThread(new C11661(str, str2));
    }
}
