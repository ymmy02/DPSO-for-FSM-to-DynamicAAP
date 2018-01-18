package dpsoforfsmtodyaap;


public class DPSOforMealyMachineConstruction {
	
	private String            trailFiles[]       = null;
	private AntSimulator      antSimulator       = null;
	private MealyMachineCodec mealyMachineCodec  = null;

	private int[][]           bestMatrix         = null;
	private double            bestFitness        = 0.0;
	private MealyMachine      bestMealyMachine   = null;
	
	private int               envIndex           = 0;
 	
	public DPSOforMealyMachineConstruction(AntSimulator antSimulator, String[] trailFiles) {
		this.trailFiles = trailFiles;
		this.antSimulator = antSimulator;
	}
	
	// ******* //
	// Private //
	// ******* //
	
	private Swarm createSwarm(int[] states, int[] inputSymbols, int[] outputSymbols,
			MealyMachineCodec mealyMachineCodec) {
		Swarm swarm = new Swarm(Const.POPULATION);
		swarm.initialize(states, inputSymbols, outputSymbols, mealyMachineCodec);
		return swarm;
	}

	private void reInitializeSwarm(Swarm swarm) {
		swarm.reInitialize(mealyMachineCodec);
		swarm.evaluate(antSimulator);
		swarm.updatePersonalBest();
		swarm.updateGlobalBest(mealyMachineCodec);
	}

	private void repelConvergence(Swarm[] swarms) {
		boolean isAllSwarmsConverged = true;
		for (Swarm swarm : swarms) {
			if (!swarm.isConverged()) {
				isAllSwarmsConverged = false;
				break;
			}
		}
		if (isAllSwarmsConverged) {
			int index = 0;
			double worstFitness = swarms[index].getGbestFitness();
			for (int i=0; i < swarms.length; i++) {
				if (swarms[i].getGbestFitness() < worstFitness) {
					index = i;
					worstFitness = swarms[i].getGbestFitness();
				}
			}
			reInitializeSwarm(swarms[index]);
		}
	}

	private void exclude(Swarm[] swarms) {
		int numOfSwarms = swarms.length;
		int row = swarms[0].getGbest().length;
		boolean doesReInitialize[] = new boolean[Const.NUMOFSWARMS];
		for (int i=0; i < doesReInitialize.length; i++) {
			doesReInitialize[i] = false;
		}

		for (int i=0; i < numOfSwarms; i++) {
			for (int j=i+1; j < numOfSwarms; j++) {
				int gbest1[][] = swarms[i].getGbest();
				int gbest2[][] = swarms[j].getGbest();
				double distance = (double) Func.calcDistance(gbest1, gbest2);
				if (distance < Const.R_EXCL*row) {
					if (swarms[i].getGbestFitness() < swarms[j].getGbestFitness()) {
						doesReInitialize[i] = true;
					} else {
						doesReInitialize[j] = true;
					}
				}
			}
		}
		for (int i=0; i < Const.NUMOFSWARMS; i++) {
			if (doesReInitialize[i]) {
				reInitializeSwarm(swarms[i]);
			}
		}
	}

	private void setBest(Swarm[] swarms) {
		int index = 0;
		int row = swarms[0].getGbest().length;
		int col = swarms[0].getGbest()[0].length;
		if (bestMatrix == null) {
			bestMatrix = new int[row][col];
		}
		bestFitness = swarms[0].getGbestFitness();
		for (int i=0; i < swarms.length; i++) {
			Swarm swarm = swarms[0];
			if (swarm.getGbestFitness() > bestFitness) {
				bestFitness = swarm.getGbestFitness();
				index = i;
			}
		}
		for (int i=0; i < row; i++) {
			for (int j=0; j < col; j++) {
				bestMatrix[i][j] = swarms[index].getGbest()[i][j];
			}
		}
		bestMealyMachine = swarms[index].getBestMealyMachine();
	}

	private void printLog(int[][] gbest, double bestFitness, int loopCount) {
		if (loopCount%Const.NSKIP == 0) {
			System.out.println("########## Time Step: " + String.valueOf(loopCount) + " ##########");
			System.out.println("Best Fitness: " + String.valueOf(bestFitness));
			//System.out.println("Best Encoded Mealy Machine");
			//Func.printMatrix(gbest);
		}
	}

	// ****** //
	// Public //
	// ****** //
	public void construct(int[] states, int[] inputSymbols, int[] outputSymbols) {
		mealyMachineCodec = new MealyMachineCodec(states, inputSymbols, outputSymbols);
		Swarm swarms[] = new Swarm[Const.NUMOFSWARMS];

		// --- Initialization --- //
		for (int i=0; i< Const.NUMOFSWARMS; i++) {
			swarms[i] = createSwarm(states, inputSymbols, outputSymbols, mealyMachineCodec);
		}
		for (Swarm swarm : swarms) {
			swarm.evaluate(antSimulator);    // Evaluate Fitness
			swarm.updatePersonalBest();      // Update Personal Best
			swarm.updateGlobalBest(mealyMachineCodec);        // Update Global Best
		}

		// --- Main Loop --- //
		for (int n=0; n < Const.MAXLOOP; n++) {
			// Anti-Convergence
			repelConvergence(swarms);
			
			for (Swarm swarm : swarms) {
				// Test if the Environment is changed
				if (swarm.doesEnvironmentChange(antSimulator, mealyMachineCodec)) {
					swarm.restAttractor();
					swarm.evaluate(antSimulator);
					swarm.updatePersonalBest();
					swarm.updateGlobalBest(mealyMachineCodec);
				}
				swarm.advance();                 // Update Position and Velocity
				swarm.encodePosToMealyMachine(mealyMachineCodec);
				swarm.evaluate(antSimulator);    // Evaluate Fitness
				swarm.updatePersonalBest();      // Update Personal Best
				swarm.updateGlobalBest(mealyMachineCodec);        // Update Global Best
			}

			// Exclusion
			exclude(swarms);

			setBest(swarms);
			printLog(bestMatrix, bestFitness, n+1);
			
			if (n%Const.NENVCHANGE == 0 && n != 0) {
				// Print Best Routing before the Environment is Changed
				System.out.println("========== Best Mealy Machine in This Environment ==========");
				bestMealyMachine.printTransitionTable();
				System.out.println("========== Best Routing in This Environment ==========");
				antSimulator.runWithFSM(bestMealyMachine);
				antSimulator.printRoute();
				System.out.println("===================================================");
				int eaten = antSimulator.getEaten();
				int moves = antSimulator.getMoves();
				System.out.println("The number of eaten foods : " + eaten + " Steps : " + moves);
				antSimulator.reset();

				envIndex = Func.changeEnvIndex(envIndex);
				System.out.println("========== Environment is Changed ==========");
				System.out.println("Trail No.: " + envIndex);
				antSimulator.parseMatrix(trailFiles[envIndex]);
			}
		}
		
	}
	
	public MealyMachine getBestMealyMachine() {
		return bestMealyMachine;
	}

}
