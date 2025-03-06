package com.pml.util;

import com.google.common.collect.Lists;

import java.util.List;

public class TypeTransfer {
    public static byte[] transferInt2Byte(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }

    public static byte[] transferShort2Byte(short s) {
        byte[] result = new byte[2];
        result[0] = (byte)((s >> 8) & 0xFF);
        result[1] = (byte)(s & 0xFF);
        return result;
    }

    public static byte[] transferLong2Byte(long l) {
        byte[] byteNum = new byte[8];
        for (int ix = 0; ix < 8; ++ix) {
            int offset = 64 - (ix + 1) * 8;
            byteNum[ix] = (byte) ((l >> offset) & 0xff);
        }
        return byteNum;
    }

    public static List<Byte> array2List(byte[] bytes) {
        List<Byte> list = Lists.newArrayList();
        for (byte aByte : bytes) {
            list.add(aByte);
        }
        return list;
    }

    public static byte[] ipv4Address2BinaryArray(String ipAdd) {
        byte[] binIp = new byte[4];
        String[] strs = ipAdd.split("\\.");
        for(int i = 0; i < strs.length; i++) {
            binIp[i] = (byte) Integer.parseInt(strs[i]);
        }
        return binIp;
    }
}
