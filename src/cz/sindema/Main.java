package cz.sindema;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The Main class is responsible for calculating and simulating the water filling process across multiple tanks.
 * Tanks are filled based on their remaining capacity and flow rate. The program determines the time
 * at which the last tank starts to overflow and the time when all tanks are completely full.
 * <p>
 * The data about tanks is loaded from an external file whose path is provided as a single command-line argument.
 * The input file must specify the total number of tanks and their initial flow rate in the first line, followed
 * by the capacities of each tank on subsequent lines.
 * <p>
 * The core filling logic involves iteratively updating the state of the tanks based on their fill times,
 * recalculating overflows, and adjusting flow rates for non-full tanks.
 * <p>
 * The program outputs values of the last tank overflow time and the time when all tanks are full (rounded down).
 * If any required condition (like single input argument or valid file format) is not met, an exception is thrown.
 */
public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("Single argument is expected (file path)");
        }
        String pathString = args[0];
        List<Tank> tanks = loadData(pathString);

        double clock = 0;
        Double lastOverflowTime = null;
        double allOverflowTime;

        while (true) {
            if (tanks.isEmpty()) {
                // all tanks are full
                allOverflowTime = clock;
                break;
            }

            double fillTime = Collections.min(tanks, Comparator.comparingDouble(Tank::getFillTime)).getFillTime();
            // move clock
            clock = clock + fillTime;

            // recalculate flow
            int overFlowRate = 0;
            Iterator<Tank> iterator = tanks.listIterator();
            while (iterator.hasNext()) {
                Tank tank = iterator.next();
                boolean isFull = tank.fill(fillTime);
                if (isFull) {
                    overFlowRate = overFlowRate + tank.getFlowRate();
                    if (lastOverflowTime == null && !iterator.hasNext()) {
                        lastOverflowTime = clock;
                    }
                    iterator.remove();
                } else if (overFlowRate > 0) {
                    tank.increaseFlowRate(overFlowRate);
                    overFlowRate = 0;
                }
            }
        }
        if (lastOverflowTime == null) {
            throw new IllegalStateException("Last overflow time not reached.");
        }


        System.out.println((int) Math.floor(roundError(lastOverflowTime)) + " " + (int) Math.floor(roundError(allOverflowTime)));
    }

    private static double roundError(double value) {
        BigDecimal bigDecimal = BigDecimal.valueOf(value);
        return bigDecimal.setScale(5, RoundingMode.CEILING).doubleValue();
    }

    private static List<Tank> loadData(String pathString) throws IOException {
        Path path = Paths.get(pathString);
        List<String> lines = Files.readAllLines(path);
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Empty input file");
        }
        String firstLine = lines.getFirst();
        String[] split = firstLine.split(" ");
        if (split.length != 2) {
            throw new IllegalArgumentException("Invalid first line format");
        }
        int tanksCount = Integer.parseInt(split[0]);
        int flowRate = Integer.parseInt(split[1]);

        List<Tank> tanks = lines.stream()
                .skip(1)
                .map(Integer::parseInt)
                .map(capacity -> new Tank(capacity, flowRate))
                .collect(Collectors.toCollection(LinkedList::new));
        if (tanks.size() != tanksCount) {
            throw new IllegalArgumentException("Number of rows does not match the first row specification");
        }
        return tanks;
    }
}
