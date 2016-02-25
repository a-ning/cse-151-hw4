/* ----------------------------------------------------------------------------
 * Alicia Ning A10796746
 * Gabriel Gaddi A10851046
 * CSE 151 - Chaudhuri
 * 25 February 2016
 * --------------------------------------------------------------------------*/

import java.io.*;
import java.util.*;
import java.text.*;
import java.lang.*;

public class hw4 {

	/* function to read files */
	public static LinkedList<int[]> read (File file) {
		LinkedList<int[]> res = new LinkedList<int[]>();
		String line = null;

		try {
			FileReader fReader = new FileReader (file);
			BufferedReader bReader = new BufferedReader (fReader);

			/* while there are still lines to read */
			while ((line = bReader.readLine()) != null) {
				/* split each line by spaces */
				String[] vals = line.split (" ");
				/* array to store values */
				int[] fVals = new int[vals.length];

				/* store values as integers */
				for (int i = 0; i < fVals.length; i++) {
					fVals[i] = Integer.parseInt (vals[i]);
				}

				/* add to LL */
				res.add (fVals);
			}

			/* close file */
			bReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println ("Failed to open file");
		} catch (IOException ex) {
			System.out.println ("Error reading file");
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

		System.out.println (s + "\n");
	}

	/* idk if printing this is a good idea */
	public static void printLL (LinkedList<int[]> ll) {
		Iterator<int[]> it = ll.iterator();
		int[] curr;
		int i = 0;

		while (it.hasNext()) {
			i++;
			System.out.println ("w" + i + ":\n");
			curr = it.next();
			printVec (curr);
		}
	}

	/* function to calculate dot product */
	public static int dot (int[] a, int[] b) {
		int res = 0;

		for (int i = 0; i < a.length; i++) {
			res += a[i] * b[i];
		}

		return res;
	}

	public static long dot (long[] a, int[] b) {
		long res = 0;

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

	/* perceptron */
	public static int[] perceptron (LinkedList<int[]> trainData, 
	LinkedList<int[]> testData, int passes, int l) {
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
				ftVec = Arrays.copyOfRange (curr, 0, curr.length - 1);

				/* check the label */
				if (curr[curr.length - 1] == l) label = 1;
				else label = -1;

				/* if y <w, x> <= 0, a mistake was made */
				if (label * dot (w, ftVec) <= 0) {
					w = adjust (w, label, ftVec);
				}
			}
		}

		return w;
	}

	/* voted/avg perceptron */
	public static LinkedList<int[]> votedPerceptron (LinkedList<int[]> 
	trainData, LinkedList<int[]> testData, int passes) {
		LinkedList<int[]> ws = new LinkedList<int[]>();

		int[] w = new int[784];
		for (int i = 0; i < w.length; i++) {
			w[i] = 0;
		}

		int[] ftVec;
		int label;

		int c = 1;

		for (int i = 0; i < passes; i++) {
			Iterator<int[]> trainIt = trainData.iterator();
			while (trainIt.hasNext()) {
				int[] curr = trainIt.next();

				ftVec = Arrays.copyOfRange (curr, 0, curr.length - 1);

				if (curr[curr.length - 1] == 0) label = -1;
				else label = 1;

				/* if y <w, x> <= 0, a mistake was made */
				if (label * dot (w, ftVec) <= 0) {
					/* store this w into a new array with the last index
					 * holding the count - how long this w survived as a
					 * classifier */
					int[] wc = Arrays.copyOf (w, 785);
					wc[784] = c;
					ws.add (wc);

					/* adjust w and reset c to 1 */
					w = adjust (w, label, ftVec);
					c = 1;
				} else {
					/* if no mistake, increment count */
					c++;
				}
			}
		}

		/* add the last w */
		int[] wc = Arrays.copyOf (w, 785);
		wc[784] = c;
		ws.add (wc);

		return ws;
	}

	public static float errs (LinkedList<int[]> data, int[] w, int l) {
		/* training error */
		
		Iterator<int[]> it = data.iterator();
		int[] ftVec;
		int label;
		int errs = 0;

		while (it.hasNext()) {
			int[] curr = it.next();

			ftVec = Arrays.copyOfRange (curr, 0, curr.length - 1);

			if (curr[curr.length - 1] == l) label = -1;
			else label = 1;

			if (label * dot (w, ftVec) <= 0) {
				errs++;
			}
		}

		return (float)errs / data.size();
	}

	/* function for calculating % error - voted perceptron */
	public static float votedErrs (LinkedList<int[]> data, 
	LinkedList<int[]> ws) {
		Iterator<int[]> it = data.iterator();
		int[] ftVec;
		int label, c;
		int errs = 0;

		while (it.hasNext()) {
			int[] curr = it.next();

			ftVec = Arrays.copyOfRange (curr, 0, curr.length - 1);

			if (curr[curr.length - 1] == 0) label = -1;
			else label = 1;

			int sum = 0;

			Iterator<int[]> wIt = ws.iterator();

			while (wIt.hasNext()) {
				int[] currW = wIt.next();

				/* get w and its count */
				c = currW[currW.length - 1];
				currW = Arrays.copyOfRange (currW, 0, currW.length - 1);

				/* c * sign of <w, x> */
				sum += c * Math.signum (dot (currW, ftVec));
			}

			if (Math.signum (sum) != (float)label) {
				errs++;
			}
		}

		return (float)errs / data.size();
	}

