package android.opengl

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * android.opengl 垫片（P3-B2 新增）。
 * GLES20：全常量 + 方法 no-op（桌面无 GL 上下文，avatar 渲染在后续阶段接 Skia/GL 桥）。
 * Matrix：float 数组矩阵数学为真实实现（纯计算，无平台依赖）。
 * GLSurfaceView：编译级 View stub。
 */

/** android.opengl.GLES20：常量齐全，方法全部 no-op。 */
object GLES20 {
    const val GL_FALSE = 0
    const val GL_TRUE = 1
    const val GL_BYTE = 0x1400
    const val GL_UNSIGNED_BYTE = 0x1401
    const val GL_SHORT = 0x1402
    const val GL_UNSIGNED_SHORT = 0x1403
    const val GL_INT = 0x1404
    const val GL_UNSIGNED_INT = 0x1405
    const val GL_FLOAT = 0x1406
    const val GL_FIXED = 0x140C
    const val GL_DEPTH_BUFFER_BIT = 0x00000100
    const val GL_STENCIL_BUFFER_BIT = 0x00000400
    const val GL_COLOR_BUFFER_BIT = 0x00004000
    const val GL_POINTS = 0x0000
    const val GL_LINES = 0x0001
    const val GL_LINE_LOOP = 0x0002
    const val GL_LINE_STRIP = 0x0003
    const val GL_TRIANGLES = 0x0004
    const val GL_TRIANGLE_STRIP = 0x0005
    const val GL_TRIANGLE_FAN = 0x0006
    const val GL_ZERO = 0
    const val GL_ONE = 1
    const val GL_SRC_COLOR = 0x0300
    const val GL_ONE_MINUS_SRC_COLOR = 0x0301
    const val GL_SRC_ALPHA = 0x0302
    const val GL_ONE_MINUS_SRC_ALPHA = 0x0303
    const val GL_DST_ALPHA = 0x0304
    const val GL_ONE_MINUS_DST_ALPHA = 0x0305
    const val GL_DST_COLOR = 0x0306
    const val GL_ONE_MINUS_DST_COLOR = 0x0307
    const val GL_SRC_ALPHA_SATURATE = 0x0308
    const val GL_FRONT = 0x0404
    const val GL_BACK = 0x0405
    const val GL_FRONT_AND_BACK = 0x0408
    const val GL_NO_ERROR = 0
    const val GL_INVALID_ENUM = 0x0500
    const val GL_INVALID_VALUE = 0x0501
    const val GL_INVALID_OPERATION = 0x0502
    const val GL_OUT_OF_MEMORY = 0x0505
    const val GL_INVALID_FRAMEBUFFER_OPERATION = 0x0506
    const val GL_CW = 0x0900
    const val GL_CCW = 0x0901
    const val GL_CULL_FACE = 0x0B44
    const val GL_DEPTH_TEST = 0x0B71
    const val GL_STENCIL_TEST = 0x0B90
    const val GL_DITHER = 0x0BD0
    const val GL_BLEND = 0x0BE2
    const val GL_SCISSOR_TEST = 0x0C11
    const val GL_TEXTURE_2D = 0x0DE1
    const val GL_TEXTURE_BINDING_2D = 0x8069
    const val GL_TEXTURE_MAG_FILTER = 0x2800
    const val GL_TEXTURE_MIN_FILTER = 0x2801
    const val GL_TEXTURE_WRAP_S = 0x2802
    const val GL_TEXTURE_WRAP_T = 0x2803
    const val GL_NEAREST = 0x2600
    const val GL_LINEAR = 0x2601
    const val GL_NEAREST_MIPMAP_NEAREST = 0x2700
    const val GL_LINEAR_MIPMAP_NEAREST = 0x2701
    const val GL_NEAREST_MIPMAP_LINEAR = 0x2702
    const val GL_LINEAR_MIPMAP_LINEAR = 0x2703
    const val GL_CLAMP_TO_EDGE = 0x812F
    const val GL_REPEAT = 0x2901
    const val GL_MIRRORED_REPEAT = 0x8370
    const val GL_ALPHA = 0x1906
    const val GL_RGB = 0x1907
    const val GL_RGBA = 0x1908
    const val GL_LUMINANCE = 0x1909
    const val GL_LUMINANCE_ALPHA = 0x190A
    const val GL_UNSIGNED_SHORT_4_4_4_4 = 0x8033
    const val GL_UNSIGNED_SHORT_5_5_5_1 = 0x8034
    const val GL_UNSIGNED_SHORT_5_6_5 = 0x8363
    const val GL_NEVER = 0x0200
    const val GL_LESS = 0x0201
    const val GL_EQUAL = 0x0202
    const val GL_LEQUAL = 0x0203
    const val GL_GREATER = 0x0204
    const val GL_NOTEQUAL = 0x0205
    const val GL_GEQUAL = 0x0206
    const val GL_ALWAYS = 0x0207
    const val GL_KEEP = 0x1E00
    const val GL_REPLACE = 0x1E01
    const val GL_INCR = 0x1E02
    const val GL_DECR = 0x1E03
    const val GL_INVERT = 0x150A
    const val GL_INCR_WRAP = 0x8507
    const val GL_DECR_WRAP = 0x8508
    const val GL_VENDOR = 0x1F00
    const val GL_RENDERER = 0x1F01
    const val GL_VERSION = 0x1F02
    const val GL_EXTENSIONS = 0x1F03
    const val GL_SHADING_LANGUAGE_VERSION = 0x8B8C
    const val GL_VIEWPORT = 0x0BA2
    const val GL_SCISSOR_BOX = 0x0C10
    const val GL_MAX_TEXTURE_SIZE = 0x0D33
    const val GL_MAX_VIEWPORT_DIMS = 0x0D3A
    const val GL_MAX_VERTEX_ATTRIBS = 0x8869
    const val GL_MAX_TEXTURE_IMAGE_UNITS = 0x8872
    const val GL_MAX_VERTEX_TEXTURE_IMAGE_UNITS = 0x8B4C
    const val GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS = 0x8B4D
    const val GL_MAX_VERTEX_UNIFORM_VECTORS = 0x8DFB
    const val GL_MAX_FRAGMENT_UNIFORM_VECTORS = 0x8DFD
    const val GL_MAX_VARYING_VECTORS = 0x8DFC
    const val GL_ARRAY_BUFFER = 0x8892
    const val GL_ELEMENT_ARRAY_BUFFER = 0x8893
    const val GL_ARRAY_BUFFER_BINDING = 0x8894
    const val GL_ELEMENT_ARRAY_BUFFER_BINDING = 0x8895
    const val GL_STATIC_DRAW = 0x88E4
    const val GL_DYNAMIC_DRAW = 0x88E8
    const val GL_STREAM_DRAW = 0x88E0
    const val GL_BUFFER_SIZE = 0x8764
    const val GL_BUFFER_USAGE = 0x8765
    const val GL_FRAGMENT_SHADER = 0x8B30
    const val GL_VERTEX_SHADER = 0x8B31
    const val GL_COMPILE_STATUS = 0x8B81
    const val GL_LINK_STATUS = 0x8B82
    const val GL_VALIDATE_STATUS = 0x8B83
    const val GL_INFO_LOG_LENGTH = 0x8B84
    const val GL_ATTACHED_SHADERS = 0x8B85
    const val GL_ACTIVE_UNIFORMS = 0x8B86
    const val GL_ACTIVE_ATTRIBUTES = 0x8B89
    const val GL_CURRENT_PROGRAM = 0x8B8D
    const val GL_SHADER_TYPE = 0x8B4F
    const val GL_DELETE_STATUS = 0x8B80
    const val GL_ACTIVE_UNIFORM_MAX_LENGTH = 0x8B87
    const val GL_ACTIVE_ATTRIBUTE_MAX_LENGTH = 0x8B8A
    const val GL_SHADER_SOURCE_LENGTH = 0x8B88
    const val GL_FRAMEBUFFER = 0x8D40
    const val GL_RENDERBUFFER = 0x8D41
    const val GL_COLOR_ATTACHMENT0 = 0x8CE0
    const val GL_DEPTH_ATTACHMENT = 0x8D00
    const val GL_STENCIL_ATTACHMENT = 0x8D20
    const val GL_FRAMEBUFFER_COMPLETE = 0x8CD5
    const val GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT = 0x8CD6
    const val GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT = 0x8CD7
    const val GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS = 0x8CD9
    const val GL_FRAMEBUFFER_UNSUPPORTED = 0x8CDD
    const val GL_FRAMEBUFFER_BINDING = 0x8CA6
    const val GL_RENDERBUFFER_BINDING = 0x8CA7
    const val GL_DEPTH_COMPONENT16 = 0x81A5
    const val GL_STENCIL_INDEX8 = 0x8D48
    const val GL_RENDERBUFFER_WIDTH = 0x8D42
    const val GL_RENDERBUFFER_HEIGHT = 0x8D43
    const val GL_RENDERBUFFER_INTERNAL_FORMAT = 0x8D44
    const val GL_TEXTURE0 = 0x84C0
    const val GL_TEXTURE31 = 0x84DF
    const val GL_ACTIVE_TEXTURE = 0x84E0
    const val GL_TEXTURE_CUBE_MAP = 0x8513
    const val GL_TEXTURE_CUBE_MAP_POSITIVE_X = 0x8515
    const val GL_TEXTURE_CUBE_MAP_NEGATIVE_X = 0x8516
    const val GL_TEXTURE_CUBE_MAP_POSITIVE_Y = 0x8517
    const val GL_TEXTURE_CUBE_MAP_NEGATIVE_Y = 0x8518
    const val GL_TEXTURE_CUBE_MAP_POSITIVE_Z = 0x8519
    const val GL_TEXTURE_CUBE_MAP_NEGATIVE_Z = 0x851A
    const val GL_TEXTURE_BINDING_CUBE_MAP = 0x8514
    const val GL_MAX_CUBE_MAP_TEXTURE_SIZE = 0x851C
    const val GL_GENERATE_MIPMAP_HINT = 0x8192
    const val GL_NICEST = 0x1102
    const val GL_FASTEST = 0x1101
    const val GL_DONT_CARE = 0x1100
    const val GL_UNPACK_ALIGNMENT = 0x0CF5
    const val GL_PACK_ALIGNMENT = 0x0D05
    const val GL_DEPTH_WRITEMASK = 0x0B72
    const val GL_DEPTH_FUNC = 0x0B74
    const val GL_LINE_WIDTH = 0x0B21
    const val GL_ALIASED_LINE_WIDTH_RANGE = 0x846E
    const val GL_FLOAT_VEC2 = 0x8B50
    const val GL_FLOAT_VEC3 = 0x8B51
    const val GL_FLOAT_VEC4 = 0x8B52
    const val GL_INT_VEC2 = 0x8B53
    const val GL_INT_VEC3 = 0x8B54
    const val GL_INT_VEC4 = 0x8B55
    const val GL_BOOL = 0x8B56
    const val GL_FLOAT_MAT2 = 0x8B5A
    const val GL_FLOAT_MAT3 = 0x8B5B
    const val GL_FLOAT_MAT4 = 0x8B5C
    const val GL_SAMPLER_2D = 0x8B5E
    const val GL_SAMPLER_CUBE = 0x8B60
    const val GL_POLYGON_OFFSET_FILL = 0x8037
    const val GL_SAMPLE_ALPHA_TO_COVERAGE = 0x809E
    const val GL_SAMPLE_COVERAGE = 0x80A0

