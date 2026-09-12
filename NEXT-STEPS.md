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
4. **JVM 签名冲突 + 重复声明**：Kotlin 属性自动生成 getX/setX，绝不手写同签名 fun；链式 fun 返回值改宿主类即可。**⚠️⚠️本地验证必须用 `--rerun-tasks --no-build-cache` 全量 clean 编译——光 `--rerun-tasks` 不够，Gradle build cache 会恢复旧的成功输出，把“Conflicting declarations / Platform declaration clash / 重复常量”这类错误漏掉，靠 CI 才发现浪费一轮（已踩 3 次：getRunningAppProcesses 方法与属性同名、INPUT_METHOD_SERVICE 重复 const、Clipboard val 赋值）。**
5. **override 可空性冲突**：app 代码里回调 override 可空/非空混用，Kotlin 垫片只能选一种 → **回调类写成 Java**（compat 已开 `withJava()`，Java 源在 `compat-android/src/desktopMain/java/`）。已转：WebViewClient、WebChromeClient、UtteranceProgressListener。待转：ServiceConnection、AnimatorListener、BluetoothGattCallback 等。
6. **扩展函数可见性**：app 用 `canvas.nativeCanvas.drawText`（nativeCanvas 桌面=Skia Canvas），扩展要显式 import。已在 `android.graphics` 建 `CanvasSkiaExt.kt` 桥接，5 个 app 文件加了 `import android.graphics.drawText`。
7. **androidx.sqlite 垫片是必需的（B8r 实验证伪了冲突假设）**：composeApp 用真 `androidx.sqlite:sqlite-bundled:2.5.2` + `room-runtime:2.7.2`，但真 room-runtime 桌面端**不提供** `androidx.sqlite.db.SupportSQLite*` 类（它们在 sqlite-framework 里，桌面 classpath 上没有）。compat 的 `androidx.sqlite.db.SupportSQLite.kt` 垫片是唯一来源——**B8r 试删它导致 +22 错误，已恢复。别删。** 但 `RoomDatabase.openHelper` 在真 Room 里是 protected/internal，app 直连 `database.openHelper.writableDatabase` 仍解析不了——这才是 Room 簇的真正根因，需要在 compat 给 RoomDatabase 加公开的 openHelper 访问途径，或走 RoomOpenHelperCompat 桥。

## 已完成的批次（详见 P3-B*-REPORT.md）
- B1a/B1b-1/B1b-2/B2/B3/B4/B6：android 核心、androidx、webview/opengl/sqlite/tts/蓝牙/定位、ObjectBox 真实生成（:objectbox-models 模块）、exoplayer、filament/mlkit/ffmpeg、kyant/fletchmckee/canhub/jlatexmath/DownloadManager/skydoves、coil2→coil3、Room 迁移→SQLiteConnection、Room databaseBuilder 适配 KMP、BuildConfig、R 资源体系（7686 ID + 9 语言）。

## 剩余工作（按优先级）
0. **当前水位**：**320 错误（B10t 普查，ui/ 145，overrides-nothing 3）**。累计 3744→320（-91.5%）。compat 无缓存 clean 绿。本轮（B8z-B9h）新增：isHapticFeedbackEnabled、INPUT_METHOD_SERVICE、isAppearanceLightStatusBars/NavigationBars、View.dispatchTouchEvent、ViewGroup.descendantFocusability+FOCUS_*、MediaStore.Downloads、Application.attachBaseContext、Bitmap.copyPixelsFromBuffer、Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION（移入 companion）、Service.application 属性、WebView.copyBackForwardList/loadUrl(url,headers) + WebBackForwardList、DownloadManager.Request.addRequestHeader、Material3 adaptive 组件 stub 全套（WideNavigationRail/ModalWideNavigationRail/ShortNavigationBar(Item)/TimePickerDialog/VerticalDragHandle——composedsl 簇 56→41）、BackdropEffectsScope 实现 Density（修 Dp.toPx）。仓库：alzatry1/operit-windows master。
1. **ViewModelProvider.Factory.create 簇（17 处）**：app override `create(modelClass: Class<T>)`，lifecycle-viewmodel KMP 2.9 要 `create(modelClass: KClass<T>, extras: CreationExtras)`。需批量改写 ViewModel 工厂。
2. **Room openHelper 验证**：B8r 移除 compat androidx.sqlite 垫片后，真 room-runtime→sqlite-framework 应提供 SupportSQLiteOpenHelper，openHelper 链应通。若仍断，查 sqlite-framework 是否在桌面 classpath。
3. **Room KSP codegen**：composeApp 加 KSP 插件 + room-compiler，生成 `AppDatabase_Impl` 等。
4. **composedsl 三件套（56 处，B8k 普查）**：ToolPkgComposeDslGeneratedRenderers/Screen/WebView 引用了桌面 Compose 没有的 Material3 adaptive 组件——WideNavigationRail/WideNavigationRailItem/ShortNavigationBarItem/TimePickerDialog/VerticalDragHandle + 照片选择器类型 VisualMediaType/ImageOnly/VideoOnly/ImageAndVideo/getPickImagesMaxLimit。需 stub composable 或降级替代。
5. **其余 android stub**：VoiceInteractionSession、DisplayManager、MediaCodec、DexClassLoader（插件加载）、work.BackoffPolicy 等，按普查里 Unresolved 频次补。
6. **疑难簇（库里本该有却不解析，需细查勿盲改）**：
   - `scrollableArea`（LazyList.kt）：`androidx.compose.foundation.scrollableArea` 是 ExperimentalFoundationApi，可能需 @OptIn 或 CMP foundation 版本不含。查 CMP 1.9.0 foundation 是否有此修饰符。
   - `preferencesDataStoreFile`（PreferencesHealthManager.kt）：`androidx.datastore.preferences.preferencesDataStoreFile`，datastore-preferences:1.1.7 是依赖。查桌面变体是否含此顶层函数。
   - `LocalImageLoader`（MessageImageGenerator.kt）：`coil3.compose.LocalImageLoader`，coil3:3.2.0。查 coil3 3.x 是否改名/移包。
   - `toPx`（LiquidGlass.kt）：`blurRadius.toPx()` 在 backdrop 的 effects 作用域里，该作用域非 Density 接收者 → toPx 无接收者。查 backdrop 垫片的 effect scope 是否应继承 Density。
   - `ColorProvider`（VoiceAssistantGlanceWidget.kt）：Glance ColorProvider 类已在 compat 建好（typealias+构造），但使用点 61/104 行仍 unresolved——查具体调用形式。
   - `openHelper/query/moveToNext/moveToFirst`（SqlViewerViewModel 等）：Room 直连根因见第 7 条。

## 验证 checkpoint 习惯
每完成一批：compat 编译绿 → 提交推送 → CI 普查 → 记录错误数降幅。当前趋势：3744→1264→1131→1109→1068→1045→1026。
