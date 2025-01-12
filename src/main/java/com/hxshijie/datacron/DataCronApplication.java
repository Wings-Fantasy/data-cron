package com.hxshijie.datacron;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;

@EnableAsync
@EnableScheduling
@SpringBootApplication
public class DataCronApplication {

    private static final String lockFileName = System.getProperty("user.dir") + File.separator + "pid.lock";

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private static FileLock runningLock;

    public static void main(String[] args) {
        // 检查pid文件锁，防止重复启动
        getRunningLock();
        // 禁用https证书验证中的主机名有效性检查
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");
        // 允许在http请求头内设置host等受限请求头
        System.setProperty("jdk.httpclient.allowRestrictedHeaders", "host");
        SpringApplication.run(DataCronApplication.class, args);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void getRunningLock() {
        try {
            File lockFile = new File(lockFileName);
            if (lockFile.exists()) {
                Files.delete(lockFile.toPath());
            }
            if (!lockFile.createNewFile()) {
                System.err.println("文件" + lockFileName + "创建失败");
                System.exit(1);
            }
            @SuppressWarnings("resource")
            FileChannel fileChannel = new RandomAccessFile(lockFile, "rw").getChannel();
            fileChannel.write(ByteBuffer.wrap(String.valueOf(ManagementFactory.getRuntimeMXBean().getPid()).getBytes()));
            runningLock = fileChannel.tryLock();
        } catch (Exception ignored) {
            System.err.println("文件" + lockFileName + "打开失败");
            System.exit(1);
        }
    }
}
