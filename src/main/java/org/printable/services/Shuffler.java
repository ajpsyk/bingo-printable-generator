package org.printable.services;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Shuffler {
    public static List<List<String>> getUniquePermutations(List<String> imageLabels, int numPermutations, Random seed) {
        Set<List<String>> uniquePermutations = new HashSet<>();
        while (uniquePermutations.size() < numPermutations) {
            List<String> permutation = new ArrayList<>(imageLabels);
            Collections.shuffle(permutation, seed);
            uniquePermutations.add(permutation);
        }
        return new ArrayList<>(uniquePermutations);
    }
}