    // ---- 方法：全部 no-op / 返回安全默认值 ----
    @JvmStatic fun glActiveTexture(texture: Int) {}
    @JvmStatic fun glAttachShader(program: Int, shader: Int) {}
    @JvmStatic fun glBindAttribLocation(program: Int, index: Int, name: String?) {}
    @JvmStatic fun glBindBuffer(target: Int, buffer: Int) {}
    @JvmStatic fun glBindFramebuffer(target: Int, framebuffer: Int) {}
    @JvmStatic fun glBindRenderbuffer(target: Int, renderbuffer: Int) {}
    @JvmStatic fun glBindTexture(target: Int, texture: Int) {}
    @JvmStatic fun glBlendColor(red: Float, green: Float, blue: Float, alpha: Float) {}
    @JvmStatic fun glBlendEquation(mode: Int) {}
    @JvmStatic fun glBlendEquationSeparate(modeRGB: Int, modeAlpha: Int) {}
    @JvmStatic fun glBlendFunc(sfactor: Int, dfactor: Int) {}
    @JvmStatic fun glBlendFuncSeparate(srcRGB: Int, dstRGB: Int, srcAlpha: Int, dstAlpha: Int) {}
    @JvmStatic fun glBufferData(target: Int, size: Int, data: java.nio.Buffer?, usage: Int) {}
    @JvmStatic fun glBufferSubData(target: Int, offset: Int, size: Int, data: java.nio.Buffer?) {}
    @JvmStatic fun glCheckFramebufferStatus(target: Int): Int = GL_FRAMEBUFFER_COMPLETE
    @JvmStatic fun glClear(mask: Int) {}
    @JvmStatic fun glClearColor(red: Float, green: Float, blue: Float, alpha: Float) {}
    @JvmStatic fun glClearDepthf(depth: Float) {}
    @JvmStatic fun glClearStencil(s: Int) {}
    @JvmStatic fun glColorMask(red: Boolean, green: Boolean, blue: Boolean, alpha: Boolean) {}
    @JvmStatic fun glCompileShader(shader: Int) {}
    @JvmStatic fun glCompressedTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, imageSize: Int, data: java.nio.Buffer?) {}
    @JvmStatic fun glCompressedTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, imageSize: Int, data: java.nio.Buffer?) {}
    @JvmStatic fun glCopyTexImage2D(target: Int, level: Int, internalformat: Int, x: Int, y: Int, width: Int, height: Int, border: Int) {}
    @JvmStatic fun glCopyTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, x: Int, y: Int, width: Int, height: Int) {}
    @JvmStatic fun glCreateProgram(): Int = 0
    @JvmStatic fun glCreateShader(type: Int): Int = 0
    @JvmStatic fun glCullFace(mode: Int) {}
    @JvmStatic fun glDeleteBuffers(n: Int, buffers: IntArray?, offset: Int) {}
    @JvmStatic fun glDeleteFramebuffers(n: Int, framebuffers: IntArray?, offset: Int) {}
    @JvmStatic fun glDeleteProgram(program: Int) {}
    @JvmStatic fun glDeleteRenderbuffers(n: Int, renderbuffers: IntArray?, offset: Int) {}
    @JvmStatic fun glDeleteShader(shader: Int) {}
    @JvmStatic fun glDeleteTextures(n: Int, textures: IntArray?, offset: Int) {}
    @JvmStatic fun glDepthFunc(func: Int) {}
    @JvmStatic fun glDepthMask(flag: Boolean) {}
    @JvmStatic fun glDepthRangef(zNear: Float, zFar: Float) {}
    @JvmStatic fun glDetachShader(program: Int, shader: Int) {}
    @JvmStatic fun glDisable(cap: Int) {}
    @JvmStatic fun glDisableVertexAttribArray(index: Int) {}
    @JvmStatic fun glDrawArrays(mode: Int, first: Int, count: Int) {}
    @JvmStatic fun glDrawElements(mode: Int, count: Int, type: Int, indices: java.nio.Buffer?) {}
    @JvmStatic fun glDrawElements(mode: Int, count: Int, type: Int, offset: Int) {}
    @JvmStatic fun glEnable(cap: Int) {}
    @JvmStatic fun glEnableVertexAttribArray(index: Int) {}
    @JvmStatic fun glFinish() {}
    @JvmStatic fun glFlush() {}
    @JvmStatic fun glFramebufferRenderbuffer(target: Int, attachment: Int, renderbuffertarget: Int, renderbuffer: Int) {}
    @JvmStatic fun glFramebufferTexture2D(target: Int, attachment: Int, textarget: Int, texture: Int, level: Int) {}
    @JvmStatic fun glFrontFace(mode: Int) {}
    @JvmStatic fun glGenBuffers(n: Int, buffers: IntArray?, offset: Int) {}
    @JvmStatic fun glGenFramebuffers(n: Int, framebuffers: IntArray?, offset: Int) {}
    @JvmStatic fun glGenRenderbuffers(n: Int, renderbuffers: IntArray?, offset: Int) {}
    @JvmStatic fun glGenTextures(n: Int, textures: IntArray?, offset: Int) {}
    @JvmStatic fun glGenerateMipmap(target: Int) {}
    @JvmStatic fun glGetActiveAttrib(program: Int, index: Int, bufsize: Int, length: IntArray?, lengthOffset: Int, type: IntArray?, typeOffset: Int, size: IntArray?, sizeOffset: Int, name: ByteArray?, nameOffset: Int) {}
    @JvmStatic fun glGetActiveUniform(program: Int, index: Int, bufsize: Int, length: IntArray?, lengthOffset: Int, type: IntArray?, typeOffset: Int, size: IntArray?, sizeOffset: Int, name: ByteArray?, nameOffset: Int) {}
    @JvmStatic fun glGetAttribLocation(program: Int, name: String?): Int = -1
    @JvmStatic fun glGetBooleanv(pname: Int, params: BooleanArray?, offset: Int) {}
    @JvmStatic fun glGetBufferParameteriv(target: Int, pname: Int, params: IntArray?, offset: Int) {}
    @JvmStatic fun glGetError(): Int = GL_NO_ERROR
    @JvmStatic fun glGetFloatv(pname: Int, params: FloatArray?, offset: Int) {}
    @JvmStatic fun glGetFramebufferAttachmentParameteriv(target: Int, attachment: Int, pname: Int, params: IntArray?, offset: Int) {}
    @JvmStatic fun glGetIntegerv(pname: Int, params: IntArray?, offset: Int) {}
    @JvmStatic fun glGetProgramInfoLog(program: Int): String = ""
    @JvmStatic fun glGetProgramiv(program: Int, pname: Int, params: IntArray?, offset: Int) {}
    @JvmStatic fun glGetRenderbufferParameteriv(target: Int, pname: Int, params: IntArray?, offset: Int) {}
    @JvmStatic fun glGetShaderInfoLog(shader: Int): String = ""
    @JvmStatic fun glGetShaderiv(shader: Int, pname: Int, params: IntArray?, offset: Int) {}
    @JvmStatic fun glGetShaderPrecisionFormat(shadertype: Int, precisiontype: Int, range: IntArray?, rangeOffset: Int, precision: IntArray?, precisionOffset: Int) {}
    @JvmStatic fun glGetShaderSource(shader: Int): String = ""
    @JvmStatic fun glGetString(name: Int): String = ""
    @JvmStatic fun glGetTexParameterfv(target: Int, pname: Int, params: FloatArray?, offset: Int) {}
    @JvmStatic fun glGetTexParameteriv(target: Int, pname: Int, params: IntArray?, offset: Int) {}
    @JvmStatic fun glGetUniformLocation(program: Int, name: String?): Int = -1
    @JvmStatic fun glGetUniformfv(program: Int, location: Int, params: FloatArray?, offset: Int) {}
    @JvmStatic fun glGetUniformiv(program: Int, location: Int, params: IntArray?, offset: Int) {}
    @JvmStatic fun glGetVertexAttribfv(index: Int, pname: Int, params: FloatArray?, offset: Int) {}
    @JvmStatic fun glGetVertexAttribiv(index: Int, pname: Int, params: IntArray?, offset: Int) {}
    @JvmStatic fun glHint(target: Int, mode: Int) {}
    @JvmStatic fun glIsBuffer(buffer: Int): Boolean = false
    @JvmStatic fun glIsEnabled(cap: Int): Boolean = false
    @JvmStatic fun glIsFramebuffer(framebuffer: Int): Boolean = false
    @JvmStatic fun glIsProgram(program: Int): Boolean = false
    @JvmStatic fun glIsRenderbuffer(renderbuffer: Int): Boolean = false
    @JvmStatic fun glIsShader(shader: Int): Boolean = false
    @JvmStatic fun glIsTexture(texture: Int): Boolean = false
    @JvmStatic fun glLineWidth(width: Float) {}
    @JvmStatic fun glLinkProgram(program: Int) {}
    @JvmStatic fun glPixelStorei(pname: Int, param: Int) {}
    @JvmStatic fun glPolygonOffset(factor: Float, units: Float) {}
    @JvmStatic fun glReadPixels(x: Int, y: Int, width: Int, height: Int, format: Int, type: Int, pixels: java.nio.Buffer?) {}
    @JvmStatic fun glReleaseShaderCompiler() {}
    @JvmStatic fun glRenderbufferStorage(target: Int, internalformat: Int, width: Int, height: Int) {}
    @JvmStatic fun glSampleCoverage(value: Float, invert: Boolean) {}
    @JvmStatic fun glScissor(x: Int, y: Int, width: Int, height: Int) {}
    @JvmStatic fun glShaderBinary(n: Int, shaders: IntArray?, offset: Int, binaryformat: Int, binary: java.nio.Buffer?, length: Int) {}
    @JvmStatic fun glShaderSource(shader: Int, string: String?) {}
    @JvmStatic fun glStencilFunc(func: Int, ref: Int, mask: Int) {}
    @JvmStatic fun glStencilFuncSeparate(face: Int, func: Int, ref: Int, mask: Int) {}
    @JvmStatic fun glStencilMask(mask: Int) {}
    @JvmStatic fun glStencilMaskSeparate(face: Int, mask: Int) {}
    @JvmStatic fun glStencilOp(fail: Int, zfail: Int, zpass: Int) {}
    @JvmStatic fun glStencilOpSeparate(face: Int, fail: Int, zfail: Int, zpass: Int) {}
    @JvmStatic fun glTexImage2D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, border: Int, format: Int, type: Int, pixels: java.nio.Buffer?) {}
    @JvmStatic fun glTexParameterf(target: Int, pname: Int, param: Float) {}
    @JvmStatic fun glTexParameterfv(target: Int, pname: Int, params: FloatArray?, offset: Int) {}
    @JvmStatic fun glTexParameteri(target: Int, pname: Int, param: Int) {}
    @JvmStatic fun glTexParameteriv(target: Int, pname: Int, params: IntArray?, offset: Int) {}
    @JvmStatic fun glTexSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, width: Int, height: Int, format: Int, type: Int, pixels: java.nio.Buffer?) {}
    @JvmStatic fun glUniform1f(location: Int, x: Float) {}
    @JvmStatic fun glUniform1fv(location: Int, count: Int, v: FloatArray?, offset: Int) {}
    @JvmStatic fun glUniform1i(location: Int, x: Int) {}
    @JvmStatic fun glUniform1iv(location: Int, count: Int, v: IntArray?, offset: Int) {}
    @JvmStatic fun glUniform2f(location: Int, x: Float, y: Float) {}
    @JvmStatic fun glUniform2fv(location: Int, count: Int, v: FloatArray?, offset: Int) {}
    @JvmStatic fun glUniform2i(location: Int, x: Int, y: Int) {}
    @JvmStatic fun glUniform2iv(location: Int, count: Int, v: IntArray?, offset: Int) {}
    @JvmStatic fun glUniform3f(location: Int, x: Float, y: Float, z: Float) {}
    @JvmStatic fun glUniform3fv(location: Int, count: Int, v: FloatArray?, offset: Int) {}
    @JvmStatic fun glUniform3i(location: Int, x: Int, y: Int, z: Int) {}
    @JvmStatic fun glUniform3iv(location: Int, count: Int, v: IntArray?, offset: Int) {}
    @JvmStatic fun glUniform4f(location: Int, x: Float, y: Float, z: Float, w: Float) {}
    @JvmStatic fun glUniform4fv(location: Int, count: Int, v: FloatArray?, offset: Int) {}
    @JvmStatic fun glUniform4i(location: Int, x: Int, y: Int, z: Int, w: Int) {}
    @JvmStatic fun glUniform4iv(location: Int, count: Int, v: IntArray?, offset: Int) {}
    @JvmStatic fun glUniformMatrix2fv(location: Int, count: Int, transpose: Boolean, value: FloatArray?, offset: Int) {}
    @JvmStatic fun glUniformMatrix3fv(location: Int, count: Int, transpose: Boolean, value: FloatArray?, offset: Int) {}
    @JvmStatic fun glUniformMatrix4fv(location: Int, count: Int, transpose: Boolean, value: FloatArray?, offset: Int) {}
    @JvmStatic fun glUseProgram(program: Int) {}
    @JvmStatic fun glValidateProgram(program: Int) {}
    @JvmStatic fun glVertexAttrib1f(indx: Int, x: Float) {}
    @JvmStatic fun glVertexAttrib1fv(indx: Int, values: FloatArray?, offset: Int) {}
    @JvmStatic fun glVertexAttrib2f(indx: Int, x: Float, y: Float) {}
    @JvmStatic fun glVertexAttrib2fv(indx: Int, values: FloatArray?, offset: Int) {}
    @JvmStatic fun glVertexAttrib3f(indx: Int, x: Float, y: Float, z: Float) {}
    @JvmStatic fun glVertexAttrib3fv(indx: Int, values: FloatArray?, offset: Int) {}
    @JvmStatic fun glVertexAttrib4f(indx: Int, x: Float, y: Float, z: Float, w: Float) {}
    @JvmStatic fun glVertexAttrib4fv(indx: Int, values: FloatArray?, offset: Int) {}
    @JvmStatic fun glVertexAttribPointer(indx: Int, size: Int, type: Int, normalized: Boolean, stride: Int, ptr: java.nio.Buffer?) {}
    @JvmStatic fun glVertexAttribPointer(indx: Int, size: Int, type: Int, normalized: Boolean, stride: Int, offset: Int) {}
    @JvmStatic fun glViewport(x: Int, y: Int, width: Int, height: Int) {}
}

