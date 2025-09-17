package core.jdbc.mysql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.util.ObjectUtils;

public class WhereRelation {
	private HashMap<String, String> fieldhashMap = new HashMap<>();
	private List<String> fieldList = new ArrayList<>();
	private String whereSQL = "";
	private String orderSQL = "";
	private String groupSQL = "";
	private String limitSQL = "";
	private String innerJoinCondition = "";
	private boolean select_show = false;
	private int increaseValue = 1;
	private List<Object> whereParameterList = new ArrayList<>();
	private HashMap<String, Object> updateData = new HashMap<>();
	private Class<? extends Object> table_clazz = null;
	//
	private String as1_table = "";
	private String SQWhereSql = "";//联合查询SQL SQ => unionSelect
	private String SQSelectSql = "";
	private String SQEndSql = "";
	//支持key="a,b,c,d,e"的String格式
	public WhereRelation setField(String field_s) {
		if(field_s.contains(",")) {
			String[] keyList = field_s.split(",");
			for(String iKey : keyList) {
				this.fieldhashMap.put(iKey, iKey);
				this.fieldList.add(iKey);
			}
			return this;
		}
		if(field_s != null && !field_s.equals("")) {
			this.fieldhashMap.put(field_s, field_s);
			this.fieldList.add(field_s);
		}
		return this;
	}
	
	public String getField() {
		return this.getSQLField();
	}
	
	private String getSQLField() {
		/*if (!this.fieldhashMap.isEmpty() && this.fieldhashMap.size() > 0) {
			Set<String> keySet = this.fieldhashMap.keySet();
			String fields = ObjectUtils.nullSafeToString(keySet);
			return (fields.substring(1, fields.length() - 1));
		}*/
		if (!this.fieldList.isEmpty() && this.fieldList.size() > 0) {
			String fields = ObjectUtils.nullSafeToString(fieldList);
			return (fields.substring(1, fields.length() - 1));
		}
		return "*";
	}
	
	public HashMap<String, String> getFieldHashMap() {
		return this.fieldhashMap;
	}
	
	public List<String> getFieldList() {
		return this.fieldList;
	}
	/*public WhereRelation emptyField() {
		this.fieldhashMap = new HashMap<>();
		return this;
	}*/
	public WhereRelation emptyField() {
		this.fieldList = new ArrayList<>();
		return this;
	}
	
	public WhereRelation setIncrease(int value) {//增减的value
		this.increaseValue = value;
		return this;
	}
	
	public int getIncrease() {
		return this.increaseValue;
	}
	
	public WhereRelation EQ(String key, Object value) {
		this.whereSQL += " AND " + key + " = ?";
		this.whereParameterList.add(value);
		return this;
	}

	public WhereRelation EQ(HashMap<String, Object> hashMapSql) {
		for (String key : hashMapSql.keySet()) {
			this.whereSQL += " AND " + key + " = ?";
			this.whereParameterList.add(hashMapSql.get(key));
		}
		return this;
	}

	public WhereRelation GT(String key, Object value) {
		this.whereSQL += " AND " + key + " > ?";
		this.whereParameterList.add(value);
		return this;
	}

	public WhereRelation GE(String key, Object value) {
		this.whereSQL += " AND " + key + " >= ?";
		this.whereParameterList.add(value);
		return this;
	}

	public WhereRelation LT(String key, Object value) {
		this.whereSQL += " AND " + key + " < ?";
		this.whereParameterList.add(value);
		return this;
	}

	public WhereRelation LE(String key, Object value) {
		this.whereSQL += " AND " + key + " <= ?";
		this.whereParameterList.add(value);
		return this;
	}

	public WhereRelation NE(String key, Object value) {
		this.whereSQL += " AND " + key + " != ?";
		this.whereParameterList.add(value);
		return this;
	}
	
	public WhereRelation NNULL(String key) {
		this.whereSQL += " AND " + key + " IS NOT NULL";
		return this;
	}
	
	public WhereRelation NULL(String key) {
		this.whereSQL += " AND " + key + " IS NULL";
		return this;
	}
	
