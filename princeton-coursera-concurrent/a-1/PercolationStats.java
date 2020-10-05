import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/*--------------------------------------------------------------------------------------------------------------
 *  By Irene Storozhko. August 11, 2018. Week 1 assignment for Algorithms I course by Wayne and Sedgewick.
 *
 *  Creates a percolation system, performs Monte Carlo simulation experiemnt with it and calculates statistics.
 *  Uses multithreading for efficiency.
 *
 *  This multithreaded version. Efficiency comparison for 300x300 matrix, 500 trials:
 *  Single-threaded: 7.5 sec
 *  Multithreaded:   1.5 sec
 *-------------------------------------------------------------------------------------------------------------*/

public class PercolationStats {
    private final int n;
    private final double[] percolationTresholds;
    private final double mean;
    private final double stddev;
    private final double confidenceLo;
    private final double confidenceHi;
    private final double CONFIDENCE_95 = 1.96;

    private static final int THREADS = 6;
    private static final ExecutorService executor = Executors.newFixedThreadPool(THREADS);

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("n and trials and must be positive integers greater than 0");

        this.n = n;
        this.percolationTresholds = new double[trials];

        Stopwatch w = new Stopwatch();

        // Single-threaded code would be
        // for (int i = 0; i < trials; i++) {
        //     this.percolationTresholds[i] = trial();
        // }


        // ----- Multithreaded code
        List<Future<double[]>> results = new ArrayList<>(trials);
        for (int i = 0; i < THREADS; i++) {
            Callable task = getTask(trials / THREADS);
            Future<double[]> fr = executor.submit(task);
            results.add(fr);
        }

        for (int i = 0; i < results.size(); i++) {
            try {
                double[] p = results.get(i).get();
                System.arraycopy(p, 0, this.percolationTresholds, p.length * i, p.length);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // ------

        this.mean = calculateMean();
        this.stddev = calculateStddev();
        this.confidenceLo = calculateConfidenceLo();
        this.confidenceHi = calculateConfidenceHi();

        System.out.println(String.format("Total elapsed time is %s", w.elapsedTime()));
    }

    private double trial() {
        Percolation p = new Percolation(n);
        int openSites = 0;

        while (!p.percolates()) {
            int row;
            int col;

            do {
                row = uniformRandom(1, n + 1);
                col = uniformRandom(1, n + 1);
            } while (p.isOpen(row, col));

            p.open(row, col);
            openSites++;
        }

        return openSites / (double)(n * n);
    }

    // Part of multithreaded code
    private Callable getTask(int t) {
        return new Callable<double[]>() {
            @Override
            public double[] call() throws Exception {
                double[] results = new double[t];
                for (int i = 0; i < t; i++) {
                    results[i] = trial();
                }
                return results;
            }
        };
    }

    private int uniformRandom(int a, int b) {
        if (b > a && (long)b - (long)a < 2147483647L) {
            return a + uniform(b - a);
        } else {
            throw new IllegalArgumentException("invalid range: [" + a + ", " + b + ")");
        }
    }

    private static int uniform(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("argument must be positive: " + n);
        } else {
            // concurrent-friendly random number generator - improves performance of mulltithreaded code
            return ThreadLocalRandom.current().nextInt(n);
        }
    }

    private double calculateMean() {
        double sum = 0;
        for (double percolationTreshold : percolationTresholds) {
            sum += percolationTreshold;
        }
        return sum / percolationTresholds.length;
    }

    private double calculateStddev() {
        if (percolationTresholds.length == 1) return Double.NaN;

        double squaredDiffFromMeanSum = 0;
        for (double percolationTreshold : percolationTresholds) {
            squaredDiffFromMeanSum += Math.pow(percolationTreshold - mean, 2);
        }
        return Math.sqrt(squaredDiffFromMeanSum / (percolationTresholds.length - 1));
    }

    private double calculateConfidenceLo() {
        return mean - ((CONFIDENCE_95 * stddev) / Math.sqrt(percolationTresholds.length));
    }

    private double calculateConfidenceHi() {
        return mean + ((CONFIDENCE_95 * stddev) / Math.sqrt(percolationTresholds.length));
    }

    public double mean() {
        return this.mean;
    }

    public double stddev() {
        return this.stddev;
    }

    public double confidenceLo() {
        return this.confidenceLo;
    }

    public double confidenceHi() {
        return this.confidenceHi;
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        StdOut.printf("mean %20s %.6f\n", "=", ps.mean());
        StdOut.printf("stddev %18s %.17f\n", "=", ps.stddev());
        StdOut.printf("95%% confidence interval %s [%.15f, %.15f]\n", "=", ps.confidenceLo(), ps.confidenceHi());
    }
}
