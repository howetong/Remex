package cn.tonghao.remex.business.core.util.security;

import java.io.IOException;
import java.io.OutputStream;

public class HexCode {

    protected static final byte[] encodingTable = {48, 49, 50, 51, 52, 53, 54,
            55, 56, 57, 97, 98, 99, 100, 101, 102};

    protected static final byte[] decodingTable;

    static {
        decodingTable = new byte[128];
        for (int i = 0; i < encodingTable.length; i++) {
            decodingTable[encodingTable[i]] = ((byte) i);
        }

        decodingTable[65] = decodingTable[97];
        decodingTable[66] = decodingTable[98];
        decodingTable[67] = decodingTable[99];
        decodingTable[68] = decodingTable[100];
        decodingTable[69] = decodingTable[101];
        decodingTable[70] = decodingTable[102];
    }

    public static int encode(byte[] data, int off, int length, OutputStream out)
            throws IOException {
        for (int i = off; i < off + length; i++) {
            int v = data[i] & 0xFF;
            out.write(encodingTable[(v >>> 4)]);
            out.write(encodingTable[(v & 0xF)]);
        }
        return length * 2;
    }

    private static boolean ignore(char c) {
        return (c == '\n') || (c == '\r') || (c == '\t') || (c == ' ');
    }

    public static int decode(byte[] data, int off, int length, OutputStream out)
            throws IOException {
        int outLen = 0;
        int end = off + length;
        while (end > off) {
            if (!ignore((char) data[(end - 1)])) {
                break;
            }
            end--;
        }

        int i = off;
        while (i < end) {
            while ((i < end) && (ignore((char) data[i]))) {
                i++;
            }

            byte b1 = decodingTable[data[(i++)]];
            while ((i < end) && (ignore((char) data[i]))) {
                i++;
            }
            byte b2 = decodingTable[data[(i++)]];
            out.write(b1 << 4 | b2);
            outLen++;
        }
        return outLen;
    }

    /**
     * 将byte[] 转换成字符串
     */
    public static String byte2Hex(byte[] srcBytes) {
        StringBuilder hexRetSB = new StringBuilder();
        for (byte b : srcBytes) {
            String hexString = Integer.toHexString(0x00ff & b);
            hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
        }
        return hexRetSB.toString();
    }

    /**
     * 将16进制字符串转为转换成字符串
     */
    public static byte[] hex2Bytes(String source) {
        byte[] sourceBytes = new byte[source.length() / 2];
        for (int i = 0; i < sourceBytes.length; i++) {
            sourceBytes[i] = (byte) Integer.parseInt(source.substring(i * 2, i * 2 + 2), 16);
        }
        return sourceBytes;
    }

}
