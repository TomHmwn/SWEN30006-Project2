package cribbage;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class PlayPairScoring extends PairsScoring {


    public void score(Hand segment, IPlayer player) {

        Card newCard = segment.get(segment.getNumberOfCards()-1);

        if ( (segment.getNumberOfCards() > 2) && (segment.get(segment.getNumberOfCards()-2).getRank().equals(newCard.getRank()))){
            if ( (segment.getNumberOfCards() > 3) && (segment.get(segment.getNumberOfCards()-3).getRank().equals(newCard.getRank())) ){
                if ((segment.getNumberOfCards() > 4) && (segment.get(segment.getNumberOfCards()-4).getRank().equals(newCard.getRank())) ){
                    //pair 4 occurred in play
                    strategy = PAIR4;
                    player.addScore(12);
                    updateScore(player,12,this);

                }
                else {
                    //pair 3 occurred in play
                    strategy = PAIR3;
                    player.addScore(6);
                    updateScore(player,6,this);

                }
            }
            else {
                //pair 2 occurred in play
                strategy = PAIR2;
                player.addScore(2);
                updateScore(player,2,this);

            }
        }
        else {
        }
    }

}
