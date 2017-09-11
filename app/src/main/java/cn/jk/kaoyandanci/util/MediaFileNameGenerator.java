package cn.jk.kaoyandanci.util;

/**
 * Created by Administrator on 2017/7/11.
 */

import android.net.Uri;

import com.danikula.videocache.file.FileNameGenerator;

public class MediaFileNameGenerator implements FileNameGenerator {

    // Urls contain mutable parts (parameter 'sessionToken') and stable video's id (parameter 'videoId').
    // e. g. http://example.com?videoId=abcqaz&sessionToken=xyz987
    public String generate(String url) {
        Uri uri = Uri.parse(url);
        String videoId = uri.getLastPathSegment();
        return videoId;
    }
}
