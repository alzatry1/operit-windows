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
4. **JVM 签名冲突**：Kotlin 属性自动生成 getX/setX，绝不手写同签名 fun；链式 fun 返回值改宿主类即可。**⚠️本地增量编译会漏这类冲突（Platform declaration clash）——compat 提交前必须 `--rerun-tasks` 全量 clean 编译一次，否则靠 CI 才发现会浪费一轮。**（踩坑例：getRunningAppProcesses() 方法 + runningAppProcesses 属性同 JVM 签名）
5. **override 可空性冲突**：app 代码里回调 override 可空/非空混用，Kotlin 垫片只能选一种 → **回调类写成 Java**（compat 已开 `withJava()`，Java 源在 `compat-android/src/desktopMain/java/`）。已转：WebViewClient、WebChromeClient、UtteranceProgressListener。待转：ServiceConnection、AnimatorListener、BluetoothGattCallback 等。
6. **扩展函数可见性**：app 用 `canvas.nativeCanvas.drawText`（nativeCanvas 桌面=Skia Canvas），扩展要显式 import。已在 `android.graphics` 建 `CanvasSkiaExt.kt` 桥接，5 个 app 文件加了 `import android.graphics.drawText`。
7. **androidx.sqlite 垫片是必需的（B8r 实验证伪了冲突假设）**：composeApp 用真 `androidx.sqlite:sqlite-bundled:2.5.2` + `room-runtime:2.7.2`，但真 room-runtime 桌面端**不提供** `androidx.sqlite.db.SupportSQLite*` 类（它们在 sqlite-framework 里，桌面 classpath 上没有）。compat 的 `androidx.sqlite.db.SupportSQLite.kt` 垫片是唯一来源——**B8r 试删它导致 +22 错误，已恢复。别删。** 但 `RoomDatabase.openHelper` 在真 Room 里是 protected/internal，app 直连 `database.openHelper.writableDatabase` 仍解析不了——这才是 Room 簇的真正根因，需要在 compat 给 RoomDatabase 加公开的 openHelper 访问途径，或走 RoomOpenHelperCompat 桥。

## 已完成的批次（详见 P3-B*-REPORT.md）
- B1a/B1b-1/B1b-2/B2/B3/B4/B6：android 核心、androidx、webview/opengl/sqlite/tts/蓝牙/定位、ObjectBox 真实生成（:objectbox-models 模块）、exoplayer、filament/mlkit/ffmpeg、kyant/fletchmckee/canhub/jlatexmath/DownloadManager/skydoves、coil2→coil3、Room 迁移→SQLiteConnection、Room databaseBuilder 适配 KMP、BuildConfig、R 资源体系（7686 ID + 9 语言）。

## 剩余工作（按优先级）
0. **当前水位**：**576 →（B8k 普查）**，B8l-B8r 又推了 8 批（jlatexmath 坐标修正+Builder、scrollTo/runningAppProcesses/setBlurBehindRadius、GLSurfaceView.preserveEGLContextOnPause、ClipboardManager.primaryClip 属性化、MotionEvent.obtain(event)+copyFields、compat 冲突修复、getRunningAppProcesses JVM 冲突、androidx.sqlite 垫片移除）。B8q/B8r 普查在 CI 跑。compat 全量 clean 绿。
1. **ViewModelProvider.Factory.create 簇（17 处）**：app override `create(modelClass: Class<T>)`，lifecycle-viewmodel KMP 2.9 要 `create(modelClass: KClass<T>, extras: CreationExtras)`。需批量改写 ViewModel 工厂。
2. **Room openHelper 验证**：B8r 移除 compat androidx.sqlite 垫片后，真 room-runtime→sqlite-framework 应提供 SupportSQLiteOpenHelper，openHelper 链应通。若仍断，查 sqlite-framework 是否在桌面 classpath。
3. **Room KSP codegen**：composeApp 加 KSP 插件 + room-compiler，生成 `AppDatabase_Impl` 等。
4. **composedsl 三件套（56 处，B8k 普查）**：ToolPkgComposeDslGeneratedRenderers/Screen/WebView 引用了桌面 Compose 没有的 Material3 adaptive 组件——WideNavigationRail/WideNavigationRailItem/ShortNavigationBarItem/TimePickerDialog/VerticalDragHandle + 照片选择器类型 VisualMediaType/ImageOnly/VideoOnly/ImageAndVideo/getPickImagesMaxLimit。需 stub composable 或降级替代。
5. **其余 android stub**：VoiceInteractionSession、DisplayManager、MediaCodec、DexClassLoader（插件加载）、work.BackoffPolicy 等，按普查里 Unresolved 频次补。

## 验证 checkpoint 习惯
每完成一批：compat 编译绿 → 提交推送 → CI 普查 → 记录错误数降幅。当前趋势：3744→1264→1131→1109→1068→1045→1026。