	public WhereRelation IN(String key, List<Integer> valueList) {
		String inStrSql = "?";
		int valueSize = valueList.size();
		String[] inStr = new String[valueSize];
		for(int i = 0; i < valueSize; i++) {
			inStr[i] = "?";
			this.whereParameterList.add(valueList.get(i));
		}
		inStrSql = Arrays.toString(inStr);
		inStrSql = inStrSql.substring(1, inStrSql.length() - 1);
		this.whereSQL += " AND " + key + " IN(" + inStrSql + ")";
		return this;
	}
	
	public WhereRelation NOTIN(String key, List<Integer> valueList) {
		String inStrSql = "?";
		int valueSize = valueList.size();
		String[] inStr = new String[valueSize];
		for(int i = 0; i < valueSize; i++) {
			inStr[i] = "?";
			this.whereParameterList.add(valueList.get(i));
		}
		inStrSql = Arrays.toString(inStr);
		inStrSql = inStrSql.substring(1, inStrSql.length() - 1);
		this.whereSQL += " AND " + key + " NOT IN(" + inStrSql + ")";
		return this;
	}
	
	public WhereRelation IN(String key, int[] arrayValue) {
		String inStrSql = "?";
		int valueSize = arrayValue.length;
		String[] inStr = new String[valueSize];
		for (int i = 0; i < valueSize; i++) {
			inStr[i] = "?";
			this.whereParameterList.add(arrayValue[i]);
		}
		inStrSql = Arrays.toString(inStr);
		inStrSql = inStrSql.substring(1, inStrSql.length() - 1);

		this.whereSQL += " AND " + key + " IN(" + inStrSql + ")";
		return this;
	}
	
	public WhereRelation IN(String key, long[] arrayValue) {
		String inStrSql = "?";
		int valueSize = arrayValue.length;
		String[] inStr = new String[valueSize];
		for (int i = 0; i < valueSize; i++) {
			inStr[i] = "?";
			this.whereParameterList.add(arrayValue[i]);
		}
		inStrSql = Arrays.toString(inStr);
		inStrSql = inStrSql.substring(1, inStrSql.length() - 1);

		this.whereSQL += " AND " + key + " IN(" + inStrSql + ")";
		return this;
	}
	
	public WhereRelation IN(String key, String[] arrayValue) {
		String inStrSql = "?";
		int valueSize = arrayValue.length;
		String[] inStr = new String[valueSize];
		for (int i = 0; i < valueSize; i++) {
			inStr[i] = "?";
			this.whereParameterList.add(arrayValue[i]);
		}
		inStrSql = Arrays.toString(inStr);
		inStrSql = inStrSql.substring(1, inStrSql.length() - 1);

		this.whereSQL += " AND " + key + " IN(" + inStrSql + ")";
		return this;
	}
	
	public WhereRelation IN(String key, String value) {
		String inStrSql = "?";
		this.whereParameterList.add(value);
		this.whereSQL += " AND " + key + " IN(" + inStrSql + ")";
		return this;
	}

	public WhereRelation LIKE(String key, Object value) {
		//this.whereSQL += " AND " + key + " LIKE ('%"+value+"%')";
		//this.whereParameterList.add("\"%"+value+"%\"");
		this.whereSQL += " AND " + key + " LIKE (?)";
		this.whereParameterList.add("%"+value+"%");
		return this;
	}

	public WhereRelation LEFT_LIKE(String key, Object value) {
		this.whereSQL += " AND " + key + " LIKE (?)";
		this.whereParameterList.add(value+"%");//'?%'
		return this;
	}

	public WhereRelation MATCH(String key, Object value) {
		this.whereSQL += " AND MATCH (" + key + ") AGAINST (?)";
		this.whereParameterList.add(value);
		return this;
	}

	public WhereRelation GROUP(String strGroup) {
		if (this.groupSQL.startsWith(" GROUP BY")) {
			this.groupSQL = this.groupSQL + ", " + strGroup;
		} else {
			this.groupSQL = " GROUP BY " + strGroup;
		}
		return this;
	}

