package com.situdai.giftledger

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar
    private lateinit var errorLayout: LinearLayout
    private lateinit var errorTitle: TextView
    private lateinit var errorMessage: TextView
    private lateinit var retryButton: Button
    private lateinit var settingsButton: Button
    
    private val PREFS_NAME = "GiftLedgerPrefs"
    private val KEY_SERVER_URL = "server_url"
    private val DEFAULT_URL = "http://你的服务器IP:9205"
    
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        initViews()
        setupWebView()
        loadUrl()
        setupListeners()
    }
    
    private fun initViews() {
        webView = findViewById(R.id.webView)
        progressBar = findViewById(R.id.progressBar)
        errorLayout = findViewById(R.id.errorLayout)
        errorTitle = findViewById(R.id.errorTitle)
        errorMessage = findViewById(R.id.errorMessage)
        retryButton = findViewById(R.id.retryButton)
        settingsButton = findViewById(R.id.settingsButton)
    }
    
    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        val webSettings = webView.settings
        
        // 启用 JavaScript（重要！）
        webSettings.javaScriptEnabled = true
        
        // 设置 WebView 支持的特性
        webSettings.domStorageEnabled = true  // 启用 DOM Storage（LocalStorage）
        webSettings.databaseEnabled = true     // 启用数据库
        webSettings.setAppCacheEnabled(true)  // 启用 App Cache
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        
        // 自适应屏幕
        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        
        // 其他设置
        webSettings.allowFileAccess = true
        webSettings.allowContentAccess = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        
        // 配置 Cookie 管理器（保持登录状态）
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.setAcceptThirdPartyCookies(webView, true)
        
        // 设置 User-Agent（可选，模拟移动浏览器）
        val userAgent = webSettings.userAgentString
        webSettings.userAgentString = "$userAgent GiftLedgerApp/1.0"
        
        // WebView 客户端
        webView.webViewClient = object : WebViewClient() {
            
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                // 在 WebView 内打开所有链接
                return false
            }
            
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = ProgressBar.VISIBLE
                errorLayout.visibility = LinearLayout.GONE
            }
            
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = ProgressBar.GONE
            }
            
            @Deprecated("Deprecated in Java")
            override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                super.onReceivedError(view, errorCode, description, failingUrl)
                showError("网络连接失败", "无法加载页面：$description")
            }
            
            override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                // 开发环境可能使用自签名证书，这里选择继续（仅开发用）
                handler?.proceed()
            }
        }
        
        // WebChromeClient 用于处理进度、标题等
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                progressBar.visibility = if (newProgress == 100) ProgressBar.GONE else ProgressBar.VISIBLE
            }
        }
    }
    
    private fun loadUrl() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val serverUrl = prefs.getString(KEY_SERVER_URL, DEFAULT_URL) ?: DEFAULT_URL
        
        if (serverUrl.isNotEmpty() && serverUrl != "http://你的服务器IP:9205") {
            webView.loadUrl(serverUrl)
        } else {
            showError("未配置服务器地址", "请点击右上角菜单，设置服务器地址")
        }
    }
    
    private fun showError(title: String, message: String) {
        webView.visibility = WebView.GONE
        errorLayout.visibility = LinearLayout.VISIBLE
        errorTitle.text = title
        errorMessage.text = message
    }
    
    private fun hideError() {
        errorLayout.visibility = LinearLayout.GONE
        webView.visibility = WebView.VISIBLE
    }
    
    private fun setupListeners() {
        retryButton.setOnClickListener {
            hideError()
            loadUrl()
        }
        
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
    
    override fun onCreateOptionsMenu(menu: android.view.Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_refresh -> {
                webView.reload()
                true
            }
            R.id.action_clear_cache -> {
                webView.clearCache(true)
                CookieManager.getInstance().removeAllCookies(null)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }
    
    override fun onResume() {
        super.onResume()
        // 从设置页面返回时，重新加载 URL
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val currentUrl = webView.url
        val savedUrl = prefs.getString(KEY_SERVER_URL, DEFAULT_URL) ?: DEFAULT_URL
        
        if (currentUrl != savedUrl && savedUrl.isNotEmpty()) {
            loadUrl()
        }
    }
}
