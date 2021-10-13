// Main application class
public class KnapsackGA {
    final private int GENERATIONS = 500;

    public static void main(String[] args) {
        Population population = new Population();
        population.evaluateFitness();
    }
}
