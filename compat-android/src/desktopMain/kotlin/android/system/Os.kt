package android.system

import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/** android.system.ErrnoException。 */
class ErrnoException(val functionName: String, val errno: Int, cause: Throwable? = null) :
    Exception("$functionName failed: errno=$errno", cause) {

    constructor(functionName: String, errno: Int) : this(functionName, errno, null)

    fun rethrowAsIOException(): java.io.IOException = java.io.IOException(message, this)
}

/** android.system.OsConstants：常用常量。 */
object OsConstants {
    const val S_IRUSR = 256
    const val S_IWUSR = 128
    const val S_IXUSR = 64
    const val S_IRWXU = 448
    const val S_IRGRP = 32
    const val S_IWGRP = 16
    const val S_IXGRP = 8
    const val S_IRWXG = 56
    const val S_IROTH = 4
    const val S_IWOTH = 2
    const val S_IXOTH = 1
    const val S_IRWXO = 7
    const val S_IFMT = 61440
    const val S_IFREG = 32768
    const val S_IFDIR = 16384
    const val S_IFLNK = 40960
    const val O_RDONLY = 0
    const val O_WRONLY = 1
    const val O_RDWR = 2
    const val O_CREAT = 64
    const val O_EXCL = 128
    const val O_TRUNC = 512
    const val O_APPEND = 1024
    const val O_NONBLOCK = 2048
    const val SEEK_SET = 0
    const val SEEK_CUR = 1
    const val SEEK_END = 2
    const val F_OK = 0
    const val X_OK = 1
    const val W_OK = 2
    const val R_OK = 4
    const val EXIT_SUCCESS = 0
    const val EXIT_FAILURE = 1
    const val EACCES = 13
    const val EEXIST = 17
    const val ENOENT = 2
    const val ENOTDIR = 20
    const val EISDIR = 21
    const val EPERM = 1
    const val POLLIN = 1
    const val POLLOUT = 4
    const val POLLERR = 8
    const val SOL_SOCKET = 1
    const val SO_REUSEADDR = 2
    const val SO_RCVTIMEO = 20
    const val SO_SNDTIMEO = 21
    const val SHUT_RD = 0
    const val SHUT_WR = 1
    const val SHUT_RDWR = 2
    const val SOCK_CLOEXEC = 0x80000
    const val MSG_DONTWAIT = 0x40
    const val AF_INET = 2
    const val AF_INET6 = 10
    const val AF_UNIX = 1
    const val SOCK_STREAM = 1
    const val SOCK_DGRAM = 2
    const val SIGTERM = 15
    const val SIGKILL = 9
    const val SIGINT = 2
    const val SIGHUP = 1

    @JvmStatic fun errnoName(errno: Int): String = "errno$errno"
    @JvmStatic fun gaiName(error: Int): String = "gai$error"
}

/** android.system.StructStat 轻 stub。 */
class StructStat(
    val st_dev: Long = 0,
    val st_ino: Long = 0,
    val st_mode: Int = 0,
    val st_nlink: Long = 0,
    val st_uid: Int = 0,
    val st_gid: Int = 0,
    val st_rdev: Long = 0,
    val st_size: Long = 0,
    val st_blksize: Long = 4096,
    val st_blocks: Long = 0,
    val st_atime: Long = 0,
    val st_mtime: Long = 0,
    val st_ctime: Long = 0,
)

/** android.system.Os：文件系统原语的可行子集。 */
object Os {
    /** 向进程发信号。桌面映射：正 pid 用 ProcessHandle；负 pid（进程组）尽力而为。 */
    /** setenv：设置环境变量（桌面 JVM 不支持运行时改环境，no-op）。——Nova 注 */
    @JvmStatic
    fun setenv(name: String, value: String, overwrite: Boolean) {
        // JVM 不提供运行时 setenv；忽略
    }
    @Throws(ErrnoException::class)
    fun kill(pid: Int, signal: Int) {
        val target = kotlin.math.abs(pid).toLong()
        val handle = java.util.Optional.ofNullable(ProcessHandle.of(target).orElse(null))
        if (handle.isPresent) {
            val p = handle.get()
            if (signal == OsConstants.SIGKILL) p.destroyForcibly() else p.destroy()
        } else {
            android.util.Log.w("Os", "kill($pid, $signal): 进程不存在，忽略")
        }
    }