/** android.opengl.Matrix：4x4 列优先矩阵数学（AOSP 算法真实实现）。 */
object Matrix {

    @JvmStatic
    fun multiplyMM(result: FloatArray, resultOffset: Int, lhs: FloatArray, lhsOffset: Int, rhs: FloatArray, rhsOffset: Int) {
        val tmp = FloatArray(16)
        for (i in 0..3) {
            for (j in 0..3) {
                var sum = 0f
                for (k in 0..3) {
                    sum += lhs[lhsOffset + k * 4 + i] * rhs[rhsOffset + j * 4 + k]
                }
                tmp[j * 4 + i] = sum
            }
        }
        for (i in 0..15) result[resultOffset + i] = tmp[i]
    }

    @JvmStatic
    fun multiplyMV(resultVec: FloatArray, resultVecOffset: Int, lhsMat: FloatArray, lhsMatOffset: Int, rhsVec: FloatArray, rhsVecOffset: Int) {
        val tmp = FloatArray(4)
        for (i in 0..3) {
            tmp[i] = lhsMat[lhsMatOffset + i] * rhsVec[rhsVecOffset] +
                lhsMat[lhsMatOffset + 4 + i] * rhsVec[rhsVecOffset + 1] +
                lhsMat[lhsMatOffset + 8 + i] * rhsVec[rhsVecOffset + 2] +
                lhsMat[lhsMatOffset + 12 + i] * rhsVec[rhsVecOffset + 3]
        }
        for (i in 0..3) resultVec[resultVecOffset + i] = tmp[i]
    }

