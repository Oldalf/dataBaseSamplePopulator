package dataBaseSamplePopulator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Random;

public class dataPopulator {
	private String dataBase;
	private String password;
	private LinkedList<dataBaseTableCharacteristic> allTables;
	private fakeWordGenerator wordGen;
	public static final int seed = 556;
	private Random rand = new Random(seed);

	private int rowAmount;

	public dataPopulator(String dataBase, String password, LinkedList<dataBaseTableCharacteristic> allTables,
			int rowAmount) {
		this.dataBase = dataBase;
		this.password = password;
		this.allTables = allTables;
		this.rowAmount = rowAmount;
		wordGen = new fakeWordGenerator(seed);
	}

	public void populate() {

		for (int i = 0; i < allTables.size(); i++) {
			populateTable(allTables.get(i), rowAmount);
		}

	}

	private void populateTable(dataBaseTableCharacteristic table, int rows) {
		Connection con;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + dataBase, "root", password);

			Statement stmt = con.createStatement();

			System.out.println("table: " + table.getTableName());
			
			// Prepare the default things every insert will use.
			String insert = "INSERT INTO " + table.getTableName() + "(";

			for (int y = 0; y < table.getColumnAmount(); y++) {
				dataBaseColumnCharacteristic thisCol = table.getColumn(y);
				if (thisCol.getExtra() == dataBaseColumnCharacteristic.Extra.auto_increment) {
					continue;
				}
				insert += thisCol.getName() + ",";
			}

			// remove the last ","
			insert = insert.substring(0, insert.length() - 1);

			insert += ") VALUES (";

			Long start = System.currentTimeMillis();
			Long stopwatch = start;
			for (int i = 0; i < rows; i++) {
				
				
				String actualInsert = insert;
				for (int j = 0; j < table.getColumnAmount(); j++) {
					dataBaseColumnCharacteristic activeColumn = table.getColumn(j);

					if (activeColumn.getExtra() == dataBaseColumnCharacteristic.Extra.auto_increment) {
						continue;
					}

					actualInsert += getData(activeColumn) + ",";

				}
				// remove the last ","
				actualInsert = actualInsert.substring(0, actualInsert.length() - 1);

				actualInsert += ");";

				// Add query as part of a batch.
				stmt.addBatch(actualInsert);
				
				if(i % 10000 == 0) {
					long timeTaken = (System.currentTimeMillis() - stopwatch)/1000;
					stopwatch = System.currentTimeMillis();
					long totalTimeTaken = (System.currentTimeMillis() - start)/1000;
					System.out.println(i+" rows completed in " + totalTimeTaken + " . The last 10k took: "+timeTaken+" seconds.");
				}
			}
			
			System.out.println("Data generation complete, total time taken: " + (System.currentTimeMillis() - start)/1000 + " seconds.");
			start = System.currentTimeMillis();
			System.out.println("Executing batch: ");
			stmt.executeBatch();
			System.out.println("batch executed in "+ (System.currentTimeMillis()- start)/1000 + " seconds. \n");
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getData(dataBaseColumnCharacteristic activeColumn) {
		String columnData = "";

		switch (activeColumn.getColumnType()) {
		case Char:
			// System.out.println("Found a " + activeColumn.getColumnType());
			columnData = "\"" + getChar(activeColumn.getLength()) + "\"";
			break;
		case Varchar:
			columnData = "\"" + getText(activeColumn.getLength()) + "\"";
			break;
		case Text:
			columnData = "\"" + getText(activeColumn.getLength()) + "\"";
			break;
		case Int:
			columnData = Integer.toString(getInt(activeColumn.getLength()));
			break;
		case Tinyint:
			columnData = String.valueOf(getBoolean(activeColumn.getLength()));
			break;
		case Bigint:
			columnData = Integer.toString(getInt(activeColumn.getLength()));
			break;
		case Boolean:
			columnData = String.valueOf(getBoolean(activeColumn.getLength()));
			break;
		case Bit:
			columnData = Integer.toString(getBit());
			break;
		case Date:
			columnData = "\"" + getDate() + "\"";
			break;
		case DateTime:
			columnData = "\"" + getDate() + "\"";
			break;
		case TimeStamp:
			columnData = "\"" + getDate() + "\"";
			break;
		case Float:
			columnData = Double.toString(getDouble(activeColumn.getLength()));
			break;
		case Double:
			columnData = Double.toString(getDouble(activeColumn.getLength()));
			break;
		case Decimal:
			columnData = Double.toString(getDouble(activeColumn.getLength()));
			break;
		default:
			System.err.println("!! MISSED TYPE: " + activeColumn.getColumnType() + " !!");
			break;
		}

		return columnData;
	}

	private String getChar(int length) {
		String retVal = "";

		for (int i = 0; i < length; i++) {
			retVal += wordGen.getChar();
			if (i > 1) {
				if (rand.nextInt(20) == 1) {
					break;
				}
			}
		}

		return retVal;
	}

	private int getInt(int length) {
		int limitedInt = 0;
		String intLimiter = "";
		for (int i = 0; i < length; i++) {
			intLimiter += "9";
		}
		limitedInt = Integer.parseInt(intLimiter);
		return rand.nextInt(limitedInt);
	}

	private String getText(int lenght) {
		String text = "";
		int half = (int) Math.floor(lenght / 23);

		while (text.length() < half) {
			text += wordGen.getWord() + " ";
		}

		if (text.length() >= lenght) {
			text = text.substring(0, lenght);
		}
		return text;
	}

	private boolean getBoolean(int length) {
		return rand.nextBoolean();
	}

	private int getBit() {
		return rand.nextInt(2);
	}

	private String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		if (rand.nextBoolean()) {
			// future date
			calendar.add(Calendar.YEAR, rand.nextInt(3));
			calendar.add(Calendar.MONTH, rand.nextInt(12));
			calendar.add(Calendar.DAY_OF_MONTH, rand.nextInt(32));
			calendar.add(Calendar.HOUR, rand.nextInt(24));
		} else {
			calendar.add(Calendar.YEAR, -rand.nextInt(6));
			calendar.add(Calendar.MONTH, -rand.nextInt(24));
			calendar.add(Calendar.DAY_OF_MONTH, -rand.nextInt(64));
			calendar.add(Calendar.HOUR, -rand.nextInt(48));
		}
		return dateFormat.format(calendar.getTime());
	}

	private Double getDouble(int length) {
		return (length * rand.nextDouble());
	}

}