	public WhereRelation ORDER_DESC(String strOrder) {
		if (this.orderSQL.startsWith(" ORDER BY")) {
			this.orderSQL += ", " + strOrder + " DESC";
		} else {
			this.orderSQL = " ORDER BY " + strOrder + " DESC";
		}
		return this;
	}

	public WhereRelation ORDER_ASC(String strOrder) {
		if (this.orderSQL.startsWith(" ORDER BY")) {
			this.orderSQL += ", " + strOrder + " ASC";
		} else {
			this.orderSQL = " ORDER BY " + strOrder + " ASC";
		}
		return this;
	}
	
	public WhereRelation ORDER_NOTIN(String key, List<Integer> valueList) {
		String inStrSql = "?";
		int valueSize = valueList.size();
		String[] inStr = new String[valueSize];
		for(int i = 0; i < valueSize; i++) {
			inStr[i] = "?";
			this.whereParameterList.add(valueList.get(i));
		}
		inStrSql = Arrays.toString(inStr);
		inStrSql = inStrSql.substring(1, inStrSql.length() - 1);
		if (this.orderSQL.startsWith(" ORDER BY")) {
			this.orderSQL += ", " + key + " NOT IN(" + inStrSql + ")";
		} else {
			this.orderSQL = " ORDER BY " + key + " NOT IN(" + inStrSql + ")";
		}
		return this;
	}

	public WhereRelation LIMIT(int offset, int rows) {
		this.limitSQL = " LIMIT " + offset + ", " + rows + ";";
		return this;
	}

	public WhereRelation LIMIT(int offset) {
		this.limitSQL = " LIMIT " + offset + ";";
		return this;
	}

	public WhereRelation setSelectShow(boolean select_show) {
		this.select_show = select_show;
		return this;
	}
	//begin 统计查询 
	//联合查询 目前只写了下面的模式：
	//SELECT * FROM `upload_file` AS t1 WHERE (SELECT COUNT(*) FROM `upload_file` 
	//WHERE member_id=t1.member_id AND file_id >t1.file_id)<=2 ORDER BY file_id DESC 
	//Statistical Query FieldEQ 统计查询的 SQ => Statistical Query,xx.id=yy.id,
	public WhereRelation AS1(String as1_table) {
		this.as1_table = as1_table;
		return this;
	}
	
	public WhereRelation SQSelectField(String field_s) {
		this.SQSelectSql = "(SELECT " + field_s + " FROM ";
		return this;
	}
	
	public WhereRelation SQEndSql(String expression) {
		this.SQEndSql += ")" + expression;
		return this;
	}
	
	public WhereRelation SQFieldEQ(String field, String as1_field) {
		this.SQWhereSql += " AND " + field +"=" + this.as1_table + "." + as1_field;
		return this;
	}
	public WhereRelation SQFieldGT(String field, String as1_field) {
		this.SQWhereSql += " AND " + field +">" + this.as1_table + "." + as1_field;
		return this;
	}

	public WhereRelation SQFieldGE(String field, String as1_field) {
		this.SQWhereSql += " AND " + field + ">=" + this.as1_table + "." + as1_field;
		return this;
	}

	public WhereRelation SQFieldLT(String field, String as1_field) {
		this.SQWhereSql += " AND " + field +"<" + this.as1_table + "." + as1_field;
		return this;
	}

	public WhereRelation SQFieldLE(String field, String as1_field) {
		this.SQWhereSql += " AND " + field +"<=" + this.as1_table + "." + as1_field;
		return this;
	}
	
	public WhereRelation SQEQ(String key, Object value) {
		this.SQWhereSql += " AND " + key + " = ?";
		this.whereParameterList.add(value);
		return this;
	}
	
	public WhereRelation SQGT(String key, Object value) {
		this.SQWhereSql += " AND " + key + " > ?";
		this.whereParameterList.add(value);
		return this;
	}

	public WhereRelation SQGE(String key, Object value) {
		this.SQWhereSql += " AND " + key + " >= ?";
		this.whereParameterList.add(value);
		return this;
	}

	public WhereRelation SQLT(String key, Object value) {
		this.SQWhereSql += " AND " + key + " < ?";
		this.whereParameterList.add(value);
		return this;
	}