	/* function for calculating % error - averaged perceptron */
	public static float avgErrs (LinkedList<int[]> data, 
	LinkedList<int[]> ws) {
		Iterator<int[]> it = data.iterator();
		int[] ftVec;
		int label, c;
		int errs = 0;

		while (it.hasNext()) {
			int[] curr = it.next();

			ftVec = Arrays.copyOfRange (curr, 0, curr.length - 1);

			if (curr[curr.length - 1] == 0) label = -1;
			else label = 1;

			long[] sum = new long[ftVec.length];
			for (int i = 0; i < sum.length; i++) {
				sum[i] = 0;
			}

			Iterator<int[]> wIt = ws.iterator();

			while (wIt.hasNext()) {
				int[] currW = wIt.next();

				c = currW[currW.length - 1];
				currW = Arrays.copyOfRange (currW, 0, currW.length - 1);

				for (int i = 0; i < currW.length; i++) {
					currW[i] = c * currW[i];
					sum[i] = sum[i] + currW[i];
				}
			}

			if (Math.signum (dot (sum, ftVec)) != (float)label) {
				errs++;
			}
		}

		return (float)errs / data.size();
	}

	/* function to return label from one vs all classifier */
	public static int oneVsAll (int[] data, int[][] classifiers) {
		int matches = 0;
		int label = 1;
		int finalLabel = 10;

		for (int i = 0; i < classifiers.length; i++) {
			if (dot (data, classifiers[i]) > 0) {
				matches++;
				finalLabel = i;
			}
		}

		/* if more than 1 match or no matches, 'don't know' */
		if (matches != 1) return 10;
		else return finalLabel;
	}

	/* main function */
	public static void main (String[] args) {
		int passes = 3;

		/* ------------------------------ Q1 ------------------------------ */

		LinkedList<int[]> aTrainData, aTestData;
		File aTrainFile, aTestFile;

		aTrainFile = new File ("hw4atrain.txt");
		aTestFile = new File ("hw4atest.txt");

		aTrainData = read (aTrainFile);
		aTestData = read (aTestFile);

		System.out.println ("running perceptron...\n");

		for (int i = 1; i < passes + 1; i++) {
			System.out.println ("\t# passes: " + i + "...\n");

			int[] res = perceptron (aTrainData, aTestData, i, 6);

			//printVec (res);

			System.out.println ("\t\ttraining error = " + 
				errs (aTrainData, res, 0));

			System.out.println ("\t\ttest error = " + 
				errs (aTestData, res, 0) + "\n");
		}

		System.out.println ("running voted perceptron...\n");

		for (int i = 1; i < passes + 1; i++) {
			System.out.println ("\t# passes: " + i + "...\n");

			LinkedList<int[]> res = votedPerceptron (aTrainData, aTestData, i);

			//printLL (res);

			/* training error */
			System.out.println ("\t\ttraining error = " + 
				votedErrs (aTrainData, res));

			/* test error */
			System.out.println ("\t\ttest error = " + 
				votedErrs (aTestData, res) + "\n");
		}

		System.out.println ("running averaged perceptron...\n");

		for (int i = 1; i < passes + 1; i++) {
			System.out.println ("\t# passes: " + i + "...\n");
			
			LinkedList<int[]> res = votedPerceptron (aTrainData, aTestData, i);

			/* training error */
			System.out.println ("\t\ttraining error = " + 
				avgErrs (aTrainData, res));

			/* test error */
			System.out.println ("\t\ttest error = " + 
				avgErrs (aTestData, res) + "\n");
		}

		/* ------------------------------ Q2 ------------------------------ */

		LinkedList<int[]> bTrainData, bTestData;
		File bTrainFile, bTestFile;

		bTrainFile = new File ("hw4btrain.txt");
		bTestFile = new File ("hw4btest.txt");

		bTrainData = read (bTrainFile);
		bTestData = read (bTestFile);

		System.out.println("calculating confusion matrix...\n");

		int[][] classifiers = new int[10][];
		int[][] c = new int[11][10];
		int[] labelsAll = new int[10];
		int res;

		for (int i = 0; i < 11; i++) {
			for (int j = 0; j < 10; j++) {
				c[i][j] = 0;
				labelsAll[j] = 0;
			}
		}

		for (int i = 0; i < 10; i++) {
			classifiers[i] = perceptron (bTrainData, bTestData, 1, i);
		}

		Iterator<int[]> testIt = bTestData.iterator();

		while (testIt.hasNext()) {
			int[] curr = testIt.next();

			int[] ftVec = Arrays.copyOfRange (curr, 0, curr.length - 1);

			res = oneVsAll (ftVec, classifiers);

			labelsAll[curr[curr.length - 1]] = 
				labelsAll[curr[curr.length - 1]] + 1;
			c[res][curr[curr.length - 1]]++;
		}

		/* print the confusion matrix */
		DecimalFormat df = new DecimalFormat("0.00");
		double val;

		System.out.println("    0000 0001 0002 0003 0004 0005 0006 0007 0008 0009");
		System.out.println("   --------------------------------------------------");
		
		for (int i = 0; i < 11; i++) {
			if (i == 10) System.out.print("? | ");
			else System.out.print(i + " | ");

			for (int j = 0; j < 10; j++) {
				val = (double)c[i][j] / labelsAll[j];
				System.out.print(df.format(val) + " ");
			}
			System.out.print("\n");
		}
	}
}