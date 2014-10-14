package au.com.immersive.xorg.encryption;

/**
 * Caesar.encrypt(str);<br>
 * Caesar.decrypt(cip);
 * 
 * @author zshah
 * 
 */
public class CaesarImpl {

	private static final char CHARS[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o',
			'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private static final char CHARS_CAPITAL[] = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
	private static final int SHIFT = 7;

	public static void main(String[] args) {
		String cipher = encrypt("DE#FGH% I@J");
		System.out.println(cipher);
		System.out.println("Expected: DE#FGH% I@J");
		System.out.println("Actual:   " + decrypt(cipher));
	}

	private static int getIndexChar(char letter) {
		for (int i = 0; i < CHARS.length; i++) {
			if (CHARS[i] == letter) {
				return i;
			}
		}
		return -1;
	}

	private static int getIndexCharCapital(char letter) {
		for (int i = 0; i < CHARS_CAPITAL.length; i++) {
			if (CHARS_CAPITAL[i] == letter) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Encrypts input string.
	 * 
	 * @param plainText
	 * @return
	 */
	public static String encrypt(String plainText) {
		String result = plainText;
		if (plainText != null && plainText.trim().length() > 0) {
			char[] cipher = plainText.toCharArray();

			for (int i = 0; i < cipher.length; i++) {
				int index = getIndexChar(cipher[i]);
				int indexCapital = getIndexCharCapital(cipher[i]);
				/* @formatter:off */
				if (index != -1) {
					cipher[i] = (index <= (CHARS.length - SHIFT)) 
						? CHARS[index + SHIFT - 1]
						: CHARS[index - CHARS.length + SHIFT - 1];
				} else if (indexCapital != -1) {
					cipher[i] = (indexCapital <= (CHARS_CAPITAL.length - SHIFT)) 
						? CHARS_CAPITAL[indexCapital + SHIFT - 1]
						: CHARS_CAPITAL[indexCapital - CHARS_CAPITAL.length + SHIFT - 1];
				}
				/* @formatter:on */
			}
			result = String.valueOf(cipher);
		}
		return result;
	}

	/**
	 * Decrypts input string.
	 * 
	 * @param cip
	 * @return
	 */
	public static String decrypt(String cipher) {
		String result = cipher;
		if (cipher != null && cipher.trim().length() > 0) {
			char[] plainText = cipher.toCharArray();
			for (int i = 0; i < plainText.length; i++) {
				int index = getIndexChar(plainText[i]);
				int indexCapital = getIndexCharCapital(plainText[i]);
				/* @formatter:off */
				if (index != -1) {
					plainText[i] = (index >= SHIFT) 
						? CHARS[index - SHIFT + 1]
						: CHARS[index + CHARS.length - SHIFT + 1];
				} else if (indexCapital != -1) {
					plainText[i] = (indexCapital >= SHIFT) 
						? CHARS_CAPITAL[indexCapital - SHIFT + 1]
						: CHARS_CAPITAL[indexCapital + CHARS_CAPITAL.length - SHIFT + 1];
				}
				/* @formatter:on */
			}
			result = String.valueOf(plainText);
		}
		return result;
	}
}
