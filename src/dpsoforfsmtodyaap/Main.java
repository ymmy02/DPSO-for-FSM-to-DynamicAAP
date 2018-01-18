package dpsoforfsmtodyaap;

import java.util.stream.IntStream;

public class Main {

	public static void main(String[] args) {
		int[] states = IntStream.rangeClosed(0, Const.NUMOFSTATES-1).toArray();
		int inputSymbols[] = {0, 1};  // 0: Not Found, 1: Found
		int outputSymbols[] = {0, 1, 2};  // 0: Forward, 1: Left, 2: Right

		AntSimulator antSimulator = new AntSimulator(Const.MAXSTEP);
		antSimulator.parseMatrix("santafetrail/santafe_trail.txt");
		DPSOforMealyMachineConstruction dpso = new DPSOforMealyMachineConstruction(antSimulator);
		dpso.construct(states, inputSymbols, outputSymbols);
		MealyMachine bestMealyMachine = dpso.getBestMealyMachine();
		bestMealyMachine.printTransitionTable();
		antSimulator.runWithFSM(bestMealyMachine);
		antSimulator.printRoute();
		int eaten = antSimulator.getEaten();
		int moves = antSimulator.getMoves();
		System.out.println("===================================================");
		System.out.println("The number of eaten foods : " + eaten + " Steps : " + moves);
	}
}
