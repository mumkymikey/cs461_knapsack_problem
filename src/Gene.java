// Gene class
public class Gene {
    public double weight;
    public double utility;

    // Initializes a gene, a component of the individual's chromosome
    public Gene(double weight, double utility) {
        this.weight = weight;
        this.utility = utility;
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