    @JvmStatic
    fun setIdentityM(sm: FloatArray, smOffset: Int) {
        for (i in 0..15) sm[smOffset + i] = 0f
        for (i in 0..15 step 5) sm[smOffset + i] = 1.0f
    }

    @JvmStatic
    fun transposeM(mTrans: FloatArray, mTransOffset: Int, m: FloatArray, mOffset: Int) {
        for (i in 0..3) {
            for (j in 0..3) {
                mTrans[mTransOffset + j * 4 + i] = m[mOffset + i * 4 + j]
            }
        }
    }

    @JvmStatic
    fun invertM(mInv: FloatArray, mInvOffset: Int, m: FloatArray, mOffset: Int): Boolean {
        val src = FloatArray(16) { m[mOffset + it] }
        // 按列优先计算逆矩阵（高斯-约当）
        val inv = FloatArray(16)
        setIdentityM(inv, 0)
        val a = src.copyOf()
        for (col in 0..3) {
            // 选主元
            var maxRow = col
            var maxVal = kotlin.math.abs(a[col * 4 + col])
            for (row in col + 1..3) {
                val v = kotlin.math.abs(a[col * 4 + row])
                if (v > maxVal) { maxVal = v; maxRow = row }
            }
            if (maxVal < 1e-10f) return false
            if (maxRow != col) {
                for (k in 0..3) {
                    var t = a[k * 4 + col]; a[k * 4 + col] = a[k * 4 + maxRow]; a[k * 4 + maxRow] = t
                    t = inv[k * 4 + col]; inv[k * 4 + col] = inv[k * 4 + maxRow]; inv[k * 4 + maxRow] = t
                }
            }
            val pivot = a[col * 4 + col]
            for (k in 0..3) {
                a[k * 4 + col] /= pivot
                inv[k * 4 + col] /= pivot
            }
            for (row in 0..3) {
                if (row == col) continue
                val factor = a[col * 4 + row]
                for (k in 0..3) {
                    a[k * 4 + row] -= factor * a[k * 4 + col]
                    inv[k * 4 + row] -= factor * inv[k * 4 + col]
                }
            }
        }
        for (i in 0..15) mInv[mInvOffset + i] = inv[i]
        return true
    }

