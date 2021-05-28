package cribbage;

public class StrategyFactory {

	static private final StrategyFactory instance = new StrategyFactory();

	public static StrategyFactory getInstance() {
		return instance;
	}



	public IScoringStrategy getStrategy(String phase) {
		IScoringStrategy strategy = null;
		if (phase.equals("starter")) {
			StarterStrategyComposite starterStrategy = new StarterStrategyComposite();

			StarterScoring s = new StarterScoring();
			s.addObserver(Logger.getInstance());
			starterStrategy.addStrategy(s);

			strategy = starterStrategy;

		} else if (phase.equals("play")) {
			PlayStrategyComposite playStrategy = new PlayStrategyComposite();

			PlayPairScoring p = new PlayPairScoring();
			p.addObserver(Logger.getInstance());
			playStrategy.addStrategy(p);

			PlayRunsScoring r = new PlayRunsScoring();
			r.addObserver(Logger.getInstance());
			playStrategy.addStrategy(r);

			TotalScoring t = new TotalScoring();
			t.addObserver(Logger.getInstance());
			playStrategy.addStrategy(t);

			strategy = playStrategy;

		} else if (phase.equals("show")) {
			ShowStrategyComposite showStrategy = new ShowStrategyComposite();

			ShowRunsScoring r = new ShowRunsScoring();
			r.addObserver(Logger.getInstance());
			showStrategy.addStrategy(r);

			ShowPairScoring p = new ShowPairScoring();
			p.addObserver(Logger.getInstance());
			showStrategy.addStrategy(p);

			FifteenCombScoring f = new FifteenCombScoring();
			f.addObserver(Logger.getInstance());
			showStrategy.addStrategy(f);

			FlushScoring fl = new FlushScoring();
			fl.addObserver(Logger.getInstance());
			showStrategy.addStrategy(fl);

			JackScoring j = new JackScoring();
			j.addObserver(Logger.getInstance());
			showStrategy.addStrategy(j);

			strategy = showStrategy;

		}

		return strategy;
	}

	public IScoringStrategy getStrategy(String phase,boolean go) {
		IScoringStrategy strategy = null;

	 	if (phase.equals("play")) {
			PlayStrategyComposite playStrategy = new PlayStrategyComposite();

			LastCardScoring l = new LastCardScoring();
			l.addObserver(Logger.getInstance());
			playStrategy.addStrategy(l);

			strategy = playStrategy;


		}

		return strategy;
	}

}
