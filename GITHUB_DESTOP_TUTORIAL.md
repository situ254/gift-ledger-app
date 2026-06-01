# GitHub Desktop 详细使用教程（图文说明）

## 📱 前言

本教程将手把手教你使用 **GitHub Desktop** 将项目上传到 GitHub，并触发自动构建生成 APK。

**预计耗时**：10-15 分钟

---

## 📥 第一步：下载安装 GitHub Desktop

### 1.1 下载 GitHub Desktop

1. 打开浏览器，访问：https://desktop.github.com/
2. 点击绿色的 **Download for Windows** 按钮
3. 下载文件：`GitHubDesktopSetup.exe`（约 150MB）

### 1.2 安装

1. 双击运行 `GitHubDesktopSetup.exe`
2. 等待安装完成（自动进行）
3. 安装完成后，GitHub Desktop 会自动启动

### 1.3 登录 GitHub 账号

**界面说明**：
- 首次启动会显示欢迎界面
- 左侧有 GitHub 的章鱼猫咪图标
- 右侧有两个按钮：
  - **Sign in to GitHub.com**（登录已有账号）
  - **Create your free account**（创建新账号）

**操作步骤**：
1. 点击 **Sign in to GitHub.com**
2. 浏览器会自动打开 GitHub 登录页面
3. 输入你的 GitHub **用户名** 和 **密码**
4. 点击 **Sign in**
5. 授权页面点击 **Authorize GitHub Desktop**
6. 返回 GitHub Desktop，显示登录成功

---

## 📁 第二步：添加本地仓库

### 2.1 打开 Add Local Repository 对话框

**方法一：菜单操作**
1. 在 GitHub Desktop 主界面
2. 点击顶部菜单 **File**
3. 选择 **Add Local Repository...**

**方法二：快捷键**
- 按 `Ctrl+Shift+O`

**方法三：欢迎界面**
- 如果看不到主界面，点击 **Add an Existing Repository from your Hard Drive...**

### 2.2 选择项目文件夹

**界面说明**：
- 弹出 **Add Local Repository** 对话框
- 有一个文本框 **Local path**
- 右侧有一个 **Choose...** 按钮

**操作步骤**：
1. 点击 **Choose...** 按钮
2. 浏览到项目目录：
   ```
   C:\Users\Situdai\WorkBuddy\2026-06-01-10-23-39\gift-ledger-app
   ```
3. 选中 `gift-ledger-app` 文件夹
4. 点击 **Select Folder**

### 2.3 初始化仓库（如果提示）

**情况 A：文件夹已是 Git 仓库**
- 直接跳到第三步

**情况 B：文件夹不是 Git 仓库**
- 界面显示：**This directory does not appear to be a Git repository**
- 下方有两个按钮：
  - **Add repository** （添加现有仓库）
  - **Create a repository** （创建新仓库）

**操作步骤**：
1. 因为我们已经用 Git 初始化过，应该选择 **Add repository**
2. 如果报错，我们手动处理：
   - 关闭对话框
   - 打开命令提示符（CMD）
   - 执行：
     ```bash
     cd C:\Users\Situdai\WorkBuddy\2026-06-01-10-23-39\gift-ledger-app
     git init
     git add .
     git commit -m "Initial commit"
     ```
   - 然后重新添加仓库

---

## 🚀 第三步：发布仓库到 GitHub

### 3.1 点击 Publish repository

**界面说明**：
- 添加成功后，主界面会显示仓库信息
- 顶部工具栏有一个按钮：**Publish repository**（蓝色的，在右上角）
- 或者显示：**Create Pull Request** 和 **Publish branch**

**操作步骤**：
1. 点击右上角的 **Publish repository** 按钮

### 3.2 填写仓库信息

**界面说明**：
- 弹出 **Publish repository** 对话框
- 包含以下字段：
  - **Name***（必填）：仓库名称
  - **Description**（可选）：仓库描述
  - **Keep this code private**（复选框）：是否私有
  - **Organization**（下拉框）：组织（一般不用管）

**填写内容**：
1. **Name**：输入 `gift-ledger-app`
2. **Description**：输入 `人情记账 Android App - WebView 套壳应用`
3. **Keep this code private**：
   - 勾选 = 私有仓库（只有你能看到）
   - 不勾选 = 公开仓库（任何人都能看到）
   - **建议**：不勾选（GitHub Actions 对公开仓库免费）

4. 点击 **Publish repository** 按钮

### 3.3 等待上传

**界面说明**：
- 主界面顶部会显示进度条
- 右下角显示：**Publishing repository...**
- 文件列表会显示正在上传的文件

