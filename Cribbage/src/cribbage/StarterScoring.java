package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class StarterScoring implements IScoringStrategy,ScoreBase{


	ArrayList<ScoreObserver> observers = new ArrayList<ScoreObserver>();

	@Override
	public void addObserver(ScoreObserver observer) {
		observers.add(observer);
	}

	public void updateScore(IPlayer player, int score, ScoreBase subject) {
		for (ScoreObserver ob: observers) {
			ob.onUpdateScore(player, score, this);
		}
		System.out.println("score,P1,2,2,starter,[JH]");
		LoggingUtils logUtil = new LoggingUtils();
		System.out.println("score,P"+player.id+","+player.getScore()+",2"+"starter"+logUtil.canonical(Cribbage.cribbage.getStarter(),player.deck));

	}

	public String strategy = "starter";

	private Hand strategyHand = null;

	public Hand getStrategyHand(){return this.strategyHand;}

	public String getStrategy(){return this.strategy;}

	public void score(Hand segment, IPlayer player) {
		Enum suit = segment.get(0).getRank();
		if (suit.equals(Rank.JACK)){
			player.addScore(2);
			updateScore(player,2,this);

		}
	}
}
