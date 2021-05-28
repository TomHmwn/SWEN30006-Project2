package cribbage;

import ch.aplu.jcardgame.Card;
// import ch.aplu.jcardgame.Hand;

public class RandomPlayer extends IPlayer {

	@Override
	public Card discard() {
		Card card = Cribbage.randomCard(hand);
		discard.insert(card.clone(), false);
		return card;
	}

	@Override
	Card selectToLay() {
		return hand.isEmpty() ? null : Cribbage.randomCard(hand);
	}

}