    @JvmStatic
    fun orthoM(m: FloatArray, mOffset: Int, left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
        val rWidth = 1.0f / (right - left)
        val rHeight = 1.0f / (top - bottom)
        val rDepth = 1.0f / (far - near)
        val x = 2.0f * rWidth
        val y = 2.0f * rHeight
        val z = -2.0f * rDepth
        val tx = -(right + left) * rWidth
        val ty = -(top + bottom) * rHeight
        val tz = -(far + near) * rDepth
        for (i in 0..15) m[mOffset + i] = 0f
        m[mOffset] = x
        m[mOffset + 5] = y
        m[mOffset + 10] = z
        m[mOffset + 12] = tx
        m[mOffset + 13] = ty
        m[mOffset + 14] = tz
        m[mOffset + 15] = 1.0f
    }

    @JvmStatic
    fun frustumM(m: FloatArray, offset: Int, left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float) {
        require(left != right) { "left == right" }
        require(bottom != top) { "bottom == top" }
        require(near != far) { "near == far" }
        require(near > 0) { "near <= 0" }
        require(far > 0) { "far <= 0" }
        val rWidth = 1.0f / (right - left)
        val rHeight = 1.0f / (top - bottom)
        val rDepth = 1.0f / (near - far)
        val x = 2.0f * (near * rWidth)
        val y = 2.0f * (near * rHeight)
        val a = (right + left) * rWidth
        val b = (top + bottom) * rHeight
        val c = (far + near) * rDepth
        val d = 2.0f * (far * near * rDepth)
        for (i in 0..15) m[offset + i] = 0f
        m[offset] = x
        m[offset + 5] = y
        m[offset + 8] = a
        m[offset + 9] = b
        m[offset + 10] = c
        m[offset + 14] = d
        m[offset + 11] = -1.0f
    }

