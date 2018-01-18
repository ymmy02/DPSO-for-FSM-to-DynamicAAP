package dpsoforfsmtodyaap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AntSimulator {
	
	//private String direction[] = {"north","east","south","west"};
	private int dirRow[] = {1, 0, -1, 0};
	private int dirCol[] = {0, 1, 0, -1};

	private int maxMoves;
	private int moves = 0;
	private int eaten = 0;
	private int allFoods = 0;
	private int row;
	private int col;
	private int dir = 1;  // Initial direction is "east"
	private int rowStart;
	private int colStart;
	private ArrayList<ArrayList<String>> matrix;
	private ArrayList<ArrayList<String>> matrixExc;
	private int matrixRow;
	private int matrixCol;
	
	public AntSimulator(int maxMoves) {
		this.maxMoves = maxMoves;
	}
	
	// ****** //
	// Getter //
	// ****** //
	public int getMaxMoves() {
		return maxMoves;
	}

	public int getMoves() {
		return moves;
	}

	public int getEaten() {
		return eaten;
	}

	public int getAllFoods() {
		return allFoods;
	}
	
	// ******* //
	// Private //
	// ******* //
	private void turnLeft() {
		if (moves < maxMoves) {
			moves++;
			dir = (dir - 1) % 4;
			if (dir < 0) dir = 3;
		}
	}

	private void turnRight() {
		if (moves < maxMoves) {
			moves++;
			dir = (dir + 1) % 4;
		}
	}
	
	private void moveForward() {
		if (moves < maxMoves) {
			moves++;
			row = (row + dirRow[dir]) % matrixRow;
			col = (col + dirCol[dir]) % matrixCol;
			if (row < 0) row = matrixRow-1;
			if (col < 0) col = matrixCol-1;
			if (matrixExc.get(row).get(col) == "food") eaten++;
			matrixExc.get(row).set(col, "passed");
		}
	}
	
	private boolean senseFood() {
		int aheadRow = (row + dirRow[dir]) % matrixRow;
		int aheadCol = (col + dirCol[dir]) % matrixCol;
		if (aheadRow < 0) aheadRow = matrixRow-1;
		if (aheadCol < 0) aheadCol = matrixCol-1;
		return (matrixExc.get(aheadRow).get(aheadCol) == "food");
	}
	
	// ****** //
	// Public //
	// ****** //
	public void reset() {
		row = rowStart;
		col = colStart;
		dir = 1;
		moves = 0;
		eaten = 0;
		for (int i=0; i < matrix.size(); i++) {
			ArrayList<String> row = matrix.get(i);
			for (int j=0; j < row.size(); j++) {
				String cel = row.get(j);
				matrixExc.get(i).set(j, cel);
			}
		}
	}
	
	public void runWithFSM(MealyMachine mealyMachine) {
		mealyMachine.reset();
		while (moves < maxMoves && eaten < allFoods) {
			// 0: Not Found, 1: Found
			int input = senseFood() ? 1 : 0; 
			int output = mealyMachine.input(input);
			switch(output) {
			case 0:
				moveForward();
				break;
			case 1:
				turnLeft();
				break;
			case 2:
				turnRight();
				break;
			}
		}
	}

	public void parseMatrix(String fileName) {
		matrix = new ArrayList<>();
		
		try {
			File f = new File(fileName);
			BufferedReader br = new BufferedReader(new FileReader(f));
			
			String line = br.readLine();
			int i = 0;
			while (line != null) {
				matrix.add(new ArrayList<String>());
				for (int j=0; j < line.length(); j++) {
					char col = line.charAt(j);
					switch (col) {
						case '#':
							matrix.get(i).add("food");
							allFoods++;
							break;
						case '.':
							matrix.get(i).add("empty");
							break;
						case 'S':
							matrix.get(i).add("empty");
							rowStart = i;
							colStart = j;
							dir = 1;
							break;
					}
				}
				line = br.readLine();
				i++;
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		
		matrixRow = matrix.size();
		matrixCol = matrix.get(0).size();
		matrixExc = new ArrayList<>();
		for (ArrayList<String> row : matrix) {
			matrixExc.add(new ArrayList<String>(row));
		}
	}
	
	public void printRoute() {
		for (ArrayList<String> row : matrixExc) {
			String line = "";
			for (String col : row) {
				switch (col) {
				case "food":
					line += "#";
					break;
				case "empty":
					line += ".";
					break;
				case "passed":
					line += "*";
					break;
				}
			}
			System.out.println(line);
		}
	}
}