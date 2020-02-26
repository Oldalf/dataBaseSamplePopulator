package dataBaseSamplePopulator;

import java.util.LinkedList;

public class dataBaseTableCharacteristic {
	private LinkedList<dataBaseColumnCharacteristic> columns = new LinkedList<dataBaseColumnCharacteristic>();
	private String tableName;
	public boolean hasRelation;

	public dataBaseTableCharacteristic(LinkedList<dataBaseColumnCharacteristic> columns, String tableName) {
		this.columns = columns;
		this.tableName = tableName;
		this.hasRelation = false;
	}

	public dataBaseTableCharacteristic(String tableName) {
		this.tableName = tableName;
	}

	public void addDataBaseColumnCharacteristic(dataBaseColumnCharacteristic dbCC) {
		columns.add(dbCC);
	}

	public LinkedList<dataBaseColumnCharacteristic> getColumns() {
		return columns;
	}

	public void setColumns(LinkedList<dataBaseColumnCharacteristic> columns) {
		this.columns = columns;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public int getColumnAmount() {
		return columns.size();
	}

	@Override
	public String toString() {
		return "dataBaseTableCharacteristic [columns=" + columns + ", tableName=" + tableName + ", hasRelation="
				+ hasRelation + "]";
	}

	public dataBaseColumnCharacteristic getColumn(int i) {
		if (i > columns.size()) {
			return null;
		}
		return columns.get(i);
	}

}
