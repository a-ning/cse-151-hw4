/* ----------------------------------------------------------------------------
 * Alicia Ning A10796746
 * Gabriel Gaddi A10851046
 * CSE 151 - Chaudhuri
 * 25 February 2016
 * --------------------------------------------------------------------------*/

import java.io.*;
import java.util.*;
import java.text.*;

public class hw4 {

	/* function to read files */
	public static LinkedList<int[]> read (File file) {
		LinkedList<int[]> res = new LinkedList<int[]>();
		String line = null;

		try {
			FileReader fReader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(fReader);

			/* while there are still lines to read */
			while((line = bReader.readLine()) != null) {
				/* split each line by spaces */
				String[] vals = line.split(" ");
				/* array to store values */
				int[] fVals = new int[vals.length];

				/* store values as integers */
				for(int i = 0; i < fVals.length; i++) {
					fVals[i] = Integer.parseInt(vals[i]);
				}

				/* add to LL */
				res.add(fVals);
			}

			/* close file */
			bReader.close();
		} catch(FileNotFoundException ex) {
			System.out.println("Failed to open file");
		} catch(IOException ex) {
			System.out.println("Error reading file");
		}

		return res;
	}

	/* function to print vector array */
	public static void printVec (int[] vec) {
		String s = "w = [";

		for (int i = 0; i < vec.length; i++) {
			if (i == vec.length - 1) {
				s += vec[i] + "]";
			} else {
				s += vec[i] + ", ";
			}
		}

		System.out.println (s);
	}

	/* function to calculate dot product */
	public static int dot (int[] a, int[] b) {
		int res = 0;

		for (int i = 0; i < a.length; i++) {
			res += a[i] * b[i];
		}

		return res;
	}

	/* function to correct w if a mistake was made */
	public static int[] adjust (int[] w, int y, int[] x) {
		for (int i = 0; i < x.length; i++) {
			w[i] += y * x[i];
		}

		return w;
	}

	public static int[] perceptron (LinkedList<int[]> trainData, 
	LinkedList<int[]> testData, int passes) {

		/* w - normal vector to the hyperplane */
		int[] w = new int[784];

		/* initialize w to 0 */
		for (int i = 0; i < w.length; i++) {
			w[i] = 0;
		}

		/* feature vector and label to pull from data */
		int[] ftVec;
		int label;

		for (int i = 0; i < passes; i++) {
			Iterator<int[]> trainIt = trainData.iterator();
			while (trainIt.hasNext()) {
				int[] curr = trainIt.next();

				/* get the feature vector */
				ftVec = Arrays.copyOfRange(curr, 0, curr.length - 1);

				/* check the label */
				if (curr[curr.length - 1] == 0) label = -1;
				else label = 1;

				/* if y <w, x> <= 0, a mistake was made */
				if (label * dot (w, ftVec) <= 0) {
					w = adjust (w, label, ftVec);
				}
			}
		}

		return w;
	}

	public static void votedPerceptron (LinkedList<int[]> trainData, 
	LinkedList<int[]> testData, int passes) {

	}

	public static void avgPerceptron (LinkedList<int[]> trainData, 
	LinkedList<int[]> testData, int passes) {

	}

	/* main function */
	public static void main (String[] args) {
		int passes = 3;

		/* ------------------------------ Q1 ------------------------------ */

		LinkedList<int[]> aTrainData, aTestData;
		File aTrainFile, aTestFile;

		aTrainFile = new File("hw4atrain.txt");
		aTestFile = new File("hw4atest.txt");

		aTrainData = read(aTrainFile);
		aTestData = read(aTestFile);

		System.out.println ("running perceptron...");

		for (int i = 0; i < passes + 1; i++) {
			System.out.println("# passes: " + i + "...");

			int[] res = perceptron (aTrainData, aTestData, i);

			//printVec (res);
		}

		System.out.println ("running voted perceptron...");

		for (int i = 0; i < passes + 1; i++) {
			System.out.println("# passes: " + i + "...");
			votedPerceptron (aTrainData, aTestData, i);
		}

		System.out.println ("running averaged perceptron...");

		for (int i = 0; i < passes + 1; i++) {
			System.out.println("# passes: " + i + "...");
			avgPerceptron (aTrainData, aTestData, i);
		}

		/* ------------------------------ Q2 ------------------------------ */

		LinkedList<int[]> bTrainData, bTestData;
		File bTrainFile, bTestFile;

		bTrainFile = new File("hw4btrain.txt");
		bTestFile = new File("hw4btest.txt");

		bTrainData = read(bTrainFile);
		bTestData = read(bTestFile);

	}
}