**预计时间**：1-5 分钟（取决于网速）

**成功后**：
- 按钮变为：**View on GitHub**（在 GitHub 上查看）
- 顶部显示：**Current repository: gift-ledger-app**
- 右上角显示分支：**main** 或 **master**

---

## 🔨 第四步：查看自动构建状态

### 4.1 打开 GitHub 仓库页面

**操作步骤**：
1. 在 GitHub Desktop 中，点击右上角的 **View on GitHub** 按钮
2. 浏览器会自动打开你的仓库页面：
   ```
   https://github.com/situ254/gift-ledger-app
   ```

### 4.2 查看 Actions 标签

**界面说明**：
- 仓库页面顶部有一排标签：
  - **Code**（代码）
  - **Issues**（问题）
  - **Pull requests**（合并请求）
  - **Actions**（自动化工作流）← **点击这里**
  - **Projects**（项目）
  - **Wiki**（维基）
  - **Security**（安全）
  - **Settings**（设置）

**操作步骤**：
1. 点击 **Actions** 标签
2. 你会看到工作流列表

### 4.3 查看构建工作流

**界面说明**：
- 左侧列表显示所有工作流
- 右侧显示工作流运行记录
- 第一条应该叫做：**Build Android APK**

**情况 A：工作流正在运行**
- 工作流名称左侧有一个 **黄色圆点** 🟡
- 或者显示：**build (push) — in progress**
- 点击工作流名称，查看详细日志

**情况 B：工作流成功完成**
- 工作流名称左侧有一个 **绿色勾号** ✅
- 显示：**build (push) — 2 minutes ago**

**情况 C：工作流失败**
- 工作流名称左侧有一个 **红色叉号** ❌
- 显示：**build (push) — 2 minutes ago**
- 点击查看错误日志

### 4.4 等待构建完成

**预计时间**：首次构建需要 **10-30 分钟**

**原因**：
- GitHub Actions 需要下载 Gradle
- 需要下载 Android SDK
- 需要下载各种依赖库

**你可以做的是**：
- 点击工作流名称，查看实时日志
- 或者最小化浏览器，去喝杯茶 ☕

---

## 📥 第五步：下载 APK

### 5.1 进入工作流详情页

**操作步骤**：
1. 构建完成后（绿色勾号 ✅）
2. 点击工作流名称：**Build Android APK**
3. 进入工作流运行详情页

### 5.2 下载 Artifacts

**界面说明**：
- 页面顶部显示：**Build Android APK — build succeeded**
- 下方有标签页：
  - **Summary**（摘要）← **当前在这里**
  - **Jobs**（任务）
  - **Checks**（检查）

**在 Summary 页面**：
- 滚动到页面底部
- 找到 **Artifacts** 部分（在右侧或底部）
- 显示：
  - **gift-ledger-app-debug** (app-debug.apk)
  - 可能还有：**gift-ledger-app-release** (如果构建成功)

**操作步骤**：
1. 点击 **gift-ledger-app-debug**
2. 浏览器自动下载 ZIP 文件：`gift-ledger-app-debug.zip`

### 5.3 解压并获取 APK

**操作步骤**：
1. 找到下载的 ZIP 文件（通常在 `下载` 文件夹）
2. 右键点击 → **解压全部...**
3. 解压后得到文件夹：`gift-ledger-app-debug`
4. 打开文件夹，找到：**app-debug.apk** 🎉

---

## 📱 第六步：安装 APK 到手机

### 6.1 方法一：USB 安装（推荐）

**前提条件**：
- Android 手机一台
- USB 数据线一根

**操作步骤**：
1. **启用开发者选项**
   - 打开手机 **设置**
   - 找到 **关于手机** / **About phone**
   - 找到 **版本号** / **Build number**
   - **连续点击 7 次**
   - 提示：**您已进入开发者模式！**

2. **启用 USB 调试**
   - 返回设置主界面
   - 找到 **开发者选项** / **Developer options**
   - 找到 **USB 调试** / **USB debugging**
   - 打开开关
   - 提示：点击 **确定**

3. **连接手机到电脑**
   - 用 USB 数据线连接手机和电脑
   - 手机上弹出：**允许 USB 调试吗？**
   - 勾选 **一律允许使用这台计算机**
   - 点击 **允许**

4. **安装 APK**
   - 在电脑上，双击 `app-debug.apk`
   - 或者使用命令行：
     ```bash
     adb install app-debug.apk
     ```
   - 手机上会显示：**安装来自未知来源的应用？**
   - 点击 **继续** → **安装**

### 6.2 方法二：手动安装（无需电脑）

