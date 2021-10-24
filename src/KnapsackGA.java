// Main application class
public class KnapsackGA {
    // Number of generations to evolve
    final static private int GENERATIONS = 5000;

    // Main application method
    public static void main(String[] args) {
        int index = 0;
        Population population = new Population();

        while (index < GENERATIONS) {
            population.evaluateFitness();
            population.nextGeneration();

            population.findAverageFitness();
            population.findAverageFitnessGrowth();

            index++;
            if (!population.evaluateAverageFitnessGrowth()) {
                population.recordPopulationStats(index);
                break;
            }
        }
    }
}
