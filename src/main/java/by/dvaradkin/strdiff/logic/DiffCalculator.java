package by.dvaradkin.strdiff.logic;

import java.util.*;

public class DiffCalculator {
    private static final int SIMILARITY_THRESHOLD = 4;

    private final LevenshteinDistanceCalculator levenshtein = new LevenshteinDistanceCalculator();

    public List<GuessResult> guessEdits(final List<String> originalBics, final List<String> resultingBics) {
        final List<DistanceHolder> pairwiseDistances = calculatePairwiseDistances(originalBics, resultingBics);

        Collections.sort(pairwiseDistances);

        final Set<Integer> matchedOriginalIndexes = new HashSet<>();
        final Set<Integer> matchedResultingIndexes = new HashSet<>();
        final List<GuessResult> results = new ArrayList<>();
        for (DistanceHolder currentDistanceHolder : pairwiseDistances) {
            if (noneOfBicsAreMatchedAlready(matchedOriginalIndexes, matchedResultingIndexes, currentDistanceHolder)
                    && bicsAreCloseEnough(currentDistanceHolder)) {
                results.add(createGuessResult(originalBics, resultingBics, currentDistanceHolder));
                matchedOriginalIndexes.add(currentDistanceHolder.getOriginalBicIndex());
                matchedResultingIndexes.add(currentDistanceHolder.getResultingBicIndex());
            }
        }

        // @formatter:off
        createSetOfIndexes(originalBics).stream()
                .filter(idx -> !matchedOriginalIndexes.contains(idx))
                .map(originalBics::get)
                .map(nonMatchedOriginalBic -> new GuessResult(nonMatchedOriginalBic, "REMOVED"))
                .forEach(results::add);
        // @formatter:on

        // @formatter:off
        createSetOfIndexes(resultingBics).stream()
                .filter(idx -> !matchedResultingIndexes.contains(idx))
                .map(resultingBics::get)
                .map(nonMatchedResultingBic -> new GuessResult("NEW", nonMatchedResultingBic))
                .forEach(results::add);
        // @formatter:on

        return results;
    }

    private List<DistanceHolder> calculatePairwiseDistances(List<String> originalBics, List<String> resultingBics) {
        final List<DistanceHolder> pairwiseDistances = new ArrayList<>();
        for (int i = 0; i < originalBics.size(); i++) {
            for (int j = 0; j < resultingBics.size(); j++) {
                int distance = calculateDistance(originalBics.get(i), resultingBics.get(j));
                // ideally equal bics should be handled before calculating pairwise distances
                if (distance > 0) {
                    pairwiseDistances.add(new DistanceHolder(i, j, distance));
                }
            }
        }
        return pairwiseDistances;
    }

    private boolean bicsAreCloseEnough(DistanceHolder currentDistanceHolder) {
        return currentDistanceHolder.getDistance() < SIMILARITY_THRESHOLD;
    }

    private GuessResult createGuessResult(List<String> originalBics, List<String> resultingBics, DistanceHolder current) {
        return new GuessResult(originalBics.get(current.getOriginalBicIndex()), resultingBics.get(current.getResultingBicIndex()));
    }

    private boolean noneOfBicsAreMatchedAlready(Set<Integer> matchedOriginalIndexes, Set<Integer> matchedResultingIndexes, DistanceHolder current) {
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

        public int getDistance() {
            return distance;
        }

        @Override
        public int compareTo(DistanceHolder other) {
            return this.distance - other.distance;
        }
    }
}
