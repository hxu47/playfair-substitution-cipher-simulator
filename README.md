# Playfair Cipher Implementation

## Overview
This program implements the Playfair substitution cipher in Java. The Playfair cipher is a manual symmetric encryption technique that encrypts pairs of letters instead of single letters as in simple substitution ciphers.

## Features
- Generates a 5x5 key matrix from a secret key
- Encrypts plaintext using the Playfair cipher rules
- Decrypts ciphertext back to plaintext
- Handles special cases such as:
  - Repeated characters in a pair
  - Odd total number of characters
  - I/J combined in the same position of the matrix

## Methods
The program implements three primary methods as required:
1. `generateKeyMatrix`: Accepts a secret key and generates a 5x5 key matrix
2. `encrypt`: Accepts plaintext and the key matrix to generate ciphertext
3. `decrypt`: Accepts ciphertext and the key matrix to recover plaintext

## How to Use
1. Compile the Java program:
   ```
   javac PlayfairCipher.java
   ```

2. Run the program:
   ```
   java PlayfairCipher
   ```

## Playfair Cipher Rules
The Playfair cipher follows these encryption rules:
1. If two letters are in the same row, replace with letters to the right (wrapping around)
2. If two letters are in the same column, replace with letters below (wrapping around)
3. If two letters form a rectangle, replace with letters at the opposite corners in the same row

## Special Handling
- Repeated letters in a pair are separated by inserting 'X'
- If the plaintext has an odd length, 'Z' is appended
- 'I' and 'J' are treated as the same letter (using 'I' in the matrix)

## Output Format
- Ciphertext is displayed with a space after every pair of letters
- Decrypted text may contain added 'X' characters (for repeated letters) and possibly a trailing 'Z'
