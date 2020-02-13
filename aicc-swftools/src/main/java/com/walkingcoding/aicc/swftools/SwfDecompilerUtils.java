package com.walkingcoding.aicc.swftools;

import com.jpexs.decompiler.flash.gui.Main;
import com.walkingcoding.aicc.swftools.exporter.ExporterType;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;


/**
 * swf文件反编译工具类，jpexs目前看只支持单线程操作，具体原理不详
 *
 * @author songhuiqing
 */
public class SwfDecompilerUtils {


    /**
     * 导出swf中的文件
     *
     * @param types        导出的文件类型
     * @param outDirectory 导出文件所在目录的路径
     * @param swfFilePath  swf文件所在的路径
     */
    public static void export(ExporterType[] types, String outDirectory, String swfFilePath) {
        if (types == null || types.length == 0) {
            throw new IllegalArgumentException("参数types不能为空");
        }
        String typeArgs = Arrays.stream(types).map(ExporterType::toString).collect(Collectors.joining(","));
        ffdecMain("-export", typeArgs, outDirectory, swfFilePath);
    }

    /**
     * 导出swf中的文件
     *
     * @param types        导出的文件类型
     * @param outDirectory 导出文件所在目录
     * @param swfFilePath  swf File对象
     */
    public static void export(ExporterType[] types, String outDirectory, File swfFilePath) {
        export(types, outDirectory, swfFilePath.getPath());
    }

    /**
     * 导出swf中的文件
     *
     * @param type         导出的文件类型
     * @param outDirectory 导出文件所在目录的路径
     * @param swfFilePath  swf文件所在的路径
     */
    public static void export(ExporterType type, String outDirectory, String swfFilePath) {
        export(new ExporterType[]{type}, outDirectory, swfFilePath);
    }

    /**
     * 导出swf中的文件
     *
     * @param type         导出的文件类型
     * @param outDirectory 导出文件所在目录
     * @param swfFilePath  swf File对象
     */
    public static void export(ExporterType type, String outDirectory, File swfFilePath) {
        export(new ExporterType[]{type}, outDirectory, swfFilePath.getPath());
    }


    /**
     * jpexs decompiler main
     *
     * @param args
     * @throws IOException
     */
    public static void ffdecMain(String... args) {
        try {
            Main.main(args);
        } catch (IOException e) {
            System.out.println("logger error, " + e.getMessage());
        }
    }
}
