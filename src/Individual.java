import java.util.BitSet;
import java.util.ArrayList;

// Individual class
public class Individual {
    final public double SELECTION_RATE = 0.05;
    public BitSet chromosome;
    public double fitness;

    // Initializes an individual's chromosome based on the gene pool
    public Individual(ArrayList<Gene> genePool) {
        chromosome = new BitSet(genePool.size());
        for (int i = 0; i < genePool.size(); i++) {
            if (geneSelection())
                chromosome.set(i);
        }
    }

    // Returns an individual's fitness score
    public double getFitness() {
        return this.fitness;
    }

    // Evaluates whether a gene should be selected
    public boolean geneSelection() {
        return Math.random() <= SELECTION_RATE;
    }

    // Calculates a gene's fitness
    public void calculateFitness(ArrayList<Gene> genePool) {
        double fitnessScore = 0.0;
        for (int i = 0; i < chromosome.size(); i++) {
            if (chromosome.get(i))
                fitnessScore += genePool.get(i).getUtility();
        }
        this.fitness = fitnessScore;
    }
}
