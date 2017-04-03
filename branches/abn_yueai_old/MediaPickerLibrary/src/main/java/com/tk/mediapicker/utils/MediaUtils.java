package com.tk.mediapicker.utils;

import android.content.Context;

import com.tk.mediapicker.bean.MediaBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by TK on 2016/9/30.
 */
public final class MediaUtils {

    private static final String IMG_PREFIX = "IMG_";
    private static final String IMG_SUFFIX = ".jpeg";

    private static final String VIDEO_PREFIX = "VIDEO_";
    private static final String VIDEO_SUFFIX = ".mp4";
    private static String TEMP_FILE_DIRECTORY;
    public static int MAX_CROPED_SIZE = 800;

    public static String getTmpFileDir() {
        return TEMP_FILE_DIRECTORY;
    }

    public static void init(Context context, String tempPath) {
        TEMP_FILE_DIRECTORY = tempPath;

        File noMediaFile = new File(TEMP_FILE_DIRECTORY + ".nomedia");
        if (!noMediaFile.exists()) {
            try {
                noMediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建临时文件
     *
     * @param context
     * @return
     * @throws IOException
     */
    public static final File createCameraTmpFile(Context context) throws IOException {
        File dir = new File(TEMP_FILE_DIRECTORY + "tempCamera/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return File.createTempFile(IMG_PREFIX, IMG_SUFFIX, dir);
    }

    /**
     * 创建临时文件
     *
     * @param context
     * @return
     * @throws IOException
     */
    public static final File createVideoTmpFile(Context context) throws IOException {
        File dir = new File(TEMP_FILE_DIRECTORY + "tempCamera/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return File.createTempFile(VIDEO_PREFIX, VIDEO_SUFFIX, dir);
    }

    /**
     * 转换成String List
     *
     * @param sourceList
     * @return
     */
    public static ArrayList<String> beanToPathList(List<MediaBean> sourceList) {
        ArrayList<String> pathList = new ArrayList<String>();
        for (MediaBean bean : sourceList) {
            pathList.add(bean.getPath());
        }
        return pathList;
    }

    /**
     * 转换成String List
     *
     * @param sourceList
     * @return
     */
    public static ArrayList<String> fileToPathList(List<File> sourceList) {
        ArrayList<String> pathList = new ArrayList<String>();
        for (File file : sourceList) {
            pathList.add(file.getAbsolutePath());
        }
        return pathList;
    }

    /**
     * 转换成String List
     *
     * @param sourceList
     * @return
     */
    public static ArrayList<File> pathToFileList(List<String> sourceList) {
        ArrayList<File> fileList = new ArrayList<File>();
        for (String path : sourceList) {
            fileList.add(new File(path));
        }
        return fileList;
    }
}
