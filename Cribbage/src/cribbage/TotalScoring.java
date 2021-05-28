package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class TotalScoring implements IScoringStrategy, ScoreBase{
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
	
	private String FIFTEEN = "fifteen";
	private String THIRTYONE = "thirtyone";

	
	public String strategy;
	
	public String getStrategy(){return this.strategy;}

	private Hand strategyHand = null;

	public Hand getStrategyHand(){return this.strategyHand;}
	
	public void score(Hand segment, IPlayer player) {
		int total = 0;
		for (Card c: segment.getCardList()) total += ((Rank)c.getRank()).getValue();
		if (total == 31){
			strategy = THIRTYONE;
			player.addScore(2);
			updateScore(player,2,this);
		}
		if (total == 15){
			strategy = FIFTEEN;
			player.addScore(2);
			updateScore(player,2,this);

		}
    }

	
}
