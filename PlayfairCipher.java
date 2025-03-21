import java.util.Scanner;

public class PlayfairCipher {
    private static final int MATRIX_SIZE = 5;

    /**
     * Method 1: Generates a key matrix from a secret key
     * @param key The secret key
     * @return A 2D array representing the key matrix
     */
    public static char[][] generateKeyMatrix(String key) {
        // Convert key to uppercase and remove any non-alphabetic characters
        key = key.toUpperCase().replaceAll("[^A-Z]", "");
        
        // Create a 5x5 matrix
        char[][] matrix = new char[MATRIX_SIZE][MATRIX_SIZE];
        
        // Create a boolean array to track which letters have been used
        boolean[] used = new boolean[26];
        
        // Treat I and J as the same letter ( use I in the matrix)
        used['J' - 'A'] = true;
        
        // Fill the matrix with the key first
        int row = 0, col = 0;
        for (char c : key.toCharArray()) {
            if (c == 'J') c = 'I'; // Replace J with I
            if (!used[c - 'A']) {
                matrix[row][col] = c;
                used[c - 'A'] = true;
                
                // Move to the next position
                col++;
                if (col == MATRIX_SIZE) {
                    col = 0;
                    row++;
                }
            }
        }
        
        // Fill the remaining cells with unused letters
        for (char c = 'A'; c <= 'Z'; c++) {
            if (!used[c - 'A']) {
                matrix[row][col] = c;
                
                // Move to the next position
                col++;
                if (col == MATRIX_SIZE) {
                    col = 0;
                    row++;
                }
                
                // If we've filled the matrix, break
                if (row == MATRIX_SIZE) break;
            }
        }
        
        return matrix;
    }

    /**
     * Method 2: Encrypts plaintext using the key matrix
     * @param plaintext The text to encrypt
     * @param keyMatrix The key matrix
     * @return The encrypted ciphertext
     */
    public static String encrypt(String plaintext, char[][] keyMatrix) {
        // Convert plaintext to uppercase and remove non-alphabetic characters
        plaintext = plaintext.toUpperCase().replaceAll("[^A-Z]", "");
        
        // Replace J with I
        plaintext = plaintext.replace('J', 'I');
        
        // Prepare the plaintext by handling repeated characters and odd length
        StringBuilder prepared = new StringBuilder();
        for (int i = 0; i < plaintext.length(); i++) {
            prepared.append(plaintext.charAt(i));
            
            // If this is the last character or the next character is the same
            if (i + 1 == plaintext.length() || plaintext.charAt(i) == plaintext.charAt(i + 1)) {
                prepared.append('X');
                // If we added X for a repeated pair, don't skip the next character
                if (i + 1 < plaintext.length() && plaintext.charAt(i) == plaintext.charAt(i + 1)) {
                    // Don't increment i here, we want to process the repeated character again
                } else {
                    i++; // Skip the next character as we've formed a pair
                }
            } else {
                prepared.append(plaintext.charAt(++i)); // Add the next character to form a pair
            }
        }
        
        // If we have an odd number of characters after all adjustments, add a trailing Z
        if (prepared.length() % 2 != 0) {
            prepared.append('Z');
        }
        
        // Encrypt the prepared text
        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < prepared.length(); i += 2) {
            char first = prepared.charAt(i);
            char second = prepared.charAt(i + 1);
            
            // Find positions of both characters in the matrix
            int[] firstPos = findPosition(keyMatrix, first);
            int[] secondPos = findPosition(keyMatrix, second);
            
            // Apply Playfair rules
            if (firstPos[0] == secondPos[0]) {
                // Same row - take character to the right (wrapping if necessary)
                ciphertext.append(keyMatrix[firstPos[0]][(firstPos[1] + 1) % MATRIX_SIZE]);
                ciphertext.append(keyMatrix[secondPos[0]][(secondPos[1] + 1) % MATRIX_SIZE]);
            } else if (firstPos[1] == secondPos[1]) {
                // Same column - take character below (wrapping if necessary)
                ciphertext.append(keyMatrix[(firstPos[0] + 1) % MATRIX_SIZE][firstPos[1]]);
                ciphertext.append(keyMatrix[(secondPos[0] + 1) % MATRIX_SIZE][secondPos[1]]);
            } else {
                // Rectangle rule - take character at the opposite corner in the same row
                ciphertext.append(keyMatrix[firstPos[0]][secondPos[1]]);
                ciphertext.append(keyMatrix[secondPos[0]][firstPos[1]]);
            }
            
            // Add a space after each pair
            if (i + 2 < prepared.length()) {
                ciphertext.append(" ");
            }
        }
        
