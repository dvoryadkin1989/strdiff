package by.dvaradkin.strdiff;

import by.dvaradkin.strdiff.logic.DiffCalculator;
import by.dvaradkin.strdiff.logic.GuessResult;

import java.util.List;

import static java.util.Arrays.asList;

public class Main {
    public static void main(String[] args) {

        final List<String> originalBics = asList("ab", "def1", "123");
        final List<String> resultingBics = asList("def", "abc", "9876");
        final DiffCalculator diffCalculator = new DiffCalculator();

        List<GuessResult> results = diffCalculator.guessEdits(originalBics, resultingBics);

        results.forEach(System.out::println);
    }
}
