package dpsoforfsmtodyaap;

import java.util.Arrays;

/**
 * Functions which have static attribute
 * @author ymmy
 *
 */
public class Func {
	
	public static void printMatrix(int[][] matrix) {
		System.out.println("-------------------------------------------");
		for (int[] line: matrix) {
			System.out.println(Arrays.toString(line));
		}
		System.out.println("-------------------------------------------");
	}

	public static void printMatrix(double[][] matrix) {
		System.out.println("-------------------------------------------");
		for (double[] line: matrix) {
			System.out.println(Arrays.toString(line));
		}
		System.out.println("-------------------------------------------");
	}
	
	public static int calcDistance(int[][] pos1, int[][] pos2) {
		int count = 0;
		for (int i=0; i < pos1.length; i++) {
			if (!Arrays.equals(pos1[i], pos2[i])) count++;
		}
		return count;
	}
	
	public static int changeEnvIndex(int currentEnvIndex) {
		int nextEnvIndex = 0;
		double uniform = Math.random();
		switch (currentEnvIndex) {
		case 0:
			if (uniform < 0.75) nextEnvIndex = 1;
			else nextEnvIndex = 4;
			break;
		case 1:
			if (uniform < 0.40) nextEnvIndex = 2;
			else nextEnvIndex = 3;
			break;
		case 2:
			if (uniform < 0.05) nextEnvIndex = 0;
			else nextEnvIndex = 4;
			break;
		case 3:
			if (uniform < 0.35) nextEnvIndex = 1;
			else nextEnvIndex = 2;
			break;
		case 4:
			if (uniform < 0.75) nextEnvIndex = 0;
			else nextEnvIndex = 3;
			break;
		}
		return nextEnvIndex;
	}

}