    @JvmStatic
    fun perspectiveM(m: FloatArray, offset: Int, fovy: Float, aspect: Float, zNear: Float, zFar: Float) {
        val f = 1.0f / kotlin.math.tan(Math.toRadians(fovy.toDouble())).toFloat()
        val rangeReciprocal = 1.0f / (zNear - zFar)
        for (i in 0..15) m[offset + i] = 0f
        m[offset] = f / aspect
        m[offset + 5] = f
        m[offset + 10] = (zFar + zNear) * rangeReciprocal
        m[offset + 11] = -1.0f
        m[offset + 14] = 2.0f * zFar * zNear * rangeReciprocal
    }

    @JvmStatic
    fun setLookAtM(rm: FloatArray, rmOffset: Int, eyeX: Float, eyeY: Float, eyeZ: Float, centerX: Float, centerY: Float, centerZ: Float, upX: Float, upY: Float, upZ: Float) {
        var fx = centerX - eyeX
        var fy = centerY - eyeY
        var fz = centerZ - eyeZ
        val rlf = 1.0f / length(fx, fy, fz)
        fx *= rlf; fy *= rlf; fz *= rlf
        var sx = fy * upZ - fz * upY
        var sy = fz * upX - fx * upZ
        var sz = fx * upY - fy * upX
        val rls = 1.0f / length(sx, sy, sz)
        sx *= rls; sy *= rls; sz *= rls
        val ux = sy * fz - sz * fy
        val uy = sz * fx - sx * fz
        val uz = sx * fy - sy * fx
        rm[rmOffset] = sx; rm[rmOffset + 1] = ux; rm[rmOffset + 2] = -fx; rm[rmOffset + 3] = 0f
        rm[rmOffset + 4] = sy; rm[rmOffset + 5] = uy; rm[rmOffset + 6] = -fy; rm[rmOffset + 7] = 0f
        rm[rmOffset + 8] = sz; rm[rmOffset + 9] = uz; rm[rmOffset + 10] = -fz; rm[rmOffset + 11] = 0f
        rm[rmOffset + 12] = 0f; rm[rmOffset + 13] = 0f; rm[rmOffset + 14] = 0f; rm[rmOffset + 15] = 1f
        translateM(rm, rmOffset, -eyeX, -eyeY, -eyeZ)
    }

    @JvmStatic
    fun length(x: Float, y: Float, z: Float): Float =
        kotlin.math.sqrt(x * x + y * y + z * z)

    @JvmStatic
    fun translateM(m: FloatArray, mOffset: Int, x: Float, y: Float, z: Float) {
        for (i in 0..3) {
            val mi = mOffset + 12 + i
            m[mi] += m[mOffset + i] * x + m[mOffset + 4 + i] * y + m[mOffset + 8 + i] * z
        }
    }

