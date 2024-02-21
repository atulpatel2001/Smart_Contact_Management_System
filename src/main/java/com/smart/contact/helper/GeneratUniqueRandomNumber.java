package com.smart.contact.helper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GeneratUniqueRandomNumber {

    public static int generateRandomNumber(int min, int max) {
        // Create a set to store generated numbers
        Set<Integer> generatedNumbers = new HashSet<>();

        // Create a random number generator
        Random random = new Random();

        // Generate a random number
        int randomNumber = random.nextInt((max - min) + 1) + min;

        // Check if the generated number is unique
        while (generatedNumbers.contains(randomNumber)) {
            // Generate a new random number
            randomNumber = random.nextInt((max - min) + 1) + min;
        }

        // Add the generated number to the set
        generatedNumbers.add(randomNumber);

        // Return the unique random number
        return randomNumber;
    }
}
