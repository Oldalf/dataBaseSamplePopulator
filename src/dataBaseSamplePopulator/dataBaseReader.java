package dataBaseSamplePopulator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.LinkedList;

public class dataBaseReader {

	private String database;
	private LinkedList<dataBaseTableCharacteristic> allTables = new LinkedList<dataBaseTableCharacteristic>();

	public dataBaseReader(String database) {
		this.database = database;
	}

	public LinkedList<dataBaseTableCharacteristic> readDataBase(String password) {

		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, "root", password);

			LinkedList<String> tables = getTables(con);

			for (int i = 0; i < tables.size(); i++) {
				allTables.add(getTableInfo(con, tables.get(i)));
			}

			for (int i = 0; i < allTables.size(); i++) {
				System.out.println(allTables.get(i));
			}

			con.close();

			return allTables;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private dataBaseTableCharacteristic getTableInfo(Connection con, String tableName) {
		dataBaseTableCharacteristic retVal = new dataBaseTableCharacteristic(tableName);
		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SHOW FULL COLUMNS FROM " + tableName);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				dataBaseColumnCharacteristic col = new dataBaseColumnCharacteristic();
				// Set column name
				col.setName(rs.getString(1));

				for (int i = 1; i <= columnsNumber; i++) {
					String columnValue = rs.getString(i);
					System.out.print(columnValue + "(" + i + ")");
				}
				System.out.println();
				setDataBaseColumnTypeAndLength(rs.getString(2), col);
				if (rs.getString(9).length() > 0) {
					// if there's a comment, try and get extra from it, if it returns 0 (fail)
					// then perform a normal extra action
					if (setDataBaseExtraFromComments(rs.getString(9), col) == 1) {
						System.out.println(col.getName() + " has an extra comment: " + col.getExtra());
						// If the column is a relation
						if (col.getExtra() == dataBaseColumnCharacteristic.Extra.relation) {
							// Modify has relation to true.
							System.out.println("hit");
							retVal.hasRelation = true;
						}
					} else {
						setDataBaseExtra(rs.getString(7), col);
					}
				} else {
					setDataBaseExtra(rs.getString(7), col);
				}

				// add column to table representation.
				retVal.addDataBaseColumnCharacteristic(col);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return retVal;
	}

	private void setDataBaseColumnTypeAndLength(String type, dataBaseColumnCharacteristic col) {
		System.out.println("**************");
		System.out.println(type);

		// Check if the type has a length
		if (type.contains("(")) {
			/*
			 * The type had a length, parse the length and put as col length and clean type
			 * value.
			 */
			String tempType[] = type.split("\\(");

			type = tempType[0];
			// Some lengths are decimals separated by comma, this is a workaround
			// to convert it into an int.
			String tempLength = tempType[1].replace(")", "");
			int colLength = Double.valueOf(tempLength.replace(",", ".")).intValue();
			col.setLength(colLength);
		} else if (type.equals("text")) {
			col.setLength(5000); // can be 65k but setting to 5k to make it not runaway in length for the
									// testing.
		} else {
			col.setLength(0);
		}
		/*
		 * for this project at least there's no need to have types be differentiated
		 * with capital vs lower case so making all lower
		 */
		type.toLowerCase();

		/*
		 * 
		 * Char, Varchar, Text, Int, Tinyint, Bit, Bigint, Date, DateTime, TimeStamp,
		 * Float, Double, etc.
		 */
		switch (type) {
		case "char":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Char);
			break;
		case "varchar":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Varchar);
			break;
		case "int":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Int);
			break;
		case "tinyint":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Tinyint);
			break;
		case "bigint":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Bigint);
			break;
		case "boolean":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Boolean);
			break;
		case "bit":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Bit);
			break;
		case "date":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Date);
			break;
		case "datetime":
			col.setColumnType(dataBaseColumnCharacteristic.Type.DateTime);
			break;
		case "timestamp":
			col.setColumnType(dataBaseColumnCharacteristic.Type.TimeStamp);
			break;
		case "float":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Float);
			break;
		case "double":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Double);
			break;
		case "decimal":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Decimal);
			break;
		case "text":
			col.setColumnType(dataBaseColumnCharacteristic.Type.Text);
			break;
		default:
			System.err.println("!! MISSED TYPE: " + type + " !!");
			break;
		}

	}

	private void setDataBaseExtra(String extra, dataBaseColumnCharacteristic col) {
		extra.toLowerCase();

		switch (extra) {
		case "auto_increment":
			col.setExtra(dataBaseColumnCharacteristic.Extra.auto_increment);
			break;
		case "blob":
			col.setExtra(dataBaseColumnCharacteristic.Extra.blob);
			break;
		case "none":
			col.setExtra(dataBaseColumnCharacteristic.Extra.None);
			break;
		case "":
			col.setExtra(dataBaseColumnCharacteristic.Extra.None);
			break;
		case "on update current_timestamp()":
			col.setExtra(dataBaseColumnCharacteristic.Extra.on_update_current_timestamp);
			break;
		default:
			System.err.println("!! MISSED Extra: " + extra + " !!");
			break;
		}
	}

	private int setDataBaseExtraFromComments(String comment, dataBaseColumnCharacteristic col) {
		comment.toLowerCase();
		int status = 1;
		switch (comment) {
		case "relation":
			col.setExtra(dataBaseColumnCharacteristic.Extra.relation);
			break;
		case "guid":
			col.setExtra(dataBaseColumnCharacteristic.Extra.guid);
			break;
		default:
			System.err.println("!! MISSED Extra(comment): " + comment + " !!");
			status = 0;
			break;
		}

		return status;
	}

	private LinkedList<String> getTables(Connection con) {
		LinkedList<String> retVal = new LinkedList<String>();
		try {

			Statement stmt = con.createStatement();

			ResultSet rs = stmt.executeQuery(
					"SELECT table_name FROM information_schema.tables WHERE table_schema ='" + database + "'");
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			while (rs.next()) {
				retVal.add(rs.getString(1));
				for (int i = 1; i <= columnsNumber; i++) {
					if (i > 1)
						System.out.print(",  ");
					String columnValue = rs.getString(i);
					// System.out.print(columnValue + " " + rsmd.getColumnName(i));
				}
				// System.out.println("");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retVal;
	}
}