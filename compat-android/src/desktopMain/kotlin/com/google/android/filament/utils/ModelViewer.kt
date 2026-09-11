package com.google.android.filament.utils

import com.google.android.filament.Camera
import com.google.android.filament.Engine
import com.google.android.filament.Renderer
import com.google.android.filament.Scene
import com.google.android.filament.View
import java.nio.ByteBuffer

/** filament utils.Utils：平台初始化 stub。 */
object Utils {
    @JvmStatic fun init() {}
    @JvmStatic fun init(anyThread: Boolean) {}
}

/** filament utils.Bookmark：相机书签。 */
open class Bookmark {
    val modelview: FloatArray = FloatArray(16)
    val fov: Float = 60f
}

/** filament utils.Manipulator：相机手势操纵器 stub。 */
open class Manipulator private constructor() {

    enum class Mode { ORBIT, MAP, FREE_FLIGHT }

    class Builder {
        fun viewport(width: Int, height: Int): Builder = this
        fun targetPosition(x: Float, y: Float, z: Float): Builder = this
        fun upVector(x: Float, y: Float, z: Float): Builder = this
        fun orbitHomePosition(x: Float, y: Float, z: Float): Builder = this
        fun orbitSpeed(x: Float, y: Float): Builder = this
        fun zoomSpeed(speed: Float): Builder = this
        fun panning(enabled: Boolean): Builder = this
        fun build(mode: Mode): Manipulator = Manipulator()
    }

    val homeBookmark: Bookmark = Bookmark()

    open fun jumpToBookmark(bookmark: Bookmark) {}
    open fun getLookAt(): FloatArray? = null
    open fun scroll(x: Int, y: Int, delta: Float) {}
    open fun grabBegin(x: Int, y: Int, pan: Boolean) {}
    open fun grabUpdate(x: Int, y: Int) {}
    open fun grabEnd() {}
}

/** gltfio.Animator：模型动画播放器 stub。 */
open class Animator {
    val animationCount: Int get() = 0

    open fun getAnimationName(index: Int): String? = null
    open fun getAnimationDuration(index: Int): Float = 0f
    open fun applyAnimation(index: Int, time: Float) {}
    open fun updateBoneMatrices() {}
}

/** gltfio.FilamentAsset 的包围盒。 */
open class Box {
    val halfExtent: FloatArray = FloatArray(3)
    val center: FloatArray = FloatArray(3)
}

/** gltfio.FilamentAsset stub。 */
open class FilamentAsset {
    val root: Int get() = 0
    val entities: IntArray get() = IntArray(0)
    val boundingBox: Box = Box()
}

/**
 * filament utils.ModelViewer 编译级 stub。
 * cameraManipulator/uiHelper 字段保留真实类型：GltfSurfaceView 经反射按
 * 字段类型查找并注入相机操纵器。
 */
open class ModelViewer(view: android.view.View) {

    val engine: Engine = Engine()
    val scene: Scene = Scene()
    val view: View = View()
    val renderer: Renderer = Renderer()
    val camera: Camera = Camera()

    var cameraNear: Float = 0.05f
    var cameraFar: Float = 500f

    /** 默认灯光实体 ID；0 表示无。 */
    val light: Int = 0

    var animator: Animator? = null
        protected set
    var asset: FilamentAsset? = null
        protected set

    @Suppress("unused") private var cameraManipulator: Manipulator? = null
    @Suppress("unused") private var uiHelper: com.google.android.filament.android.UiHelper? = null

    open fun loadModelGlb(buffer: ByteBuffer) {}
    open fun loadModelGltf(buffer: ByteBuffer, resourceResolver: (String) -> ByteBuffer) {}
    open fun render(frameTimeNanos: Long) {}
    open fun destroyModel() {}
    open fun clearRootTransform() {}
    open fun transformToUnitCube() {}
}
