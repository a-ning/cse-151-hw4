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

	/* main function */
	public static void main (String[] args) {

		/* ------------------------------ Q1 ------------------------------ */

		LinkedList<int[]> aTrainData, aTestData;
		File aTrainFile, aTestFile;

		aTrainFile = new File("hw4atrain.txt");
		aTestFile = new File("hw4atest.txt");

		aTrainData = read(aTrainFile);
		aTestData = read(aTestFile);

		/* ------------------------------ Q2 ------------------------------ */

		LinkedList<int[]> bTrainData, bTestData;
		File bTrainFile, bTestFile;

		bTrainFile = new File("hw4btrain.txt");
		bTestFile = new File("hw4btest.txt");

		bTrainData = read(bTrainFile);
		bTestData = read(bTestFile);

	}
}