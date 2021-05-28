package cribbage;

// Cribbage.java

import ch.aplu.jcardgame.*;
//import ch.aplu.jgamegrid.*;
//
//import java.awt.Color;
//import java.awt.Font;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cribbage extends CardGame {
	static Cribbage cribbage;  // Provide access to singleton

	static int cardValue(Card c) { return ((Rank) c.getRank()).value; }

	class MyCardValues implements Deck.CardValues { // Need to generate a unique value for every card
		public int[] values(Enum suit) {  // Returns the value for each card in the suit
			return Stream.of(Rank.values()).mapToInt(r -> (((Rank) r).order-1)*(Suit.values().length)+suit.ordinal()).toArray();
		}

	}

	static Random random;

	public static <T extends Enum<?>> T randomEnum(Class<T> clazz){
		int x = random.nextInt(clazz.getEnumConstants().length);
		return clazz.getEnumConstants()[x];
	}

	static boolean ANIMATE;

	void transfer(Card c, Hand h) {
		if (ANIMATE) {
			c.transfer(h, true);
		} else {
			c.removeFromHand(true);
			h.insert(c, true);
		}

	}

	private void dealingOut(Hand pack, Hand[] hands) {
		for (int i = 0; i < nStartCards; i++) {
			for (int j=0; j < nPlayers; j++) {
				Card dealt = randomCard(pack);
				dealt.setVerso(false);  // Show the face
				transfer(dealt, hands[j]);
			}
		}
	}

	static int SEED;

	public Hand getStarter() {
		return starter;
	}

	public static Card randomCard(Hand hand){
		int x = random.nextInt(hand.getNumberOfCards());
		return hand.get(x);
	}

	//  private final String version = "0.1";
	static public final int nPlayers = 2;


	public final int nStartCards = 6;
	public final int nDiscards = 2;
	public final int nCardsAfterDiscard = nStartCards - nDiscards;

	private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover", new MyCardValues());

	private final Hand[] hands = new Hand[nPlayers];
	private Hand starter;
	private Hand crib;


	static private final IPlayer[] players = new IPlayer[nPlayers];

	ArrayList<StateObserver> observers = new ArrayList<StateObserver>();
	public void addObserver(StateObserver observer) {
		assert observer!= null;
		observers.add(observer);
	}
	public void removeObserver(StateObserver observer) {
		observers.remove(observer);
	}
	private void notifyShow(IPlayer player, Hand starterCard) {
		for(StateObserver ob: observers) {
			ob.onShow(player,starterCard);
		}
	}
	private void notifyShowCrib(Hand crib, Hand starterCard) {
		for(StateObserver ob: observers) {
			ob.onShowCrib(crib,starterCard);
		}
	}
	private void notifyPlay(IPlayer player,Card playCard, Hand segment) {
		for(StateObserver ob: observers) {
			ob.onPlay(player,playCard, segment);
		}
	}
	private void notifyStarter(Hand starterCard) {
		for(StateObserver ob: observers) {
			ob.onStarter(starterCard);
		}
	}
	private void notifyDeal(IPlayer player) {
		for(StateObserver ob: observers) {
			ob.onDeal(player);
		}
	}
	private void notifyDiscard(IPlayer player) {
		for(StateObserver ob: observers) {
			ob.onDiscard(player);
		}
	}
	private void notifySeed(int seed) {
		for(StateObserver ob: observers) {
			ob.onSeed(seed);
		}
	}


	private void deal(Hand pack, Hand[] hands) {
		for (int i = 0; i < nPlayers; i++) {
			hands[i] = new Hand(deck);
			// players[i] = (1 == i ? new HumanPlayer() : new RandomPlayer());
			players[i].setId(i);
			players[i].startSegment(deck, hands[i]);
		}
		CribbageUI.getInstance().displayDeal(hands);


		dealingOut(pack, hands);
		for (int i = 0; i < nPlayers; i++) {
			hands[i].sort(Hand.SortType.POINTPRIORITY, true);
		}


		//notifying to log the cards at deal for each player
		for (int i = 0; i < nPlayers; i++) {
			notifyDeal(players[i]);
		}

		LoggingUtils logUtil = new LoggingUtils();
		for (int i = 0; i < nPlayers; i++) {
			System.out.println("deal,P"+i+","+logUtil.canonical(players[i].hand,deck));

		}

	}

	private void discardToCrib() {
		crib = new Hand(deck);

		CribbageUI.getInstance().displayCrib(crib);

		for (IPlayer player: players) {
			for (int i = 0; i < nDiscards; i++) {
				transfer(player.discard(), crib);
			}
			crib.sort(Hand.SortType.POINTPRIORITY, true);
		}
		for (IPlayer player: players) {
			player.setInitialHand();
		}
		//notifying to log the discards of each player
		for (int i = 0; i < nPlayers; i++) {
			notifyDiscard(players[i]);
		}
		LoggingUtils logUtil = new LoggingUtils();
		for (int i = 0; i < nPlayers; i++) {
			System.out.println("discard,P"+i+","+logUtil.canonical(players[i].discard,deck));

		}
	}

	private void starter(Hand pack) {
		starter = new Hand(deck);  // if starter is a Jack, the dealer gets 2 points
		CribbageUI.getInstance().displayStarter(starter);

		Card dealt = randomCard(pack);
		dealt.setVerso(false);
		transfer(dealt, starter);

		//notify
		notifyStarter(starter);
		LoggingUtils logUtil = new LoggingUtils();
		System.out.println("starter,"+logUtil.canonical(starter,deck).replaceAll("\\[","").replaceAll("]",""));
		//score
		ScoringManager.getInstance().Score("starter",starter,players[1]); //dealer


	}

	int total(Hand hand) {
		int total = 0;
		for (Card c: hand.getCardList()) total += cardValue(c);
		return total;
	}

	class Segment {
		Hand segment;
		boolean go;
		int lastPlayer;
		boolean newSegment;

		void reset(final List<Hand> segments) {
			segment = new Hand(deck);


			CribbageUI.getInstance().displaySegment(segment,segments.size());
//			segment.setView(Cribbage.this, new RowLayout(segmentLocations[segments.size()], segmentWidth));
//			segment.draw();

			go = false;        // No-one has said "go" yet
			lastPlayer = -1;   // No-one has played a card yet in this segment
			newSegment = false;  // Not ready for new segment yet
		}
	}

	private void play() {
		final int thirtyone = 31;
		List<Hand> segments = new ArrayList<>();
		int currentPlayer = 0; // Player 1 is dealer
		boolean playerSwap = false;
		Segment s = new Segment();
		s.reset(segments);
		while (!(players[0].emptyHand() && players[1].emptyHand())) {
			// System.out.println("segments.size() = " + segments.size());
			Card nextCard = players[currentPlayer].lay(thirtyone-total(s.segment));
			if (nextCard == null ) {
				if (s.go) {
					// Another "go" after previous one with no intervening cards
					// lastPlayer gets 1 point for a "go"
//					ScoringManager.getInstance().Score("play",s.segment,players[s.lastPlayer],s.go);
					s.newSegment = true;
				} else {
					// currentPlayer says "go"
					s.go = true;
				}
				playerSwap = true;
			} else {
				s.lastPlayer = currentPlayer; // last Player to play a card in this segment
				transfer(nextCard, s.segment);
				if (total(s.segment) == thirtyone) {
					s.newSegment = true;
					playerSwap = true;
				} else {
					// if total(segment) == 15, lastPlayer gets 2 points for a 15
					if (!s.go) { // if it is "go" then same player gets another turn
						playerSwap = true;
					}
				}
			}
			//notify the card put on play by a player
			LoggingUtils logUtil = new LoggingUtils();

			//score, shouldn't happen when no card is played
			if (nextCard != null){
				notifyPlay(players[currentPlayer],nextCard,s.segment);
				System.out.println("play,P"+currentPlayer+","+total(s.segment)+","+logUtil.canonical(nextCard));
				ScoringManager.getInstance().Score("play",s.segment,players[currentPlayer]);
			}
			if (((players[0].emptyHand() && players[1].emptyHand())) && ((total(s.segment)) != thirtyone)){
				ScoringManager.getInstance().Score("play",s.segment,players[s.lastPlayer],s.go);
			}
			if (playerSwap){
				currentPlayer = (currentPlayer+1) % 2;
				playerSwap = false;
			}

			if (s.newSegment) {
				segments.add(s.segment);
				s.reset(segments);
			}


		}
	}

	void showHandsCrib() {
//		 score player 0 (non dealer)
		notifyShow(players[0],starter);
		ScoringManager.getInstance().Score("show",players[0].initialHand,players[0]);
//		LoggingUtils logUtil = new LoggingUtils();
//		System.out.println("show,P"+0+","+logUtil.canonical(starter,players[0].deck).replaceAll("\\[","").replaceAll("]","")+ "+" +logUtil.canonical(players[0].initialHand,players[0].deck));
		// score player 1 (dealer)
		notifyShow(players[1],starter);
		ScoringManager.getInstance().Score("show",players[1].initialHand,players[1]);
//		System.out.println("show,P"+1+","+logUtil.canonical(starter,players[1].deck).replaceAll("\\[","").replaceAll("]","")+ "+" +logUtil.canonical(players[1].initialHand,players[1].deck));
		// score crib (for dealer)
		notifyShowCrib(crib,starter);
		ScoringManager.getInstance().Score("show",crib,players[1]);
//		System.out.println("show,P"+1+","+logUtil.canonical(starter,players[1].deck).replaceAll("\\[","").replaceAll("]","")+ "+" +logUtil.canonical(crib,players[1].deck));

	}

	public Cribbage()
	{

		cribbage = this;

		CribbageUI ui = CribbageUI.getInstance();

		Hand pack = deck.toHand(false);
		ui.displayPack(pack);

		addObserver(Logger.getInstance());

		//notify logger the seed of the game
		notifySeed(SEED);

		/* Play the round */
		deal(pack, hands);
		discardToCrib();
		starter(pack);
		play();
		showHandsCrib();


		ui.displayGameOver();

	}

	public static void main(String[] args)
			throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException,
			InstantiationException, IllegalAccessException {
		/* Handle Properties */
		// System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Properties cribbageProperties = new Properties();
		// Default properties
		cribbageProperties.setProperty("Animate", "true");
		cribbageProperties.setProperty("Player0", "cribbage.RandomPlayer");
		cribbageProperties.setProperty("Player1", "cribbage.HumanPlayer");

		// Read properties
		try (FileReader inStream = new FileReader("cribbage.properties")) {
			cribbageProperties.load(inStream);
		}

		// Control Graphics
		ANIMATE = Boolean.parseBoolean(cribbageProperties.getProperty("Animate"));

		// Control Randomisation
		/* Read the first argument and save it as a seed if it exists */
		if (args.length > 0 ) { // Use arg seed - overrides property
			SEED = Integer.parseInt(args[0]);
		} else { // No arg
			String seedProp = cribbageProperties.getProperty("Seed");  //Seed property
			if (seedProp != null) { // Use property seed
				SEED = Integer.parseInt(seedProp);
			} else { // and no property
				SEED = new Random().nextInt(); // so randomise
			}
		}

		random = new Random(SEED);
		System.out.println("seed,"+ SEED);

		// Control Player Types
		Class<?> clazz;
		clazz = Class.forName(cribbageProperties.getProperty("Player0"));
		players[0] = (IPlayer) clazz.getConstructor().newInstance();
		System.out.println(cribbageProperties.getProperty("Player0")+",P0");

		clazz = Class.forName(cribbageProperties.getProperty("Player1"));
		players[1] = (IPlayer) clazz.getConstructor().newInstance();

		System.out.println(cribbageProperties.getProperty("Player0")+",P1");

		// End properties

		new Cribbage();
	}

}
