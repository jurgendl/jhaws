package org.jhaws.common.io.media;

import org.jhaws.common.io.FilePath.Filters;
import org.jhaws.common.io.FilePath.Filters.FlashVideoFilter;
import org.jhaws.common.io.FilePath.Filters.Html5VideoFilter;
import org.jhaws.common.io.FilePath.Filters.ImageFilter;
import org.jhaws.common.io.FilePath.Filters.QuickTimeVideoFilter;
import org.jhaws.common.io.FilePath.Filters.VideoFilter;
import org.jhaws.common.io.FilePath.Filters.WebImageFilter;

public interface MediaCte {
    public static final Filters.ExtensionFilter webImageFilter = new WebImageFilter();

    public static final Filters.ExtensionFilter imageFilter = new ImageFilter();

    public static final Filters.ExtensionFilter videoFilter = new VideoFilter();

    public static final Filters.ExtensionFilter flashVideoFilter = new FlashVideoFilter();

    public static final Filters.ExtensionFilter html5Videofilter = new Html5VideoFilter();

    public static final Filters.ExtensionFilter qtFilter = new QuickTimeVideoFilter();

    public static final String MP4 = "mp4";

    public static final String M4A = "m4a";

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

    public static final String MONO = "mono";

    public static final String MP3C = "libmp3lame";

    public static final String FLV = "flv";

    public static final String WEBM = "webm";
}
