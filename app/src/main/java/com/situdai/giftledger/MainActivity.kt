package com.situdai.giftledger

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class MainActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorLayout: android.widget.LinearLayout
    private lateinit var errorTitle: TextView
    private lateinit var errorMessage: TextView

    companion object {
        const val KEY_SERVER_URL = "server_url"
        const val DEFAULT_URL = ""
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
        setupWebView()

        if (!isServerConfigured()) {
            showSetupGuide()
        } else {
            loadUrl()
        }
    }

    private fun initViews() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        errorLayout = findViewById(R.id.errorLayout)
        errorTitle = findViewById(R.id.errorTitle)
        errorMessage = findViewById(R.id.errorMessage)

        // 绑定 errorLayout 中的按钮
        val retryBtn = findViewById<Button>(R.id.retryButton)
        val settingsBtn = findViewById<Button>(R.id.settingsButton)
        retryBtn?.setOnClickListener { hideError(); loadUrl() }
        settingsBtn?.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun isServerConfigured(): Boolean {
        val url = getSavedUrl()
        return url.isNotEmpty()
    }

    private fun getSavedUrl(): String {
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        return prefs.getString(KEY_SERVER_URL, DEFAULT_URL) ?: DEFAULT_URL
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val webSettings = webView.settings

        // JavaScript
        webSettings.javaScriptEnabled = true

        // 存储 & 缓存
        webSettings.domStorageEnabled = true
        webSettings.databaseEnabled = true
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT

        // 自适应屏幕
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false

        // 文件访问（保留安全限制）
        webSettings.allowFileAccess = false
        webSettings.allowContentAccess = false

        // 【关键修复】允许混合内容：HTTP 页面中的 HTTP API 请求
        // 因为用户的服务器通常是纯 HTTP 部署，需要允许同源 HTTP 请求
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

        // Cookie 持久化
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)

        // User-Agent
        val userAgent = webSettings.userAgentString
        webSettings.userAgentString = "$userAgent GiftLedgerApp/2.0"

        // WebViewClient
        webView.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val url = request?.url?.toString() ?: return false
                // 只允许 http/https 协议，阻止其他协议跳转
                return !url.startsWith("http://") && !url.startsWith("https://")
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = ProgressBar.VISIBLE
                errorLayout.visibility = android.widget.LinearLayout.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = ProgressBar.GONE
                // 确保 Cookie 持久化
                CookieManager.getInstance().flush()
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                if (request?.isForMainFrame == true) {
                    showError("加载失败", "无法连接服务器，请检查地址配置和网络连接")
                }
            }

            override fun onReceivedHttpError(
                view: WebView?,
                request: WebResourceRequest?,
                errorResponse: WebResourceResponse?
            ) {
                super.onReceivedHttpError(view, request, errorResponse)
                if (request?.isForMainFrame == true && errorResponse?.statusCode == 401) {
                    // JWT 过期，让前端自行处理登录跳转
                }
            }

            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler?,
                error: SslError?
            ) {
                // 【关键修复】内网/自签名证书场景下允许继续加载
                // 用户自行配置的服务器地址，信任其证书
                val url = view?.url ?: ""
                val savedUrl = getSavedUrl()
                if (url.startsWith(savedUrl) || savedUrl.isEmpty()) {
                    handler?.proceed()
                } else {
                    // 非用户配置的地址出现 SSL 错误，仍然拒绝
                    showError("SSL 错误", "服务器证书验证失败：${error?.toString()?.substringBefore('\n')}")
                    handler?.cancel()
                }
            }
        }

        // WebChromeClient
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress == 100) {
                    progressBar.visibility = ProgressBar.GONE
                }
            }
        }
    }

    private fun loadUrl() {
        val serverUrl = getSavedUrl()
        if (serverUrl.isNotEmpty()) {
            hideError()
            webView.loadUrl(serverUrl)
        } else {
            showSetupGuide()
        }
    }

    private fun showSetupGuide() {
        showError("欢迎使用人情记账", "首次使用需配置服务器地址，请点击下方按钮进入设置")
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun showError(title: String, message: String) {
        webView.visibility = WebView.GONE
        progressBar.visibility = ProgressBar.GONE
        errorLayout.visibility = android.widget.LinearLayout.VISIBLE
        errorTitle.text = title
        errorMessage.text = message
    }

    private fun hideError() {
        errorLayout.visibility = android.widget.LinearLayout.GONE
        webView.visibility = WebView.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_refresh -> {
                if (isServerConfigured()) webView.reload()
                else showSetupGuide()
                true
            }
            R.id.action_clear_cache -> {
                webView.clearCache(true)
                CookieManager.getInstance().removeAllCookies(null)
                CookieManager.getInstance().flush()
                Toast.makeText(this, "缓存已清除", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // 处理返回键：优先 WebView 后退
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        super.onPause()
        CookieManager.getInstance().flush()
    }

    override fun onResume() {
        super.onResume()
        // 从设置页返回时，如果 URL 变更则重新加载
        val savedUrl = getSavedUrl()
        val currentUrl = webView.url
        if (savedUrl.isNotEmpty() && currentUrl != savedUrl) {
            loadUrl()
        }
    }

    override fun onDestroy() {
        webView.destroy()
        super.onDestroy()
    }
}
