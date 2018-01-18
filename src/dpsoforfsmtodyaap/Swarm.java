package dpsoforfsmtodyaap;

public class Swarm {

	private int population;
	private Particle particles[];
	private MealyMachine bestMealyMachine = null;

	public Swarm(int population) {
		this.population = population;
		particles = new Particle[population];
	}

	// ******* //
	// Private //
	// ******* //
	private Particle createParticle(int[] states, int[] inputSymbols, int[] outputSymbols,
			MealyMachineCodec mealyMachineCodec) {

		int rowSize = states.length * inputSymbols.length;
		int colSize = states.length * outputSymbols.length;
		double velocity[][] = new double[rowSize][colSize];
		for (int i=0; i < rowSize; i++) {
			for (int j=0; j < colSize; j++) {
				velocity[i][j] = (Math.random() * (Const.VMAX-Const.VMIN)) + Const.VMIN;
			}
		}

		Particle particle = new Particle(velocity);
		particle.initialize(states, inputSymbols, outputSymbols, mealyMachineCodec);

		return particle;
	}

	private int getBestParticleIndex() {
		double bestFitness = particles[0].getGbestFitness();
		int index = -1;
		for (int i=0; i < population; i++) {
			Particle particle = particles[i];
			if (particle.getPbestFitness() > bestFitness) {
				index = i;
				bestFitness = particle.getPbestFitness();
			}
		}
		return index;
	}
	
	
	// ****** //
	// Public //
	// ****** //
	public void initialize(int[] states, int[] inputSymbols, int[] outputSymbols,
			MealyMachineCodec mealyMachineCodec) {
		int initState = states[0];
		bestMealyMachine = new MealyMachine(states, initState, inputSymbols, outputSymbols);
		for (int i=0; i < population; i++) {
			particles[i] = createParticle(states, inputSymbols, outputSymbols, mealyMachineCodec);
		}
	}
	
	public void reInitialize(MealyMachineCodec mealyMachineCodec) {
		for (Particle particle : particles) {
			particle.reInitialize(mealyMachineCodec);
		}
	}

	public boolean doesEnvironmentChange(AntSimulator antSimulator, MealyMachineCodec mealyMachineCodec) {
	 	double eaten, moves, maxMoves, fitness, bestFitness;
	 	antSimulator.runWithFSM(bestMealyMachine);
	 	eaten = antSimulator.getEaten();
	 	moves = antSimulator.getMoves();
	 	maxMoves = antSimulator.getMaxMoves();
	 	fitness = eaten + ((maxMoves - moves) / maxMoves);
	 	antSimulator.reset();
	 	bestFitness = particles[0].getGbestFitness();
	 	return fitness != bestFitness;
	}

	public void advance() {
		for (Particle particle : particles) {
			particle.advance();
		}
	}

	public void encodePosToMealyMachine(MealyMachineCodec mealyMachineCodec) {
		for (Particle particle : particles) {
			particle.encodePosToMealyMachine(mealyMachineCodec);
		}
	}

	public void evaluate(AntSimulator antSimulator) {
		for (Particle particle : particles) {
			particle.evaluate(antSimulator);
		}
	}
	
	public void updatePersonalBest() {
		for (Particle particle : particles) {
			particle.updatePersonalBest();
		}
	}
	
	public void updateGlobalBest(MealyMachineCodec mealyMachineCodec) {
		// Set Global Best
		int index = getBestParticleIndex();
		if (index > -1) {
			Particle bestParticle = particles[index];
			for (Particle particle : particles) {
				particle.setGbest(bestParticle.getPbest(), bestParticle.getPbestFitness());
			}
			TransitionAction functions = mealyMachineCodec.decode(bestParticle.getPbest());
			bestMealyMachine.setFunctions(functions);
		}
	}
	
	public void restAttractor() {
		for (Particle particle : particles) {
			particle.restBestFitness();
		}
	}

	public boolean isConverged() {
		int row = particles[0].getGbest().length;
		int[][] best = particles[0].getGbest();
		int[][] worst;
		int index = 0;
		double worstFitness = particles[0].getFitness();
		for (int i=0; i < population; i++) {
			if (particles[0].getFitness() < worstFitness) {
				index = i;
				worstFitness = particles[i].getFitness();
			}
		}
		worst = particles[index].getPosition();
		double distance = (double) Func.calcDistance(best, worst);
		return distance < Const.R_CLOUD*row;
	}

	public int[][] getGbest() {
		return particles[0].getGbest();
	}

	public double getGbestFitness() {
		return particles[0].getGbestFitness();
	}
	
	public MealyMachine getBestMealyMachine() {
		return bestMealyMachine;
	}
	
}
