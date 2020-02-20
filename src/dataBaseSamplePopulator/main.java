package dataBaseSamplePopulator;

import java.util.LinkedList;

public class main {

	public static void main(String[] args) {
		
		dataBaseReader dbReader = new dataBaseReader("examensarbete_test");
		LinkedList<dataBaseTableCharacteristic> tables = dbReader.readDataBase("");

		dataPopulator populator = new dataPopulator("examensarbete_test", "", tables, 10000);
		populator.populate();
	}

}
