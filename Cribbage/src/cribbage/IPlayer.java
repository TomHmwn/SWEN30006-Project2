package cribbage;

import ch.aplu.jcardgame.*;

import java.util.ArrayList;

public abstract class IPlayer {
    int id;
    Deck deck; // Need this since can't get from hand to deck
    Hand hand;
    Hand discard = new Hand(deck);
    Hand initialHand = new Hand(deck);


    private int score;

    public int getScore() {
        return score;
    }

    public void addScore(int addscore) {
        score += addscore;
        CribbageUI.getInstance().displayScore(id,score);
    }

    void setId(int id)  {
        this.id = id;
    }

    public void setInitialHand() {
        for (Card c: ((ArrayList<Card>) hand.getCardList())) {
            initialHand.insert(c.clone(), false);
        }
    }


    void startSegment(Deck deck, Hand hand) {this.deck = deck; this.hand = hand;}
    abstract Card discard();
    boolean emptyHand() {return hand.isEmpty();}
    abstract Card selectToLay();
    Card lay(int limit) {
        // System.out.println("lay(" + limit + ")");
        Hand unlayable = new Hand(deck);
        for (Card c: ((ArrayList<Card>) hand.getCardList().clone()))  // Modify list, so need to iterate over clone
            if (Cribbage.cardValue(c) > limit) {
                c.removeFromHand(true);
                // System.out.println("hand = " + hand.toString());
                unlayable.insert(c, false);
            }
        // hand.draw(); Cribbage.delay(1000);
        Card s = selectToLay();
        hand.insert(unlayable, true);
        return s;
    }

}
