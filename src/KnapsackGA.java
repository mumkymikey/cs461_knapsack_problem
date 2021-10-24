// Main application class
public class KnapsackGA {
    // Number of generations to evolve
    final static private int GENERATIONS = 5000;

    // Main application method
    public static void main(String[] args) {
        int index = 0;
        Population population = new Population();

        index++;
        while (index < GENERATIONS) {
            population.evaluateFitness();
            population.nextGeneration();
            population.findAverageFitnessGrowth();

            if (!population.evaluateAverageFitnessGrowth()) {
                population.recordPopulationStats(index);
                break;
            }
        }
    }
}
