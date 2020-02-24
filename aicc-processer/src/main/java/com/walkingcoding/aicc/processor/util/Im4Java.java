package com.walkingcoding.aicc.processor.util;

import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.ImageCommand;
import org.im4java.process.ProcessStarter;
import org.im4java.process.ProcessTask;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * im4java调用
 *
 * @author songhuiqing
 */
public class Im4Java {
    public static void main(String[] args) throws InterruptedException, IOException, IM4JavaException {
        String src = "/Users/songhuiqing/docker/nginx/html/workspace/resource/ADKS2018030/image/index_bg.png";
        String dist = "/Users/songhuiqing/docker/nginx/html/workspace/resource/ADKS2018030/image/index_bg1.jpg";
        new Im4Java("/Users/songhuiqing/apps/ImageMagick-7.0.9/bin").transferToJpg(src, dist);
    }

    class ConvertCmd extends ImageCommand {

        boolean isWindows = File.pathSeparator.equals(";");

        public ConvertCmd() {
            super();
            setCommand("convert");
        }

        @Override
        protected int run(LinkedList<String> pArgs) throws IOException, InterruptedException, Exception {
            // create and execute process (synchronous mode)
            if (!super.isAsyncMode()) {
                Process pr = startProcess(pArgs);
                int rc = waitForProcess(pr);
                finished(rc);
                return rc;
            } else {
                ProcessTask pt = getProcessTask(pArgs);
                (new Thread(pt)).start();
                return 0;
            }
        }

        private Process startProcess(LinkedList<String> pArgs) throws IOException, InterruptedException {
            if (super.getSearchPath() != null) {
                String cmd = pArgs.getFirst();
                cmd = searchForCmd(cmd, super.getSearchPath());
                pArgs.set(0, cmd);
            } else if (ProcessStarter.getGlobalSearchPath() != null) {
                String cmd = pArgs.getFirst();
                cmd = searchForCmd(cmd, ProcessStarter.getGlobalSearchPath());
                pArgs.set(0, cmd);
            }
            ProcessBuilder builder = new ProcessBuilder(pArgs);
            if (isWindows) {
                builder.inheritIO();
                builder.redirectErrorStream();
                if (super.getSearchPath() != null) {
                    builder.directory(new File(super.getSearchPath()));
                } else if (ProcessStarter.getGlobalSearchPath() != null) {
                    builder.directory(new File(ProcessStarter.getGlobalSearchPath()));
                }
            }
            return builder.start();
        }
    }

    private ConvertCmd convertCmd;


    public Im4Java() {
    }

    public Im4Java(String imageMagickPath) {
        this.convertCmd = new ConvertCmd();
        if (imageMagickPath != null && !"".equals(imageMagickPath)) {
            this.convertCmd.setSearchPath(imageMagickPath);
        }
    }

    public void resize(String src, String dist, int w, int h)
            throws IOException, InterruptedException, IM4JavaException {
        IMOperation op = new IMOperation();
        op.addImage(src);
        op.resize(w, h);
        op.addImage(dist);
        convertCmd.run(op);
    }

    public void crop(String src, String dist, int w, int h, int x, int y)
            throws IOException, InterruptedException, IM4JavaException {
        IMOperation op = new IMOperation();
        op.addImage(src);
        op.crop(w, h, x, y);
        op.addImage(dist);
        convertCmd.run(op);
    }

    public void trim(String src, String dist) throws InterruptedException, IOException, IM4JavaException {
        IMOperation op = new IMOperation();
        op.trim().addImage(src).addImage(dist);
        convertCmd.run(op);
    }

    public void transferToJpg(String src, String dist) throws IOException, InterruptedException, IM4JavaException {
        IMOperation op = new IMOperation();
        op.addImage(src);
        op.addImage(dist);
        convertCmd.run(op);
    }
}
