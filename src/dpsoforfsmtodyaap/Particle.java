package dpsoforfsmtodyaap;

public class Particle {

	 private int position[][];
	 private double velocity[][];
	 private int rowSize;
	 private int colSize;
	 private double fitness;
	 private int pbest[][];
	 private double pbestFitness = Const.LARGENEGATIVE;
	 private int gbest[][];
	 private double gbestFitness = Const.LARGENEGATIVE;
	 private MealyMachine mealyMachine;
	 
	 public Particle(double[][] velocity) {
		 this.velocity = velocity;
		 rowSize = velocity.length;
		 colSize = velocity[0].length;
	 }
	 
	 // Getters //
	 public double getFitness() { return fitness; }
	 
	 public int[][] getPosition() { return position; }

	 public double[][] getVelocity() { return velocity; }

	 public int[][] getPbest() { return pbest; }

	 public double getPbestFitness() { return pbestFitness; }

	 public int[][] getGbest() { return gbest; }

	 public double getGbestFitness() { return gbestFitness; }

	 public MealyMachine getMealyMachine() { return mealyMachine; }
	 
	 // ******* //
	 // Private //
	 // ******* //
	 private void makePositionFromVelocity() {
		for (int i=0; i < rowSize; i++) {
			for (int j=0; j < colSize; j++) {
				position[i][j] = 0;
			}
		}
	 	for (int i=0; i < rowSize; i++) {
	 		int index = -1;
	 		double maxValue = Const.LARGENEGATIVE;
	 		for (int j=0; j < colSize; j++) {
	 			double value = velocity[i][j];
	 			if (value > maxValue) {
	 				maxValue = value;
	 				index = j;
	 			} 
	 		}
	 		position[i][index] = 1;
	 	}
	 }

	 private void allocatePositions() {
		 position = new int[rowSize][colSize];
		 pbest = new int[rowSize][colSize];
		 gbest = new int[rowSize][colSize];
	 }

	 // ****** //
	 // Public //
	 // ****** //
	 public void initialize(int[] states, int[] inputSymbols, int[] outputSymbols,
			 MealyMachineCodec mealyMachineCodec) {
		 int initState = states[0];
		 allocatePositions();
		 mealyMachine = new MealyMachine(states, initState, inputSymbols, outputSymbols);
		 makePositionFromVelocity();
		 encodePosToMealyMachine(mealyMachineCodec);
	 }

	 public void reInitialize(MealyMachineCodec mealyMachineCodec) {
		for (int i=0; i < rowSize; i++) {
			for (int j=0; j < colSize; j++) {
				velocity[i][j] = (Math.random() * (Const.VMAX-Const.VMIN)) + Const.VMIN;
			}
		}
		makePositionFromVelocity();
		encodePosToMealyMachine(mealyMachineCodec);
	 }

	 public void setGbest(int[][] best, double bestFitness) {
		 for (int i=0; i < rowSize; i++) {
			 for (int j=0; j < colSize; j++) {
				 gbest[i][j] = best[i][j];
			 }
		 }
		 gbestFitness = bestFitness;
	 } 


	 public void encodePosToMealyMachine(MealyMachineCodec mealyMachineCodec) {
		 TransitionAction functions = mealyMachineCodec.decode(position);
		 mealyMachine.setFunctions(functions);
	 }

	 public void evaluate(AntSimulator antSimulator) {
	 	double eaten, moves, maxMoves;
	 	antSimulator.runWithFSM(mealyMachine);
	 	eaten = antSimulator.getEaten();
	 	moves = antSimulator.getMoves();
	 	maxMoves = antSimulator.getMaxMoves();
	 	fitness = eaten + ((maxMoves - moves) / maxMoves);
	 	antSimulator.reset();
	 }
	 
	 public void updatePersonalBest() {
		 if (fitness > pbestFitness) {
			 for (int i=0; i < rowSize; i++) {
				 for (int j=0; j < colSize; j++) {
					 pbest[i][j] = position[i][j];
				 }
			 }
			 pbestFitness = fitness;
		 }
	 }
	 
	 public void restBestFitness() {
		 pbestFitness = Const.LARGENEGATIVE;
		 gbestFitness = Const.LARGENEGATIVE;
	 }
	 
	 public void advance() {
		 double r1 = Math.random();
		 double r2 = Math.random();

		 for (int i=0; i < rowSize; i++) {
			 for (int j=0; j < colSize; j++) {
				 //velocity[i][j] += Const.W * (double)position[i][j];
				 velocity[i][j] += Const.C1 * r1 * (double) (pbest[i][j] - position[i][j]);
				 velocity[i][j] += Const.C2 * r2 * (double) (gbest[i][j] - position[i][j]);
			 }
		 }
		 makePositionFromVelocity();
	 }

}
