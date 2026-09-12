package android.provider

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri

/** android.provider.MediaStore：常量 + 尽力文件化查询。 */
object MediaStore {

    const val ACTION_IMAGE_CAPTURE = "android.media.action.IMAGE_CAPTURE"
    const val ACTION_VIDEO_CAPTURE = "android.media.action.VIDEO_CAPTURE"
    const val ACTION_PICK_IMAGES = "android.provider.action.PICK_IMAGES"
    const val ACTION_PICK_IMAGES_SETTINGS = "android.provider.action.PICK_IMAGES_SETTINGS"
    const val ACTION_REVIEW = "android.provider.action.REVIEW"
    const val ACTION_IMAGE_CAPTURE_SECURE = "android.media.action.IMAGE_CAPTURE_SECURE"
    const val ACTION_STILL_IMAGE_CAMERA = "android.media.action.STILL_IMAGE_CAMERA"
    const val ACTION_STILL_IMAGE_CAMERA_SECURE = "android.media.action.STILL_IMAGE_CAMERA_SECURE"
    const val ACTION_VIDEO_CAMERA = "android.media.action.VIDEO_CAMERA"
    const val INTENT_ACTION_MUSIC_PLAYER = "android.intent.action.MUSIC_PLAYER"
    const val INTENT_ACTION_MEDIA_SEARCH = "android.intent.action.MEDIA_SEARCH"
    const val INTENT_ACTION_TEXT_OPEN_FROM_SEARCH = "android.intent.action.TEXT_OPEN_FROM_SEARCH"
    const val INTENT_ACTION_VIDEO_PLAY_FROM_SEARCH = "android.intent.action.VIDEO_PLAY_FROM_SEARCH"
    const val INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH = "android.intent.action.MEDIA_PLAY_FROM_SEARCH"
    const val INTENT_ACTION_STILL_IMAGE_CAMERA = "android.media.action.STILL_IMAGE_CAMERA"
    const val INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE = "android.media.action.STILL_IMAGE_CAMERA_SECURE"

    const val EXTRA_OUTPUT = "output"
    const val EXTRA_VIDEO_QUALITY = "android.intent.extra.videoQuality"
    const val EXTRA_SIZE_LIMIT = "android.intent.extra.sizeLimit"
    const val EXTRA_DURATION_LIMIT = "android.intent.extra.durationLimit"
    const val EXTRA_SCREEN_ORIENTATION = "android.intent.extra.screenOrientation"
    const val EXTRA_FULL_SCREEN = "android.intent.extra.fullScreen"
    const val EXTRA_SHOW_ACTION_ICONS = "android.intent.extra.showActionIcons"
    const val EXTRA_FINISH_ON_COMPLETION = "android.intent.extra.finishOnCompletion"
    const val EXTRA_MEDIA_CAPTION = "android.intent.extra.CAPTION"
    const val EXTRA_PICK_IMAGES_MAX = "android.provider.extra.PICK_IMAGES_MAX"
    const val EXTRA_PICK_IMAGES_LAUNCH_TEXT_AND_VISUAL = "android.provider.extra.PICK_IMAGES_LAUNCH_TEXT_AND_VISUAL"
    const val EXTRA_PICK_IMAGES_ACCENT_COLOR = "android.provider.extra.PICK_IMAGES_ACCENT_COLOR"
    const val EXTRA_PICK_IMAGES_IN_ORDER = "android.provider.extra.PICK_IMAGES_IN_ORDER"
    const val EXTRA_CALLER_PACKAGE = "android.provider.extra.CALLER_PACKAGE"
    const val EXTRA_CALLER_PACKAGE_NAME = "android.provider.extra.CALLER_PACKAGE_NAME"

    const val VOLUME_INTERNAL = "internal"
    const val VOLUME_EXTERNAL = "external"
    const val VOLUME_EXTERNAL_PRIMARY = "external_primary"

    const val AUTHORITY = "media"
    const val AUTHORITY_LEGACY = "media_legacy"

    const val MEDIA_SCANNER_VOLUME = "volume"
    const val MEDIA_SCANNER_FILE = "file"

    @JvmStatic
    fun getMediaScannerUri(): Uri = Uri.parse("content://media/none/media_scanner")

    @JvmStatic
    fun getVolumeName(uri: Uri): String = uri.pathSegments.firstOrNull() ?: VOLUME_EXTERNAL

    @JvmStatic
    fun getVersion(context: android.content.Context): String = "1.0"

    @JvmStatic
    fun getVersion(context: android.content.Context, volumeName: String): String = "1.0"

