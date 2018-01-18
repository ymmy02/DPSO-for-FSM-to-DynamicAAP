package dpsoforfsmtodyaap;

public class MealyMachine {

	private int states[];
	private int currentState;
	private int initState;
	private int inputSymbols[];
	private int outputSymbols[];
	private TransitionAction functions;
	
	public MealyMachine(int states[], int initState, int inputSymbols[], int outputSymbols[]) {
		this.states = states;
		this.initState = initState;
		this.inputSymbols = inputSymbols;
		this.outputSymbols = outputSymbols;
	}
	
	public void reset() {
		currentState = initState;
	}

	public void setFunctions(TransitionAction functions) {
		this.functions = functions;
	}
	
	public int input(int inputSymbol) {
		int endState, outputSymbol;
		endState = functions.getTransition(currentState, inputSymbol);
		outputSymbol = functions.getAction(currentState, inputSymbol);
		currentState = endState;
		return outputSymbol;
	}
	
	// TODO: Implement run function
	
	public void printTransitionTable() {
		int size = inputSymbols.length;
		System.out.println("===================================================");

		String inputSymbolsStr[] = new String[size];
		for (int i = 0; i < size; i++) {
			int input = inputSymbols[i];
			inputSymbolsStr[i] = String.valueOf(input);
		}
		System.out.println("\t" + String.join("|\t", inputSymbolsStr));

		System.out.println("---------------------------------------------------");
		
		for (int state : states) {
			String endStatesStr[] = new String[size];
			String outputSymbolsStr[] = new String[size];
			String endStatesWithOutput[] = new String[size];
			for (int i=0; i < size; i++) {
				int input = inputSymbols[i];
				int endState = functions.getTransition(state, input);
				endStatesStr[i] = String.valueOf(endState);
			}
			for (int i=0; i < size; i++) {
				int input = inputSymbols[i];
				int output = functions.getAction(state, input);
				outputSymbolsStr[i] = String.valueOf(output);
			}
			for (int i=0; i < size; i++) {
				endStatesWithOutput[i] = endStatesStr[i] + "/" + outputSymbolsStr[i];
			}
			System.out.println(String.valueOf(state) + "|\t" + String.join("|\t", endStatesWithOutput));
		}

		System.out.println("---------------------------------------------------");
		System.out.println("===================================================");
	}
}