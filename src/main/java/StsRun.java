import com.google.gson.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by Stephen on 3/13/2018.
 */
// TODO: Everything
    // TODO: Ban Fiindil
    // TODO: Ban Robojumper(?)
public class StsRun {

    enum CharacterClass { IRONCLAD, SILENT }

    private Path filePath;
    private CardCollection cardCollection;

    private String characterClass;
    private int floorReached;
    private boolean isAscensionMode;
    private boolean isVictory;
    private JsonArray cardChoices;
    private JsonArray bossRelics;
    private int ascensionLevel;
    private JsonArray pathPerFloor;
    private JsonArray masterDeck;

    public StsRun(Path filePath, CardCollection cardCollection) {

        this.filePath = filePath;
        this.cardCollection = cardCollection;
        parseFile();
        this.cardCollection.recordNewGame(isVictory);

    }

    public void cardBattle() {

        int battles = 0;

        for (JsonElement obj : cardChoices) {
            battles++;
            JsonObject choiceObj = obj.getAsJsonObject();
            String picked = choiceObj.get("picked").getAsString();
            JsonArray notPicked = choiceObj.getAsJsonArray("not_picked");
            for (JsonElement element : notPicked) {
                cardCollection.cardBattle(picked, element.getAsString(),
                        isVictory, battles, masterDeck);
            }

        }

    }

    public void relicBattle() {

        for (JsonElement obj : bossRelics) {
            JsonObject choiceObj = obj.getAsJsonObject();
            if (choiceObj.get("picked") != null) {
                String picked = choiceObj.get("picked").getAsString();
                JsonArray notPicked = choiceObj.getAsJsonArray("not_picked");
                for (JsonElement element : notPicked) {
                    cardCollection.cardBattle(picked, element.getAsString(),
                            isVictory, masterDeck);
                }
            }
        }

    }

