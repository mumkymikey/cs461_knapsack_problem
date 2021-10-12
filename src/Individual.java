import java.util.BitSet;
import java.util.ArrayList;

// Individual class
public class Individual {
    public double SELECTION_RATE = 0.05;
    public BitSet chromosome;

    // Initializes an individual's chromosome based on the gene pool
    public Individual(ArrayList<Gene> genePool) {
        chromosome = new BitSet(genePool.size());
        for (int i = 0; i < genePool.size(); i++) {
            if (geneSelection())
                chromosome.set(i);
        }
    }

    // evaluates whether a gene should be selected
    public boolean geneSelection() {
        return Math.random() <= SELECTION_RATE;
    }
}
