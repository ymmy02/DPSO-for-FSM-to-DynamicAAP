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

}
