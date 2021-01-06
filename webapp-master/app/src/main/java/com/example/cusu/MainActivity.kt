package com.example.cusu

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity: AppCompatActivity() {

    var ShowOrHideWebViewInitialUse = "show"
    private var myWebView: WebView? = null
    private var myUrl = "https://www.google.com/search?q=lion"
    private var oldScroll = 0

    var fav: Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)



        setContentView(R.layout.activity_main)

        myWebView = findViewById(R.id.web_view)



        try {

            myWebView!!.webViewClient = object: CustomWebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    // Pour rescroll où on était
                    oldScroll = myWebView!!.scrollY
                    // Ouvre l'app tel
                    if (url!!.startsWith("tel:")) {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse(url))
                        startActivity(intent)
                        view!!.reload()
                        return true
                    }
                    else if (url.contains("fb://page/")) {
                        intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(url)
                        startActivity(intent)

                        return true
                    }


                    view?.loadUrl(url)
                    return true
                }


            }

            myWebView!!.settings.javaScriptEnabled = true
            myWebView!!.settings.domStorageEnabled = true
            myWebView!!.overScrollMode = View.OVER_SCROLL_NEVER
            myWebView!!.loadUrl(myUrl)

        } catch(e: Exception) {
            Log.d("BrowserActivity", e.toString())
        }

    }


    open class CustomWebViewClient : WebViewClient() {

        override fun onPageStarted(webview: WebView, url: String, favicon: Bitmap?) {
            webview.visibility = View.INVISIBLE
        }

        override fun onPageFinished(view: WebView, url: String) {
            view.visibility = View.VISIBLE
            super.onPageFinished(view, url)
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        // Permet de retourner à la page précédente sans quitter l'activité
        if (event.getAction() === KeyEvent.ACTION_DOWN) {
            when (keyCode) {
                KEYCODE_BACK -> {
                    if (myWebView!!.canGoBack()) {
                        myWebView!!.goBack()
                        myWebView!!.scrollY = oldScroll
                    } else {
                        finish()
                    }
                    return true
                }
            }

        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}