        return ciphertext.toString();
    }

    /**
     * Method 3: Decrypts ciphertext using the key matrix
     * @param ciphertext The text to decrypt
     * @param keyMatrix The key matrix
     * @return The decrypted plaintext
     */
    public static String decrypt(String ciphertext, char[][] keyMatrix) {
        // Convert ciphertext to uppercase and remove non-alphabetic characters
        ciphertext = ciphertext.toUpperCase().replaceAll("[^A-Z]", "");

        // Decrypt the ciphertext
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < ciphertext.length(); i += 2) {
            char first = ciphertext.charAt(i);
            char second = ciphertext.charAt(i + 1);
            
            // Find positions of both characters in the matrix
            int[] firstPos = findPosition(keyMatrix, first);
            int[] secondPos = findPosition(keyMatrix, second);
            
            // Apply Playfair rules in reverse
            if (firstPos[0] == secondPos[0]) {
                // Same row - take character to the left (wrapping if necessary)
                plaintext.append(keyMatrix[firstPos[0]][(firstPos[1] + MATRIX_SIZE - 1) % MATRIX_SIZE]);
                plaintext.append(keyMatrix[secondPos[0]][(secondPos[1] + MATRIX_SIZE - 1) % MATRIX_SIZE]);
            } else if (firstPos[1] == secondPos[1]) {
                // Same column - take character above (wrapping if necessary)
                plaintext.append(keyMatrix[(firstPos[0] + MATRIX_SIZE - 1) % MATRIX_SIZE][firstPos[1]]);
                plaintext.append(keyMatrix[(secondPos[0] + MATRIX_SIZE - 1) % MATRIX_SIZE][secondPos[1]]);
            } else {
                // Rectangle rule - take character at the opposite corner in the same row
                plaintext.append(keyMatrix[firstPos[0]][secondPos[1]]);
                plaintext.append(keyMatrix[secondPos[0]][firstPos[1]]);
            }
        }

        // Note: The decrypted text might contain 'X's that were added during encryption
        // and a trailing 'Z' if the original plaintext had an odd length.        
        return plaintext.toString();
    }



    /**
     * Utility method to print the key matrix
     * @param matrix The key matrix to print
     */
    public static void printMatrix(char[][] matrix) {
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Helper method to find the position of a character in the key matrix
     * @param matrix The key matrix
     * @param c The character to find
     * @return An array containing the row and column indices
     */
    private static int[] findPosition(char[][] matrix, char c) {
        // Replace J with I since they're treated as the same letter
        if (c == 'J') c = 'I';
        
        // Search for the character in the matrix
        for (int i = 0; i < MATRIX_SIZE; i++) {
            for (int j = 0; j < MATRIX_SIZE; j++) {
                if (matrix[i][j] == c) {
                    return new int[] {i, j};
                }
            }
        }
        
        // This should never happen if the input is valid
        throw new IllegalArgumentException("Character " + c + " not found in key matrix");
    }

    /**
     * Driver program to test the Playfair cipher implementation
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("PLAYFAIR CIPHER - INTERACTIVE MODE");
        // Prompt user to input secret key and plaintext
        System.out.print("Enter secret key: ");
        String secretKey = scanner.nextLine().toUpperCase();
        
        System.out.print("Enter plaintext to encrypt: ");
        String plaintext = scanner.nextLine().toUpperCase();
        
        // Generate the key matrix
        char[][] keyMatrix = generateKeyMatrix(secretKey);
        System.out.println("\nKey Matrix:");
        printMatrix(keyMatrix);
        System.out.println();

        // Generate ciphertext
        String ciphertext = encrypt(plaintext, keyMatrix);
        System.out.println("\nEncrypted text:");
        System.out.println(ciphertext);
        
        // Generate Decrypted text
        String recoveredText = decrypt(ciphertext.replaceAll(" ", ""), keyMatrix);
        System.out.println("\nDecrypted text (with potential X and Z characters added):");
        System.out.println(recoveredText);
    }
}