package cribbage;


import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;



import java.awt.*;

public class CribbageUI extends CardGame{
	static private final CribbageUI instance = new CribbageUI();

	public static CribbageUI getInstance() {

		return instance;
	}

	private final String version = "0.1";
	private int nPlayers = Cribbage.nPlayers;

	private int SEED = Cribbage.SEED;


	private final int handWidth = 400;
	private final int cribWidth = 150;
	private final int segmentWidth = 180;

	private final Location[] handLocations = {
			new Location(360, 75),
			new Location(360, 625)
	};
	private final Location[] scoreLocations = {
			new Location(590, 25),
			new Location(590, 675)
	};
	private final Location[] segmentLocations = {  // need at most three as 3x31=93 > 2x4x10=80
			new Location(150, 350),
			new Location(400, 350),
			new Location(650, 350)
	};

	private final Location starterLocation = new Location(50, 625);
	private final Location cribLocation = new Location(700, 625);
	private final Location seedLocation = new Location(5, 25);
	// private final TargetArea cribTarget = new TargetArea(cribLocation, CardOrientation.NORTH, 1, true);
	private final Actor[] scoreActors = {null, null}; //, null, null };
	private final Location textLocation = new Location(350, 450);

	public static void setStatus(String string) {CribbageUI.getInstance().setStatusText(string); }

	final Font normalFont = new Font("Serif", Font.BOLD, 24);
	final Font bigFont = new Font("Serif", Font.BOLD, 36);


	public void displayScore() {
		for (int i = 0; i < nPlayers; i++) {
			scoreActors[i] = new TextActor("0", Color.WHITE, bgColor, bigFont);
			addActor(scoreActors[i], scoreLocations[i]);
		}
	}

	public void displayScore(int player,int score) {
		removeActor(scoreActors[player]);
		scoreActors[player] = new TextActor(String.valueOf(score), Color.WHITE, bgColor, bigFont);
		addActor(scoreActors[player], scoreLocations[player]);
	}

	public void displayDeal(Hand[] hands) {
		RowLayout[] layouts = new RowLayout[nPlayers];
		for (int i = 0; i < nPlayers; i++)
		{
			layouts[i] = new RowLayout(handLocations[i], handWidth);
			layouts[i].setRotationAngle(0);
			// layouts[i].setStepDelay(10);
			// setLayoutDelay(layouts,i,10);
			hands[i].setView(this, layouts[i]);
			hands[i].draw();
		}
		setLayoutDelay(layouts,0,0);
		//layouts[0].setStepDelay(0);
	}

	public void setLayoutDelay(RowLayout[] layouts,int index, int delay) {
		layouts[index].setStepDelay(delay);
	}
	public void displayCrib(Hand crib) {
		RowLayout layout = new RowLayout(cribLocation, cribWidth);
		layout.setRotationAngle(0);
		crib.setView(this, layout);
		// crib.setTargetArea(cribTarget);
		crib.draw();
	}

	public void displayStarter(Hand starter) {
		RowLayout layout = new RowLayout(starterLocation, 0);
		layout.setRotationAngle(0);
		starter.setView(this, layout);
		starter.draw();
	}

	public void displaySegment(Hand segment, int size) {
		segment.setView(this, new RowLayout(segmentLocations[size], segmentWidth));
		segment.draw();
	}

	public void displayPack(Hand pack) {
		RowLayout layout = new RowLayout(starterLocation, 0);
		layout.setRotationAngle(0);
		pack.setView(this, layout);
		pack.setVerso(true);
		pack.draw();
		addActor(new TextActor("Seed: " + SEED, Color.BLACK, bgColor, normalFont), seedLocation);
	}

	public void displayGameOver() {
		addActor(new Actor("sprites/gameover.gif"), textLocation);
		setStatusText("Game over.");
		refresh();
	}
	public CribbageUI()
	{
		super(850, 700, 30);

		setTitle("Cribbage (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
		setStatusText("Initializing...");
		displayScore();
		setSimulationPeriod(1);


	}
}
