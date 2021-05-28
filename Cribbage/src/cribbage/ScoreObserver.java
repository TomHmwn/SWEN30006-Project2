package cribbage;

public interface ScoreObserver {

	void onUpdateScore(IPlayer player, int score, ScoreBase subject);


}
