package com.redpois0n.crypto;

public class Hex {
	
	/**
	 * Apache commons codec
	 */

	public static final char[] DIGITS_LOWER = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String encode(String data) throws Exception {
		return encode(data.getBytes("UTF-8"), DIGITS_LOWER);
	}
	
	public static String encode(byte[] data) {
		return encode(data, DIGITS_LOWER);
	}

	private static String encode(byte[] data, char[] toDigits) {
		int l = data.length;
		char[] out = new char[l << 1];

		int i = 0;
		for (int j = 0; i < l; i++) {
			out[(j++)] = toDigits[((0xF0 & data[i]) >>> 4)];
			out[(j++)] = toDigits[(0xF & data[i])];
		}

		return new String(out);
	}

	public static String decode(String s) throws Exception {
		char[] data = s.toCharArray();
		int len = data.length;

		if ((len & 0x1) != 0) {
			throw new Exception("Odd number of characters.");
		}

		byte[] out = new byte[len >> 1];

		int i = 0;
		for (int j = 0; j < len; i++) {
			int f = toDigit(data[j], j) << 4;
			j++;
			f |= toDigit(data[j], j);
			j++;
			out[i] = ((byte) (f & 0xFF));
		}

		return new String(out);
	}

	private static int toDigit(char ch, int index) throws Exception {
		int digit = Character.digit(ch, 16);
		if (digit == -1) {
			throw new Exception("Illegal hexadecimal character " + ch + " at index " + index);
		}
		return digit;
	}

}
