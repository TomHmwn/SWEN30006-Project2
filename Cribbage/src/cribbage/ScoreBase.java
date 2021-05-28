package cribbage;

import java.util.ArrayList;

public interface ScoreBase  {

	ArrayList<ScoreObserver> observers = new ArrayList<ScoreObserver>();

	void addObserver(ScoreObserver observer);

	void updateScore(IPlayer player, int score, ScoreBase subject);



}
