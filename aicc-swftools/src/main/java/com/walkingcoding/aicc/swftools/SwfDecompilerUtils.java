package com.walkingcoding.aicc.swftools;

import com.walkingcoding.aicc.swftools.exporter.ExporterType;

import java.io.IOException;

/**
 * @author songhuiqing
 */
public class SwfDecompilerUtils {
    public static void main(String[] args) throws IOException {
        export(ExporterType.frame, "/Users/songhuiqing/Downloads/frame", "/Users/songhuiqing/docker/nginx/html/cw/ADKS2018030/inchen/core3.swf");
    }

    public static void export(ExporterType type, String outDirectory, String swfFilePath) {
        String exportType = ExporterType.all.toString();
        if (type != null) {
            exportType = type.toString();
        }
        try {
            ffdecMain("-export", exportType, outDirectory, swfFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * jpexs decompiler main
     *
     * @param args
     * @throws IOException
     */
    public static void ffdecMain(String... args) throws IOException {
        com.jpexs.decompiler.flash.gui.Main.main(args);
    }
}
