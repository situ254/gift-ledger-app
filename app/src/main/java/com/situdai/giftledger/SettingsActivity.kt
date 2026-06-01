package com.situdai.giftledger

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var serverUrlInput: EditText
    private lateinit var saveButton: Button
    private lateinit var resetButton: Button
    private lateinit var clearCacheButton: Button
    private lateinit var currentConfigText: TextView
    
    private val PREFS_NAME = "GiftLedgerPrefs"
    private val KEY_SERVER_URL = "server_url"
    private val DEFAULT_URL = "http://你的服务器IP:9205"
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        initViews()
        loadCurrentSettings()
        setupListeners()
        updateCurrentConfig()
    }
    
    private fun initViews() {
        serverUrlInput = findViewById(R.id.serverUrlInput)
        saveButton = findViewById(R.id.saveButton)
        resetButton = findViewById(R.id.resetButton)
        clearCacheButton = findViewById(R.id.clearCacheButton)
        currentConfigText = findViewById(R.id.currentConfigText)
    }
    
    private fun loadCurrentSettings() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val savedUrl = prefs.getString(KEY_SERVER_URL, DEFAULT_URL) ?: DEFAULT_URL
        serverUrlInput.setText(savedUrl)
    }
    
    private fun setupListeners() {
        saveButton.setOnClickListener {
            saveSettings()
        }
        
        resetButton.setOnClickListener {
            resetToDefault()
        }
        
        clearCacheButton.setOnClickListener {
            clearCache()
        }
    }
    
    private fun saveSettings() {
        val url = serverUrlInput.text.toString().trim()
        
        if (url.isEmpty()) {
            Toast.makeText(this, "请输入服务器地址", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            Toast.makeText(this, "地址必须以 http:// 或 https:// 开头", Toast.LENGTH_SHORT).show()
            return
        }
        
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putString(KEY_SERVER_URL, url)
        editor.apply()
        
        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show()
        updateCurrentConfig()
        finish() // 返回主界面并重新加载
    }
    
    private fun resetToDefault() {
        serverUrlInput.setText(DEFAULT_URL)
        Toast.makeText(this, "已恢复默认设置", Toast.LENGTH_SHORT).show()
    }
    
    private fun clearCache() {
        // 清除 WebView 缓存
        val webView = android.webkit.WebView(this)
        webView.clearCache(true)
        webView.clearFormData()
        webView.clearHistory()
        webView.clearSslPreferences()
        
        // 清除 Cookie
        val cookieManager = CookieManager.getInstance()
        cookieManager.removeAllCookies(null)
        cookieManager.flush()
        
        // 清除 WebStorage
        WebStorage.getInstance().deleteAllData()
        
        Toast.makeText(this, "缓存和 Cookie 已清除", Toast.LENGTH_SHORT).show()
        updateCurrentConfig()
    }
    
    private fun updateCurrentConfig() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val savedUrl = prefs.getString(KEY_SERVER_URL, DEFAULT_URL) ?: DEFAULT_URL
        
        val configText = """
            服务器地址：$savedUrl
            
            缓存状态：已启用
            Cookie 状态：已启用
        """.trimIndent()
        
        currentConfigText.text = configText
    }
}
