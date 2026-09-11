package android.graphics

/**
 * android.graphics.Path：包壳 org.jetbrains.skia.Path。
 */
open class Path {
    internal var skiaPath: org.jetbrains.skia.Path = org.jetbrains.skia.Path()

    constructor()
    constructor(src: Path) {
        skiaPath = org.jetbrains.skia.Path().also { copy ->
            // skia Path 无直接拷贝构造，用 transform 复制
            src.skiaPath.transform(org.jetbrains.skia.Matrix33.IDENTITY, copy, true)
        }
    }

    enum class Direction {
        CW, CCW;

        internal fun toSkia(): org.jetbrains.skia.PathDirection = when (this) {
            CW -> org.jetbrains.skia.PathDirection.CLOCKWISE
            CCW -> org.jetbrains.skia.PathDirection.COUNTER_CLOCKWISE
        }
    }

    enum class Op {
        DIFFERENCE, INTERSECT, UNION, XOR, REVERSE_DIFFERENCE;

        internal fun toSkia(): org.jetbrains.skia.PathOp = when (this) {
            DIFFERENCE -> org.jetbrains.skia.PathOp.DIFFERENCE
            INTERSECT -> org.jetbrains.skia.PathOp.INTERSECT
            UNION -> org.jetbrains.skia.PathOp.UNION
            XOR -> org.jetbrains.skia.PathOp.XOR
            REVERSE_DIFFERENCE -> org.jetbrains.skia.PathOp.REVERSE_DIFFERENCE
        }
    }

    enum class FillType {
        WINDING, EVEN_ODD, INVERSE_WINDING, INVERSE_EVEN_ODD;

        internal fun toSkia(): org.jetbrains.skia.PathFillMode = when (this) {
            WINDING -> org.jetbrains.skia.PathFillMode.WINDING
            EVEN_ODD -> org.jetbrains.skia.PathFillMode.EVEN_ODD
            INVERSE_WINDING -> org.jetbrains.skia.PathFillMode.INVERSE_WINDING
            INVERSE_EVEN_ODD -> org.jetbrains.skia.PathFillMode.INVERSE_EVEN_ODD
        }

        internal companion object {
            fun fromSkia(m: org.jetbrains.skia.PathFillMode): FillType = when (m) {
                org.jetbrains.skia.PathFillMode.WINDING -> WINDING
                org.jetbrains.skia.PathFillMode.EVEN_ODD -> EVEN_ODD
                org.jetbrains.skia.PathFillMode.INVERSE_WINDING -> INVERSE_WINDING
                org.jetbrains.skia.PathFillMode.INVERSE_EVEN_ODD -> INVERSE_EVEN_ODD
            }
        }
    }