    @JvmStatic
    fun setIncludePending(uri: Uri): Uri = uri

    @JvmStatic
    fun setRequireOriginal(uri: Uri): Uri = uri

    @JvmStatic
    fun getExternalVolumeNames(context: android.content.Context): Set<String> = setOf(VOLUME_EXTERNAL_PRIMARY)

    @JvmStatic
    fun getRecentExternalVolumeNames(context: android.content.Context): Set<String> = setOf(VOLUME_EXTERNAL_PRIMARY)

    @JvmStatic
    fun getOwnerPackageName(uri: Uri): String? = null

    @JvmStatic
    fun getRedactedUri(resolver: ContentResolver, uri: Uri): Uri = uri

    /** MediaColumns 基础列。 */
    interface MediaColumns {
        companion object {
            const val _ID = "_id"
            const val _COUNT = "_count"
            const val DATA = "_data"
            const val SIZE = "_size"
            const val DISPLAY_NAME = "_display_name"
            const val TITLE = "title"
            const val DATE_ADDED = "date_added"
            const val DATE_MODIFIED = "date_modified"
            const val DATE_EXPIRES = "date_expires"
            const val DATE_TAKEN = "datetaken"
            const val MIME_TYPE = "mime_type"
            const val WIDTH = "width"
            const val HEIGHT = "height"
            const val RESOLUTION = "resolution"
            const val ORIENTATION = "orientation"
            const val DURATION = "duration"
            const val OWNER_PACKAGE_NAME = "owner_package_name"
            const val VOLUME_NAME = "volume_name"
            const val RELATIVE_PATH = "relative_path"
            const val IS_PENDING = "is_pending"
            const val IS_TRASHED = "is_trashed"
            const val IS_DRM = "is_drm"
            const val IS_FAVORITE = "is_favorite"
            const val GROUP_ID = "group_id"
            const val PRIMARY_DIRECTORY = "primary_directory"
            const val SECONDARY_DIRECTORY = "secondary_directory"
            const val DOCUMENT_ID = "document_id"
            const val INSTANCE_ID = "instance_id"
            const val ORIGINAL_DOCUMENT_ID = "original_document_id"
            const val BUCKET_ID = "bucket_id"
            const val BUCKET_DISPLAY_NAME = "bucket_display_name"
        }
    }

    /** Images。 */
    object Images {
        interface ImageColumns {
            companion object {
                const val DESCRIPTION = "description"
                const val PICASA_ID = "picasa_id"
                const val IS_PRIVATE = "isprivate"
                const val LATITUDE = "latitude"
                const val LONGITUDE = "longitude"
                const val DATE_TAKEN = "datetaken"
                const val ORIENTATION = "orientation"
                const val MINI_THUMB_MAGIC = "mini_thumb_magic"
                const val BUCKET_ID = "bucket_id"
                const val BUCKET_DISPLAY_NAME = "bucket_display_name"
                const val GROUP_ID = "group_id"
                const val INDEX_IN_BUCKET = "index_in_bucket"
                const val TITLE = "title"
            }
        }

        object Media {
            @JvmField val EXTERNAL_CONTENT_URI: Uri = Uri.parse("content://media/external/images/media")
            @JvmField val INTERNAL_CONTENT_URI: Uri = Uri.parse("content://media/internal/images/media")
            @JvmField val CONTENT_URI: Uri = EXTERNAL_CONTENT_URI
            const val DEFAULT_SORT_ORDER = "date_modified DESC"

            @JvmStatic fun getContentUri(volumeName: String): Uri =
                Uri.parse("content://media/$volumeName/images/media")

            @JvmStatic
            fun getBitmap(cr: ContentResolver, url: Uri): Bitmap? = try {
                cr.openInputStream(url)?.use { BitmapFactory.decodeStream(it) }
            } catch (e: Exception) {
                null
            }

            // 列名（自含副本，兼容 MediaStore.Images.Media._ID 访问）
            const val _ID = "_id"
            const val DATA = "_data"
            const val SIZE = "_size"
            const val DISPLAY_NAME = "_display_name"
            const val TITLE = "title"
            const val DATE_ADDED = "date_added"
            const val DATE_MODIFIED = "date_modified"
            const val MIME_TYPE = "mime_type"
            const val WIDTH = "width"
            const val HEIGHT = "height"
            const val ORIENTATION = "orientation"
            const val BUCKET_ID = "bucket_id"
            const val BUCKET_DISPLAY_NAME = "bucket_display_name"
            const val DATE_TAKEN = "datetaken"
            const val DESCRIPTION = "description"
            const val LATITUDE = "latitude"
            const val LONGITUDE = "longitude"
        }

