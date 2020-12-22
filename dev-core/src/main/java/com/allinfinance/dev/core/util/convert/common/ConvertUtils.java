package com.allinfinance.dev.core.util.convert.common;

import com.allinfinance.dev.core.constant.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobDataMap;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;

public class ConvertUtils {

	public ConvertUtils() {
	}

	public static String string2ASCIIHex(String s) {
		StringBuilder hexStr = new StringBuilder();
		if (StringUtils.isEmpty(s)) {
			return null;
		}
		char[] chars = s.toCharArray();
		for (char aChar : chars) {
			hexStr.append(Integer.toHexString(aChar).toUpperCase());
		}
		return hexStr.toString();
	}


	public static String HexASCII2String(String content) {
		StringBuilder ret = new StringBuilder();
		try {
			if (StringUtils.isNotEmpty(content)) {
				if(content.length() % 2 != 0) {
					return null;
				}
				int len = content.length() / 2;
				for (int i = 0; i < len; i++) {
					String hexStr = content.substring(i * 2, i * 2 + 2);
					ret.append((char) Integer.parseInt(hexStr, 16));
				}
			} else {
				ret = new StringBuilder(content);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return ret.toString();
	}

	public static String getFixedString(String src, int length) {
		String tmp = String.format("%-" + length + "s", src);
		if (tmp.length() > length) {
			return tmp.substring(0, length);
		}
		else {
			return tmp;
		}
	}

	public static String getString(byte[] src, int srcPos, int length) {
		byte[] tmp = new byte[length];
		System.arraycopy(src, srcPos, tmp, 0, length);
		return (new String(tmp).trim());
	}

	public static String getStringGbk(byte[] src, int srcPos, int length) {
		byte[] tmp = new byte[length];
		System.arraycopy(src, srcPos, tmp, 0, length);
		String ret = "";
		try {
			ret = new String(tmp, "gbk").trim();
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return ret;
	}

	public static String toStringGbk(byte[] src, int srcPos, int length) {
		byte[] tmp = new byte[length];
		System.arraycopy(src, srcPos, tmp, 0, length);
		String ret = "";
		try {
			ret = new String(tmp, "gbk");
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return ret;
	}

	public static byte[] toFixedBytes(byte[] src, int len) {
		byte[] ret = new byte[len];
		try {
			Arrays.fill(ret, (byte) ' ');
			int srcLen = src.length;
			len = Math.min(len, srcLen);
			System.arraycopy(src, 0, ret, 0, len);
		} catch (Exception e) {

			e.printStackTrace();
		}
		return ret;
	}

	public static byte[] toFixedBytes(String src, int len) {
		
		return toFixedBytes("GBK", src, len);
	}

	public static byte[] toFixedBytes(String charset, String src, int len) {
		byte[] tmp = new byte[len];
		try {
			Arrays.fill(tmp, (byte) ' ');
			int srcLen = src.getBytes(charset).length;
			len = len > srcLen ? srcLen : len;
			System.arraycopy(src.getBytes(charset), 0, tmp, 0, len);
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return tmp;
	}

	public static String byte2String(byte[] buf, int offset) {
		int pos = offset;
		while (offset < buf.length) {
			if (buf[offset] == 0) {
				break;
			}
			offset++;
		}

		if (offset > pos) {
			offset--;
		} else if (offset == pos) {
			return "";
		}
		int len = (offset - pos) + 1;
		byte[] bb = new byte[len];
		System.arraycopy(buf, pos, bb, 0, len);
		String str = new String(bb);
		return str;
	}

	public static int byte2int(byte[] b) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16
				| (b[0] & 0xff) << 24;
	}

	public static int byte2int(byte[] b, int offset) {
		return b[offset + 3] & 0xff | (b[offset + 2] & 0xff) << 8
				| (b[offset + 1] & 0xff) << 16 | (b[offset] & 0xff) << 24;
	}

	public static long byte2long(byte[] b, int offset) {
		return (long) b[offset + 7] & 255L | ((long) b[offset + 6] & 255L) << 8
				| ((long) b[offset + 5] & 255L) << 16
				| ((long) b[offset + 4] & 255L) << 24
				| ((long) b[offset + 3] & 255L) << 32
				| ((long) b[offset + 2] & 255L) << 40
				| ((long) b[offset + 1] & 255L) << 48 | (long) b[offset] << 56;
	}

	public static long byte2long(byte[] b) {
		return (long) b[7] & 255L | ((long) b[6] & 255L) << 8
				| ((long) b[5] & 255L) << 16 | ((long) b[4] & 255L) << 24
				| ((long) b[3] & 255L) << 32 | ((long) b[2] & 255L) << 40
				| ((long) b[1] & 255L) << 48 | (long) b[0] << 56;
	}

	public static short byte2short(byte[] b, int offset) {
		return (short) (b[offset + 1] & 0xff | (b[offset] & 0xff) << 8);
	}

	public static short byte2short(byte[] b) {
		return (short) (b[1] & 0xff | (b[0] & 0xff) << 8);
	}

	public static short byte2tinyint(byte[] b, int offset) {
		return (short) (b[offset] & 0xff);
	}

	public static void int2byte(int n, byte[] buf, int offset) {
		buf[offset] = (byte) (n >> 24);
		buf[offset + 1] = (byte) (n >> 16);
		buf[offset + 2] = (byte) (n >> 8);
		buf[offset + 3] = (byte) n;
	}

	public static void int2byte2(int n, byte[] buf, int offset) {
		buf[offset] = (byte) (n >> 8);
		buf[offset + 1] = (byte) n;
	}

	public static void int2byte3(int n, byte[] buf, int offset) {
		// buf[offset] = (byte)(n >> 8);
		buf[offset] = (byte) n;
	}

	public static byte[] int2byte(int n) {
		byte[] b = new byte[4];
		b[0] = (byte) (n >> 24);
		b[1] = (byte) (n >> 16);
		b[2] = (byte) (n >> 8);
		b[3] = (byte) n;
		return b;
	}

	public static void long2byte(long n, byte[] buf, int offset) {
		buf[offset] = (byte) (int) (n >> 56);
		buf[offset + 1] = (byte) (int) (n >> 48);
		buf[offset + 2] = (byte) (int) (n >> 40);
		buf[offset + 3] = (byte) (int) (n >> 32);
		buf[offset + 4] = (byte) (int) (n >> 24);
		buf[offset + 5] = (byte) (int) (n >> 16);
		buf[offset + 6] = (byte) (int) (n >> 8);
		buf[offset + 7] = (byte) (int) n;
	}

	public static byte[] long2byte(long n) {
		byte[] b = new byte[8];
		b[0] = (byte) (int) (n >> 56);
		b[1] = (byte) (int) (n >> 48);
		b[2] = (byte) (int) (n >> 40);
		b[3] = (byte) (int) (n >> 32);
		b[4] = (byte) (int) (n >> 24);
		b[5] = (byte) (int) (n >> 16);
		b[6] = (byte) (int) (n >> 8);
		b[7] = (byte) (int) n;
		return b;
	}

	public static void short2byte(int n, byte[] buf, int offset) {
		buf[offset] = (byte) (n >> 8);
		buf[offset + 1] = (byte) n;
	}

	public static byte[] short2byte(int n) {
		byte[] b = new byte[2];
		b[0] = (byte) (n >> 8);
		b[1] = (byte) n;
		return b;
	}

//	public static String toXML(XStream xStream, Object obj) {
//		xStream.processAnnotations(obj.getClass());
//		return xStream.toXML(obj);
//	}

	public static String getFixedBytesString(String src, int srcPos, int len,
			String charset) {

		byte[] tmp = null;
		try {
			if (src != null) {
				tmp = src.getBytes(charset);
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return getFixedBytesString(tmp, srcPos, len, charset);
	}

	public static String getFixedBytesString(byte[] src, int srcPos, int len,
			String charset) {
		
		String ret = null;
		if(len == -1) {
			len = src.length - srcPos;
		}
		byte[] retbyte = new byte[len];
		Arrays.fill(retbyte, (byte) ' ');
		try {
			if (src != null) {
				int l = src.length;
				if (l > srcPos) {
					l = l > (srcPos + len) ? len : l - srcPos;
					System.arraycopy(src, srcPos, retbyte, 0, l);
				}
			}
			ret = new String(retbyte, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static String getFixedBytesStringByCharset(String src, int srcPos,
			int len,String charset) {

		try {
			if (len == -1) {
				len = src.getBytes(charset).length - srcPos;
				if(len == 0) {
					return null;
				}
			}
			return getFixedBytesString(src, srcPos, len, charset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getFixedBytesGBKString(String src, int len) {
		return getFixedBytesString(src, 0, len, "GBK");
	}

	public static String getFixedBytesGBKString(byte[] src, int srcPos,
			int len) {
		return getFixedBytesString(src, srcPos, len, "GBK");
	}

	public static String getFixedBytesUTF8String(byte[] src, int srcPos,
												int len) {
		return getFixedBytesString(src, srcPos, len, "utf-8");
	}

	public static String getFixedBytesUTF8String(byte[] src,
												 int len) {
		return getFixedBytesString(src, 0, len, "utf-8");
	}
	public static String getFixedBytesGBKString(byte[] src, int len) {
		return getFixedBytesString(src, 0, len, "GBK");
	}

	public static byte[] getFixedBytesByCharset(String src, int srcPos, int len,String charset) {
		String retStr = getFixedBytesStringByCharset(src, srcPos, len, charset);
		if(retStr == null) {
			return null;
		}
		byte[] ret = null;
		try {
			ret = retStr.getBytes(charset);
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		}
		return ret;
	}
	public static byte[] getFixedBytes(byte[] src, int srcPos, int len) {
		byte[] ret = new byte[len];
		System.arraycopy(src, srcPos, ret, 0, len);
		return ret;
	}
	
    /**
     * 字符串填充函数
     *
     * @param str
     * @param filler
     * @param totalLength
     * @param atEnd
     * @return
     */
    public static String fillString(String str, char filler,
            int totalLength, boolean atEnd) {
		str = str == null ? "" : str;
		byte[] tempbyte = str.getBytes();
		int currentLength = tempbyte.length;
		int delta = totalLength - currentLength;
		for (int i = 0; i < delta; i++) {
			if (atEnd) {
				str += filler;
			} else {
				str = filler + str;
			}
		}
		return str;
    }

	public static JobDataMap HashToJobData(HashMap<String,String> hashMap)
	{
		JobDataMap jobDataMap = new JobDataMap();
		if(hashMap != null && hashMap.size() > 0) {
			jobDataMap.putAll(hashMap);
		}
		return jobDataMap;
	}

	public static void main(String[] args) {
		System.out.println(ConvertUtils.fillString("12345678", CommonConstants.CHAR_BLANK,11,true)+"||");
	}
}