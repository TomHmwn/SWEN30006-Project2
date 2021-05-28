package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Hand;

public class JackScoring implements IScoringStrategy, ScoreBase{

	ArrayList<ScoreObserver> observers = new ArrayList<ScoreObserver>();

	@Override
	public void addObserver(ScoreObserver observer) {
		observers.add(observer);
	}

	public void updateScore(IPlayer player, int score, ScoreBase subject) {
		for (ScoreObserver ob: observers) {
			ob.onUpdateScore(player, score, this);
		}
	}
	private String strategy = "jack";

	private Hand strategyHand = null;

	public Hand getStrategyHand(){return this.strategyHand;}

	public String getStrategy(){return this.strategy;}

	public void score(Hand segment, IPlayer player) {
	}
}