**操作步骤**：
1. 将 `app-debug.apk` 复制到手机存储（通过 QQ、微信、网盘等）
2. 在手机上，找到 APK 文件
3. 点击安装
4. 如果提示：**禁止安装未知来源应用**
   - 点击 **设置**
   - 启用 **允许来自此来源应用**
   - 返回并继续安装

---

## 🎯 第七步：配置并使用 App

### 7.1 首次打开 App

**操作步骤**：
1. 安装完成后，点击 **打开**
2. 或者到手机桌面，找到 **人情记账** 图标，点击打开

**预期结果**：
- 显示错误页面：**无法连接服务器**
- 有两个按钮：**重试** 和 **设置**

### 7.2 配置服务器地址

**操作步骤**：
1. 点击 **设置** 按钮
2. 进入设置页面
3. 在 **服务器地址** 输入框中，输入你的服务器地址：
   ```
   http://你的服务器IP:9205
   ```
   - 例如：`http://192.168.1.100:9205`
   - 例如：`http://10.0.0.5:9205`

4. 点击 **保存** 按钮
5. 提示：**保存成功**
6. 自动返回主界面，并加载你的 Web 应用

### 7.3 登录并使用

**操作步骤**：
1. App 加载完成后，显示你的 Web 登录页面
2. 输入用户名和密码
3. 点击登录
4. 登录成功后，关闭 App 再重新打开
5. **验证**：登录状态应该被保持（不需要重新登录）

---

## ❓ 常见问题排查

### 问题 1：GitHub Desktop 显示 "Authentication failed"

**原因**：GitHub 账号登录失败

**解决方法**：
1. 在 GitHub Desktop 中，点击 **File** → **Options** → **Accounts**
2. 点击 **Sign out**
3. 重新点击 **Sign in to GitHub.com**
4. 重新授权

### 问题 2：推送代码时显示 "Repository not found"

**原因**：仓库名称填错，或者没有创建仓库

**解决方法**：
1. 访问：https://github.com/situ254/gift-ledger-app
2. 如果显示 404，说明仓库不存在
3. 需要在 GitHub 上手动创建仓库：
   - 访问：https://github.com/new
   - 仓库名：`gift-ledger-app`
   - 不要勾选初始化
   - 点击 **Create repository**

### 问题 3：GitHub Actions 构建失败

**查看错误日志**：
1. 进入 **Actions** 标签
2. 点击失败的工作流
3. 点击 **build** 任务
4. 查看红色的错误日志

**常见错误**：
- **Gradle build daemon disappeared unexpectedly**：重新运行工作流
- **Could not resolve all dependencies**：网络问题，重新运行
- **SDK not found**：GitHub Actions 配置错误（应该不会）

**重新运行工作流**：
1. 在工作流详情页
2. 点击右上角 **Re-run all jobs**
3. 等待重新构建

### 问题 4：App 无法连接服务器

**检查清单**：
1. **服务器地址是否正确**？
   - 在手机浏览器中访问服务器地址
   - 如果能打开，说明地址正确

2. **手机和服务器是否在同一网络**？
   - 手机是否连接了同一个 Wi-Fi？

3. **服务器是否监听 0.0.0.0**？
   - 检查 Node.js 代码：
     ```javascript
     app.listen(9205, '0.0.0.0', () => {
         console.log('Server running');
     });
     ```

4. **防火墙是否允许端口 9205**？
   - Windows 防火墙添加入站规则
   - 允许端口 9205

---

## 📊 流程图总结

```
[安装 GitHub Desktop]
        ↓
[登录 GitHub 账号]
        ↓
[添加本地仓库]
        ↓
[发布到 GitHub]
        ↓
[等待自动构建] ← 10-30 分钟
        ↓
[下载 APK]
        ↓
[安装到手机]
        ↓
[配置服务器地址]
        ↓
[使用 App] ← 完成！
```

---

## 🎉 完成！

至此，你已经成功：
- ✅ 使用 GitHub Desktop 上传代码
- ✅ 触发 GitHub Actions 自动构建
- ✅ 下载并安装 APK
- ✅ 配置并使用 App

**下一步**：
- 如果遇到问题，查看 **常见问题排查** 部分
- 或者问我！（随时在对话框中输入你的问题）

---

## 📞 获取帮助

如果你在任何步骤遇到问题，可以：

1. **截图发给我**
   - 截取当前界面的图片
   - 描述你遇到的问题
   - 我会帮你分析并解决

2. **描述错误信息**
   - 复制错误提示文字
   - 告诉我做到哪一步了
   - 我会给出针对性的解决方案

**祝你好运！** 🍀
