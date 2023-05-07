package test;
import java.util.ArrayList;

public class Board {
    private final Tile[][] gameBoard;

    private Board() {
        gameBoard = new Tile[15][15];
        for (int i = 0; i < gameBoard.length; i++)
            for (int j = 0; j < gameBoard[i].length; j++)
                gameBoard[i][j] = null;
    }

    //Singlton - Board.
    private static Board myBoard = null;
    public static Board getBoard() {
        if (myBoard == null)
            myBoard = new Board();
        return myBoard;
    }

    //Returns the tiles on the board.
    public Tile[][] getTiles() {
        return gameBoard.clone();
    }

    //Checks the legality of the row and column (in the range or not).
    public boolean legalRowCol(Word word) {
        return word.getRow() >= 0 && word.getRow() <= 14 && word.getCol() >= 0 && word.getCol() <= 14;
    }

    //Checks if the location of the word is inside the board.
    public boolean isInBoard(Word word) {
        if (!legalRowCol(word))
            return false;
        if (word.getVertical()) {
            return (word.getRow() + word.getTiles().length) <= 15;
        } else return (word.getCol() + word.getTiles().length) <= 15;
    }

    //Checks if the word uses a tile or around the word there is a tile that was already on the board.
    public boolean isBasedOnTile(Word word) {
        if (word.getVertical()) {
            for (int i = 0; i < word.getTiles().length; i++)
                if (gameBoard[word.getRow() + i][word.getCol()] != null)
                    return true;
            if (word.getCol() != 0)
                for (int i = 0; i < word.getTiles().length; i++)
                    if (gameBoard[word.getRow() + i][word.getCol() - 1] != null)
                        return true;
            if (word.getCol() != 14)
                for (int i = 0; i < word.getTiles().length; i++)
                    if (gameBoard[word.getRow() + i][word.getCol() + 1] != null)
                        return true;
            if (word.getRow() != 0)
                if (gameBoard[word.getRow() - 1][word.getCol()] != null)
                    return true;
            if (word.getRow() != 14)
                return gameBoard[word.getRow() + 1][word.getCol()] != null;
        } else {
            for (int i = 0; i < word.getTiles().length; i++)
                if (gameBoard[word.getRow()][word.getCol() + i] != null)
                    return true;
            if (word.getRow() != 0)
                for (int i = 0; i < word.getTiles().length; i++)
                    if (gameBoard[word.getRow() - 1][word.getCol() + i] != null)
                        return true;
            if (word.getRow() != 14)
                for (int i = 0; i < word.getTiles().length; i++)
                    if (gameBoard[word.getRow() + 1][word.getCol() + i] != null)
                        return true;

            if (word.getCol() != 0)
                if (gameBoard[word.getRow()][word.getCol() - 1] != null)
                    return true;
            if (word.getCol() != 14)
                return gameBoard[word.getRow()][word.getCol() + 1] != null;
        }
        return false;
    }

    //Checks if the new word replaces a tile that was already on the board with another tile.
    public boolean noChangeTile(Word word) {
        if (word.getVertical()) {
            for (int i = 0; i < word.getTiles().length; i++) {
                if(word.getTiles()[i]!=null)
                    if ((gameBoard[word.getRow() + i][word.getCol()] != null) && (gameBoard[word.getRow() + i][word.getCol()].letter != word.getTiles()[i].letter))
                        return false;
            }
        } else {
            for (int i = 0; i < word.getTiles().length; i++) {
                if(word.getTiles()[i]!=null)
                    if ((gameBoard[word.getRow()][word.getCol() + i] != null) && (gameBoard[word.getRow()][word.getCol() + i].letter != word.getTiles()[i].letter))
                        return false;
            }
        }
        return true;
    }

    //True - board is empty
    public boolean isEmptyBoard() {
        for (int i = 0; i < gameBoard.length; i++)
            for (int j = 0; j < gameBoard[i].length; j++)
                if (gameBoard[i][j] != null)
                    return false;
        return true;
    }

    //Checks if the word should receive a star bonus.
    public boolean isBasedOnStar(Word word) {
        if (word.getVertical()) {
            for (int i = 0; i < word.getTiles().length; i++)
                if (word.getRow() + i == 7 && word.getCol() == 7)
                    return true;
        } else {
            for (int i = 0; i < word.getTiles().length; i++)
                if (word.getRow() == 7 && word.getCol() + i == 7)
                    return true;
        }
        return false;
    }