    private void parseFile() {

        try {
            byte[] file = Files.readAllBytes(filePath);
            String jsonString = new String(file);
            JsonParser parser = new JsonParser();

            characterClass = parser.parse(jsonString).getAsJsonObject()
                    .getAsJsonPrimitive("character_chosen").getAsString();
            floorReached = parser.parse(jsonString).getAsJsonObject()
                    .getAsJsonPrimitive("floor_reached").getAsInt();
            isAscensionMode = parser.parse(jsonString).getAsJsonObject()
                    .getAsJsonPrimitive("is_ascension_mode").getAsBoolean();
            isVictory = parser.parse(jsonString).getAsJsonObject()
                    .getAsJsonPrimitive("victory").getAsBoolean();
            cardChoices = parser.parse(jsonString).getAsJsonObject()
                    .getAsJsonArray("card_choices");
            bossRelics = parser.parse(jsonString).getAsJsonObject()
                    .getAsJsonArray("boss_relics");
            ascensionLevel = parser.parse(jsonString).getAsJsonObject()
                    .getAsJsonPrimitive("ascension_level").getAsInt();
            pathPerFloor = parser.parse(jsonString).getAsJsonObject()
                    .getAsJsonArray("path_per_floor");
            masterDeck = parser.parse(jsonString).getAsJsonObject()
                    .getAsJsonArray("master_deck");

            for (JsonElement obj : cardChoices) {
                JsonObject choiceObj = obj.getAsJsonObject();
                String picked = choiceObj.get("picked").getAsString();
                JsonArray notPicked = choiceObj.getAsJsonArray("not_picked");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

//robojumper: Ideally, you'd convert it to some kind of List<String>
//        robojumper: Amd this is where it gets hairy. card_choices is an ARRAY of OBJECTS, each with an ARRAY of strings "not_picked" and a single string "picked"
//robojumper: like it or not, this is what ideal coding looks like

//{"path_per_floor":["M","M","M","M","M","E","M","R","T","E","?","M","R","E","R","B",null,"M","M","?","?","$","E","$","E","T","R","M","M","M","E","R","B",null,"M","$","M","M","M","R","?","M","T","R","M","M","R","M","R","B"],
//        "character_chosen":"IRONCLAD",
//        "items_purchased":["Medical Kit","Whirlwind","Spot Weakness","True Grit","Shockwave","Uppercut"],
//        "gold_per_floor":[112,124,144,162,176,204,219,219,242,272,272,292,292,320,320,392,392,410,421,421,336,4,35,9,36,62,62,72,89,103,135,135,211,211,227,45,59,77,87,87,137,147,147,147,167,184,184,202,202,202],
//        "floor_reached":51,
//        "campfire_rested":0,
//        "playtime":4023,
//        "current_hp_per_floor":[60,65,65,59,56,58,65,65,65,57,57,58,58,64,64,35,58,65,65,44,44,44,44,44,51,51,51,46,55,51,48,48,69,77,76,76,79,79,79,79,79,79,79,79,79,79,79,79,79,55],
//        "items_purged":["Strike_R"],
//        "gold":202,
//        "score":1199,
//        "play_id":"475a1f26-5bf1-4011-859f-188699f60173",
//        "local_time":"20180309141836",
//        "is_prod":false,
//        "is_daily":false,
//        "is_ascension_mode":true,
//        "campfire_upgraded":9,
//        "timestamp":1520633916,
//        "path_taken":["M","M","M","M","M","E","M","R","T","E","?","M","R","E","R","BOSS","M","M","?","?","$","E","$","E","T","R","M","?","?","E","R","BOSS","M","$","?","?","M","R","?","M","T","R","M","M","R","M","R","BOSS"],
//        "build_version":"2018-03-08-BETA",
//        "purchased_purges":2,
//        "victory":true,
//        "master_deck":["Strike_R","Strike_R","Strike_R","Defend_R","Defend_R","Defend_R","Defend_R","Bash+1","Spot Weakness+1","Reckless Charge","Reaper","Pummel","Metallicize","Ghostly Armor","Havoc+1","Iron Wave","Headbutt","Reaper","Dark Embrace","Necronomicurse","Whirlwind+1","Spot Weakness+1","Havoc","True Grit+1","Shrug It Off","Iron Wave+1","Feel No Pain+1","Demon Form+1","Shockwave+1","Uppercut+1","Corruption","Barricade","Havoc","Bloodletting+1"],
//        "max_hp_per_floor":[65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,65,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79,79],
//        "relics":["Burning Blood","Paper Frog","Art of War","Happy Flower","Bag of Marbles","Sozu","Necronomicon","Prayer Wheel","Medical Kit","Mango","Bag of Preparation","Orichalcum","Molten Egg 2","Runic Dome","Lantern"],
//        "card_choices":[{"not_picked":["Ghostly Armor","Perfected Strike"],"picked":"Spot Weakness"},{"not_picked":["Headbutt","Warcry"],"picked":"Reckless Charge"},{"not_picked":["True Grit","Twin Strike"],"picked":"Reaper"},{"not_picked":["Body Slam","Perfected Strike"],"picked":"Pummel"},{"not_picked":["Twin Strike","Clash"],"picked":"Metallicize"},{"not_picked":["Warcry","Clash"],"picked":"Ghostly Armor"},{"not_picked":["Warcry","Clash"],"picked":"Havoc"},{"not_picked":["Burning Pact","Cleave"],"picked":"Iron Wave"},{"not_picked":["Cleave","Whirlwind"],"picked":"Headbutt"},{"not_picked":["Perfected Strike","Anger"],"picked":"Reaper"},{"not_picked":["Barricade","Juggernaut"],"picked":"Dark Embrace"},{"not_picked":["Brutality","Twin Strike"],"picked":"Havoc"},{"not_picked":["Flame Barrier","Iron Wave"],"picked":"Shrug It Off"},{"not_picked":["Wild Strike","Searing Blow"],"picked":"Iron Wave"},{"not_picked":["Evolve","Sword Boomerang"],"picked":"Feel No Pain"},{"not_picked":["Fiend Fire","Dark Embrace"],"picked":"Demon Form"},{"not_picked":["Iron Wave","Armaments"],"picked":"Corruption"},{"not_picked":["Combust","Cleave"],"picked":"Barricade"},{"not_picked":["Blood for Blood","Battle Trance"],"picked":"Havoc"},{"not_picked":["Twin Strike","Perfected Strike"],"picked":"Bloodletting"}],
//        "player_experience":318795,
//        "potions_floor_usage":[10,10,16,50],
//        "damage_taken":[{"damage":11,"enemies":"Small Slimes","floor":1,"turns":4},{"damage":1,"enemies":"Cultist","floor":2,"turns":4},{"damage":0,"enemies":"2 Louse","floor":3,"turns":2},{"damage":12,"enemies":"Exordium Wildlife","floor":4,"turns":4},{"damage":9,"enemies":"Blue Slaver","floor":5,"turns":5},{"damage":24,"enemies":"3 Sentries","floor":6,"turns":9},{"damage":12,"enemies":"3 Louse","floor":7,"turns":3},{"damage":14,"enemies":"Lagavulin","floor":10,"turns":7},{"damage":12,"enemies":"Red Slaver","floor":12,"turns":4},{"damage":13,"enemies":"3 Sentries","floor":14,"turns":8},{"damage":58,"enemies":"The Guardian","floor":16,"turns":9},{"damage":7,"enemies":"3 Byrds","floor":18,"turns":5},{"damage":0,"enemies":"Chosen","floor":19,"turns":4},{"damage":20,"enemies":"Book of Stabbing","floor":23,"turns":3},{"damage":65,"enemies":"Slavers","floor":25,"turns":5},{"damage":18,"enemies":"Snake Plant","floor":28,"turns":4},{"damage":11,"enemies":"Snecko","floor":29,"turns":4},{"damage":10,"enemies":"Slaver and Parasite","floor":30,"turns":3},{"damage":30,"enemies":"Book of Stabbing","floor":31,"turns":4},{"damage":37,"enemies":"Champ","floor":33,"turns":8},{"damage":31,"enemies":"3 Shapes","floor":35,"turns":5},{"damage":0,"enemies":"3 Darklings","floor":37,"turns":1},{"damage":23,"enemies":"Spire Growth","floor":38,"turns":5},{"damage":7,"enemies":"Sphere and 2 Shapes","floor":39,"turns":3},{"damage":0,"enemies":"Maw","floor":42,"turns":5},{"damage":1,"enemies":"3 Darklings","floor":45,"turns":2},{"damage":5,"enemies":"Flame Bruiser 2 Orb","floor":46,"turns":4},{"damage":9,"enemies":"Spire Growth","floor":48,"turns":4},{"damage":90,"enemies":"Awakened One","floor":50,"turns":9}],
//        "event_choices":[{"event_name":"Match and Keep!","player_choice":"0 cards matched","floor":11,"damage_taken":0},{"event_name":"Cursed Tome","player_choice":"Obtained Book","floor":20,"damage_taken":0},{"event_name":"Addict","player_choice":"Obtained Relic","floor":21,"damage_taken":0},{"event_name":"Golden Shrine","player_choice":"Pray","floor":41,"damage_taken":0}],
//        "boss_relics":[{"not_picked":["Snecko Eye","White Beast Statue"],"picked":"Sozu"},{"not_picked":["Black Blood","Orrery"],"picked":"Runic Dome"}],
//        "potions_floor_spawned":[1,3,6,10,18,19,30,33,37,39,42,48],
//        "seed_played":"-1752195877897154443",
//        "ascension_level":15,
//        "is_trial":false}