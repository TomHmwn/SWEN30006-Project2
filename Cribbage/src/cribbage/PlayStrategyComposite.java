package cribbage;

import java.util.ArrayList;

import ch.aplu.jcardgame.Hand;

public class PlayStrategyComposite implements IScoringStrategy{
	private String phase ="play";
	
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
