package test;
import java.util.Objects;
import java.util.Random;

public class Tile {
    public final char letter;
    public final int score;

    private Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return letter == tile.letter && score == tile.score;
    }

    @Override
    public int hashCode() {
        return Objects.hash(letter, score);
    }

    public static class Bag {
        //The amount of tiles in the sack for each letter at the start of a game.
        private final int[] startAmountArray = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        //The current amount of tiles in the bag for each letter.
        private final int[] amountLetterArray = {9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};
        private final Tile[] tileArray=new Tile[26];
        
        //Return a random tile from the sack or null.
        public Tile getRand() {
            if (size() == 0)
                return null;
            int rndIndex = new Random().nextInt(tileArray.length);
            while (amountLetterArray[rndIndex] == 0)
                rndIndex = ((rndIndex + 1) % tileArray.length);
            amountLetterArray[rndIndex]--;
            return tileArray[rndIndex];
        }

        //Given a letter, returns his tile from the sack or null.
        public Tile getTile(char letter) {
            int index = letter - 'A';
            if(index<0||index> amountLetterArray.length-1)
                return null;
            if (amountLetterArray[index] == 0)
                return null;
            amountLetterArray[index]--;
            return tileArray[index];
        }

        //Given a tile, returns it to the bag and limits the amount according to the rules of the game.
        public void put(Tile tile) {
            int index = tile.letter - 'A';
            if(amountLetterArray[index]==startAmountArray[index])
                return;
            amountLetterArray[index]++;
        }

        //Return the current total of tiles in the bag.
        public int size() {
            int tilesInBag = 0;
            for (int i : amountLetterArray)
                tilesInBag += i;
            return tilesInBag;
        }

        //Return the currnet amount of each tile.
        public int[] getQuantities() {
            return amountLetterArray.clone();
        }

        private Bag() {
            char[] letterArray = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            //Score of each letter
            int[] scoreArray = {1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
            for (int i = 0; i< tileArray.length; i++)
                tileArray[i]=new Tile(letterArray[i], scoreArray[i]);
        }

        //Singlton - Bag
        private static Bag myBag = null;
        public static Bag getBag() {
            if (myBag == null)
                myBag = new Bag();
            return myBag;
        }
    }
}
