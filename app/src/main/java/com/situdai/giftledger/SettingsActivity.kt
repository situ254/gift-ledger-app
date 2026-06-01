package com.situdai.giftledger

import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebStorage
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val KEY_SERVER_URL = "server_url"
        const val DEFAULT_URL = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "服务器设置"

        val serverUrlInput = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.serverUrlInput)
        val saveButton = findViewById<com.google.android.material.button.MaterialButton>(R.id.saveButton)
        val resetButton = findViewById<com.google.android.material.button.MaterialButton>(R.id.resetButton)
        val clearCacheButton = findViewById<com.google.android.material.button.MaterialButton>(R.id.clearCacheButton)
        val testButton = findViewById<com.google.android.material.button.MaterialButton>(R.id.testButton)
        val currentConfigText = findViewById<android.widget.TextView>(R.id.currentConfigText)

        // 加载已保存的配置
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        val savedUrl = prefs.getString(KEY_SERVER_URL, DEFAULT_URL) ?: DEFAULT_URL
        if (savedUrl.isNotEmpty()) {
            serverUrlInput.setText(savedUrl)
        }
        updateConfigDisplay(currentConfigText, savedUrl)

        // 保存
        saveButton.setOnClickListener {
            val url = serverUrlInput.text.toString().trim()

            if (url.isEmpty()) {
                serverUrlInput.error = "请输入服务器地址"
                return@setOnClickListener
            }

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                serverUrlInput.error = "地址必须以 http:// 或 https:// 开头"
                return@setOnClickListener
            }

            // 简单 URL 格式验证
            if (!url.contains(".") && !url.contains("localhost")) {
                serverUrlInput.error = "请输入有效的服务器地址"
                return@setOnClickListener
            }

            // 移除末尾的斜杠
            val cleanUrl = url.trimEnd('/')

            prefs.edit().putString(KEY_SERVER_URL, cleanUrl).apply()
            Toast.makeText(this, "保存成功，即将返回主页面", Toast.LENGTH_SHORT).show()
            updateConfigDisplay(currentConfigText, cleanUrl)
            setResult(RESULT_OK)
            finish()
        }

        // 恢复默认
        resetButton.setOnClickListener {
            serverUrlInput.setText("")
            serverUrlInput.error = null
        }

        // 测试连接
        testButton.setOnClickListener {
            val url = serverUrlInput.text.toString().trim()
            if (url.isEmpty() || (!url.startsWith("http://") && !url.startsWith("https://"))) {
                serverUrlInput.error = "请先输入有效的服务器地址"
                return@setOnClickListener
            }
            testConnection(url)
        }

        // 清除缓存
        clearCacheButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("确认清除")
                .setMessage("将清除所有缓存、Cookie 和登录状态，确定继续吗？")
                .setPositiveButton("确定") { _, _ ->
                    val webView = android.webkit.WebView(this)
                    webView.clearCache(true)
                    webView.clearFormData()
                    webView.clearHistory()
                    webView.clearSslPreferences()
                    webView.destroy()

                    CookieManager.getInstance().removeAllCookies(null)
                    CookieManager.getInstance().flush()
                    WebStorage.getInstance().deleteAllData()

                    Toast.makeText(this, "缓存和 Cookie 已清除", Toast.LENGTH_SHORT).show()
                    updateConfigDisplay(currentConfigText, prefs.getString(KEY_SERVER_URL, "") ?: "")
                }
                .setNegativeButton("取消", null)
                .show()
        }
    }

    private fun testConnection(url: String) {
        Toast.makeText(this, "正在测试连接...", Toast.LENGTH_SHORT).show()
        Thread {
            try {
                val cleanUrl = url.trimEnd('/')
                val connection = java.net.URL(cleanUrl).openConnection() as java.net.HttpURLConnection
                connection.connectTimeout = 5000
                connection.readTimeout = 5000
                connection.requestMethod = "GET"
                val responseCode = connection.responseCode
                connection.disconnect()
                runOnUiThread {
                    if (responseCode in 200..399) {
                        Toast.makeText(this, "连接成功 (HTTP $responseCode)", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "服务器响应异常 (HTTP $responseCode)", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this, "连接失败：${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }.start()
    }

    private fun updateConfigDisplay(textView: android.widget.TextView, url: String) {
        val displayUrl = if (url.isEmpty()) "未配置" else url
        val status = if (url.isEmpty()) "⚠️ 需要配置" else "✅ 已配置"
        textView.text = "服务器地址：$displayUrl\n连接状态：$status"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
