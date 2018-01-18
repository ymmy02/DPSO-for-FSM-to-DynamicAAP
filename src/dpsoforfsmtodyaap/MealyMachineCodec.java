package dpsoforfsmtodyaap;

import java.util.HashMap;

public class MealyMachineCodec {

	private int states[];
	private int inputSymbols[];
	private int outputSymbols[];
	private int numStates;
	private int numInputs;
	private int numOutputs;
	private int rowSize;
	private int colSize;

	public MealyMachineCodec(int states[], int inputSymbols[], int outputSymbols[]) {
		this.states = states;
		this.inputSymbols = inputSymbols;
		this.outputSymbols = outputSymbols;
		numStates = states.length;
		numInputs = inputSymbols.length;
		numOutputs = outputSymbols.length;
		rowSize = numStates * numInputs;
		colSize = numStates * numOutputs;
	}
	
	public int[][] encode(TransitionAction functions) {
		int [][] transitionMatrix = new int[rowSize][colSize];  // All elements are zero
		
		for (int i=0; i < numStates; i++) {
			for (int j=0; j < numInputs; j++) {
				int state = states[i];
				int input = inputSymbols[j];
				int endState = functions.getTransition(state, input);
				int output = functions.getAction(state, input);
				int row = numStates*i + j;
				// !!! Assumption !!! : states = {0, 1, 2, ...}, output symbols = {0, 1, 2, ...}
				int col = numOutputs*endState + output;
				transitionMatrix[row][col] = 1;
			}
		}
		return transitionMatrix;
	}
	
	public TransitionAction decode(int[][] transitionMatrix) {
		HashMap<Integer, HashMap<Integer, Integer>> transition = new HashMap<>();
		HashMap<Integer, HashMap<Integer, Integer>> action = new HashMap<>();
		TransitionAction functions = new TransitionAction(transition, action);
		for (int i=0; i < rowSize; i++) {
			for (int j=0; j < colSize; j++) {
				if (transitionMatrix[i][j] == 1) {
					int startState = i / numInputs;
					int input = i % numInputs;
					int endState = j / numOutputs;
					int output = j % numOutputs;
					functions.add(startState, input, endState, output);
				}
			}
		}
		return functions;
	}
	
}