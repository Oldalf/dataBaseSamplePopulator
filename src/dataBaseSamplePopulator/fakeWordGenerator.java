package dataBaseSamplePopulator;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class fakeWordGenerator {
	protected int numberOfWords;
	protected char[] consonants = { 'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v',
			'w', 'x', 'z' };
	protected char[] vowels = { 'a', 'e', 'i', 'o', 'u', 'y' };
	
	protected Random rand;
	private ArrayList<String> words = new ArrayList<String>();
	private static final String path = "src/resources/words_alpha.txt";
	
	public fakeWordGenerator(int seed) {
		readWords();
		rand = new Random(seed);
	}
	
	@Deprecated
	private String genWord(int minLength, int maxLength) {
		String retVal = "";
		int actualLenght = rand.nextInt() % maxLength + minLength;
		actualLenght = Math.abs(actualLenght);

		boolean prevWasConst = false;
		boolean beforeWasConst = false;
		int coin = 0;
		int pick;

		for (int i = 0; i < actualLenght; i++) {
			// coin = 0 --> consonsant.
			coin = rand.nextInt() % 2;

			// the first 2 we don't care about the previous ones.
			if (i < 2) {
				if (coin == 0) {
					pick = (rand.nextInt() % consonants.length);
					pick = Math.abs(pick);
					retVal += consonants[pick];
				} else {
					pick = (rand.nextInt() % vowels.length);
					pick = Math.abs(pick);
					retVal += vowels[pick];
				}
			} else {
				if (coin == 0 && (prevWasConst && beforeWasConst)) {
					pick = (rand.nextInt() % consonants.length);
					pick = Math.abs(pick);
					retVal += consonants[pick];
				} else if (coin == 0 && !prevWasConst) {
					pick = (rand.nextInt() % vowels.length);
					pick = Math.abs(pick);
					retVal += vowels[pick];
				} else {
					pick = (rand.nextInt() % consonants.length);
					pick = Math.abs(pick);
					retVal += consonants[pick];
				}
			}
		}

		return retVal;
	}
	
	public String getChar() {
		if(rand.nextBoolean()) {
			return Integer.toString(consonants[rand.nextInt(consonants.length)]);
		}
		return Integer.toString(vowels[rand.nextInt(vowels.length)]);
	}
	
	public String getWord() {
		String retVal = "";
		retVal = words.get(rand.nextInt(words.size()));
		return retVal;
	}
	
	private void readWords() {
		try {
			FileInputStream inputStream = new FileInputStream(path);
			Scanner scan = new Scanner(inputStream);
			
			while(scan.hasNextLine()){
				words.add(scan.nextLine());
			}
			
			scan.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
