package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Hand;

public abstract class RunsScoring implements IScoringStrategy, ScoreBase{

	ArrayList<ScoreObserver> observers = new ArrayList<ScoreObserver>();

	@Override
	public void addObserver(ScoreObserver observer) {
		observers.add(observer);
	}

	public void updateScore(IPlayer player, int score, ScoreBase subject) {
		for (ScoreObserver ob: observers) {
			ob.onUpdateScore(player, score, this);
		}
		System.out.println("score,P"+player.id+","+player.getScore()+","+strategy);

	}


	@Override
	public void score(Hand segment, IPlayer player) {
	}

	public String RUN3 ="run3";
	public String RUN4 ="run4";
	public String RUN5 ="run5";
	public String RUN6 ="run6";
	public String RUN7 ="run7";


	public String strategy;

	public Hand strategyHand = null;

	public Hand getStrategyHand(){return this.strategyHand;}

	public String getStrategy(){return this.strategy;}
}