	public WhereRelation SQLE(String key, Object value) {
		this.SQWhereSql += " AND " + key + " <= ?";
		this.whereParameterList.add(value);
		return this;
	}

	public WhereRelation SQNE(String key, Object value) {
		this.SQWhereSql += " AND " + key + " != ?";
		this.whereParameterList.add(value);
		return this;
	}
	
	public WhereRelation SQNNULL(String key) {
		this.SQWhereSql += " AND " + key + " IS NOT NULL";
		return this;
	}
	
	public WhereRelation SQNULL(String key) {
		this.SQWhereSql += " AND " + key + " IS NULL";
		return this;
	}
	
	public WhereRelation SQIN(String key, List<Integer> valueList) {
		String inStrSql = "?";
		int valueSize = valueList.size();
		String[] inStr = new String[valueSize];
		for(int i = 0; i < valueSize; i++) {
			inStr[i] = "?";
			this.whereParameterList.add(valueList.get(i));
		}
		inStrSql = Arrays.toString(inStr);
		inStrSql = inStrSql.substring(1, inStrSql.length() - 1);
		this.SQWhereSql += " AND " + key + " IN(" + inStrSql + ")";
		return this;
	}
	
	public WhereRelation SQIN(String key, int[] arrayValue) {
		String inStrSql = "?";
		int valueSize = arrayValue.length;
		String[] inStr = new String[valueSize];
		for (int i = 0; i < valueSize; i++) {
			inStr[i] = "?";
			this.whereParameterList.add(arrayValue[i]);
		}
		inStrSql = Arrays.toString(inStr);
		inStrSql = inStrSql.substring(1, inStrSql.length() - 1);

		this.SQWhereSql += " AND " + key + " IN(" + inStrSql + ")";
		return this;
	}
	//end 统计查询
	//begin inner join INNERJOIN = IJ
	public WhereRelation IJCondition(String inner_join_table_t_on_as1_field_eq_t_field) {
		this.innerJoinCondition = " "+inner_join_table_t_on_as1_field_eq_t_field+" ";
		return this;
	}
	//end inner join
	public boolean getSelectShow() {
		return this.select_show;
	}
	
	public Class<? extends Object> getTable_clazz() {
		return table_clazz;
	}

	public WhereRelation setTable_clazz(Class<? extends Object> table_clazz) {
		this.table_clazz = table_clazz;
		return this;
	}

	public WhereRelation setUpdate(String field, Object value) {//
		this.updateData.put(field, value);
		return this;
	}
	
	public WhereRelation setUpdate(HashMap<String, Object> updateData) {//
		this.updateData.putAll(updateData);
		return this;
	}
	
	public HashMap<String, Object> getUpdate() {
		return this.updateData;
	}
	
	public String getWhereSQL() {
		String where = this.SQSelectSql.equals("") ? " WHERE 1=1 " : "";
		return where + this.whereSQL;
	}

	public Object[] getWhereParamters() {
		return this.whereParameterList.toArray();
	}

	public String sql(String table_name) throws SQLException {
		//SELECT * FROM `upload_file` AS t1 WHERE (SELECT COUNT(*) FROM `upload_file` 
		//WHERE member_id=t1.member_id AND file_id >t1.file_id)<=2 ORDER BY file_id DESC 
		//---
		//SELECT times.* FROM times INNER JOIN member_from ON times.`member_id` = member_from.`member_id` 
		//WHERE 1=1 AND member_from.`member_gender` = 0 AND times_id < 60 LIMIT 1
		String where = this.SQSelectSql.equals("") ? " WHERE 1=1 " : " WHERE " + this.SQSelectSql + table_name + " WHERE 1=1 " + this.SQWhereSql + this.SQEndSql;// WHERE 1=1
		String as1_table = this.as1_table.equals("") ? "" : " AS " + this.as1_table;
		return "SELECT " + this.getSQLField() + " FROM " + table_name + as1_table + this.innerJoinCondition + where + this.whereSQL + this.groupSQL + this.orderSQL
				+ this.limitSQL;
	}
}
