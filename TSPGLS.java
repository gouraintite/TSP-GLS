import java.util.*;

public class GLSTSP {

    // Define the distance matrix
    static int[][] distanceMatrix = {
        {0, 10, 15, 20},
        {10, 0, 35, 25},
        {15, 35, 0, 30},
        {20, 25, 30, 0}
    };

    public static void main(String[] args) {
        int[] solution = GLS_TSP();
        System.out.println("Optimal Tour: " + Arrays.toString(solution));
        System.out.println("Optimal Tour Length: " + calculateTourLength(solution));
    }

    // GLS algorithm
    public static int[] GLS_TSP() {
        int[] currentSolution = getRandomSolution();
        int currentCost = calculateTourLength(currentSolution);
        int maxIterations = 1000; // Set maximum number of iterations
        int iteration = 0;

        while (iteration < maxIterations) {
            int[] localOptimum = localSearch(currentSolution);
            int localOptimumCost = calculateTourLength(localOptimum);

            if (localOptimumCost < currentCost) {
                currentSolution = localOptimum;
                currentCost = localOptimumCost;
            } else {
                // Apply guided perturbation
                currentSolution = perturb(currentSolution);
            }

            iteration++;
        }

        return currentSolution; // Return the best solution found
    }


    // Randomly generate an initial solution
    public static int[] getRandomSolution() {
        int n = distanceMatrix.length;
        int[] solution = new int[n];
        for (int i = 0; i < n; i++) {
            solution[i] = i;
        }
        // Shuffle the solution
        for (int i = 0; i < n; i++) {
            int j = (int) (Math.random() * (n - i)) + i;
            int temp = solution[i];
            solution[i] = solution[j];
            solution[j] = temp;
        }
        return solution;
    }

    // Perform local search to find a local optimum
// Perform 2-opt local search to find a local optimum
    public static int[] localSearch(int[] solution) {
        int n = solution.length;
        int[] bestSolution = Arrays.copyOf(solution, n);
        boolean improved;

        do {
            improved = false;
            for (int i = 0; i < n - 1; i++) {
                for (int j = i + 1; j < n; j++) {
                    int[] newSolution = twoOptSwap(bestSolution, i, j);
                    int newCost = calculateTourLength(newSolution);

                    if (newCost < calculateTourLength(bestSolution)) {
                        bestSolution = Arrays.copyOf(newSolution, n);
                        improved = true;
                    }
                }
            }
        } while (improved);

        return bestSolution;
    }

    // Perform 2-opt swap operation
    public static int[] twoOptSwap(int[] solution, int i, int j) {
        int n = solution.length;
        int[] newSolution = Arrays.copyOf(solution, n);

        while (i < j) {
            int temp = newSolution[i];
            newSolution[i] = newSolution[j];
            newSolution[j] = temp;
            i++;
            j--;
        }

        return newSolution;
    }


    // Apply guided perturbation
// Apply guided perturbation
    public static int[] perturb(int[] solution) {
        int n = solution.length;
        int[] perturbedSolution = Arrays.copyOf(solution, n);

        // Select a random city
        int randomCity = (int) (Math.random() * n);

        // Find the distances to all other cities
        int[] distances = new int[n];
        for (int i = 0; i < n; i++) {
            distances[i] = distanceMatrix[randomCity][i];
        }

        // Sort the cities based on their distances
        Integer[] sortedIndices = new Integer[n];
        for (int i = 0; i < n; i++) {
            sortedIndices[i] = i;
        }
        Arrays.sort(sortedIndices, Comparator.comparingInt(a -> distances[a]));

        // Apply guided perturbation by swapping nearby cities
        int k = n / 2;
        for (int i = 0; i < k; i++) {
            int city1 = sortedIndices[i];
            int city2 = sortedIndices[n - i - 1];
            int temp = perturbedSolution[city1];
            perturbedSolution[city1] = perturbedSolution[city2];
            perturbedSolution[city2] = temp;
        }

        return perturbedSolution;
    }


    // Calculate the length of the tour given a solution
    public static int calculateTourLength(int[] solution) {
        int tourLength = 0;
        for (int i = 0; i < solution.length - 1; i++) {
            tourLength += distanceMatrix[solution[i]][solution[i + 1]];
        }
        tourLength += distanceMatrix[solution[solution.length - 1]][solution[0]]; // Return to the starting city
        return tourLength;
    }
}
