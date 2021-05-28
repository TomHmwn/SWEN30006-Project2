package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Hand;

public class FlushScoring implements IScoringStrategy, ScoreBase{

	private final String FLUSH4 ="flush4";
	private final String FLUSH5 ="flush5";

	private String strategy;


	public String getStrategy(){return this.strategy;}

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


	private Hand strategyHand = null;

	public Hand getStrategyHand(){return this.strategyHand;}

	public void score(Hand segment, IPlayer player) {
		Hand starter = Cribbage.cribbage.getStarter();

		for (int i = 0; i < segment.getNumberOfCards();i++){
			if (segment.get(i).equals(segment.get(i+1))){

			}
			else{
				return;
			}
		}
		strategyHand = new Hand(player.deck);
		if (starter.get(0).getSuit().equals(segment.get(0))){
			strategy = FLUSH5;
			player.addScore(5);
			segment.insert(Cribbage.cribbage.getStarter().get(0).clone(),false);
			strategyHand = segment;
			updateScore(player,5,this);
		}
		else{
			strategy = FLUSH4;
			player.addScore(4);
			strategyHand = segment;
			updateScore(player,4,this);

		}

	}
}