        object Thumbnails {
            @JvmField val EXTERNAL_CONTENT_URI: Uri = Uri.parse("content://media/external/images/thumbnails")
            const val FULL_SCREEN_KIND = 2
            const val MINI_KIND = 1
            const val MICRO_KIND = 3
            const val _ID = "_id"
            const val DATA = "_data"
            const val IMAGE_ID = "image_id"
            const val KIND = "kind"
            const val WIDTH = "width"
            const val HEIGHT = "height"
        }
    }

    /** MediaStore.Downloads（下载目录内容提供者）。——Nova 注 */
    object Downloads {
        @JvmField val EXTERNAL_CONTENT_URI: Uri = Uri.parse("content://media/external/downloads")
        @JvmField val INTERNAL_CONTENT_URI: Uri = Uri.parse("content://media/internal/downloads")
        @JvmField val CONTENT_URI: Uri = EXTERNAL_CONTENT_URI
    }

    /** Video。 */
    object Video {
        interface VideoColumns {
            companion object {
                const val ALBUM = "album"
                const val ARTIST = "artist"
                const val BOOKMARK = "bookmark"
                const val CATEGORY = "category"
                const val DESCRIPTION = "description"
                const val DURATION = "duration"
                const val IS_PRIVATE = "isprivate"
                const val LATITUDE = "latitude"
                const val LONGITUDE = "longitude"
                const val LANGUAGE = "language"
                const val MINI_THUMB_MAGIC = "mini_thumb_magic"
                const val RESOLUTION = "resolution"
                const val TAGS = "tags"
                const val DATE_TAKEN = "datetaken"
            }
        }

        object Media {
            @JvmField val EXTERNAL_CONTENT_URI: Uri = Uri.parse("content://media/external/video/media")
            @JvmField val INTERNAL_CONTENT_URI: Uri = Uri.parse("content://media/internal/video/media")
            @JvmField val CONTENT_URI: Uri = EXTERNAL_CONTENT_URI
            const val DEFAULT_SORT_ORDER = "date_modified DESC"

            @JvmStatic fun getContentUri(volumeName: String): Uri =
                Uri.parse("content://media/$volumeName/video/media")

            const val _ID = "_id"
            const val DATA = "_data"
            const val SIZE = "_size"
            const val DISPLAY_NAME = "_display_name"
            const val TITLE = "title"
            const val DATE_ADDED = "date_added"
            const val DATE_MODIFIED = "date_modified"
            const val MIME_TYPE = "mime_type"
            const val WIDTH = "width"
            const val HEIGHT = "height"
            const val DURATION = "duration"
            const val RESOLUTION = "resolution"
            const val DESCRIPTION = "description"
            const val ARTIST = "artist"
            const val ALBUM = "album"
        }

        object Thumbnails {
            @JvmField val EXTERNAL_CONTENT_URI: Uri = Uri.parse("content://media/external/video/thumbnails")
            const val FULL_SCREEN_KIND = 2
            const val MINI_KIND = 1
            const val MICRO_KIND = 3
        }
    }

    /** Audio。 */
    object Audio {
        interface AudioColumns {
            companion object {
                const val ALBUM = "album"
                const val ALBUM_ID = "album_id"
                const val ARTIST = "artist"
                const val ARTIST_ID = "artist_id"
                const val COMPOSER = "composer"
                const val DURATION = "duration"
                const val IS_ALARM = "is_alarm"
                const val IS_AUDIOBOOK = "is_audiobook"
                const val IS_MUSIC = "is_music"
                const val IS_NOTIFICATION = "is_notification"
                const val IS_PODCAST = "is_podcast"
                const val IS_RECORDING = "is_recording"
                const val IS_RINGTONE = "is_ringtone"
                const val TRACK = "track"
                const val YEAR = "year"
                const val BOOKMARK = "bookmark"
                const val WRITER = "writer"
                const val COMPILATION = "compilation"
                const val IS_DOWNLOAD = "is_download"
                const val CD_TRACK_NUMBER = "cd_track_number"
                const val DISC_NUMBER = "disc_number"
                const val GENRE = "genre"
                const val GENRE_ID = "genre_id"
            }
        }

        object Media {
            @JvmField val EXTERNAL_CONTENT_URI: Uri = Uri.parse("content://media/external/audio/media")
            @JvmField val INTERNAL_CONTENT_URI: Uri = Uri.parse("content://media/internal/audio/media")
            @JvmField val CONTENT_URI: Uri = EXTERNAL_CONTENT_URI
            const val DEFAULT_SORT_ORDER = "title ASC"
            const val RECORD_SOUND_ACTION = "android.provider.MediaStore.RECORD_SOUND"
            const val EXTRA_MAX_BYTES = "android.provider.MediaStore.extra.MAX_BYTES"

