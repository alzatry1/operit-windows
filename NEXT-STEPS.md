# Operit → Windows 移植 · 接续手册（2026-09-11）

> 给下一个接手会话/子代理：这是当前全部进展、方法论、坑的完整交接。读这份就能无缝继续。

## 一句话现状
Android 应用 Operit（48万行 Kotlin，Compose）正移植到 Windows（Compose Multiplatform Desktop JVM）。**兼容层完整编译绿（1364 类），全量编译错误从 3744 降到 1026**，GitHub CI 云端全量编译流水线已就绪。

## 关键路径与产物
- 工程根：`operit-port/windows/`（GitHub: `alzatry1/operit-windows`，master 分支）
- 兼容层：`windows/compat-android/`（android.*/androidx.*/第三方库的桌面迷你框架，**编译绿 1364 类**）
- 应用源码：`windows/composeApp/src/desktopMain/kotlin/`（1236 文件）
- 原始源码：`operit-port/android-src/`（只读参考，勿改）
- **最新全量错误普查**：`operit-port/CENSUS-LATEST.log`（CI 拉取，`e: file:///D:/a/...` 格式）

## 编译方法论（血泪教训，务必遵守）
1. **本机 2.4GB 内存编不动全量**（OOM）。本机只能跑：
   - `:compat-android:compileKotlinDesktop`（兼容层，~3分钟）
   - `:composeApp:compileKotlinDesktop -PportExclude=ui`（非 UI 26万行，~6分钟）
   - 全量只能推 GitHub Actions（16GB runner）做普查。
2. **单构建纪律**：同时刻只跑一个 gradle 构建，构建前 `ps -C java -o pid=` 确认无残留。绝不 kill 构建进程（会留孤儿 daemon 抢内存）。
3. **CI 普查循环**（主驱动）：本地改 → `git push` → CI 全量编译 → `gh run list` / `gh api repos/:owner/:repo/actions/jobs/<jobid>/logs > /tmp/ci.log` 拉错误 → 再改。CI run 在 `alzatry1/operit-windows`。
4. **JVM 签名冲突**：Kotlin 属性自动生成 getX/setX，绝不手写同签名 fun；链式 fun 返回值改宿主类即可。
5. **override 可空性冲突**：app 代码里回调 override 可空/非空混用，Kotlin 垫片只能选一种 → **回调类写成 Java**（compat 已开 `withJava()`，Java 源在 `compat-android/src/desktopMain/java/`）。已转：WebViewClient、WebChromeClient。待转：ServiceConnection、AnimatorListener、BluetoothGattCallback 等。
6. **扩展函数可见性**：app 用 `canvas.nativeCanvas.drawText`（nativeCanvas 桌面=Skia Canvas），扩展要显式 import。已在 `android.graphics` 建 `CanvasSkiaExt.kt` 桥接，5 个 app 文件加了 `import android.graphics.drawText`。

## 已完成的批次（详见 P3-B*-REPORT.md）
- B1a/B1b-1/B1b-2/B2/B3/B4/B6：android 核心、androidx、webview/opengl/sqlite/tts/蓝牙/定位、ObjectBox 真实生成（:objectbox-models 模块）、exoplayer、filament/mlkit/ffmpeg、kyant/fletchmckee/canhub/jlatexmath/DownloadManager/skydoves、coil2→coil3、Room 迁移→SQLiteConnection、Room databaseBuilder 适配 KMP、BuildConfig、R 资源体系（7686 ID + 9 语言）。

## 剩余工作（按优先级）
1. **ViewModelProvider.Factory.create 簇（17 处）**：app override `create(modelClass: Class<T>)`，lifecycle-viewmodel KMP 2.9 要 `create(modelClass: KClass<T>, extras: CreationExtras)`。需批量改写 ViewModel 工厂（注意 `modelClass.newInstance()` → KMP 的反射/无参构造差异）。
2. **Room 直连 SQL（openHelper.writableDatabase，15 处）**：Room KMP 无公开 openHelper。需在 compat 写 `androidx.sqlite.db.SupportSQLiteDatabase`/`SupportSQLiteOpenHelper` 垫片 + `RoomDatabase.openHelper` 扩展属性（底层走 sqlite-bundled 驱动直连同一文件）。涉及 SQL 查看器/备份/恢复功能。
3. **Room KSP codegen**：composeApp 加 KSP 插件 + room-compiler，生成 `AppDatabase_Impl` 等，否则 Room 运行时拿不到实现。
4. **其余 android stub**：VoiceInteractionSession、DisplayManager、MediaCodec、DexClassLoader（插件加载）、work.BackoffPolicy 等，按普查里 Unresolved 频次补。
5. **ServiceConnection 等回调转 Java**（同 webview 模式）。
6. **ui/ 剩余 ~442 错误**：多是个性化 Compose 用法/动画/第三方库，逐个燃尽。
7. **Windows 平台服务实装**（P8）：终端走 ConPTY、自动化走无障碍替代、通知走原生 toast、悬浮窗、文件关联、开机自启、系统托盘。
8. **本地推理**：llama.cpp Windows 版加载（把 Android 的 libllama 换成 llama.cpp 的 Windows dll）。
9. **打包**：`packageReleaseDistributionForCurrentOS` 出 MSI/EXE（CI 已配置）。

## 验证 checkpoint 习惯
每完成一批：compat 编译绿 → 提交推送 → CI 普查 → 记录错误数降幅。当前趋势：3744→1264→1131→1109→1068→1045→1026。
