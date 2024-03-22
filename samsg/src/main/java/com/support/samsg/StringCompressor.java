package com.support.samsg;

import android.os.Build;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

public class StringCompressor {
    private static int htonl(int x) {
        int a;
        a = (x >> 24) & 0xFF;
        a |= ((x >> 16) & 0xFF) << 8;
        a |= ((x >> 8) & 0xFF) << 16;
        a |= (x & 0xFF) << 24;
        return a;
    }
    public static byte[] Contents_compress(byte[] data, int offset, int len) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(len);
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data, offset, len);
            gzip.close();

            byte[] pbTemp = bos.toByteArray();
            byte[] pbData = new byte[pbTemp.length + 4 - 10];
            System.arraycopy(ByteBuffer.allocate(4).putInt(htonl(len)).array(), 0, pbData, 0, 4);
            System.arraycopy(pbTemp, 10, pbData, 4, pbTemp.length - 10);

            return pbData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static byte[] gzip_compress(byte[] data) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
            GZIPOutputStream gzip = new GZIPOutputStream(bos);
            gzip.write(data);
            gzip.close();

            byte[] pbTemp = bos.toByteArray();
            byte[] pbData = new byte[pbTemp.length + 4 - 10];
            System.arraycopy(ByteBuffer.allocate(4).putInt(htonl(data.length)).array(), 0, pbData, 0, 4);
            System.arraycopy(pbTemp, 10, pbData, 4, pbTemp.length - 10);

            return pbData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] deflate_compress(byte[] data) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return null;
        }

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Deflater deflater = new Deflater(-1);
            DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(byteArrayOutputStream, deflater, false);

            deflaterOutputStream.write(data, 0, data.length);
            deflaterOutputStream.close();

            byte[] pbTemp = byteArrayOutputStream.toByteArray();
            byte[] pbData = new byte[pbTemp.length + 4 - 2];
            System.arraycopy(ByteBuffer.allocate(4).putInt(htonl(data.length)).array(), 0, pbData, 0, 4);
            System.arraycopy(pbTemp, 2, pbData, 4, pbTemp.length - 2);

            return pbData;
        } catch (Exception e) {
            return null;
        }
    }
}