            @JvmStatic fun getContentUri(volumeName: String): Uri =
                Uri.parse("content://media/$volumeName/audio/media")

            @JvmStatic fun getContentUriForPath(path: String): Uri = EXTERNAL_CONTENT_URI

            const val _ID = "_id"
            const val DATA = "_data"
            const val SIZE = "_size"
            const val DISPLAY_NAME = "_display_name"
            const val TITLE = "title"
            const val DATE_ADDED = "date_added"
            const val DATE_MODIFIED = "date_modified"
            const val MIME_TYPE = "mime_type"
            const val ALBUM = "album"
            const val ALBUM_ID = "album_id"
            const val ARTIST = "artist"
            const val ARTIST_ID = "artist_id"
            const val DURATION = "duration"
            const val TRACK = "track"
            const val YEAR = "year"
            const val IS_MUSIC = "is_music"
        }

        object Artists {
            object Columns {
                const val ARTIST = "artist"
                const val ARTIST_KEY = "artist_key"
                const val NUMBER_OF_ALBUMS = "number_of_albums"
                const val NUMBER_OF_TRACKS = "number_of_tracks"
            }
        }

        object Albums {
            object Columns {
                const val ALBUM = "album"
                const val ALBUM_ID = "album_id"
                const val ALBUM_KEY = "album_key"
                const val ARTIST = "artist"
                const val FIRST_YEAR = "minyear"
                const val LAST_YEAR = "maxyear"
                const val NUMBER_OF_SONGS = "numsongs"
                const val NUMBER_OF_SONGS_FOR_ARTIST = "numsongsbyartist"
            }
        }

        object Genres {
            const val NAME = "name"
            object Members {
                const val AUDIO_ID = "audio_id"
                const val GENRE_ID = "genre_id"
            }
        }

        object Playlists {
            object Members {
                const val PLAYLIST_ID = "playlist_id"
                const val AUDIO_ID = "audio_id"
                const val PLAY_ORDER = "play_order"
            }
        }

        object Radio {
            const val ENTRY_CONTENT_TYPE = "vnd.android.cursor.item/radio"
        }
    }

    /** Files。 */
    object Files {
        interface FileColumns {
            companion object {
                const val MEDIA_TYPE = "media_type"
                const val MEDIA_TYPE_NONE = 0
                const val MEDIA_TYPE_IMAGE = 1
                const val MEDIA_TYPE_AUDIO = 2
                const val MEDIA_TYPE_VIDEO = 3
                const val MEDIA_TYPE_PLAYLIST = 4
                const val MEDIA_TYPE_STATIC_IMAGE = 5
                const val MEDIA_TYPE_DOCUMENT = 6
                const val MEDIA_TYPE_SUBTITLE = 7
                const val PARENT = "parent"
                const val FORMAT = "format"
            }
        }

        object FileColumnsValues {
            const val MEDIA_TYPE_IMAGE = 1
            const val MEDIA_TYPE_AUDIO = 2
            const val MEDIA_TYPE_VIDEO = 3
        }

        @JvmStatic fun getContentUri(volumeName: String): Uri =
            Uri.parse("content://media/$volumeName/file")

        @JvmStatic fun getContentUri(volumeName: String, rowId: Long): Uri =
            Uri.parse("content://media/$volumeName/file/$rowId")

        @JvmField val CONTENT_URI: Uri = getContentUri(VOLUME_EXTERNAL)
    }

    object Download {
        const val _ID = "_id"
    }
}

/** android.provider.ContactsContract 常量 stub（census 未直接出现，防御性补齐）。 */
object ContactsContract {
    const val AUTHORITY = "com.android.contacts"

    @JvmField val AUTHORITY_URI: Uri = Uri.parse("content://com.android.contacts")

    object Contacts {
        @JvmField val CONTENT_URI: Uri = Uri.parse("content://com.android.contacts/contacts")
        const val _ID = "_id"
        const val DISPLAY_NAME = "display_name"
        const val DISPLAY_NAME_PRIMARY = "display_name"
        const val HAS_PHONE_NUMBER = "has_phone_number"
    }

    object CommonDataKinds {
        object Phone {
            @JvmField val CONTENT_URI: Uri = Uri.parse("content://com.android.contacts/data/phones")
            const val NUMBER = "data1"
            const val DISPLAY_NAME = "display_name"
        }
    }
}