    //Checks the board legality of inserting the word into the board.
    public boolean boardLegal(Word word) {
        if (isEmptyBoard()) {
            if (!isInBoard(word))
                return false;
            return isBasedOnStar(word);
        } else {
            if (!isInBoard(word))
                return false;
            if (!isBasedOnTile(word))
                return false;
            return noChangeTile(word);
        }
    }

    //Checks the dictionary legality of the word.
    public boolean dictionaryLegal(Word word) {
        return true;
    }

    //Checks the direction type of the word.
    //1-verticalLeft,2-verticalRight,3-verticalMiddle,4-horizontalUp,5-horizontalDown,6-horizontalMiddle.
    public int typeOfWord(Word word) {
        if (word.getVertical()) {
            if (word.getCol() == 0)
                return 1;
            if (word.getCol() == gameBoard.length)
                return 2;
            return 3;
        } else {
            if (word.getRow() == 0)
                return 4;
            if (word.getRow() == gameBoard.length)
                return 5;
            return 6;
        }
    }

    //Returns a word that created by placing other word on the board.
    public Word createWord(int line, int firstIndex, int lastIndex, boolean vertical,Word initialWord) {
        Tile[]newWordTiles=new Tile[lastIndex-firstIndex+1];
        Word newWord;
        if (vertical) {
            for (int i = firstIndex; i <=lastIndex ; i++) {
                newWordTiles[i - firstIndex] = gameBoard[i][line];
            }
            newWord=new Word(newWordTiles,firstIndex,line,true);
        } else {
            for (int i = firstIndex; i <=lastIndex ; i++) {
                newWordTiles[i - firstIndex] = gameBoard[line][i];
            }
            newWord=new Word(newWordTiles,line,firstIndex,false);
        }
        return newWord;
    }

    //Given a word, returns an array of the new words that will be created on the board including that word.
    //1-verticalLeft,2-verticalRight,3-verticalMiddle,4-horizontalUp,5-horizontalDown,6-horizontalMiddle.
    public ArrayList<Word> getWords(Word word) {
        int counter = 0;
        ArrayList<Word> wordsArray = new ArrayList<Word>();
        wordsArray.add(counter, word);
        if (!isBasedOnTile(word)) {
            return wordsArray;
        } else {
            switch (typeOfWord(word)) {
                case 1:
                    for (int i = 0; i < word.getTiles().length; i++) {
                        if(word.getTiles()[i]==null)
                            continue;
                        int lastIndex = word.getCol();
                        while (gameBoard[word.getRow() + i][lastIndex+1] != null)
                            lastIndex++;
                        if (lastIndex - word.getCol() > 0) {
                            Word newWord = createWord(word.getRow() + i, word.getCol(), lastIndex, false,word);
                            wordsArray.add(++counter, newWord);
                        }
                    }
                    break;
                case 2:
                    for (int i = 0; i < word.getTiles().length; i++) {
                        if(word.getTiles()[i]==null)
                            continue;
                        int firstIndex = word.getCol();
                        while (gameBoard[word.getRow() + i][firstIndex-1] != null)
                            firstIndex--;
                        if (word.getCol() - firstIndex > 0) {
                            Word newWord = createWord(word.getRow() + i, firstIndex, word.getCol(), false,word);
                            wordsArray.add(++counter, newWord);
                        }
                    }
                    break;
                case 3:
                    for (int i = 0; i < word.getTiles().length; i++) {
                        if(word.getTiles()[i]==null)
                            continue;
                        int firstIndex = word.getCol();
                        int lastIndex = word.getCol();
                        while (gameBoard[word.getRow() + i][firstIndex-1] != null)
                            firstIndex--;
                        while (gameBoard[word.getRow() + i][lastIndex+1] != null)
                            lastIndex++;
                        if (lastIndex - firstIndex > 0) {
                            Word newWord = createWord(word.getRow() + i, firstIndex, lastIndex, false,word);
                            wordsArray.add(++counter, newWord);
                        }
                    }
                    break;
                case 4:
                    for (int i = 0; i < word.getTiles().length; i++) {
                        if(word.getTiles()[i]==null)
                            continue;
                        int lastIndex = word.getRow();
                        while (gameBoard[lastIndex+1][word.getCol() + i] != null)
                            lastIndex++;
                        if (lastIndex - word.getRow() > 0) {
                            Word newWord = createWord(word.getCol() + i, word.getRow(), lastIndex, true,word);
                            wordsArray.add(++counter, newWord);
                        }
                    }
                    break;
                case 5:
                    for (int i = 0; i < word.getTiles().length; i++) {
                        if(word.getTiles()[i]==null)
                            continue;
                        int firstIndex = word.getRow();
                        while (gameBoard[firstIndex-1][word.getCol() + i] != null)
                            firstIndex--;
                        if (word.getRow() - firstIndex > 0) {
                            Word newWord = createWord(word.getCol() + i, firstIndex, word.getRow(), true,word);
                            wordsArray.add(++counter, newWord);
                        }
                    }

                    break;
                case 6:
                    for (int i = 0; i < word.getTiles().length; i++) {
                        if(word.getTiles()[i]==null)
                            continue;
                        int firstIndex = word.getRow();
                        int lastIndex = word.getRow();
                        while (gameBoard[firstIndex-1][word.getCol() + i] != null)
                            firstIndex--;
                        while (gameBoard[lastIndex+1][word.getCol() + i] != null)
                            lastIndex++;
                        if (lastIndex - firstIndex > 0) {
                            Word newWord = createWord(word.getCol() + i, firstIndex, lastIndex, true,word);
                            wordsArray.add(++counter, newWord);
                        }
                    }
                    break;
            }
        }
        return wordsArray;
    }

