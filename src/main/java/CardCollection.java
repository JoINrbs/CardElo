import com.google.gson.JsonArray;

import java.util.*;

/**
 * Created by Stephen on 3/13/2018.
 */
public class CardCollection {

    SortedMap<String, Card> cards;
    int games;
    int wins;

    public CardCollection() {

        cards = new TreeMap<String, Card>();
        games = 0;
        wins = 0;

    }

    public void printEloRatings() {

        List<Card> cardList = new LinkedList<Card>();

        for (Card card : cards.values()) {
            cardList.add(card);
        }

        Collections.sort(cardList, new Comparator<Card>() {
            @Override
            public int compare(Card o1, Card o2) {
                return o1.compareTo(o2);
            };

        });

        for (Card card : cardList) {
            System.out.println(card);
        }

        System.out.println("Overall winrate," + (double)wins/(double)games);


//        System.out.print("Card Name, Overall Winrate, ");
//
//        for (Card card : cardList) {
//            System.out.print(card.getCardName() + ",");
//        }
//
//        System.out.println();

//        for (Card card : cardList) {
//            System.out.print(card.getCardName() + "," + card.getWinrate() +
//                    ",");
//            Map<String, Double> companionWinrateMap = card.getCompanionWinrateMap();
//            for (Card card2 : cardList) {
//                if (!companionWinrateMap.containsKey(card2.getCardName())) {
//                    companionWinrateMap.put(card2.getCardName(), 0.0);
//                }
//                System.out.print(companionWinrateMap.get(card2.getCardName())
//                        + ",");
//            }
//            System.out.println();
//        }

    }

    public void cardBattle(String winner, String loser, boolean victory, int
            battles, JsonArray masterDeck) {

        addCard(winner);
        addCard(loser);

        Card winnerCard = cards.get(winner);
        Card loserCard = cards.get(loser);

        double winnerRating = winnerCard.getElo();
        double loserRating = loserCard.getElo();

        winnerCard.updateRating(1, loserRating, victory, battles, masterDeck);
        loserCard.updateRating(0, winnerRating, victory, battles, masterDeck);

    }

    public void cardBattle(String winner, String loser, boolean victory,
                           JsonArray masterDeck) {
        cardBattle(winner, loser, victory, 1, masterDeck);
    }

    // adds new Card to CardCollection if Card having that name didn't
    // already exist.
    public void addCard(String newCard) {

        if (!this.contains(newCard)) {
            cards.put(newCard, new Card(newCard));
        }

    }

    // checks if CardCollection contains a Card with the parameter name.
    public boolean contains(String cardName) {

        for (String name : cards.keySet()) {
            if (name.equals(cardName)) {
                return true;
            }
        }

        return false;

    }

    public void recordNewGame(boolean victory) {
        games++;
        if (victory) {
            wins++;
        }
    }

}
