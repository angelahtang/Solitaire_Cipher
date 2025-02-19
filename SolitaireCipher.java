package assignment2;

public class SolitaireCipher {
	public Deck key;

	public SolitaireCipher (Deck key) {
		this.key = new Deck(key); // deep copy of the deck
	}

	/* 
	 * TODO: Generates a keystream of the given size
	 */
	public int[] getKeystream(int size) {
		/**** ADD CODE HERE ****/
		int[] keyStream = new int[size];
		
		for (int i = 0; i < size; i++) {
			keyStream[i] = this.key.generateNextKeystreamValue();
		}

		return keyStream;
	}

	/* 
	 * TODO: Encodes the input message using the algorithm described in the pdf.
	 */
	public String encode(String msg) {
		/**** ADD CODE HERE ****/
		msg = msg.toUpperCase().replaceAll("[^A-Z]", "");

		char[] msgChars = msg.toCharArray();
		int[] keyStream = this.getKeystream(msgChars.length);
		
		for (int i = 0; i < msgChars.length; i++) {
			char letter = msgChars[i];
			letter = (char) (letter + keyStream[i]);

			if (letter > 'Z') {
				letter = (char) (letter - 26);
			}
			msgChars[i] = letter;
		}

		return String.valueOf(msgChars);
	}

	/* 
	 * TODO: Decodes the input message using the algorithm described in the pdf.
	 */
	public String decode(String msg) {
		/**** ADD CODE HERE ****/

		char[] msgChars = msg.toCharArray();
		int[] keyStream = this.getKeystream(msgChars.length);
		
		for (int i = 0; i < msgChars.length; i++) {
			char letter = msgChars[i];
			letter = (char) (letter - keyStream[i]);

			if (letter < 'A') {
				letter = (char) (letter + 26);
			}
			msgChars[i] = letter;
		}

		return String.valueOf(msgChars);
	}

}
