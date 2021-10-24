// Gene class
public class Gene {
    final private double utility;
    final private double weight;

    // Initializes a gene, a component of the individual's chromosome
    public Gene(double utility, double weight) {
        this.utility = utility;
        this.weight = weight;
    }

    // Retrieve a gene's weight
    public double getWeight() {
        return this.weight;
    }

    // Retrieve a gene's utility
    public double getUtility() {
        return this.utility;
    }
}
