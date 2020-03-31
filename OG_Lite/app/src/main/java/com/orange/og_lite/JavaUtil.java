package com.orange.og_lite;

import static com.jianzhi.jzblehelper.FormatConvert.StringHexToByte;

public class JavaUtil {
    public static String getBit(byte by) {
        StringBuffer sb = new StringBuffer();
        sb.append((by >> 7) & 0x1);
        sb.append((by >> 6) & 0x1);
        sb.append((by >> 5) & 0x1);
        sb.append((by >> 4) & 0x1);
        sb.append((by >> 3) & 0x1);
        sb.append((by >> 2) & 0x1);
        sb.append((by >> 1) & 0x1);
        sb.append((by >> 0) & 0x1);
        return sb.toString();
    }

    public static String getBit(String a) {
        byte data[] = StringHexToByte(a);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            sb.append((data[i] >> 7) & 0x1);
            sb.append((data[i] >> 6) & 0x1);
            sb.append((data[i] >> 5) & 0x1);
            sb.append((data[i] >> 4) & 0x1);
            sb.append((data[i] >> 3) & 0x1);
            sb.append((data[i] >> 2) & 0x1);
            sb.append((data[i] >> 1) & 0x1);
            sb.append((data[i] >> 0) & 0x1);
        }

        return sb.toString();
    }
}
