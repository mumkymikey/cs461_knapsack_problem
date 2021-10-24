import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

// Population class
public class Population {
    final int INITIAL_POPULATION = 5000;
    final String INPUT_FILE_PATH = Paths.get(".").toAbsolutePath().normalize().toString() + "/Program2Input.txt";
    final String OUTPUT_FILE_PATH = Paths.get(".").toAbsolutePath().normalize().toString() + "/output.txt";

    private ArrayList<Gene> genePool;
    private ArrayList<Individual> population;
    private ArrayList<Double> averageFitnessHistory, fitnessGrowthHistory;
    private Individual fittestIndividual;
    private double totalFitness, totalFitnessForNormalization, maxFitness = 0.0;

    // Creates an initial population
    public Population() {
        initializeGenePool();
        population = new ArrayList<>();
        averageFitnessHistory = new ArrayList<>();
        fitnessGrowthHistory = new ArrayList<>();
        fittestIndividual = new Individual();

        for (int i = 0; i < INITIAL_POPULATION; i++) {
            Individual dude = new Individual(genePool);
            population.add(dude);
        }
    }

    // Evaluates each individual in a population's fitness and
    // perform L2 normalization of fitness scores
    public void evaluateFitness() {
        for (Individual individual : population) {
            individual.calculateFitness(genePool);
        }
        this.findTotalFitness();
        this.normalizeFitness();
    }

    // Facilitates the normalization of each individual
    // in a population's fitness
    public void normalizeFitness() {
        this.findTotalFitnessForNormalization();
        for (Individual individual : population) {
            individual.findNormalizedFitness(this.totalFitnessForNormalization);
        }
    }

    // Finds the fittest individual in a given population
    public Individual findFittestIndividual() {
        Individual fittest = new Individual(population.get(0));
        for (int i = 1; i < population.size(); i++) {
            if (fittest.fitness < population.get(i).fitness)
                fittest = population.get(i);
        }
        return fittest;
    }

    // Find average fitness of a single generation
    public double findAverageFitness() {
        double averageFitness = totalFitness / this.population.size();
        averageFitnessHistory.add(averageFitness);
        return averageFitness;
    }

    // Checks whether our population's growth has plateaued
    public boolean evaluateAverageFitnessGrowth() {
        int historySize = fitnessGrowthHistory.size() - 1;
        boolean sufficientGrowth = true;
        if (fitnessGrowthHistory.size() >= 10) {
            sufficientGrowth = false;
            for (int i = historySize; i > historySize - 10; i--) {
                if (fitnessGrowthHistory.get(i) >= 1.0) {
                    sufficientGrowth = true;
                    break;
                }
            }
        }
        return sufficientGrowth;
    }

    // Find fitness growth between two generations
    public void findAverageFitnessGrowth() {
        int index = averageFitnessHistory.size() - 1;
        double increase, fitnessGrowth;
        if (index > 0) {
            increase = averageFitnessHistory.get(index) - averageFitnessHistory.get(index - 1);
            fitnessGrowth = increase / averageFitnessHistory.get(index - 1) * 100;
            fitnessGrowthHistory.add(fitnessGrowth);
        }
    }

    // Find total fitness of a population
    public void findTotalFitness() {
        double totalFitness = 0.0;
        for (Individual individual : population) {
            totalFitness += individual.fitness;
        }
        this.totalFitness = totalFitness;
    }

    // Sets the total fitness of a population for L2 normalization
    public void findTotalFitnessForNormalization() {
        double totalFitness = 0.0, squaredFitness;
        for (Individual individual : population) {
            squaredFitness = Math.pow(individual.fitness, 2);
            totalFitness += squaredFitness;
        }
        this.totalFitnessForNormalization = totalFitness;
    }

    // Selects an individual within a population through
    // tournament selection
    public Individual selectIndividual() {
        ArrayList<Individual> tournamentPopulation = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            int randIndex = (int)(Math.random() * this.population.size());
            Individual contestant = this.population.get(randIndex);
            tournamentPopulation.add(new Individual(contestant));
        }

        if (tournamentPopulation.get(0).fitness >= tournamentPopulation.get(1).fitness)
            return tournamentPopulation.get(0);
        else
            return tournamentPopulation.get(1);
    }

    // Begins evolving the current generation into the next
    public void nextGeneration() {
        ArrayList<Individual> nextGenPopulation = new ArrayList<>();

        // performs elitism to find fittest individual and store
        // in next gen population
        Individual fittest = new Individual(this.findFittestIndividual());
        nextGenPopulation.add(fittest);

        if (fittestIndividual.fitness < fittest.fitness)
            fittestIndividual = fittest;

        // performs tournament selection and crossover
        for (int i = 0; i < population.size() - 1; i++) {
            int crossoverPoint = (int)(Math.random() * genePool.size());
            Individual parent1, parent2, child;

            parent1 = selectIndividual();
            parent2 = selectIndividual();

            child = performCrossover(parent1, parent2, crossoverPoint);
            child.mutateIndividual();

            nextGenPopulation.add(child);
        }

        this.population = nextGenPopulation;
    }

    // Perform crossover between two parents
    public static Individual performCrossover(Individual parent1, Individual parent2, int crossoverPoint) {
        Individual child = new Individual();
        for (int i = 0; i < parent1.chromosome.size(); i++) {
            if ((i < crossoverPoint && parent1.chromosome.get(i)) || (i >= crossoverPoint && parent2.chromosome.get(i)))
                child.chromosome.add(true);
            else
                child.chromosome.add(false);
        }
        return child;
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
                double utility = Double.parseDouble(item.substring(0, spaceIndex));
                double weight = Double.parseDouble(item.substring(spaceIndex));

                Gene gene = new Gene(utility, weight);
                genePool.add(gene);
            }
            reader.close();
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Record population information into text file
    public void recordPopulationStats(int generations) {
        try {
            FileWriter writer = new FileWriter(OUTPUT_FILE_PATH);
            DecimalFormat round = new DecimalFormat("###.###");

            writer.write("Average Fitness For " + generations + " Generations:\n");
            for (Double avgFitness : averageFitnessHistory) {
                writer.write(round.format(avgFitness) + "\n");
            }

            writer.write("\nHIGHEST FITNESS SELECTION\n");
            writer.write("Total Utility:\n" + round.format(fittestIndividual.fitness));

            writer.write("\nItems Taken (utility, weight):\n");
            for (int i = 0; i < fittestIndividual.chromosome.size(); i++) {
                if (fittestIndividual.chromosome.get(i)) {
                    String utility = String.valueOf(genePool.get(i).getUtility());
                    String weight = String.valueOf(genePool.get(i).getWeight());

                    writer.write(utility + " " + weight + "\n");
                }
            }
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
