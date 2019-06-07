import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ProfanityFilter {


    public ProfanityFilter() {
        try {
            loadConfigs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static int largestWordLength = 0;
    static Map<String, String[]> words;

    public static void loadConfigs() throws MalformedURLException, IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://docs.google.com/spreadsheets/d/1GUu7PQMZzKCkf2PRwfcJeJQ0T7driE2KBtoUM_c6i58/export?format=csv").openConnection().getInputStream()));
            words = new HashMap<String, String[]>();
            String line = "";
            int counter = 0;
            while((line = reader.readLine()) != null) {
                counter++;
                String[] content = null;
                try {
                    content = line.split(",");
                    if(content.length == 0) {
                        continue;
                    }
                    String word = content[0];
                    String[] ignore_in_combination_with_words = new String[]{};
                    if(content.length > 1) {
                        ignore_in_combination_with_words = content[1].split("_");
                    }

                    if(word.length() > largestWordLength) {
                        largestWordLength = word.length();
                    }
                    words.put(word.replaceAll(" ", ""), ignore_in_combination_with_words);

                } catch(Exception e) {
                    e.printStackTrace();
                }

            }
            System.out.println("Loaded " + counter + " words to filter out");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    /**
     * Iterates over a String input and checks whether a cuss word was found in a list, then checks if the word should be ignored (e.g. bass contains the word *ss).
     * @param input
     * @return
     */
    public static boolean badWordsFound(String input) {

        if(input == null) {
            return false;
        }

        // remove leetspeak
        input = input.replaceAll("1","i");
        input = input.replaceAll("!","i");
        input = input.replaceAll("3","e");
        input = input.replaceAll("4","a");
        input = input.replaceAll("@","a");
        input = input.replaceAll("5","s");
        input = input.replaceAll("7","t");
        input = input.replaceAll("0","o");
        input = input.replaceAll("9","g");

        input = input.toLowerCase().replaceAll("[^a-zA-Z]", "");

        // iterate over each letter in the word
        for(int start = 0; start < input.length(); start++) {
            // from each letter, keep going to find bad words until either the end of the sentence is reached, or the max word length is reached.
            for(int offset = 1; offset < (input.length()+1 - start) && offset < largestWordLength; offset++)  {
                String wordToCheck = input.substring(start, start + offset);
                if(words.containsKey(wordToCheck)) {
                    // for example, if you want to say the word bass, that should be possible.
                    String[] ignoreCheck = words.get(wordToCheck);
                    boolean ignore = false;
                    for(int s = 0; s < ignoreCheck.length; s++ ) {
                        if(input.contains(ignoreCheck[s])) {
                            ignore = true;
                            break;
                        }
                    }
                    if(!ignore) {
                        return true;
                    }
                }
            }
        }
        return false;


    }

    public String respondToCurse(String badWord) {
        Random rand = new Random();

        String[] responseList = {
                "! I will not allow that in my classroom.",
                ". It's a good thing I heard that and not Jem!",
                ". You need to re-write the circles program if you're gonna curse like that.",
                ". Oh my goodness!",
                "! :angry: :eyes:",
                "! You stop that right now.",
                ". Do you kiss your mother with that mouth?"};
        if (badWordsFound(badWord)) {
            if(rand.nextInt(3) == 0) {
                return responseList[rand.nextInt(6)];
            }
        }
        return "";
    }

}
