package org.bingo.services;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class BingoSquareShuffler {
    public static List<List<String>> getUniquePermutations(List<String> imageLabels, int numPermutations, long seed) {
        Set<List<String>> uniquePermutations = new HashSet<>();
        Random randomSeed = new Random(1);
        while (uniquePermutations.size() < numPermutations) {
            List<String> permutation = new ArrayList<>(imageLabels);
            Collections.shuffle(permutation, randomSeed);
            uniquePermutations.add(permutation);
        }
        return new ArrayList<>(uniquePermutations);
    }
}
