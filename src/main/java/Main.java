import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Stephen on 3/13/2018.
 */
public class Main {

    private static CardCollection cardCollection;

    public static void main(String[] args) {

        cardCollection = new CardCollection();

        File dir = new File("D:\\Users\\Stephen\\Documents\\adventskynet\\IRONCLAD");
        File dir2 = new File
                ("D:\\Users\\Stephen\\Documents\\adventskynet\\SILENT");
        printCardAndRelicElos(dir.listFiles());
        printCardAndRelicElos(dir2.listFiles());

    }

    private static void printCardAndRelicElos(File[] directoryListing) {

        for (File child : directoryListing) {
            new StsRun(Paths.get(child.getPath()), cardCollection).cardBattle();
        }
        printEloRatings();
        System.out.println();
        cardCollection = new CardCollection();

        for (File child : directoryListing) {
            new StsRun(Paths.get(child.getPath()), cardCollection)
                    .relicBattle();
        }
        printEloRatings();
        System.out.println();
        cardCollection = new CardCollection();

    }

    public static void printEloRatings() {

        cardCollection.printEloRatings();

    }

}