    //Given location, Returns the color of the tile.
    //0-Green,1-Red,2-Yellow,3-PaleBlue,4-Blue,5-Star.
    public int getColor(int R, int C) {
        if ((R == 0 && C == 0) || (R == 0 && C == 7) || (R == 0 && C == 14) || (R == 7 && C == 0) || (R == 7 && C == 14) || (R == 14 && C == 0) || (R == 14 && C == 7) || (R == 14 && C == 14))
            return 1;
        else if ((R == 1 && C == 1) || (R == 1 && C == 13) || (R == 2 && C == 2) || (R == 2 && C == 12) || (R == 3 && C == 3) || (R == 3 && C == 11) || (R == 4 && C == 4) || (R == 4 && C == 10) || (R == 10 && C == 4) || (R == 10 && C == 10) || (R == 11 && C == 3) || (R == 11 && C == 11) || (R == 12 && C == 2) || (R == 12 && C == 12) || (R == 13 && C == 1) || (R == 13 && C == 13))
            return 2;
        else if ((R == 0 && C == 3) || (R == 0 && C == 11) || (R == 2 && C == 6) || (R == 2 && C == 8) || (R == 3 && C == 0) || (R == 3 && C == 7) || (R == 3 && C == 14) || (R == 6 && C == 2) || (R == 6 && C == 6) || (R == 6 && C == 8) || (R == 6 && C == 12) || (R == 7 && C == 3) || (R == 7 && C == 11) || (R == 8 && C == 2) || (R == 8 && C == 6) || (R == 8 && C == 8) || (R == 8 && C == 12) || (R == 11 && C == 0) || (R == 11 && C == 7) || (R == 11 && C == 14) || (R == 12 && C == 6) || (R == 12 && C == 8) || (R == 14 && C == 3) || (R == 14 && C == 11))
            return 3;
        else if ((R == 1 && C == 5) || (R == 1 && C == 9) || (R == 5 && C == 1) || (R == 5 && C == 5) || (R == 5 && C == 9) || (R == 5 && C == 13) || (R == 9 && C == 1) || (R == 9 && C == 5) || (R == 9 && C == 9) || (R == 9 && C == 13) || (R == 13 && C == 5) || (R == 13 && C == 9))
            return 4;
        else if(R == 7 && C == 7)
            return 5;
        return 0;
    }