    // ---- 绘图指令 ----
    open fun moveTo(x: Float, y: Float) { skiaPath.moveTo(x, y) }
    open fun rMoveTo(dx: Float, dy: Float) { skiaPath.rMoveTo(dx, dy) }
    open fun lineTo(x: Float, y: Float) { skiaPath.lineTo(x, y) }
    open fun rLineTo(dx: Float, dy: Float) { skiaPath.rLineTo(dx, dy) }
    open fun quadTo(x1: Float, y1: Float, x2: Float, y2: Float) { skiaPath.quadTo(x1, y1, x2, y2) }
    open fun rQuadTo(dx1: Float, dy1: Float, dx2: Float, dy2: Float) { skiaPath.rQuadTo(dx1, dy1, dx2, dy2) }
    open fun cubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) { skiaPath.cubicTo(x1, y1, x2, y2, x3, y3) }
    open fun rCubicTo(x1: Float, y1: Float, x2: Float, y2: Float, x3: Float, y3: Float) { skiaPath.rCubicTo(x1, y1, x2, y2, x3, y3) }
    open fun conicTo(x1: Float, y1: Float, x2: Float, y2: Float, weight: Float) { skiaPath.conicTo(x1, y1, x2, y2, weight) }
    open fun rConicTo(dx1: Float, dy1: Float, dx2: Float, dy2: Float, weight: Float) { skiaPath.rConicTo(dx1, dy1, dx2, dy2, weight) }

    open fun arcTo(oval: RectF, startAngle: Float, sweepAngle: Float, forceMoveTo: Boolean) {
        skiaPath.arcTo(
            org.jetbrains.skia.Rect.makeLTRB(oval.left, oval.top, oval.right, oval.bottom),
            startAngle, sweepAngle, forceMoveTo,
        )
    }

    open fun arcTo(oval: RectF, startAngle: Float, sweepAngle: Float) = arcTo(oval, startAngle, sweepAngle, false)

    open fun arcTo(left: Float, top: Float, right: Float, bottom: Float, startAngle: Float, sweepAngle: Float, forceMoveTo: Boolean) =
        arcTo(RectF(left, top, right, bottom), startAngle, sweepAngle, forceMoveTo)

    open fun addArc(oval: RectF, startAngle: Float, sweepAngle: Float) {
        skiaPath.addArc(
            org.jetbrains.skia.Rect.makeLTRB(oval.left, oval.top, oval.right, oval.bottom),
            startAngle, sweepAngle,
        )
    }

    open fun addArc(left: Float, top: Float, right: Float, bottom: Float, startAngle: Float, sweepAngle: Float) =
        addArc(RectF(left, top, right, bottom), startAngle, sweepAngle)

    open fun addRect(oval: RectF, dir: Direction) = addRect(oval.left, oval.top, oval.right, oval.bottom, dir)

    open fun addRect(left: Float, top: Float, right: Float, bottom: Float, dir: Direction) {
        skiaPath.addRect(org.jetbrains.skia.Rect.makeLTRB(left, top, right, bottom), dir.toSkia(), 0)
    }

    open fun addRect(r: Rect, dir: Direction) =
        addRect(r.left.toFloat(), r.top.toFloat(), r.right.toFloat(), r.bottom.toFloat(), dir)

    open fun addOval(oval: RectF, dir: Direction) = addOval(oval.left, oval.top, oval.right, oval.bottom, dir)

    open fun addOval(left: Float, top: Float, right: Float, bottom: Float, dir: Direction) {
        skiaPath.addOval(org.jetbrains.skia.Rect.makeLTRB(left, top, right, bottom), dir.toSkia(), 0)
    }

    open fun addCircle(x: Float, y: Float, radius: Float, dir: Direction) {
        skiaPath.addCircle(x, y, radius, dir.toSkia())
    }

    open fun addRoundRect(rect: RectF, rx: Float, ry: Float, dir: Direction) =
        addRoundRect(rect, floatArrayOf(rx, ry, rx, ry, rx, ry, rx, ry), dir)

    open fun addRoundRect(left: Float, top: Float, right: Float, bottom: Float, rx: Float, ry: Float, dir: Direction) =
        addRoundRect(RectF(left, top, right, bottom), rx, ry, dir)

    open fun addRoundRect(rect: RectF, radii: FloatArray, dir: Direction) =
        addRoundRect(rect.left, rect.top, rect.right, rect.bottom, radii, dir)

    open fun addRoundRect(left: Float, top: Float, right: Float, bottom: Float, radii: FloatArray, dir: Direction) {
        val r = if (radii.size >= 8) radii else FloatArray(8) { radii.getOrElse(it) { 0f } }
        skiaPath.addRRect(org.jetbrains.skia.RRect.makeComplexLTRB(left, top, right, bottom, r), dir.toSkia(), 0)
    }

    open fun addPath(src: Path) { skiaPath.addPath(src.skiaPath) }
    open fun addPath(src: Path, dx: Float, dy: Float) {
        val m = Matrix(); m.setTranslate(dx, dy)
        src.skiaPath.transform(m.toSkia(), skiaPath, true)
    }
    open fun addPath(src: Path, matrix: Matrix) {
        src.skiaPath.transform(matrix.toSkia(), skiaPath, true)
    }

    open fun setLastPoint(dx: Float, dy: Float) { skiaPath.setLastPt(dx, dy) }

    // ---- 查询 ----
    open val isEmpty: Boolean get() = skiaPath.isEmpty
    open val isConvex: Boolean get() = skiaPath.isConvex
    open val isValid: Boolean get() = true
    open val isLastContourClosed: Boolean get() = skiaPath.isLastContourClosed
    open val isFinite: Boolean get() = skiaPath.isFinite
    open val isVolatile: Boolean get() = false
    open val isInterpolatable: Boolean get() = false
    open val isRect: Boolean get() = false

    open fun computeBounds(bounds: RectF, exact: Boolean) = computeBounds(bounds)
    open fun computeBounds(bounds: RectF) {
        val b = skiaPath.bounds
        bounds.set(b.left, b.top, b.right, b.bottom)
    }

    open fun getSegmentFractions(): FloatArray = floatArrayOf()

    // ---- 变换/操作 ----
    open fun transform(matrix: Matrix) { skiaPath = skiaPath.transform(matrix.toSkia()) }
    open fun transform(matrix: Matrix, dst: Path) {
        val result = org.jetbrains.skia.Path()
        skiaPath.transform(matrix.toSkia(), result, true)
        dst.skiaPath = result
    }

    open fun set(src: Path) { skiaPath = org.jetbrains.skia.Path().also { src.skiaPath.transform(org.jetbrains.skia.Matrix33.IDENTITY, it, true) } }
    open fun offset(dx: Float, dy: Float) {
        val m = Matrix(); m.setTranslate(dx, dy)
        transform(m)
    }
    open fun offset(dx: Float, dy: Float, dst: Path) {
        val m = Matrix(); m.setTranslate(dx, dy)
        transform(m, dst)
    }

    open fun op(path1: Path, path2: Path, op: Op): Boolean {
        val result = org.jetbrains.skia.Path.makeCombining(path1.skiaPath, path2.skiaPath, op.toSkia())
        if (result != null) { skiaPath = result; return true }
        return false
    }

    open fun op(path: Path, op: Op): Boolean = op(this, path, op)

    open fun interpolate(other: Path, t: Float, interpolatedResult: Path): Boolean {
        val result = skiaPath.makeLerp(other.skiaPath, t)
        if (result != null) { interpolatedResult.skiaPath = result; return true }
        return false
    }

    open fun reset() { skiaPath.reset() }
    open fun rewind() { skiaPath.rewind() }
    open fun close() { skiaPath.closePath() }

    open fun toggleInverseFillType() {
        fillType = when (fillType) {
            FillType.WINDING -> FillType.INVERSE_WINDING
            FillType.EVEN_ODD -> FillType.INVERSE_EVEN_ODD
            FillType.INVERSE_WINDING -> FillType.WINDING
            FillType.INVERSE_EVEN_ODD -> FillType.EVEN_ODD
        }
    }

    open var fillType: FillType
        get() = FillType.fromSkia(skiaPath.fillMode)
        set(value) { skiaPath.fillMode = value.toSkia() }

    open val isInverseFillType: Boolean
        get() = fillType == FillType.INVERSE_WINDING || fillType == FillType.INVERSE_EVEN_ODD

    open fun incReserve(extraPtCount: Int) { skiaPath.incReserve(extraPtCount) }

    override fun toString(): String = "Path(bounds=${skiaPath.bounds})"

    companion object {
        @JvmStatic fun interpolate(path: Path, other: Path, t: Float, out: Path): Boolean = path.interpolate(other, t, out)
    }
}

