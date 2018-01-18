package dpsoforfsmtodyaap;

import java.util.stream.IntStream;

public class Main {

	public static void main(String[] args) {
		int[] states = IntStream.rangeClosed(0, Const.NUMOFSTATES-1).toArray();
		int inputSymbols[] = {0, 1};  // 0: Not Found, 1: Found
		int outputSymbols[] = {0, 1, 2};  // 0: Forward, 1: Left, 2: Right
		String trailFiles[] = {"trail1.txt", "trail2.txt", "trail3.txt", "trail4.txt", "trail5.txt"};
		for (int i=0; i < trailFiles.length; i++) {
			trailFiles[i] = "santafetrail/" + trailFiles[i];
		}

		AntSimulator antSimulator = new AntSimulator(Const.MAXSTEP);
		antSimulator.parseMatrix(trailFiles[0]);
		DPSOforMealyMachineConstruction dpso = new DPSOforMealyMachineConstruction(antSimulator, trailFiles);
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