    //Given a word, calculates the total score of the word, including bonuses.
    public int getScore(Word word,boolean isFirstWord) {
        int score = 0;
        int multiWord = 1;
        if (word.getVertical()) {
            for (int i = 0; i < word.getTiles().length; i++) {
                if(word.getTiles()[i]==null&&gameBoard[word.getRow()+i][word.getCol()]==null)
                    continue;
                switch (getColor(word.getRow() + i, word.getCol())) {
                    case 1:
                        if(word.getTiles()[i]==null)
                        {
                            score+=gameBoard[word.getRow()+i][word.getCol()].score;
                        }
                        else score += word.getTiles()[i].score;
                        multiWord *= 3;
                        break;
                    case 2:
                        if(word.getTiles()[i]==null)
                        {
                            score+=gameBoard[word.getRow()+i][word.getCol()].score;
                        }
                        else score += word.getTiles()[i].score;
                        multiWord *= 2;
                        break;
                    case 3:
                        if(word.getTiles()[i]==null)
                        {
                            score+=2*gameBoard[word.getRow()+i][word.getCol()].score;
                        }
                        else score += 2 * (word.getTiles()[i].score);
                        break;
                    case 4:
                        if(word.getTiles()[i]==null)
                        {
                            score+=3*gameBoard[word.getRow()+i][word.getCol()].score;
                        }
                        else score += 3 * (word.getTiles()[i].score);
                        break;
                    case 5:
                        if(word.getTiles()[i]==null)
                        {
                            score+=gameBoard[word.getRow()+i][word.getCol()].score;
                        }
                        else score += word.getTiles()[i].score;
                        if(isFirstWord)
                            multiWord *= 2;
                        break;
                    default:
                        if(word.getTiles()[i]==null)
                        {
                            score+=gameBoard[word.getRow()+i][word.getCol()].score;
                        }
                        else score += word.getTiles()[i].score;
                }
            }
        } else {
            for (int i = 0; i < word.getTiles().length; i++) {
                if(word.getTiles()[i]==null&&gameBoard[word.getRow()][word.getCol()+i]==null)
                    continue;
                switch (getColor(word.getRow(), word.getCol() + i)) {
                    case 1:
                        if(word.getTiles()[i]==null)
                        {
                            score+=gameBoard[word.getRow()][word.getCol()+i].score;
                        }
                        else score += word.getTiles()[i].score;
                        multiWord *= 3;
                        break;
                    case 2:
                        if(word.getTiles()[i]==null)
                        {
                            score+=gameBoard[word.getRow()][word.getCol()+i].score;
                        }
                        else score += word.getTiles()[i].score;
                        multiWord *= 2;
                        break;
                    case 3:
                        if(word.getTiles()[i]==null)
                        {
                            score+=2*gameBoard[word.getRow()][word.getCol()+i].score;
                        }
                        else score += 2 * (word.getTiles()[i].score);
                        break;
                    case 4:
                        if(word.getTiles()[i]==null)
                        {
                            score+=3*gameBoard[word.getRow()][word.getCol()+i].score;
                        }
                        else score += 3 * (word.getTiles()[i].score);
                        break;
                    case 5:
                        if(word.getTiles()[i]==null)
                        {
                            score+=gameBoard[word.getRow()][word.getCol()+i].score;
                        }
                        else score += word.getTiles()[i].score;
                        if(isFirstWord)
                            multiWord *= 2;
                        break;
                    default:
                        if(word.getTiles()[i]==null)
                        {
                            score+=gameBoard[word.getRow()][word.getCol()+i].score;
                        }
                        else score += word.getTiles()[i].score;
                }
            }
        }
        score *= multiWord;
        return score;
    }

    //Given a word, checks board legality, than checks dictionary legality for the word and the created words.
    //Returns the total score for all words or 0.
    public int tryPlaceWord(Word word) {
        int totalScore = 0;
        boolean emptyFlag=false;
        if(word.getTiles().length<=1)
            return 0;
        if(isEmptyBoard())
            emptyFlag=true;
        if (!boardLegal(word))
            return 0;
        if (!dictionaryLegal(word))
            return 0;
        if(word.getVertical()) {
            for (int j = 0; j < word.getTiles().length; j++) {
                if(word.getTiles()[j]!=null)
                    gameBoard[word.getRow()+j][word.getCol()]=word.getTiles()[j];
            }
        }
        else {
            for (int j = 0; j < word.getTiles().length; j++) {
                if(word.getTiles()[j]!=null)
                    gameBoard[word.getRow()][word.getCol() + j] = word.getTiles()[j];
            }
        }
        totalScore+=getScore(word,emptyFlag);
        ArrayList<Word> newWords=newWords = getWords(word);
        if(newWords.size()>1) {
            for (int i = 1; i < newWords.size(); i++) {
                if (!dictionaryLegal(newWords.get(i)))
                    return 0;
                totalScore += getScore(newWords.get(i), false);
            }
        }
        return totalScore;
    }
}