    @Throws(ErrnoException::class)
    fun chmod(path: String, mode: Int) {
        val f = File(path)
        f.setReadable(OsConstants.S_IRUSR and mode != 0, true)
        f.setWritable(OsConstants.S_IWUSR and mode != 0, true)
        f.setExecutable(OsConstants.S_IXUSR and mode != 0, true)
    }

    fun stat(path: String): StructStat {
        val f = File(path)
        return StructStat(
            st_mode = when {
                f.isDirectory -> OsConstants.S_IFDIR
                f.isFile -> OsConstants.S_IFREG
                else -> 0
            },
            st_size = f.length(),
            st_mtime = f.lastModified() / 1000,
        )
    }

    fun lstat(path: String): StructStat = stat(path)

    @Throws(ErrnoException::class)
    fun symlink(oldPath: String, newPath: String) {
        try {
            Files.createSymbolicLink(Paths.get(newPath), Paths.get(oldPath))
        } catch (e: Exception) {
            android.util.Log.d("Os", "symlink not supported: ${e.message}")
        }
    }

    fun readlink(path: String): String =
        try { Files.readSymbolicLink(Paths.get(path)).toString() } catch (e: Exception) { path }

    fun access(path: String, mode: Int): Boolean {
        val f = File(path)
        if (!f.exists()) return false
        if (mode and OsConstants.R_OK != 0 && !f.canRead()) return false
        if (mode and OsConstants.W_OK != 0 && !f.canWrite()) return false
        if (mode and OsConstants.X_OK != 0 && !f.canExecute()) return false
        return true
    }

    @Throws(ErrnoException::class)
    fun mkdir(path: String, mode: Int) { File(path).mkdirs() }

    fun getpid(): Int = android.os.Process.myPid()
    fun gettid(): Int = android.os.Process.myTid()
    fun getuid(): Int = 1000
    fun getgid(): Int = 1000
    fun geteuid(): Int = 1000
    fun getegid(): Int = 1000
    fun getppid(): Int = 0
    fun isatty(fd: java.io.FileDescriptor?): Boolean = false
    fun strerror(errno: Int): String = "errno $errno"

    fun uname(): StructUtsname = StructUtsname()

    class StructUtsname(
        val sysname: String = "Windows",
        val nodename: String = "localhost",
        val release: String = "11",
        val version: String = "11",
        val machine: String = "x86_64",
    )

    // ============ Socket API（LocalWebServer 的本地 HTTP 服务器用）——Nova 注 ============
    // 桌面实现：FileDescriptor → java.net 套接字 注册表。
    private val socketRegistry = java.util.concurrent.ConcurrentHashMap<java.io.FileDescriptor, Any>()

    @Throws(ErrnoException::class)
    fun socket(domain: Int, type: Int, protocol: Int): java.io.FileDescriptor {
        val fd = java.io.FileDescriptor()
        socketRegistry[fd] = SocketState() // 未绑定状态
        return fd
    }

    /** 内部 socket 状态：未绑定 / ServerSocket（监听）/ Socket（连接）。 */
    private class SocketState {
        var server: java.net.ServerSocket? = null
        var socket: java.net.Socket? = null
        var soTimeout: Int = 0
    }

    private fun state(fd: java.io.FileDescriptor): SocketState =
        socketRegistry[fd] as? SocketState ?: throw ErrnoException("socket", 9 /* EBADF */)

