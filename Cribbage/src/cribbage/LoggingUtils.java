package cribbage;

import java.util.stream.Collectors;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;


public class LoggingUtils {
	
	/*
	Canonical String representations of Suit, Rank, Card, and Hand
	*/



	String canonical(Suit s) { return s.toString().substring(0, 1); }

	String canonical(Rank r) {
		switch (r) {
			case ACE:case KING:case QUEEN:case JACK:case TEN:
				return r.toString().substring(0, 1);
			default:
				return String.valueOf(r.value);
		}
	}

    String canonical(Card c) { return canonical((Rank) c.getRank()) + canonical((Suit) c.getSuit()); }

    String canonical(Hand h, Deck deck) {
		Hand h1 = new Hand(deck); // Clone to sort without changing the original hand
		for (Card C: h.getCardList()) h1.insert(C.getSuit(), C.getRank(), false);
		h1.sort(Hand.SortType.POINTPRIORITY, false);
		return "[" + h1.getCardList().stream().map(this::canonical).collect(Collectors.joining(",")) + "]";
    }
}
