import java.util.BitSet;
import java.util.ArrayList;

// Individual class
public class Individual {
    public ArrayList<Boolean> chromosome;
    public double fitness, normalizedFitness;

    // Individual constructor
    public Individual() {
        this.chromosome = new ArrayList<Boolean>(); // mystery number - should resolve
        this.fitness = 0.0;
        this.normalizedFitness = 0.0;
    }

    // Initializes an individual's chromosome based on the gene pool
    public Individual(ArrayList<Gene> genePool) {
        chromosome = new ArrayList<>();
        for (int i = 0; i < genePool.size(); i++) {
            if (geneSelection())
                chromosome.add(true);
            else
                chromosome.add(false);
        }
    }

    // Copy constructor for individual
    public Individual(Individual indivl) {
        this.chromosome = indivl.chromosome;
        this.fitness = indivl.fitness;
        this.normalizedFitness = indivl.normalizedFitness;
    }

    // Displays chromosome as 1s and 0s
    public void displayChromosome() {
        for (int i = 0; i < chromosome.size(); i++) {
            if (chromosome.get(i))
                System.out.print("1");
            else
                System.out.print("0");
        }
        System.out.print("\n");
    }

    // Evaluates whether a gene should be selected
    public boolean geneSelection() {
        double SELECTION_RATE = 0.05;
        return Math.random() <= SELECTION_RATE;
    }

    // Mutates an individual's chromosome
    public void mutateIndividual() {
        double MUTATION_RATE = 0.0001;
        for (int i = 0; i < chromosome.size(); i++) {
            if (Math.random() <= MUTATION_RATE)
                if (chromosome.get(i))
                    chromosome.set(i, false);
                else
                    chromosome.set(i, true);
        }
    }

    // Calculates a gene's fitness
    public void calculateFitness(ArrayList<Gene> genePool) {
        double fitnessScore = 0.0, weight = 0.0;
        for (int i = 0; i < chromosome.size(); i++) {
            if (chromosome.get(i)) {
                fitnessScore += genePool.get(i).getUtility();
                weight += genePool.get(i).getWeight();
            }
        }
        if (weight < 500.0)
            this.fitness = fitnessScore;
        else
            this.fitness = 1.0;
    }

    // Finds an individual's normalized fitness score
    // using L2 normalization
    public void findNormalizedFitness(double totalFitness) {
        double exponentialFitness = Math.pow(this.fitness, 2.0);
        this.normalizedFitness = exponentialFitness / totalFitness;
    }
}
