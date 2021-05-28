package cribbage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class Logger implements ScoreObserver, StateObserver {



	private File fileName = new File("cribbage.log");

	static private final Logger instance = new Logger();

	private String phase = null;

	private Deck deck;



	LoggingUtils logUtil = new LoggingUtils();

	public static Logger getInstance() {
		return instance;
	}



	@Override
	public void onUpdateScore(IPlayer player, int score, ScoreBase subject) {


		int id = player.id;
		int playerScore = player.getScore();

		String strategy="";
		Hand strategyHand = null;


		if (subject instanceof FifteenCombScoring ) {
			strategy = ((FifteenCombScoring) subject).getStrategy();
			strategyHand = ((FifteenCombScoring) subject).getStrategyHand();
		} else if (subject instanceof StarterScoring) {
			strategy = ((StarterScoring) subject).getStrategy();
			strategyHand = ((StarterScoring) subject).getStrategyHand();
		} else if (subject instanceof FlushScoring) {
			strategy = ((FlushScoring) subject).getStrategy();
			strategyHand = ((FlushScoring) subject).getStrategyHand();
		} else if (subject instanceof LastCardScoring) {
			strategy = ((LastCardScoring) subject).getStrategy();
			strategyHand = ((LastCardScoring) subject).getStrategyHand();
		} else if (subject instanceof TotalScoring) {
			strategy = ((TotalScoring) subject).getStrategy();
			strategyHand = ((TotalScoring) subject).getStrategyHand();
		} else if (subject instanceof PairsScoring) {
			strategy = ((PairsScoring) subject).getStrategy();
			strategyHand = ((PairsScoring) subject).getStrategyHand();
		} else if (subject instanceof RunsScoring) {
			strategy = ((RunsScoring) subject).getStrategy();
			strategyHand = ((RunsScoring) subject).getStrategyHand();
		} else if (subject instanceof JackScoring) {
			strategy = ((JackScoring) subject).getStrategy();
			strategyHand = ((JackScoring) subject).getStrategyHand();
		}




		// For logging Score: Append format {score, player, total player score, score earned, strategy used}
		// into a file

		try {
			FileWriter outStream = new FileWriter ( fileName , true );
			if(phase.equals("show")) {
				outStream.write ("score,P"+ id +","+playerScore+","+ strategy+","+logUtil.canonical(strategyHand,deck)+"\n");
			}
			else{
				outStream.write ("score,P"+ id +","+playerScore+","+ strategy+"\n");
			}
			outStream.close ();
		} catch (IOException e) {
			e.printStackTrace ();
			System.exit (1);
		}

	}
	@Override
	public void onShow(IPlayer player, Hand StarterCard) {
		phase = "show";
		int id = player.id;
		Hand initialHand  = player.initialHand;


		// For logging Score: Append format {show, player, starter, player initial hand}
		// into a file

		try {
			FileWriter outStream = new FileWriter ( fileName , true );
			outStream.write("show,"+"P"+id+","+logUtil.canonical(StarterCard, deck).replaceAll("\\W","")+"+"+logUtil.canonical(initialHand,deck)+"\n");
			outStream.close ();
		} catch (IOException e) {
			e.printStackTrace ();
			System.exit (1);
		}


	}
	@Override
	public void onShowCrib(Hand crib, Hand StarterCard) {
		phase = "show";
		int id = 1;



		// For logging Score: Append format {show, player, starter, player initial hand}
		// into a file

		try {
			FileWriter outStream = new FileWriter ( fileName , true );
			outStream.write("show,"+"P"+id+","+logUtil.canonical(StarterCard, deck).replaceAll("\\W","")+"+"+logUtil.canonical(crib,deck)+"\n");
			outStream.close ();
		} catch (IOException e) {
			e.printStackTrace ();
			System.exit (1);
		}

	}
	@Override
	public void onPlay(IPlayer player, Card playCard, Hand segment) {
		phase = "play";
		int id = player.id;

		int total = Cribbage.cribbage.total(segment);

		// For logging Score: Append format {play, player, limit, play card}
		// into a file

		try {
			FileWriter outStream = new FileWriter ( fileName , true );
			outStream.write("play,"+"P"+id+","+total+","+logUtil.canonical(playCard)+"\n");
			outStream.close ();
		} catch (IOException e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}
	@Override
	public void onStarter(Hand starterCard) {


		// For logging Score: Append format {starter, starter card}
		// into a file



		try {
			FileWriter outStream = new FileWriter ( fileName , true );
			outStream.write ("starter,"+logUtil.canonical(starterCard,deck).replaceAll("\\W","")+"\n");
			outStream.close ();
		} catch (IOException e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}
	@Override
	public void onDeal(IPlayer player) {

		int id = player.id;
		Hand hand = player.hand;
		// For logging Score: Append format {deal, player, hand}
		// into a file
		deck = player.deck;
		try {
			FileWriter outStream = new FileWriter ( fileName , true );
			outStream.write ("deal,"+"P"+id+","+logUtil.canonical(hand,deck)+"\n");
			outStream.close ();
		} catch (IOException e) {
			e.printStackTrace ();
			System.exit (1);
		}

	}
	@Override
	public void onDiscard(IPlayer player) {

		int id = player.id;
		Hand discards = player.discard;


		// For logging Score: Append format {deal, player, discard hand}
		// into a file

		try {
			FileWriter outStream = new FileWriter ( fileName , true );
			outStream.write ("discard,P"+id+","+logUtil.canonical(discards,deck)+"\n");
			outStream.close ();
		} catch (IOException e) {
			e.printStackTrace ();
			System.exit (1);
		}
	}
	@Override
	public void onSeed(int seed) {
		// For logging seed  : Create a new file and append format {seed, seed number}

		try {
			FileWriter outStream = new FileWriter ( fileName );
			outStream . write ("seed,"+seed+"\n");
			outStream . close ();
		} catch ( IOException e) {
			e. printStackTrace ();
			System . exit (1);
		}

	}

}
