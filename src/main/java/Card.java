import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by Stephen on 3/13/2018.
 */
public class Card implements Comparable<Card> {

    private String cardName;
    private int games;
    private int wins;
    private int runWins;
    private double elo;
    private Map<String, Integer> winningCompanions;
    private Map<String, Integer> losingCompanions;

    public Card(String cardName) {

        this.cardName = cardName;
        this.games = 0;
        this.wins = 0;
        this.runWins = 0;
        this.elo = 1600;
        this.winningCompanions = new HashMap<String, Integer>();
        this.losingCompanions = new HashMap<String, Integer>();

    }

    public String getCardName() {
        return cardName;
    }

    public void updateRating(int score, double opposingElo, boolean victory,
                             JsonArray finalDeck) {
        updateRating(score, opposingElo, victory, 1, finalDeck);
    }

    // weights early-game picks 1.5x as heavily as late-game picks. weights
    // the first times that the card is seen considerably more heavily than
    // others with exponential decay on importance.
    public void updateRating(int score, double opposingElo, boolean victory,
                             int battles, JsonArray finalDeck) {
        this.games++;
        if (score == 1) {
            this.wins++;
            if (victory) {
                this.runWins++;
            }
            // add other cards picked with to winning companions or losing
            // companions list
            for (JsonElement ele : finalDeck) {
                String cardName = ele.getAsString();
                if (victory) {
                    addCompanionToMap(cardName, winningCompanions);
                } else {
                    addCompanionToMap(cardName, losingCompanions);
                }
            }
        }

        double k = 32.0;
        if (battles < 10) {
            k = k + 8.0;
        }

        this.elo = this.elo + (k + (100.0 / (Math.pow(games, 2)))) * (
                (double)
                        score - this.expectedScore(opposingElo));
    }

    private void addCompanionToMap(String companion, Map<String, Integer> map) {

        if (map.containsKey(companion)) {
            map.put(companion, map.get(companion)+1);
        } else {
            map.put(companion, 1);
        }

    }

    private double expectedScore(double opposingElo) {
        return 1.0 / (1 +  Math.pow(10, ((opposingElo - this.elo) / 400)));
    }

    public double getElo() {
        return elo;
    }

    public String toString() {

        String workingName = cardName;

        if (cardName.equalsIgnoreCase("venomology")) {
            workingName = "Alchemize";
        }

//        return workingName + ": " + games + " occurences, " + wins + " times " +
//                "picked, " + (int)elo + " rating.";

        return workingName + "," + games + "," + (double)wins/(double)games +
                "," + elo + "," + getWinrate();

    }

    public double getWinrate() {

        return (double)runWins/(double)wins;

    }

    public int compareTo(Card card) {

        return (int)(card.elo - this.elo);

    }

    public Map<String, Double> getCompanionWinrateMap() {

        Map<String, Double> companionWinrateMap = new HashMap<String, Double>
                ();

        for (String str : losingCompanions.keySet()) {

            // never been won with
            if (!winningCompanions.containsKey(str)) {
                winningCompanions.put(str, 0);
            }

            companionWinrateMap.put(str, (double)winningCompanions.get(str) /
                    (double)(winningCompanions.get(str) + losingCompanions.get
                    (str)));

        }

        // never been lost with
        for (String str : winningCompanions.keySet()) {

            if (!losingCompanions.containsKey(str)) {
                companionWinrateMap.put(str, 1.0);
            }

        }

        return companionWinrateMap;

    }

}




















