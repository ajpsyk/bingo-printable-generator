package org.bingo.model;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Collections;

public class ImageShuffler {
    public static Set<List<String>> getUniquePermutations(List<String> imageLabels, int numPermutations) {
        Set<List<String>> uniquePermutations = new HashSet<>();
        while  (uniquePermutations.size() < numPermutations) {
            List<String> permutation = new ArrayList<>(imageLabels);
            Collections.shuffle(permutation);
            uniquePermutations.add(permutation);
        }
        return uniquePermutations;
    }
}