    @JvmStatic
    fun translateM(m: FloatArray, mOffset: Int, tm: FloatArray, tmOffset: Int, x: Float, y: Float, z: Float) {
        for (i in 0..11) tm[tmOffset + i] = m[mOffset + i]
        for (i in 0..3) {
            val tmi = tmOffset + 12 + i
            val mi = mOffset + i
            tm[tmi] = m[mi + 12] + m[mi] * x + m[mi + 4] * y + m[mi + 8] * z
        }
    }

    @JvmStatic
    fun scaleM(m: FloatArray, mOffset: Int, x: Float, y: Float, z: Float) {
        for (i in 0..3) {
            val mi = mOffset + i
            m[mi] *= x
            m[mi + 4] *= y
            m[mi + 8] *= z
        }
    }

    @JvmStatic
    fun scaleM(sm: FloatArray, smOffset: Int, m: FloatArray, mOffset: Int, x: Float, y: Float, z: Float) {
        for (i in 0..3) {
            val smi = smOffset + i
            val mi = mOffset + i
            sm[smi] = m[mi] * x
            sm[smi + 4] = m[mi + 4] * y
            sm[smi + 8] = m[mi + 8] * z
            sm[smi + 12] = m[mi + 12]
        }
    }

    @JvmStatic
    fun rotateM(m: FloatArray, mOffset: Int, a: Float, x: Float, y: Float, z: Float) {
        val rm = FloatArray(16)
        setRotateM(rm, 0, a, x, y, z)
        val tmp = FloatArray(16) { m[mOffset + it] }
        multiplyMM(m, mOffset, tmp, 0, rm, 0)
    }

    @JvmStatic
    fun rotateM(rm: FloatArray, rmOffset: Int, m: FloatArray, mOffset: Int, a: Float, x: Float, y: Float, z: Float) {
        val r = FloatArray(16)
        setRotateM(r, 0, a, x, y, z)
        multiplyMM(rm, rmOffset, m, mOffset, r, 0)
    }

    @JvmStatic
    fun setRotateM(rm: FloatArray, rmOffset: Int, a: Float, x: Float, y: Float, z: Float) {
        rm[rmOffset + 3] = 0f; rm[rmOffset + 7] = 0f; rm[rmOffset + 11] = 0f
        rm[rmOffset + 12] = 0f; rm[rmOffset + 13] = 0f; rm[rmOffset + 14] = 0f
        rm[rmOffset + 15] = 1f
        var angle = a
        angle = (angle * (Math.PI / 180.0)).toFloat()
        val s = kotlin.math.sin(angle)
        val c = kotlin.math.cos(angle)
        if (1.0f == x && 0.0f == y && 0.0f == z) {
            rm[rmOffset + 5] = c; rm[rmOffset + 10] = c
            rm[rmOffset + 6] = s; rm[rmOffset + 9] = -s
            rm[rmOffset + 1] = 0f; rm[rmOffset + 2] = 0f
            rm[rmOffset + 4] = 0f; rm[rmOffset + 8] = 0f
            rm[rmOffset] = 1f
        } else if (0.0f == x && 1.0f == y && 0.0f == z) {
            rm[rmOffset] = c; rm[rmOffset + 10] = c
            rm[rmOffset + 8] = s; rm[rmOffset + 2] = -s
            rm[rmOffset + 1] = 0f; rm[rmOffset + 4] = 0f
            rm[rmOffset + 6] = 0f; rm[rmOffset + 9] = 0f
            rm[rmOffset + 5] = 1f
        } else if (0.0f == x && 0.0f == y && 1.0f == z) {
            rm[rmOffset] = c; rm[rmOffset + 5] = c
            rm[rmOffset + 1] = s; rm[rmOffset + 4] = -s
            rm[rmOffset + 2] = 0f; rm[rmOffset + 6] = 0f
            rm[rmOffset + 8] = 0f; rm[rmOffset + 9] = 0f
            rm[rmOffset + 10] = 1f
        } else {
            val len = length(x, y, z)
            var nx = x; var ny = y; var nz = z
            if (1.0f != len) {
                val recipLen = 1.0f / len
                nx *= recipLen; ny *= recipLen; nz *= recipLen
            }
            val nc = 1.0f - c
            val xy = nx * ny
            val yz = ny * nz
            val zx = nz * nx
            val xs = nx * s
            val ys = ny * s
            val zs = nz * s
            rm[rmOffset] = nx * nx * nc + c
            rm[rmOffset + 4] = xy * nc - zs
            rm[rmOffset + 8] = zx * nc + ys
            rm[rmOffset + 1] = xy * nc + zs
            rm[rmOffset + 5] = ny * ny * nc + c
            rm[rmOffset + 9] = yz * nc - xs
            rm[rmOffset + 2] = zx * nc - ys
            rm[rmOffset + 6] = yz * nc + xs
            rm[rmOffset + 10] = nz * nz * nc + c
        }
    }
}

/** android.opengl.GLSurfaceView：编译级 stub。 */
open class GLSurfaceView : View {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    interface Renderer {
        fun onSurfaceCreated(gl: javax.microedition.khronos.opengles.GL10?, config: javax.microedition.khronos.egl.EGLConfig?)
        fun onSurfaceChanged(gl: javax.microedition.khronos.opengles.GL10?, width: Int, height: Int)
        fun onDrawFrame(gl: javax.microedition.khronos.opengles.GL10?)
    }

    private var renderer: Renderer? = null
    private var renderMode = RENDERMODE_CONTINUOUSLY

