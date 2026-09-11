/**
 * com.google.android.filament 编译级 stub（P3-B4）。
 * 桌面无 filament 原生引擎；GLTF avatar 渲染在后续阶段接 Skia/GL 桥。
 * 类名/方法签名与 app 调用面对齐，方法体为空或安全默认值。
 * 注意：ModelViewer 的 cameraManipulator/uiHelper 字段以真实类型保留——
 * app 侧经反射按字段类型查找（field.type == Manipulator::class.java）。
 */
package com.google.android.filament

/** filament 渲染引擎句柄。 */
open class Engine {
    val transformManager: TransformManager = TransformManager()
    val renderableManager: RenderableManager = RenderableManager()

    open fun destroy() {}
}

/** TransformManager：实体变换矩阵管理。 */
open class TransformManager {
    open fun getInstance(entity: Int): Int = if (entity != 0) 1 else 0
    open fun setTransform(instance: Int, matrix: FloatArray) {}
    open fun setParent(instance: Int, parent: Int) {}
    open fun destroy(instance: Int) {}
}

/** RenderableManager：可渲染实体管理。 */
open class RenderableManager {
    open fun destroy(entity: Int) {}
}

/** EntityManager：实体 ID 分配。 */
open class EntityManager private constructor() {
    fun create(): Int = nextId++
    fun create(n: Int, entities: IntArray) {
        for (i in 0 until n) entities[i] = nextId++
    }
    fun destroy(entity: Int) {}

    companion object {
        private var nextId = 1
        @JvmStatic fun get(): EntityManager = EntityManager()
    }
}

/** Renderer：帧渲染器。 */
open class Renderer {
    /** Renderer.ClearOptions。 */
    class ClearOptions {
        var clear: Boolean = false
        var discard: Boolean = false
        var clearColor: FloatArray? = null
    }

    var clearOptions: ClearOptions? = null

    open fun render(view: View): Boolean = false
    open fun beginFrame(frameTimeNanos: Long): Boolean = false
    open fun endFrame() {}
}

/** filament View（渲染视图，非 android.view.View）。 */
open class View {
    /** View.BlendMode。 */
    enum class BlendMode { OPAQUE, TRANSPARENT, TRANSLUCENT }

    var blendMode: BlendMode = BlendMode.OPAQUE
    var scene: Scene? = null
    var camera: Camera? = null

    open fun setPostProcessingEnabled(enabled: Boolean) {}
    open fun setAmbientOcclusionEnabled(enabled: Boolean) {}
    open fun setShadowsEnabled(enabled: Boolean) {}
}

/** Scene：场景图。 */
open class Scene {
    var indirectLight: IndirectLight? = null
    var skybox: Skybox? = null

    open fun addEntity(entity: Int) {}
    open fun addEntities(entities: IntArray) {}
    open fun removeEntity(entity: Int) {}
    open fun removeEntities(entities: IntArray) {}
}

/** Camera。 */
open class Camera {
    open fun lookAt(
        eyeX: Double, eyeY: Double, eyeZ: Double,
        centerX: Double, centerY: Double, centerZ: Double,
        upX: Double, upY: Double, upZ: Double
    ) {}

    open fun setProjection(fovInDegrees: Double, aspect: Double, near: Double, far: Double) {}
    open fun setModelTransform(transform: FloatArray) {}
}

/** Skybox。 */
open class Skybox

/** IndirectLight + Builder。 */
open class IndirectLight {
    class Builder {
        fun irradiance(harmonics: Int, sh3: FloatArray): Builder = this
        fun intensity(envIntensity: Float): Builder = this
        fun rotations(v: FloatArray): Builder = this
        fun radiance(bands: Int, sh3: FloatArray): Builder = this
        fun build(engine: Engine): IndirectLight = IndirectLight()
    }
}

/** LightManager + Builder + Type。 */
open class LightManager {
    enum class Type { SUN, DIRECTIONAL, POINT, FOCUSED_SPOT, SPOT }

    class Builder(type: Type) {
        fun color(r: Float, g: Float, b: Float): Builder = this
        fun intensity(intensity: Float): Builder = this
        fun intensity(candela: Float, efficiency: Float): Builder = this
        fun direction(x: Float, y: Float, z: Float): Builder = this
        fun position(x: Float, y: Float, z: Float): Builder = this
        fun castShadows(enabled: Boolean): Builder = this
        fun castShadowsCastOptions(enabled: Boolean): Builder = this
        fun falloff(radius: Float): Builder = this
        fun build(engine: Engine, entity: Int) {}
    }
}

/** Material/MaterialInstance/Texture 等编译级占位。 */
open class Material
open class MaterialInstance
open class Texture
open class VertexBuffer
open class IndexBuffer
