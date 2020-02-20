package dataBaseSamplePopulator;

public class dataBaseColumnCharacteristic {
	enum Type {
		Char, Varchar, Text, Int, Tinyint, Bit, Bigint, Date, DateTime, TimeStamp, Float, Double, Boolean, Decimal
	}

	enum Extra {
		auto_increment, blob, None, on_update_current_timestamp, relation, guid
	}

	// Normal fördelnings värden för att få en fördelning. ??

	private Type columnType;
	private int length;
	private Extra extra;
	private String name;

	public dataBaseColumnCharacteristic(Type columnType, int length, Extra extra, String name) {
		super();
		this.columnType = columnType;
		this.length = length;
		this.extra = extra;
	}

	public dataBaseColumnCharacteristic() {
		this.columnType = null;
		this.length = 0;
		this.extra = null;
	}

	public Type getColumnType() {
		return columnType;
	}

	public void setColumnType(Type columnType) {
		this.columnType = columnType;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public Extra getExtra() {
		return extra;
	}

	public void setExtra(Extra extra) {
		this.extra = extra;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "dataBaseColumnCharacteristic [columnType=" + columnType + ", length=" + length + ", extra=" + extra
				+ ", name=" + name + "]";
	}
	
}
