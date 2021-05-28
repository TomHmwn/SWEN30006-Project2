package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowPairScoring extends PairsScoring {

    public List<Card> getCards(Hand hand,int x,int y){
        List<Card> c = hand.getCardList().subList(x,y);

        return c;

    }
	
	
	public void score(Hand segment, IPlayer player) {
        int stack = 1;
        Enum temp = segment.get(0).getRank();
        Hand newHand = new Hand(player.deck);
        segment.insert(Cribbage.cribbage.getStarter().get(0).clone(),false);
        segment.sort(Hand.SortType.POINTPRIORITY, false);
        int tmp;
        for (int i = 1; i < segment.getNumberOfCards();i++){
            if (segment.get(i).getRank().equals(temp)){
                stack += 1;
            }
            else{
                if (stack == 1) {
                    player.addScore(0);
                }
                else if (stack == 2) {
                    // a pair 2 occurred in show, there could be more so keep running the for loop
                    strategy = PAIR2;
                    strategyHand = new Hand(player.deck);
                    tmp = i - 3;
                    for (int j = (i-1); j > tmp;j--){
                        newHand.insert(segment.get(j).clone(), false);
                    }
                    strategyHand = newHand;
                    player.addScore(2);
                    updateScore(player,2,this);
                }
                else if (stack == 3) {
                    // a pair 3 occurred in show, there could be more so keep running the for loop
                    strategy = PAIR2;
                    strategyHand = new Hand(player.deck);
                    tmp = i - 4;
                    for (int j = (i-1); j > tmp;j--){
                        newHand.insert(segment.get(j).clone(), false);
                    }
                    strategyHand = newHand;
                    player.addScore(6);
                    updateScore(player,6,this);
                }
                else if (stack == 4) {
                    // a pair 4 occurred in show, there could be more so keep running the for loop
                    strategyHand = new Hand(player.deck);
                    tmp = i - 5;
                    for (int j = (i-1); j > tmp;j--){
                        newHand.insert(segment.get(j).clone(), false);
                    }
                    strategyHand = newHand;
                    strategy = PAIR3;
                    player.addScore(12);
                    updateScore(player,12,this);
                }
                temp = segment.get(i).getRank();
                stack = 1;
            }

        }
    }

}
