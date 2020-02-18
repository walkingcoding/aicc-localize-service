package com.walkingcoding.aicc.processor.util;

import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片工具类
 *
 * @author songhuiqing
 */
public class ImageUtils {

    /**
     * 支持的图片后缀 png、jpg
     */
    public final static String[] SUFFIX_IMAGE = new String[]{"png", "jpg"};

    public interface ImageFilter {

        /**
         * 图片过滤
         *
         * @param image BufferedImage对象
         * @return
         */
        boolean filter(BufferedImage image);
    }

    /**
     * 过滤图片，返回图片文件列表，支持递归，目前只支持jpg和png后缀的图片过滤
     *
     * @param folderPath 图片目录
     * @param filter     过滤器
     * @return
     */
    public static List<File> filterImages(String folderPath, ImageFilter filter) {

        List<File> imageList = new ArrayList<>(16);
        try {
            filter(new File(folderPath), imageList, filter);
        } catch (IOException e) {

        }
        return imageList;
    }

    private static void filter(File folder, List<File> imageFiles, ImageFilter imageFilter) throws IOException {

        File[] files = folder.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isDirectory()) {
                    filter(file, imageFiles, imageFilter);
                } else if (FilenameUtils.isExtension(file.getName(), SUFFIX_IMAGE)) {
                    BufferedImage image = ImageIO.read(new FileInputStream(file));
                    if (imageFilter.filter(image)) {
                        imageFiles.add(file);
                    }
                }
            }
        }
    }

}
