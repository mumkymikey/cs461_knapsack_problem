import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import java.nio.file.Paths;

// Population class
public class Population {
    final int INITIAL_POPULATION = 1000;
    final String INPUT_FILE_PATH = Paths.get(".").toAbsolutePath().normalize().toString() + "/Program2Input.txt";

    public ArrayList<Gene> genePool;
    public ArrayList<Individual> population;
    public double totalFitness, averageFitnessGrowth;

    // Creates an initial population
    public Population() {
        initializeGenePool();
        population = new ArrayList<>();

        for (int i = 0; i < INITIAL_POPULATION; i++) {
            Individual dude = new Individual(genePool);
            population.add(dude);
        }
    }

    // Returns the array list of individuals, i.e. the population
    public ArrayList<Individual> getPopulation() {
        return this.population;
    }

    // Evaluates each individual in a population's fitness and
    // perform L2 normalization of fitness scores
    public void evaluateFitness() {
        for (Individual individual : population) {
            individual.calculateFitness(genePool);
        }
        this.normalizeFitness();
    }

    // Facilitates the normalization of each individual
    // in a population's fitness
    public void normalizeFitness() {
        this.findTotalFitness();
        for (Individual individual : population) {
            individual.findNormalizedFitness(this.totalFitness);
        }
    }

    // Sets the total fitness of a population for L2 normalization
    public void findTotalFitness() {
        double totalFitness = 0.0, squaredFitness;
        for (Individual individual : population) {
            squaredFitness = Math.pow(individual.fitness, 2);
            totalFitness += squaredFitness;
        }
        this.totalFitness = totalFitness;
    }

    // Read weight and utility values from text file
    // and store in an array list
    public void initializeGenePool () {
        try {
            Scanner reader = new Scanner(new File(INPUT_FILE_PATH));
            genePool = new ArrayList<>();
            while (reader.hasNextLine()) {
                int spaceIndex = 0;
                String item = reader.nextLine();

                for (int i = 0; i < item.length(); i++) {
                    if (spaceIndex == 0 && Character.isWhitespace(item.charAt(i)))
                        spaceIndex = i;
                }
                double weight = Double.parseDouble(item.substring(0, spaceIndex));
                double utility = Double.parseDouble(item.substring(spaceIndex));

                Gene gene = new Gene(weight, utility);
                genePool.add(gene);
            }
            reader.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
