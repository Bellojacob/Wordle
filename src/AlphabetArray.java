public class AlphabetArray {
    public static void main(String[] args) {
        // Create an array to store the alphabet letters
        char[] alphabet = new char[26]; // 26 letters in the English alphabet

        // Populate the array with the alphabet letters
        char letter = 'a';
        for (int i = 0; i < 26; i++) {
            alphabet[i] = letter;
            letter++;
        }

        // Print the array
        for (char character : alphabet) {
            System.out.print(character + " ");
        }
    }
}