    @Throws(ErrnoException::class)
    fun bind(fd: java.io.FileDescriptor, address: java.net.InetAddress?, port: Int) {
        val st = state(fd)
        try {
            val server = java.net.ServerSocket()
            server.reuseAddress = true
            server.bind(java.net.InetSocketAddress(address, port))
            st.server = server
        } catch (e: Exception) { throw ErrnoException("bind", 98 /* EADDRINUSE */, e) }
    }

    @Throws(ErrnoException::class)
    fun listen(fd: java.io.FileDescriptor, backlog: Int) { /* ServerSocket 在 bind 时即监听 */ }

    @Throws(ErrnoException::class)
    fun accept(fd: java.io.FileDescriptor, address: java.net.InetSocketAddress?): java.io.FileDescriptor {
        val st = state(fd)
        val server = st.server ?: throw ErrnoException("accept", 22)
        return try {
            val conn = server.accept()
            if (st.soTimeout > 0) conn.soTimeout = st.soTimeout
            val cfd = java.io.FileDescriptor()
            val cst = SocketState(); cst.socket = conn; cst.soTimeout = st.soTimeout
            socketRegistry[cfd] = cst
            cfd
        } catch (e: Exception) { throw ErrnoException("accept", 11 /* EAGAIN */, e) }
    }

    @Throws(ErrnoException::class)
    fun read(fd: java.io.FileDescriptor, buffer: ByteArray, offset: Int, length: Int): Int {
        val conn = state(fd).socket ?: throw ErrnoException("read", 32 /* EPIPE */)
        return try { conn.getInputStream().read(buffer, offset, length) } catch (e: java.net.SocketTimeoutException) { throw ErrnoException("read", 11, e) } catch (e: Exception) { throw ErrnoException("read", 5, e) }
    }

    @Throws(ErrnoException::class)
    fun write(fd: java.io.FileDescriptor, buffer: ByteArray, offset: Int, length: Int): Int {
        val conn = state(fd).socket ?: throw ErrnoException("write", 32)
        return try { conn.getOutputStream().write(buffer, offset, length); length } catch (e: Exception) { throw ErrnoException("write", 32, e) }
    }

    fun close(fd: java.io.FileDescriptor) {
        socketRegistry.remove(fd)?.let { (it as? SocketState)?.let { s -> runCatching { s.server?.close(); s.socket?.close() } } }
    }

    fun shutdown(fd: java.io.FileDescriptor, how: Int) {
        state(fd).socket?.let { s ->
            runCatching {
                if (how == OsConstants.SHUT_RD || how == OsConstants.SHUT_RDWR) s.shutdownInput()
                if (how == OsConstants.SHUT_WR || how == OsConstants.SHUT_RDWR) s.shutdownOutput()
            }
        }
    }

    fun getsockname(fd: java.io.FileDescriptor): java.net.SocketAddress? {
        val st = state(fd)
        return st.server?.localSocketAddress ?: st.socket?.localSocketAddress
    }

    fun setsockoptInt(fd: java.io.FileDescriptor, level: Int, option: Int, value: Int) {
        state(fd).let { st ->
            if (option == OsConstants.SO_REUSEADDR) { st.server?.reuseAddress = value != 0; st.socket?.reuseAddress = value != 0 }
            if (option == OsConstants.SO_RCVTIMEO) { st.soTimeout = value; st.socket?.soTimeout = value }
        }
    }

    fun setsockoptTimeval(fd: java.io.FileDescriptor, level: Int, option: Int, timeval: StructTimeval?) {
        val ms = timeval?.toMillis()?.toInt() ?: 0
        state(fd).let { st -> st.soTimeout = ms; st.socket?.soTimeout = ms }
    }
}

/** android.system.StructTimeval（setsockopt SO_RCVTIMEO 用）。——Nova 注 */
class StructTimeval(val tv_sec: Long, val tv_usec: Long) {
    fun toMillis(): Long = tv_sec * 1000 + tv_usec / 1000
    companion object {
        @JvmStatic fun fromMillis(millis: Long): StructTimeval = StructTimeval(millis / 1000, (millis % 1000) * 1000)
    }
}