    open fun setRenderer(renderer: Renderer?) { this.renderer = renderer }
    open fun setRenderMode(mode: Int) { renderMode = mode }
    open fun getRenderMode(): Int = renderMode
    open fun requestRender() {}
    open fun queueEvent(r: Runnable?) { r?.run() }
    open fun onPause() {}
    open fun onResume() {}
    open fun setEGLContextClientVersion(version: Int) {}
    open fun setPreserveEGLContextOnPause(preserve: Boolean) {}
    open fun setEGLConfigChooser(needDepth: Boolean) {}
    open fun setEGLConfigChooser(red: Int, green: Int, blue: Int, alpha: Int, depth: Int, stencil: Int) {}
    open fun setDebugFlags(flags: Int) {}

    companion object {
        const val RENDERMODE_WHEN_DIRTY = 0
        const val RENDERMODE_CONTINUOUSLY = 1
        const val DEBUG_CHECK_GL_ERROR = 1
        const val DEBUG_LOG_GL_CALLS = 2
    }
}

/** android.opengl EGL 轻 stub。 */
open class EGLContext
open class EGLDisplay
open class EGLSurface

object EGL14 {
    val EGL_NO_CONTEXT: EGLContext? = null
    val EGL_NO_DISPLAY: EGLDisplay? = null
    val EGL_NO_SURFACE: EGLSurface? = null
    const val EGL_DEFAULT_DISPLAY = 0
    const val EGL_OPENGL_ES2_BIT = 4
    const val EGL_NONE = 0x3038
    const val EGL_RED_SIZE = 0x3024
    const val EGL_GREEN_SIZE = 0x3023
    const val EGL_BLUE_SIZE = 0x3022
    const val EGL_ALPHA_SIZE = 0x3021
    const val EGL_DEPTH_SIZE = 0x3025
    const val EGL_STENCIL_SIZE = 0x3026
    const val EGL_RENDERABLE_TYPE = 0x3040
    const val EGL_SURFACE_TYPE = 0x3033
    const val EGL_PBUFFER_BIT = 1
    const val EGL_WINDOW_BIT = 4
    const val EGL_WIDTH = 0x3057
    const val EGL_HEIGHT = 0x3056
    const val EGL_BACK_BUFFER = 0x3084
    const val EGL_CONTEXT_CLIENT_VERSION = 0x3098

    @JvmStatic fun eglGetDisplay(displayId: Int): EGLDisplay = EGLDisplay()
    @JvmStatic fun eglGetCurrentContext(): EGLContext? = null
    @JvmStatic fun eglGetCurrentDisplay(): EGLDisplay? = null
    @JvmStatic fun eglGetCurrentSurface(readdraw: Int): EGLSurface? = null
    @JvmStatic fun eglGetError(): Int = 0x3000
    @JvmStatic fun eglInitialize(display: EGLDisplay?, major: IntArray?, majorOffset: Int, minor: IntArray?, minorOffset: Int): Boolean = false
    @JvmStatic fun eglTerminate(display: EGLDisplay?): Boolean = false
    @JvmStatic fun eglCreateContext(display: EGLDisplay?, config: javax.microedition.khronos.egl.EGLConfig?, shareContext: EGLContext?, attribList: IntArray?, offset: Int): EGLContext = EGLContext()
    @JvmStatic fun eglDestroyContext(display: EGLDisplay?, context: EGLContext?): Boolean = false
    @JvmStatic fun eglCreateWindowSurface(display: EGLDisplay?, config: javax.microedition.khronos.egl.EGLConfig?, surface: Any?, attribList: IntArray?, offset: Int): EGLSurface = EGLSurface()
    @JvmStatic fun eglCreatePbufferSurface(display: EGLDisplay?, config: javax.microedition.khronos.egl.EGLConfig?, attribList: IntArray?, offset: Int): EGLSurface = EGLSurface()
    @JvmStatic fun eglDestroySurface(display: EGLDisplay?, surface: EGLSurface?): Boolean = false
    @JvmStatic fun eglMakeCurrent(display: EGLDisplay?, draw: EGLSurface?, read: EGLSurface?, context: EGLContext?): Boolean = false
    @JvmStatic fun eglSwapBuffers(display: EGLDisplay?, surface: EGLSurface?): Boolean = false
    @JvmStatic fun eglChooseConfig(display: EGLDisplay?, attribList: IntArray?, attribListOffset: Int, configs: Array<out javax.microedition.khronos.egl.EGLConfig?>?, configsOffset: Int, configSize: Int, numConfig: IntArray?, numConfigOffset: Int): Boolean = false
    @JvmStatic fun eglQuerySurface(display: EGLDisplay?, surface: EGLSurface?, attribute: Int, value: IntArray?, offset: Int): Boolean = false
}

/** android.opengl.GLUtils：位图→纹理上传 no-op（桌面无 GL 上下文）。 */
object GLUtils {
    @JvmStatic fun texImage2D(target: Int, level: Int, bitmap: android.graphics.Bitmap?, border: Int) {}
    @JvmStatic fun texImage2D(target: Int, level: Int, internalFormat: Int, bitmap: android.graphics.Bitmap?, border: Int) {}
    @JvmStatic fun texImage2D(target: Int, level: Int, internalFormat: Int, bitmap: android.graphics.Bitmap?, type: Int, border: Int) {}
    @JvmStatic fun texSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, bitmap: android.graphics.Bitmap?) {}
    @JvmStatic fun texSubImage2D(target: Int, level: Int, xoffset: Int, yoffset: Int, bitmap: android.graphics.Bitmap?, format: Int, type: Int) {}
    @JvmStatic fun getInternalFormat(bitmap: android.graphics.Bitmap?): Int = 0
    @JvmStatic fun getType(bitmap: android.graphics.Bitmap?): Int = 0
    @JvmStatic fun getEGLErrorString(error: Int): String = "0x${Integer.toHexString(error)}"
}
