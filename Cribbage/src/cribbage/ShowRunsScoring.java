package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.*;

public class ShowRunsScoring extends RunsScoring {


    public void score(Hand segment, IPlayer player) {


//
//        ArrayList<Hand> tmp = new ArrayList<>();
        segment.insert(Cribbage.cribbage.getStarter().get(0).clone(), false);
        Hand newHand = new Hand(player.deck);
        for (Card c : (segment.getCardList())) {
            newHand.insert(c.clone(), false);
        }

        LinkedHashSet<Set<Card>> powerSet = new LinkedHashSet<Set<Card>>();
        for (int i = 0; i < (1 << segment.getNumberOfCards()); i++) {
            Set<Card> element = new HashSet<Card>();
            for (int j = 0; j < segment.getNumberOfCards(); j++) {
                if ((i >> j) % 2 == 1) element.add(segment.get(j));
            }
            powerSet.add(element);
        }
        Iterator<Set<Card>> it = powerSet.iterator();
        ArrayList<Hand> dd = new ArrayList<Hand>();
        for (int i = 0; i < powerSet.size(); i++) {
            dd.add(new Hand(player.deck));
            dd.get(i).getCardList().addAll((it.next()));
        }
        
    }
}
