package de.custom.firetvlauncher

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var webView: WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            android.view.View.SYSTEM_UI_FLAG_FULLSCREEN or
            android.view.View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            android.view.View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY

        webView = WebView(this).apply {
            setBackgroundColor(android.graphics.Color.rgb(8, 11, 19))
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            addJavascriptInterface(FireTvBridge(), "FireTV")
            isFocusable = true
            isFocusableInTouchMode = true
            requestFocus()
            loadUrl("file:///android_asset/index.html")
        }
        setContentView(webView)
    }

    inner class FireTvBridge {
        @JavascriptInterface
        fun launchApp(appName: String): Boolean {
            return try {
                val intents = listOf(
                    Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER),
                    Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)
                )
                val matches = intents.flatMap { intent ->
                    packageManager.queryIntentActivities(intent, 0)
                }
                val match = matches.firstOrNull {
                    it.loadLabel(packageManager).toString().equals(appName, ignoreCase = true)
                } ?: matches.firstOrNull {
                    it.loadLabel(packageManager).toString().contains(appName, ignoreCase = true)
                }
                val launchIntent = match?.activityInfo?.let {
                    packageManager.getLaunchIntentForPackage(it.packageName)
                        ?: Intent(Intent.ACTION_MAIN).apply {
                            component = android.content.ComponentName(it.packageName, it.name)
                            addCategory(Intent.CATEGORY_LAUNCHER)
                        }
                }
                if (launchIntent != null) {
                    runOnUiThread { startActivity(launchIntent) }
                    true
                } else {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "App nicht gefunden: $appName", Toast.LENGTH_SHORT).show()
                    }
                    false
                }
            } catch (_: Exception) {
                false
            }
        }
    }

    override fun onBackPressed() {
        if (::webView.isInitialized) {
            webView.evaluateJavascript("window.history.back()", null)
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        if (::webView.isInitialized) webView.destroy()
        super.onDestroy()
    }
}
