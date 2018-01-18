package dpsoforfsmtodyaap;

import java.util.HashMap;

public class TransitionAction {
	
	private HashMap<Integer, HashMap<Integer, Integer>> transition;
	private HashMap<Integer, HashMap<Integer, Integer>> action;
	
	public TransitionAction(HashMap<Integer, HashMap<Integer, Integer>> transition,
			HashMap<Integer, HashMap<Integer, Integer>> action) {
		this.transition = transition;
		this.action = action;
	}
 
	public void add(int startState, int input, int endState, int output) {
		if (transition.containsKey(startState) && action.containsKey(startState)) {
			transition.get(startState).put(input, endState);
			action.get(startState).put(input, output);
		} else {
			HashMap<Integer, Integer> mapTransition = new HashMap<>();
			HashMap<Integer, Integer> mapAction = new HashMap<>();
			mapTransition.put(input, endState);
			mapAction.put(input, output);
			transition.put(startState, mapTransition);
			action.put(startState, mapAction);
		}
	}

	public int getTransition(int state, int input) {
		int endState;
		endState = transition.get(state).get(input);
		return endState;
	}

	public int getAction(int state, int input) {
		int output;
		output = action.get(state).get(input);
		return output;
	}
}