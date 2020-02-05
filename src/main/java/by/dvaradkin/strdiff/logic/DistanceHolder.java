package by.dvaradkin.strdiff.logic;

public class DistanceHolder implements Comparable<DistanceHolder> {
    private final int originalBicIndex;
    private final int resultingBicIndex;
    private final int distance;

    DistanceHolder(int originalBicIndex, int resultingBicIndex, int distance) {
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