import java.io.FileNotFoundException;
import java.util.Scanner;

public class RunRamblersAstrat {

    /**
     * constructor, given a PGM image Reads a PGM file. The maximum greyscale value
     * is rescaled to be between 0 and 255.
     *
     * @param RunRamblersBB
     * @return
     * @throws FileNotFoundException
     */

    public static void main(String[] arg) {

        TerrainMap tm = new TerrainMap("tmc.pgm");
        Scanner myInput = new Scanner( System.in );

        System.out.print("Enter x coordinate of origin: ");
        int xO = myInput.nextInt();
        System.out.print("Enter y coordinte of origin: ");
        int yO = myInput.nextInt();
        System.out.print("Enter x coordinate of destination: ");
        int xD = myInput.nextInt();
        System.out.print("Enter y coordinate of destination: ");
        int yD = myInput.nextInt();

        RamblersSearch s = new RamblersSearch(tm.getWidth(), tm.getDepth(), tm.getTmap());
        RamblersState begin = new RamblersState(xO,yO);
        RamblersState end = new RamblersState(xD,yD);

        String strat = "A*";
        System.out.println(s.runSearch(begin, end, strat));
    }
}
