package org.jhaws.common.io.media;

import org.jhaws.common.io.FilePath.Filters;
import org.jhaws.common.io.FilePath.Filters.FlashVideoFilter;
import org.jhaws.common.io.FilePath.Filters.Html5VideoFilter;
import org.jhaws.common.io.FilePath.Filters.QuickTimeVideoFilter;
import org.jhaws.common.io.FilePath.Filters.VideoFilter;
import org.jhaws.common.io.FilePath.Filters.WebImageFilter;

public interface MediaCte {
	public static final String PROC_END = "exit value=";

	public static final String MP4 = "mp4";

	public static final String M4A = "m4a";

	public static final String M_TASK_TOTAL_TIME = "task.totalTime";

	public static final String M_TASK_CURRENT_SIZE = "task.currentSize";

	public static final String M_TASK_CURRENT_TIME = "task.currentTime";

	public static final String M_TMP = "tmp";

	public static final String M_SIZE = "size";

	public static final String M_TASK_START_TIMESTAMP = "task.starttimestamp";

	public static final String M_TASK_CURRENT_TIMESTAMP = "task.currenttimestamp";

	public static final String M_TASK_END_TIMESTAMP = "task.endtimestamp";

	public static final String M_TASK_PROGRESS = "task.progress";

	public static final String M_TASK_LOG = "task.log";

	public static final String M_TASKS = "tasks";

	public static final String M_FILE = "file";

	public static final String M_PARENT = "parent";

	public static final String TREMAW = " : ";

	public static final String TREMAZ = " :0";

	public static final String UNKNOWN = "unknown";

	public static final String IH = "Image Height";

	public static final String IW = "Image Width";

	public static final String GIF = "gif";

	public static final String L_TYPE = "TYPE";

	public static final String L_TYPE_AUTO = "auto";

	public static final String L_TYPE_FILE = "file";

	public static final String L_TYPE_DIR = "dir";

	public static final String L_NAME = "name";

	public static final String L_SHORT_NAME = "shortname";

	public static final String L_PARENT_NAME = "parentname";

	public static final String L_SORTABLE = "sortable";

	public static final String L_W = "w";

	public static final String L_DURATION = "duration";

	public static final String L_VFR = "vfr";

	public static final String L_SIZE = "size";

	public static final String L_H = "h";

	public static final String L_VIDEO = "video";

	public static final String L_MIME = "mime";

	public static final String L_BITRATE = "bitrate";

	public static final String L_AUDIO = "audio";

	public static final String H = "Source Image Height";

	public static final String W = "Source Image Width";

	public static final String MIME1 = "MIME Type";

	public static final String DURATION1 = "Media Duration";

	public static final String DURATION2 = "Duration";

	public static final String DURATION3 = "Play Duration";

	public static final String DURATION4 = "Send Duration";

	public static final String VIDEO1 = "Compressor ID";

	public static final String VIDEO2 = "Video Codec";

	public static final String VIDEO3 = "Video Encoding";

	public static final String VIDEO4 = "Video Codec Name";

	public static final String AUDIO1 = "Audio Format";

	public static final String AUDIO2 = "Audio Codec";

	public static final String AUDIO3 = "Audio Encoding";

	public static final String AUDIO4 = "Audio Codec Name";

	public static final String VFR1 = "Video Frame Rate";

	public static final String VFR2 = "Frame Rate";

	public static final String AVGBITRATE1 = "Avg Bitrate";

	public static final String AVGBITRATE2 = "Avg Bytes Per Sec";

	public static final Filters webImageFilter = new WebImageFilter();

	public static final Filters videoFilter = new VideoFilter();

	public static final Filters flashVideoFilter = new FlashVideoFilter();

	public static final Filters html5Videofilter = new Html5VideoFilter();

	public static final Filters qtFilter = new QuickTimeVideoFilter();

	public static final String L_C_VIDEOS = "c_videos";

	public static final String L_C_QT = "c_qt";

	public static final String L_C_IMAGES = "c_images";

	public static final String L_C_FLOWED = "c_flowed";

	public static final String L_C_FILES = "c_files";

	public static final String MKV = "mkv";

	public static final String M4V = "m4v";

	public static final String AVI = "avi";

	public static final String DIVX = "divx";

	public static final String XVID = "xvid";

	public static final String MP3 = "mp3";

	public static final String AVC = "avc";

	public static final String H264 = "264";

	public static final String MOV = "mov";

	public static final String DTS = "DTS";

	public static final String AAC = "aac";

	public static final String MP3C = "libmp3lame";

	public static final String FLV = "flv";

	public static final String WEBM = "webm";
}
