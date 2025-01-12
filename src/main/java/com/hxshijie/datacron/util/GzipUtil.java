package com.hxshijie.datacron.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * 数据压缩和解压工具，建议搭配base64使用
 * @author hxshijie
 * */
public class GzipUtil {

    private GzipUtil() {
    }

    /**
     * 压缩方法
     * @param src 待压缩的字符串
     * @return 压缩后的字符数组
     * */
    public static byte[] zip(String src) throws IOException {
        if (src == null || src.isEmpty()) {
            return null;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(src.getBytes(StandardCharsets.UTF_8));
            return out.toByteArray();
        }
    }

    /**
     * 解压方法
     * @param bytes 待解压的字符数组
     * @return 解压后的字符串
     * */
    public static String unZip(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             ByteArrayInputStream in = new ByteArrayInputStream(bytes);
             GZIPInputStream gzip = new GZIPInputStream(in)) {
            byte[] buffer = new byte[1024];
            int line;
            while ((line = gzip.read(buffer)) >= 0) {
                out.write(buffer, 0, line);
            }
            return out.toString(StandardCharsets.UTF_8);
        }
    }
}
