package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class ScoringManager {


//	private Hand starter;

	static private final ScoringManager instance = new ScoringManager();

	public static ScoringManager getInstance() {
		return instance;
	}


	public void Score(String phase, Hand segment, IPlayer player) {
		if (phase.equals("show")) {
			Hand tmp = new Hand(player.deck);
			for (Card c: (ArrayList<Card>) segment.getCardList()) {
				tmp.insert(c.clone(), false);
			}
//			for (Card c: (ArrayList<Card>) Cribbage.cribbage.getStarter().getCardList()) {
//				tmp.insert(c.clone(), false);
//			}
			StrategyFactory.getInstance().getStrategy(phase).score(tmp,player);
		}
		else {
			StrategyFactory.getInstance().getStrategy(phase).score(segment, player);
		}
	}

	public void Score(String phase, Hand segment, IPlayer player, boolean go) {
		StrategyFactory.getInstance().getStrategy(phase,go).score(segment,player);
	}

}
