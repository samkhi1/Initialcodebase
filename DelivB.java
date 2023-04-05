import java.io.*;
import java.util.Arrays;

// Class DelivB does the work for deliverable DelivB of the Prog340

public class DelivB {

	File inputFile;
	File outputFile;
	PrintWriter output;
	Graph g;
	
	public DelivB( File in, Graph gr ) {
		inputFile = in;
		g = gr;
		
		// Get output file name.
		String inputFileName = inputFile.toString();
		String baseFileName = inputFileName.substring( 0, inputFileName.length()-4 ); // Strip off ".txt"
		String outputFileName = baseFileName.concat( "_out.txt" );
		outputFile = new File( outputFileName );
		if ( outputFile.exists() ) {    // For retests
			outputFile.delete();
		}
		
		try (PrintWriter output = new PrintWriter(outputFile)) {
			int[] coinValues = g.getNodeList().stream()
				.mapToInt(node -> Integer.parseInt(node.getVal()))
				.toArray();
			
			output.print("Coin Values");
			for (int coinValue : coinValues) {
				output.printf("\t%d", coinValue);
			}
			output.println("\tTotal coins\nChange Needed");
			
			for (int i = 1; i <= 100; i++) {
				int[] coins = bruteForceRecursiveChange(coinValues, i);
				int totalCoins = Arrays.stream(coins).sum();
				output.printf("\t\t%d", i);
				for (int coin : coins) {
					output.printf("\t%d", coin);
				}
				output.printf("\t%d\n", totalCoins);
			}
			
			output.println("Dynamic Program, same formatting");
			for (int i = 1; i <= 100; i++) {
				int[] coins = bottomUpDynamicChange(coinValues, i);
				int totalCoins = Arrays.stream(coins).sum();
				output.printf("\t\t%d", i);
				for (int coin : coins) {
					output.printf("\t%d", coin);
				}
				output.printf("\t%d\n", totalCoins);
			}
		} catch (FileNotFoundException e) {
			System.err.format("Exception: %s%n", e);
		}
	}
	
	private int[] bruteForceRecursiveChange(int[] coinValues, int changeValue) {
		int[] minCoins = new int[coinValues.length];
		Arrays.fill(minCoins, Integer.MAX_VALUE);
		return bruteForceRecursiveChangeHelper(coinValues, changeValue, minCoins);
	}
	
	private int[] bruteForceRecursiveChangeHelper(int[] coinValues, int changeValue, int[] minCoins) {
		if (changeValue == 0) {
			return minCoins;
		}
		for (int i = 0; i < coinValues.length; i++) {
			if (coinValues[i] <= changeValue) {
				int[] newMinCoins = Arrays.copyOf(minCoins, minCoins.length);
				newMinCoins[i]++;
				newMinCoins = bruteForceRecursiveChangeHelper(coinValues, changeValue - coinValues[i], newMinCoins);
				int currentTotalCoins = Arrays.stream(minCoins).sum();
				int newTotalCoins = Arrays.stream(newMinCoins).sum();
				if (newTotalCoins < currentTotalCoins) {
					minCoins = newMinCoins;
				}
			}
		}
		return minCoins;
	}
	
	private int[] bottomUpDynamicChange(int[] coinValues, int change) {
	    int[] minCoins = new int[change + 1];
	    Arrays.fill(minCoins, Integer.MAX_VALUE);
	    minCoins[0] = 0;
	    for (int i = 1; i <= change; i++) {
	        for (int j = 0; j < coinValues.length; j++) {
	            if (coinValues[j] <= i) {
	                int subResult = minCoins[i - coinValues[j]];
	                if (subResult != Integer.MAX_VALUE && subResult + 1 < minCoins[i]) {
	                    minCoins[i] = subResult + 1;
	                }
	            }
	        }
	    }
	    int[] result = new int[coinValues.length];
	    for (int i = 0; i < coinValues.length; i++) {
	        int coinValue = coinValues[i];
	        int coinCount = 0;
	        int remainingChange = change;
	        while (remainingChange > 0) {
	            if (remainingChange >= coinValue && minCoins[remainingChange - coinValue] + 1 == minCoins[remainingChange]) {
	                coinCount++;
	                remainingChange -= coinValue;
	            } else {
	                break;
	            }
	        }
	        result[i] = coinCount;
	    }
	    return result;
	}

}