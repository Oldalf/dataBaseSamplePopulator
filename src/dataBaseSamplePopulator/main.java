package dataBaseSamplePopulator;

import java.util.LinkedList;

public class main {

	public static void main(String[] args) {
		
		dataBaseReader dbReader = new dataBaseReader("bachelorproject_db");
		LinkedList<dataBaseTableCharacteristic> tables = dbReader.readDataBase("");

		dataPopulator populator = new dataPopulator("bachelorproject_db", "", tables, 2500);
		populator.populate();
	}

}
