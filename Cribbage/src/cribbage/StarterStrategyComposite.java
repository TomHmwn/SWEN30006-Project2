package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Hand;

public class StarterStrategyComposite implements IScoringStrategy {
	private String phase ="starter";
	
	private ArrayList<IScoringStrategy> strategies = new ArrayList<IScoringStrategy>();
	
	public void addStrategy( IScoringStrategy ScoringStrategy) {
		strategies.add(ScoringStrategy);
	}
	
	public void score(Hand Segment, IPlayer player) {
		for (IScoringStrategy st: strategies) {
			st.score(Segment, player);
		}
	}
}

