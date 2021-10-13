import java.util.BitSet;
import java.util.ArrayList;

// Individual class
public class Individual {
    final public double SELECTION_RATE = 0.05;
    public BitSet chromosome;
    public double fitness, normalizedFitness;

    // Initializes an individual's chromosome based on the gene pool
    public Individual(ArrayList<Gene> genePool) {
        chromosome = new BitSet(genePool.size());
        for (int i = 0; i < genePool.size(); i++) {
            if (geneSelection())
                chromosome.set(i);
        }
    }

    // Evaluates whether a gene should be selected
    public boolean geneSelection() {
        return Math.random() <= SELECTION_RATE;
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
