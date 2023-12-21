import java.io.*;
import java.util.*;
// doesn't properly print words with repeating !!! ex check
public class Wordle {
    private ArrayList<String> words;
    private String[] chosenWord = new String[5];
    private ArrayList<String> userGuesses;

    private Dictionary<String, String> keyboard = new Hashtable<>();

//    var names for the colors !!!

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLACK = "\u001B[47m";
    public static void main(String[] args) {
        Wordle temp = new Wordle();
    }

    /*
    basically --- :
    - reads in file of words
    - chooses random one !!! using random with max num as parameter (.size of the array)
        - stores it at word array
        - store user guess at array too !!!
    - calls new game !!! 6 turns with user guesses
    - print function !!
    - checks word and prints
    - prints keyboard
    - writes to history file
     */
    public Wordle() {
        File file = new File("words.txt");
        userGuesses = new ArrayList<>();
        words = new ArrayList<String>();
        Scanner scnr;
        try {
            scnr = new Scanner(file);
            while (scnr.hasNext()) {
                words.add(scnr.nextLine());
            }
            scnr.close();
        } catch (FileNotFoundException e) {
            System.out.println("i don't know how to pick a word!!!");
            throw new RuntimeException(e);
        } finally {
            Random rand = new Random();
            String temp = "NotFoundd";
            int random;
            do {
                random = rand.nextInt(words.size()); // gets random integer that represents word in array !!
                temp = words.get(random);
            } while (existsInHistory(temp));
            for (int i = 0; i < temp.length(); i++) {
                chosenWord[i] = temp.substring(i, i+1);
            }
//            chosenWord[0] = "c";
//            chosenWord[1] = "h";
//            chosenWord[2] = "e";
//            chosenWord[3] = "c";
//            chosenWord[4] = "k";
//            temp = "check";
            populateKeyboard();
            writeToHistoryFile(words.get(random));
            Game(temp);
        }
    }

    private void Game(String word) {
        int numGuessesLeft = 6;
        Scanner scanner = new Scanner(System.in);
        String guessWord;
        boolean won = false;
        while (numGuessesLeft > 0) {
            if (numGuessesLeft == 1) {
                System.out.println("You have " + numGuessesLeft + " guess left!!");
            } else {
                System.out.println("You have " + numGuessesLeft + " guesses left!!");
            }
            System.out.print(ANSI_RESET + "Enter a guess: ");
            guessWord = scanner.nextLine().toLowerCase();
            if (guessWord.length() != 5) {
                System.out.println("wrong length !!!");
                continue;
            }
            won = printTheWord(guessWord);
            if (won) {
                System.out.println("you won the game !!!! hehehe :)");
                break;
            }
            numGuessesLeft--;
            printKeyboard();
        }
        if (!won) {
            System.out.println("out of guesses !!!!!!");
            System.out.println("The word was actually: " + word);
        }
        scanner.close();
    }

    private void populateKeyboard() {
        for(char ch = 'a'; ch <= 'z'; ++ch) {
            keyboard.put(String.valueOf(ch), "");
        }
    }
    public boolean printTheWord(String guessWord) {
// returns true if the word is all rightttt
        StringBuilder out = new StringBuilder();
        int lettersCorrect = 0;
        String s;
        for (int i = 0; i < guessWord.length(); i++) {
            s = guessWord.substring(i, i + 1).toUpperCase();
            if (guessWord.substring(i, i+1).equals(chosenWord[i])) {
                out.append(ANSI_GREEN).append(s).append(ANSI_RESET);
                keyboard.put(guessWord.substring(i, i+1), ANSI_GREEN);
                lettersCorrect++;
            } else if (letterExistsInWord(guessWord.substring(i, i+1))) {
                if (Objects.equals(keyboard.get(guessWord.substring(i, i + 1)), ANSI_GREEN)) {
                    out.append(ANSI_YELLOW).append(s).append(ANSI_RESET);
                    continue;
                }
                out.append(ANSI_YELLOW).append(s).append(ANSI_RESET);
                keyboard.put(guessWord.substring(i, i+1), ANSI_YELLOW);
            } else {
                out.append(s);
                keyboard.put(guessWord.substring(i, i+1), ANSI_BLACK);
            }
        }
        for (String userGuess : userGuesses) {
            System.out.print(userGuess);
        }
        out.append("\n");
        System.out.println("\n" + out);
        userGuesses.add(out.toString());
        return (lettersCorrect == 5);
    }

    public void printKeyboard() {
        int i = 0;
        for(char ch = 'a'; ch <= 'z'; ++ch) {
            System.out.print(keyboard.get(String.valueOf(ch)) + String.valueOf(ch).toUpperCase() + ANSI_RESET);
            i++;
            if (i % 9 == 0) {
                System.out.println();
            }
        }
        System.out.println();
    }


    private boolean letterExistsInWord(String ch) {
        for (String s : chosenWord) {
            if (s.equals(ch)) {
                return true;
            }
        }
        return false;
    }

    private void writeToHistoryFile(String word) {
        FileWriter writ = null;
        try {
            File f = new File("history.txt");
            writ = new FileWriter(f, true);
            writ.append(word).append("\n");
            writ.flush();
            writ.close();
        } catch (IOException e) {
            System.out.println("idk what is going on !!");
            throw new RuntimeException(e);
        }
    }

    private boolean existsInHistory(String find) {
        ArrayList<String> hist = new ArrayList<>();
        Scanner scnr;
        File f = new File("history.txt");
        try {
            scnr = new Scanner(f);
            String temp;
            while (scnr.hasNext()) {
                temp = scnr.nextLine();
                hist.add(temp);
            }
        } catch (FileNotFoundException e) {
            System.out.println("i don't know how to pick a word!!!");
            throw new RuntimeException(e);
        }
        return hist.contains(find);
    }

}
