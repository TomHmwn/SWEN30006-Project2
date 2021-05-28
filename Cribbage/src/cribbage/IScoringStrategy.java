package cribbage;

import ch.aplu.jcardgame.Hand;

public interface IScoringStrategy {

	void score(Hand segment, IPlayer player);
}

