package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public interface StateObserver {

	void onShow(IPlayer player, Hand StarterCard);
	void onPlay(IPlayer player, Card playCard, Hand segment);
	void onStarter(Hand starterCard);
	void onDeal(IPlayer player);
	void onShowCrib(Hand crib, Hand StarterCard);
	public void onDiscard(IPlayer player);
	void onSeed(int seed);
}

