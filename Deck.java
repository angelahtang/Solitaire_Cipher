package assignment2;

import java.util.Random;

public class Deck {
	public static String[] suitsInOrder = { "clubs", "diamonds", "hearts", "spades" };
	public static Random gen = new Random();

	public int numOfCards; // contains the total number of cards in the deck
	public Card head; // contains a pointer to the card on the top of the deck

	/*
	 * TODO: Initializes a Deck object using the inputs provided
	 */
	public Deck(int numOfCardsPerSuit, int numOfSuits) {
		/**** ADD CODE HERE ****/
		if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13 || numOfSuits < 1 || numOfSuits > suitsInOrder.length) {
			String errmsg = "Error: 1-13 cards per suit; 1-suitsInOrder number of suits.";
			throw new IllegalArgumentException(errmsg);
		}

		// this.numOfCards = (numOfCardsPerSuit * numOfSuits) + 2;
		this.numOfCards = 0;
		// add to numOfCards when adding card

		for (int i = 0; i < numOfSuits; i++) {
			for (int j = 1; j <= numOfCardsPerSuit; j++) {
				PlayingCard tmpCard = new PlayingCard(suitsInOrder[i], j);
				this.addCard(tmpCard);
			}
		}

		this.addCard(new Joker("Red"));
		this.addCard(new Joker("Black"));
	}

	/*
	 * TODO: Implements a copy constructor for Deck using Card.getCopy().
	 * This method runs in O(n), where n is the number of cards in d.
	 */
	public Deck(Deck d) {
		/**** ADD CODE HERE ****/
		// use getCopy method????? --> returns a copy of the card with suit and rank.

		this.numOfCards = 0;
		Card crtCard = d.head;

		for (int i = 0; i < d.numOfCards; i++) {
			this.addCard(crtCard.getCopy());
			crtCard = crtCard.next;
		}
	}

	/*
	 * For testing purposes we need a default constructor.
	 */
	public Deck() {
	}

	/*
	 * TODO: Adds the specified card at the bottom of the deck. This
	 * method runs in $O(1)$.
	 */
	public void addCard(Card c) {
		/**** ADD CODE HERE ****/
		// if first card in deck
		if (this.head == null) { // head == null is same as num = 0??
			this.head = c;
			this.head.prev = c;
			this.head.next = c;
		} else {
			/*
			 * eg. add 2C
			 * 2c prev = null; next = null
			 * Ac prev = Ac; next = Ac
			 * 
			 * 2C prev = AC prev = AC SO 2C prev = AC
			 * AC prev next = AC next =
			 * 3C next = AC
			 * AC prev = 3C
			 */

			Card tailNode = this.head.prev;
			tailNode.next = c;
			c.prev = tailNode;

			c.next = this.head;
			this.head.prev = c;
		}
		this.numOfCards++;
	}

	/*
	 * TODO: Shuffles the deck using the algorithm described in the pdf.
	 * This method runs in O(n) and uses O(n) space, where n is the total
	 * number of cards in the deck.
	 */
	public void shuffle() {
		/****
		 * ADD CODE HERE
		 * 1. Copy all cards into an array
		 * 2. use shuffle array on algorithm
		 * 3. rebuild Deck
		 ****/
		if (this.numOfCards > 1) {
			Card[] toShuffle = new Card[this.numOfCards];
			Card crtCard = this.head;

			for (int i = 0; i < this.numOfCards; i++) {
				toShuffle[i] = crtCard;
				crtCard = crtCard.next;
			}

			for (int i = this.numOfCards - 1; i >= 1; i--) {
				int j = gen.nextInt(i + 1);
				Card tmp = toShuffle[j];
				toShuffle[j] = toShuffle[i];
				toShuffle[i] = tmp;
			}

			// HELP, what happens to the other deck???
			// WHAT SPACE IS THIS IN
			this.head = null;
			this.numOfCards = 0;
			for (Card x : toShuffle) {
				this.addCard(x);
			}
		}
	}

	/*
	 * TODO: Returns a reference to the joker with the specified color in
	 * the deck. This method runs in O(n), where n is the total number of
	 * cards in the deck.
	 */
	public Joker locateJoker(String color) {
		/**** ADD CODE HERE ****/
		if (numOfCards == 0) {
			return null;
		}

		Card crtCard = this.head;
		boolean joker2 = false;
		for (int i = 0; i < this.numOfCards; i++) {
			if (crtCard instanceof Joker) {
				if (((Joker) crtCard).getColor().equalsIgnoreCase(color) || color.equals("1")) {
					// return if found
					return (Joker) crtCard;
				} else if (color.equals("2")) {
					if (!joker2) {
						joker2 = true;
					} else {
						return (Joker) crtCard;
					} // if the CURRENT CARD is a JOKER, THEN check if color is correct
				}
			}
			crtCard = crtCard.next;
		}
		return null;
	}

	/*
	 * TODO: Moved the specified Card, p positions down the deck. You can
	 * assume that the input Card does belong to the deck (hence the deck is
	 * not empty). This method runs in O(p).
	 */
	public void moveCard(Card c, int p) {
		/**** ADD CODE HERE ****/
		// change pointers of current card
		Card topCard, nextCard, nextCard2, bottomCard;
		if (c == this.head) {
			// p cards that follow wil be placed at the bottom
			// Z A C B
			
			for (int i = 0; i < p; i++) {
				topCard = c;
				nextCard = c.next;
				nextCard2 = nextCard.next;
				bottomCard = c.prev;

				nextCard.next = topCard;
				nextCard.prev = nextCard2;
				bottomCard.next = nextCard;

				topCard.next = nextCard2;
				topCard.prev = nextCard;
				nextCard2.prev = topCard;
			}
		} else {
			for (int i = 0; i < p; i++) {
				nextCard = c;
				nextCard2 = c.next;
				topCard = c.prev;
				bottomCard = c.next.next;

				nextCard.next = bottomCard;
				nextCard.prev = nextCard2;
				nextCard2.next = nextCard;
				nextCard2.prev = topCard;

				topCard.next = nextCard2;
				bottomCard.prev = nextCard;
			}
		}
	}

	/*
	 * TODO: Performs a triple cut on the deck using the two input cards. You
	 * can assume that the input cards belong to the deck and the first one is
	 * nearest to the top of the deck. This method runs in O(1)
	 */
	public void tripleCut(Card firstCard, Card secondCard) {
		/**** ADD CODE HERE ****/
		// change pointers
		// the cards above firstCard are placed below second Card
		// the card below secondCard are placed above first Card
		// change prev of firstCard and next of secondCard
		// last card in first card is now the tail; point to head
		// first card in secondCard is now the head; point to tail

		Card newBot = firstCard.prev;
		Card newTop = secondCard.next;
		Card ogTop = this.head;
		Card ogBot = this.head.prev;

		if (firstCard != this.head && secondCard != this.head.prev) {
			newBot.next = newTop;
			newTop.prev = newBot;

			ogBot.next = firstCard;
			firstCard.prev = ogBot;

			ogTop.prev = secondCard;
			secondCard.next = ogTop;

			this.head = newTop;
		} else if (firstCard == ogTop) {
			// empty above first card
			this.head = newTop;
		} else if (secondCard == ogBot) {
			this.head = firstCard;
		}
	}

	/*
	 * TODO: Performs a count cut on the deck. Note that if the value of the
	 * bottom card is equal to a multiple of the number of cards in the deck,
	 * then the method should not do anything. This method runs in O(n).
	 */
	public void countCut() {
		/**** ADD CODE HERE ****/

		// AC 2C 3C 4C RJ BJ

		Card botCard = this.head.prev;
		int botValue = botCard.getValue() % this.numOfCards;
		for (int i = 0; i < botValue; i++) {
			this.head = this.head.next;
			this.moveCard(botCard, 1);
		}
	}

	/*
	 * TODO: Returns the card that can be found by looking at the value of the
	 * card on the top of the deck, and counting down that many cards. If the
	 * card found is a Joker, then the method returns null, otherwise it returns
	 * the Card found. This method runs in O(n).
	 */
	public Card lookUpCard() {
		/**** ADD CODE HERE ****/
		int topVal = this.head.getValue();

		Card currentNode = this.head;
		for (int i = 0; i < topVal; i++) {
			currentNode = currentNode.next;
		}

		if (currentNode instanceof Joker) { // if not joker
			return null;
		}

		return currentNode;
	}

	/*
	 * TODO: Uses the Solitaire algorithm to generate one value for the keystream
	 * using this deck. This method runs in O(n).
	 */
	public int generateNextKeystreamValue() {
		/**** ADD CODE HERE ****/
		do {
			this.moveCard(this.locateJoker("red"), 1);
			this.moveCard(this.locateJoker("black"), 2);
			this.tripleCut(this.locateJoker("1"), this.locateJoker("2"));
			this.countCut();

			Card keyCard = this.lookUpCard();
			if (keyCard != null) {
				return keyCard.getValue();
			}
		} while (this.lookUpCard() == null);

		return 0;
	}

	public abstract class Card {
		public Card next;
		public Card prev;

		public abstract Card getCopy();

		public abstract int getValue();

	}

	public class PlayingCard extends Card {
		public String suit;
		public int rank;

		public PlayingCard(String s, int r) {
			this.suit = s.toLowerCase();
			this.rank = r;
		}

		public String toString() {
			String info = "";
			if (this.rank == 1) {
				// info += "Ace";
				info += "A";
			} else if (this.rank > 10) {
				String[] cards = { "Jack", "Queen", "King" };
				// info += cards[this.rank - 11];
				info += cards[this.rank - 11].charAt(0);
			} else {
				info += this.rank;
			}
			// info += " of " + this.suit;
			info = (info + this.suit.charAt(0)).toUpperCase();
			return info;
		}

		public PlayingCard getCopy() {
			return new PlayingCard(this.suit, this.rank);
		}

		public int getValue() {
			int i;
			for (i = 0; i < suitsInOrder.length; i++) {
				if (this.suit.equals(suitsInOrder[i]))
					break;
			}

			return this.rank + 13 * i;
		}

	}

	public class Joker extends Card {
		public String redOrBlack;

		public Joker(String c) {
			if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black"))
				throw new IllegalArgumentException("Jokers can only be red or black");

			this.redOrBlack = c.toLowerCase();
		}

		public String toString() {
			// return this.redOrBlack + " Joker";
			return (this.redOrBlack.charAt(0) + "J").toUpperCase();
		}

		public Joker getCopy() {
			return new Joker(this.redOrBlack);
		}

		public int getValue() {
			return numOfCards - 1;
		}

		public String getColor() {
			return this.redOrBlack;
		}
	}

}
