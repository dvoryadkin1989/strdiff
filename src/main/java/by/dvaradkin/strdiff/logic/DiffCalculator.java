package by.dvaradkin.strdiff.logic;

import java.util.*;

public class DiffCalculator {
    private final LevenshteinDistanceCalculator levenshtein = new LevenshteinDistanceCalculator();

    public List<GuessResult> guessEdits(List<String> originalBics, List<String> resultingBics) {
        List<DistanceHolder> pairwiseDistances = new ArrayList<>();
        for (int i = 0; i < originalBics.size(); i++) {
            for (int j = 0; j < resultingBics.size(); j++) {
                int distance = calculateDistance(originalBics.get(i), resultingBics.get(j));
                if (distance > 0) {
                    pairwiseDistances.add(new DistanceHolder(i, j, distance));
                }
            }
        }
        Collections.sort(pairwiseDistances);

        Set<Integer> matchedOriginalIndexes = new HashSet<>();
        Set<Integer> matchedResultingIndexes = new HashSet<>();
        List<GuessResult> results = new ArrayList<>();
        for (DistanceHolder currentDistanceHolder : pairwiseDistances) {
            if (isNotMatchedAlready(matchedOriginalIndexes, matchedResultingIndexes, currentDistanceHolder)) {
                results.add(createGuessResult(originalBics, resultingBics, currentDistanceHolder));
                matchedOriginalIndexes.add(currentDistanceHolder.getOriginalBicIndex());
                matchedResultingIndexes.add(currentDistanceHolder.getResultingBicIndex());
            }
        }

//        // @formatter:off
//        createSetOfIndexes(originalBics).stream()
//                .filter(matchedOriginalIndexes::contains)
//                .map(originalBics::get)
//                .map(nonMatchedOriginalBic -> new GuessResult( nonMatchedOriginalBic, "DELETED"))
//                .forEach(results::add);
//        // @formatter:on
//
//        // @formatter:off
//        createSetOfIndexes(resultingBics).stream()
//                .filter(matchedResultingIndexes::contains)
//                .map(resultingBics::get)
//                .map(nonMatchedResultingBic -> new GuessResult("NEW", nonMatchedResultingBic))
//                .forEach(results::add);
//        // @formatter:on

        return results;
    }

    private GuessResult createGuessResult(List<String> originalBics, List<String> resultingBics, DistanceHolder current) {
        return new GuessResult(originalBics.get(current.getOriginalBicIndex()), resultingBics.get(current.getResultingBicIndex()));
    }

    private boolean isNotMatchedAlready(Set<Integer> matchedOriginalIndexes, Set<Integer> matchedResultingIndexes, DistanceHolder current) {
        return !matchedOriginalIndexes.contains(current.getOriginalBicIndex()) && !matchedResultingIndexes.contains(current.getResultingBicIndex());
    }

    private int calculateDistance(String bic1, String bic2) {
        return levenshtein.calculate(bic1, bic2);
    }

    // I know, I know...
    private Set<Integer> createSetOfIndexes(List<String> bics) {
        Set<Integer> indexes = new HashSet<>();
        for (int i = 0; i < bics.size(); i++) {
            indexes.add(i);
        }
        return indexes;
    }

    private static class DistanceHolder implements Comparable<DistanceHolder> {
        private final int originalBicIndex;
        private final int resultingBicIndex;
        private final int distance;

        private DistanceHolder(int originalBicIndex, int resultingBicIndex, int distance) {
            this.originalBicIndex = originalBicIndex;
            this.resultingBicIndex = resultingBicIndex;
            this.distance = distance;
        }

        public int getOriginalBicIndex() {
            return originalBicIndex;
        }

        public int getResultingBicIndex() {
            return resultingBicIndex;
        }

        @Override
        public int compareTo(DistanceHolder other) {
            return this.distance - other.distance;
        }
    }
}