/** android.graphics.PathMeasure：包壳 skia PathMeasure。 */
class PathMeasure {
    private var skiaMeasure: org.jetbrains.skia.PathMeasure? = null

    constructor()
    constructor(path: Path?, forceClosed: Boolean) {
        setPath(path, forceClosed)
    }

    fun setPath(path: Path?, forceClosed: Boolean) {
        skiaMeasure = if (path != null) org.jetbrains.skia.PathMeasure(path.skiaPath, forceClosed, 1f) else null
    }

    val length: Float get() = skiaMeasure?.length ?: 0f
    val isClosed: Boolean get() = skiaMeasure?.isClosed ?: false

    fun getPosTan(distance: Float, pos: FloatArray?, tan: FloatArray?): Boolean {
        val m = skiaMeasure ?: return false
        val p = m.getPosition(distance) ?: return false
        pos?.let {
            if (it.size >= 2) { it[0] = p.x; it[1] = p.y }
        }
        if (tan != null) {
            val t = m.getTangent(distance)
            if (t != null && tan.size >= 2) { tan[0] = t.x; tan[1] = t.y }
        }
        return true
    }

    fun getMatrix(distance: Float, matrix: Matrix, flags: Int): Boolean {
        val m = skiaMeasure ?: return false
        val needPos = flags and POSITION_MATRIX_FLAG != 0
        val needTan = flags and TANGENT_MATRIX_FLAG != 0
        val skiaMatrix = m.getMatrix(distance, needPos, needTan) ?: return false
        matrix.setValues(skiaMatrix.mat)
        return true
    }

    fun getSegment(startD: Float, stopD: Float, dst: Path, startWithMoveTo: Boolean): Boolean {
        val m = skiaMeasure ?: return false
        return m.getSegment(startD, stopD, dst.skiaPath, startWithMoveTo)
    }

    fun nextContour(): Boolean = skiaMeasure?.nextContour() ?: false

    companion object {
        const val POSITION_MATRIX_FLAG = 1
        const val TANGENT_MATRIX_FLAG = 2
    }
}
