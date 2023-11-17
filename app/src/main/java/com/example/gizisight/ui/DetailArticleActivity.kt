package com.example.gizisight.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.gizisight.R
import com.example.gizisight.databinding.ActivityDataDiriBinding
import com.example.gizisight.databinding.ActivityDetailArticleBinding

class DetailArticleActivity : AppCompatActivity() {
    private var _binding: ActivityDetailArticleBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailArticleBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.webView?.webViewClient = WebViewClient()


        val link = intent.getStringExtra("link")

        if (!link.isNullOrEmpty()) {
            binding?.webView?.loadUrl(link)

        }
        // this will enable the javascript settings, it can also allow xss vulnerabilities
        binding?.webView?.settings?.javaScriptEnabled = true

        // if you want to enable zoom feature
        binding?.webView?.settings?.setSupportZoom(true)

    }

    override fun onBackPressed() {
        if (binding?.webView!!.canGoBack())
            binding?.webView!!.goBack()
        // if your webview cannot go back
        // it will exit the application
        else
            super.onBackPressed()
    }
}