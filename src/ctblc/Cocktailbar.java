package ctblc;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class Cocktailbar {

    public static Set<String> getAvailableDrinks(String filename) {
        Set<String> drinks = new TreeSet<>();

        try (BufferedReader br = Files.newBufferedReader(Path.of(filename), StandardCharsets.UTF_8)) {
            List<String> lines = new ArrayList<>(br.lines().filter(line -> (!line.isBlank() && !line.startsWith("#"))).toList());
            if (lines.isEmpty()) {
                return drinks;
            }
            Map<String, Set<String>> dwi = new TreeMap<>();
            Set<String> ingredients = Arrays.stream(lines.getLast().split(",")).map(String::strip).collect(Collectors.toCollection(TreeSet::new));
            lines.remove(lines.getLast());
            lines.forEach(s -> {
                String i = s.split(":")[0].strip();
                Set<String> poss = Set.of(s.split(":")[1].split(",")).stream().map(String::strip).collect(Collectors.toCollection(TreeSet::new));
                poss.forEach(p -> {
                    if (dwi.containsKey(p)) {
                        dwi.get(p).add(i);
                    } else {
                        Set<String> temp = new TreeSet<>();
                        temp.add(i);
                        dwi.put(p, temp);
                    }
                });
            });
            dwi.forEach((k, v) -> {
                if (ingredients.containsAll(v)) drinks.add(k);

            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return drinks;
    }
}