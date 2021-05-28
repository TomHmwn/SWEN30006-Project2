package cribbage;

import ch.aplu.jcardgame.Hand;

import java.util.Arrays;

public class PlayRunsScoring extends RunsScoring{

    public void score(Hand segment, IPlayer player) {
        int orderNewCard = ((Rank)segment.get(segment.getNumberOfCards() - 1).getRank()).getOrder() - 1;
        int orderCard;
//        segment.sort(Hand.SortType.POINTPRIORITY, false);
        int[] checkRun = new int[13];
        for (int i = 0; i < 13;i++){
            checkRun[i] = 0;
        }
        int nCards = Math.min(segment.getNumberOfCards(), 7);
        int k = 1;
        for (int i = (segment.getNumberOfCards() - 1); i >= (segment.getNumberOfCards() - nCards);i-- ){

            orderCard = ((Rank)segment.get(i).getRank()).getOrder();
//            System.out.println("new Order "+ orderCard);
            if (checkRun[orderCard-1] == 0){
                checkRun[orderCard-1] = k;
                k++;
            }
            else{
//                System.out.println("break");
                break;
            }

        }
        int runLen = 1;
        int runVal = 1;

        for (int i = (orderNewCard + 1); i < checkRun.length;i++){
            if (checkRun[i] != 0){
                runLen += 1;
                runVal += checkRun[i];
            }
            else {
                break;
            }
        }
        for (int i = (orderNewCard - 1);i >= 0;i--){
            if (checkRun[i] != 0){
                runLen += 1;
                runVal += checkRun[i];
            }
            else{
                break;
            }
        }

//        System.out.println(Arrays.toString(checkRun));
//        System.out.println("Longest len " + runLen);
//        System.out.println("runVal " + runVal);
//        System.out.println();
//        System.out.println();

        if ((runLen >= 3) && (runVal == ((runLen * (runLen+1)) / 2))){
            if (runLen == 3){
                strategy = RUN3;
            }
            if (runLen == 4){
                strategy = RUN4;
            }
            if (runLen == 5){
                strategy = RUN5;
            }
            if (runLen == 6){
                strategy = RUN6;
            }
            if (runLen == 7){
                strategy = RUN7;
            }
            // at this point the value of runLen is the point you get for run, (e.g. if runLen == 3, 3-run occurred, +3 points)
            player.addScore(runLen);
            updateScore(player,runLen,this);

        }
    }
}


