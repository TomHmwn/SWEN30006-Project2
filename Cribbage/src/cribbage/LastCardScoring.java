package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Hand;

public class LastCardScoring implements IScoringStrategy,ScoreBase{
	
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
	private String strategy = "go";
	
	public String getStrategy(){return this.strategy;}

	private Hand strategyHand = null;

	public Hand getStrategyHand(){return this.strategyHand;}

	public void score(Hand segment, IPlayer player) {
		player.addScore(1);
		updateScore(player,1,this);
    }

}
