package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Hand;

public abstract class PairsScoring implements IScoringStrategy,ScoreBase {


	ArrayList<ScoreObserver> observers = new ArrayList<>();

	@Override
	public void addObserver(ScoreObserver observer) {
		assert observer!= null;
		observers.add(observer);
	}

	public void updateScore(IPlayer player, int score, ScoreBase subject) {
		System.out.println("observer len" + observers.size());
		for (ScoreObserver ob: observers) {
			ob.onUpdateScore(player, score, this);
		}
		System.out.println("score,P"+player.id+","+player.getScore()+","+strategy);

	}

	@Override
	public void score(Hand segment, IPlayer player) {
	}

	public String PAIR2 ="pair2";
	public String PAIR3 ="pair3";
	public String PAIR4 ="pair4";

	public String strategy;
	public Hand strategyHand;

	public Hand getStrategyHand(){return this.strategyHand;}

	public String getStrategy(){return this.strategy;}

}
