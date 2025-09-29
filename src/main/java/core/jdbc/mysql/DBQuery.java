package core.jdbc.mysql;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import core.custom_interface.Column;
import core.custom_interface.Table;

public abstract class DBQuery {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	private String table_name = "";
	private Class<?> table_class = null;
	private final String read = "read";
	private final String write = "write";
	private String dbJdbcDsn = "test";
	private static HashMap<Class<?>, EntityParse> entityParseMap= new HashMap<>();
	private static HashMap<Class<?>, String> tableMap= new HashMap<>();
	
	private static ConcurrentHashMap<Thread, HashMap<String, Connection>> currentConnHashMap = new ConcurrentHashMap<>();
	// 当前使用connection
	private Connection writeConnection = null;
	private Connection readConnection = null;

	public DBQuery(String dbJdbcDsn) throws SQLException {
		if (!dbJdbcDsn.equals(""))
			this.dbJdbcDsn = dbJdbcDsn;
	}

	public DBQuery setDsn(String dbJdbcDsn) {
		this.dbJdbcDsn = dbJdbcDsn;
		return this;
	}

	public DBQuery table(Class<?> clazz) throws SQLException {
		this.table_class = clazz;
		if(tableMap.containsKey(clazz)) {
			this.table_name = tableMap.get(clazz);
		} else {
			String temp = clazz.getName();
			temp = temp.substring(temp.lastIndexOf(".") + 1);
			String str_regex = "([A-Z]+?)";//table 名字用驼峰式构造 AzzzBzzz = azzz_bzzz
			Pattern pattern = Pattern.compile(str_regex);
			Matcher table_matcher = pattern.matcher(temp);
			int i = 0;
			while (table_matcher.find()) {
				if (i > 0) {
					temp = temp.replace(table_matcher.group(), "_" + table_matcher.group().toLowerCase());
				}
				i++;
			}
			this.table_name = temp.toLowerCase();
			tableMap.put(clazz, this.table_name);
		}
		this.parseEntityClass(clazz);
		return this;
	}

	public DBQuery jointable(String table1_x_join_table2_ON_1_2) {
		this.table_name = table1_x_join_table2_ON_1_2;
		return this;
	}
	
	private void parseEntityClass(Class<?> clazz) throws SQLException {
		if(entityParseMap.containsKey(clazz)) return;
		EntityParse entityParse = new EntityParse();
		Class<?> entityClassSource = clazz;
		TableAnnotation tableAnnotation = getTableAnnotation(clazz);
		boolean isAnnotation = tableAnnotation.isAnnotationField;
		if(isAnnotation) {
			this.table_name = tableAnnotation.getTableName();
			tableMap.put(clazz, this.table_name);
		}
		HashMap<String, String>  selectIgnoreMap =  new HashMap<>();//是否是选择忽略字段
		HashMap<String, String>  updateIgnoreMap =  new HashMap<>();//是否是更新忽略字段
		HashMap<String, Field> dbFieldHashMap = new HashMap<>();//和表对应的field
		HashMap<String, Method> methodHashMap = new HashMap<>();//和表对应的method
		HashMap<String,String> annotationFileIdMap = new HashMap<>();//把数据库的columnName 转换成fieldName 输出浏览器 为用到插入更新，只用到提取数据
		String dbFieldName = "";
		for (; entityClassSource  != Object.class; entityClassSource = entityClassSource.getSuperclass()) {
			Field[] fields = entityClassSource.getDeclaredFields();
			//Field[] fields = objectClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {// 寻找该元素对应的字段属性
				Field field = fields[i];
				String fieldName = field.getName();
				dbFieldName = fieldName.toLowerCase();//数据库对应字段为小写
				annotationFileIdMap.put(dbFieldName, fieldName);//把数据库的columnName 转换成fieldName 输出浏览器 用来查找数据
				if (isAnnotation) {// 使用Annotation的Field
					FieldAnnotation fieldAnnotation = getFieldAnnotation(fields[i]);
					if(fieldAnnotation.isAnnotationColumn()) {
						dbFieldName = fieldAnnotation.getColumnName().toLowerCase();// 用Annotation name保持一致性 
						annotationFileIdMap.put(dbFieldName, fieldName);//把数据库的columnName 转换成fieldName 输出浏览器 用来查找数据
						if(fieldAnnotation.isSelectIgnore()) {
							selectIgnoreMap.put(dbFieldName, "");
						}
						if(fieldAnnotation.isUpdateIgnore()) {
							updateIgnoreMap.put(dbFieldName, "");
						}
						if(fieldAnnotation.isPrimaryKey()) {
							updateIgnoreMap.put(dbFieldName, "");
						}
						if(fieldAnnotation.isAutoIncrement()) {
							updateIgnoreMap.put(dbFieldName, "");
						}
						if (fieldAnnotation.isIgnore()) {
							continue;
						}
					}
					
				}
				String methodNameBegin = fieldName.substring(0, 1).toUpperCase();
				String methodName = "get" + methodNameBegin + fieldName.substring(1);
				Method method = null;
				try {
					method = entityClassSource.getDeclaredMethod(methodName);
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					MDC.put("APP_NAME", "mysql_error");
					logger.error("error SQL NoSuchMethodException==>:", methodName, e);
					throw new SQLException("SQL NoSuchMethodException-->:" +methodName+","+ e.getMessage());
				} 
				methodHashMap.put(dbFieldName, method);
				dbFieldHashMap.put(dbFieldName, field);
			}
		}
		entityParse.setSelectIgnoreMap(selectIgnoreMap);
		entityParse.setDbFieldHashMap(dbFieldHashMap);
		entityParse.setMethodHashMap(methodHashMap);
		entityParse.setAnnotationFileIdMap(annotationFileIdMap);
		entityParse.setUpdateIgnoreMap(updateIgnoreMap);
		entityParseMap.put(clazz, entityParse);
		
	}
	//返回list
	public List<HashMap<String, Object>> getList(WhereRelation whereRelation) throws SQLException {
		this.table(whereRelation.getTable_clazz());
		return this.getList(whereRelation.sql(this.table_name), whereRelation.getWhereParamters());
	}
	//含 Entity 表示返回的是 实体类 没有则是 hashMap数据
	private List<HashMap<String, Object>> getList(String sql, Object... paramters) throws SQLException {
		ResultSet rs = null;
		try {
			long start = System.currentTimeMillis();
			PreparedStatement preparedStatement = this.thisReadConnection().prepareStatement(sql);//
			rs = this.executeForQuery(preparedStatement, paramters);
			List<HashMap<String, Object>> list = resultSetToListMap(rs, this.table_class, false);
			logger.info("查询耗时：" + (System.currentTimeMillis() - start) + " ms" + ",SQL:"+sql);
			if (rs != null)
				rs.close();
			this.thisReadConnection().close();
			return list;
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error." + sql, e);
		}
	}

	public List<HashMap<String, Object>> getList(Object object, WhereRelation whereRelation) throws SQLException {
		Class<?> objectClass = object.getClass();
		this.table(objectClass);
		ResultSet rs = null;
		String sql = "";
		try {
			HashMap<String, Method> methodHashMap = entityParseMap.get(objectClass).getMethodHashMap();//
			TableAnnotation tableAnnotation = getTableAnnotation(objectClass);// 取得objectClass table的Annotation
			for (String dbFieldName : methodHashMap.keySet()) {
				Object tempObj = methodHashMap.get(dbFieldName).invoke(object);
				if (tempObj != null) {
					whereRelation.EQ(dbFieldName, tempObj);// 构造数据库的SQL
				}
			}
			sql = whereRelation.sql(tableAnnotation.getTableName());
			PreparedStatement preparedStatement = this.thisReadConnection().prepareStatement(sql);
			logger.debug(sql);
			rs = this.executeForQuery(preparedStatement, whereRelation.getWhereParamters());
			List<HashMap<String, Object>> list = resultSetToListMap(rs, objectClass, whereRelation.getSelectShow());
			if (rs != null)
				rs.close();
			this.thisReadConnection().close();
			return list;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error." + sql);
		}
	}
	
	public List<HashMap<String, Object>> getListByEntity(Class<?> entityClassT, WhereRelation whereRelation) throws SQLException {
		this.table(entityClassT);
		ResultSet rs = null;
		String sql = "";
		try {
			TableAnnotation tableAnnotation = getTableAnnotation(entityClassT);// 取得objectClass table的Annotation
			sql = whereRelation.sql(tableAnnotation.getTableName());
			PreparedStatement preparedStatement = this.thisReadConnection().prepareStatement(sql);
			logger.debug(sql);
			rs = this.executeForQuery(preparedStatement, whereRelation.getWhereParamters());
			List<HashMap<String, Object>> list = resultSetToListMap(rs, entityClassT, whereRelation.getSelectShow());
			if (rs != null)
				rs.close();
			this.thisReadConnection().close();
			return list;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error." + sql);
		}
	}
	/**
	 * 执行返回泛型集合的SQL语句 含 Entity 表示返回的是 实体类 没有则是 hashMap数据
	 * @return 泛型集合||null
	 * @throws SQLException
	 */
	public <T> T getEntity(Class<T> entityClassT, WhereRelation whereRelation) throws SQLException {
		List<T> entityList = this.getEntityList(entityClassT, whereRelation);
		if (entityList == null)
			return null;
		if (entityList.size() > 0)
			return entityList.get(0);
		return null;
	}
	
	public <T> Object getEntity(Object object) throws SQLException {
		List<?> entityList = this.getEntityList(object.getClass(), object);
		if (entityList == null)
			return null;
		if (entityList.size() > 0)
			return entityList.get(0);
		return null;
	}
	//返回list
	public <T> List<T> getEntityList(Class<T> entityClassT, Object object) throws SQLException {
		this.table(entityClassT);
		List<T> list = new ArrayList<>();
		ResultSet rs = null;
		String sql = "";
		WhereRelation whereRelation = new WhereRelation();
		try {
			TableAnnotation tableAnnotation = getTableAnnotation(entityClassT);// 取得objectClass table的Annotation
			HashMap<String, Method> methodHashMap = entityParseMap.get(entityClassT).getMethodHashMap();//
			for (String dbFieldName : methodHashMap.keySet()) {
				Object tempObj = methodHashMap.get(dbFieldName).invoke(object);
				if (tempObj != null) {
					whereRelation.EQ(dbFieldName, tempObj);// 构造数据库的SQL
				}
				
			}
			sql = whereRelation.sql(tableAnnotation.getTableName());
			list = new ArrayList<T>();
			PreparedStatement preparedStatement = this.thisReadConnection().prepareStatement(sql);
			logger.debug(sql);
			rs = this.executeForQuery(preparedStatement, whereRelation.getWhereParamters());
			list = this.executeResultSet(entityClassT, rs, list, whereRelation.getSelectShow());
			if (rs != null)
				rs.close();
			this.thisReadConnection().close();
			return (List<T>) list;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error." + sql);
		}
	}
	
	public <T> List<T> getEntityList(Class<T> entityClassT, WhereRelation whereRelation) throws SQLException {
		this.table(entityClassT);
		String sql = whereRelation.sql(this.table_name);
		List<T> list;
		ResultSet rs = null;
		try {
			list = new ArrayList<T>();
			PreparedStatement preparedStatement = this.thisReadConnection().prepareStatement(sql);
			logger.debug(sql);
			rs = this.executeForQuery(preparedStatement, whereRelation.getWhereParamters());
			list = this.executeResultSet(entityClassT, rs, list, whereRelation.getSelectShow());
			if (rs != null)
				rs.close();
			this.thisReadConnection().close();
			return (List<T>) list;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error." + sql);
		}
	}
	
	public <T> List<T> getJoinTableEntityList(Class<T> entityClassT, WhereRelation whereRelation) throws SQLException {
		String sql = whereRelation.sql(this.table_name);
		this.parseEntityClass(entityClassT);
		List<T> list;
		ResultSet rs = null;
		try {
			list = new ArrayList<T>();
			PreparedStatement preparedStatement = this.thisReadConnection().prepareStatement(sql);
			logger.debug(sql);
			rs = this.executeForQuery(preparedStatement, whereRelation.getWhereParamters());
			list = this.executeResultSet(entityClassT, rs, list, whereRelation.getSelectShow());
			if (rs != null)
				rs.close();
			this.thisReadConnection().close();
			return (List<T>) list;
		} catch (Exception e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error." + sql);
		}
	}
	
	
	public int getCount(WhereRelation whereRelation) throws Exception {
		whereRelation.setField("COUNT(*) NUM");
		return Integer.parseInt(this.getOne(whereRelation).toString());
	}

	public Object getOne(WhereRelation whereRelation) throws SQLException, InterruptedException {
		String sql = whereRelation.sql(this.table_name);
		Object result = null;
		ResultSet rs = null;
		try {
			PreparedStatement preparedStatement = this.thisReadConnection().prepareStatement(sql);
			rs = this.executeForQuery(preparedStatement, whereRelation.getWhereParamters());
			if (rs.next()) {
				result = rs.getObject(1);
			}
			if (rs != null)
				rs.close();
			this.thisReadConnection().close();
			return result;
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error." + sql);
		}
	}

	/*
	 * 数据更新 updateData<field,value>
	 */
	public int update(WhereRelation whereRelation) throws SQLException {
		HashMap<String, Object> updateData = whereRelation.getUpdate();
		return this.update(updateData, whereRelation);
	}

	private int update(HashMap<String, Object> updateData, WhereRelation whereRelation) throws SQLException {
		StringBuilder updateSQL = new StringBuilder("");
		List<Object> updateParamtersList = new ArrayList<>();
		if (updateData != null && !updateData.isEmpty()) {
			for (Entry<String, Object> updateEntry : updateData.entrySet()) {
				updateSQL.append(", " + updateEntry.getKey() + " = ? ");
				updateParamtersList.add(updateEntry.getValue());
			}
		}
		String sql = "UPDATE " + this.table_name + " SET " + updateSQL.toString().substring(2)
				+ whereRelation.getWhereSQL();
		try {
			PreparedStatement preparedStatement = this.thisWriteConnection().prepareStatement(sql);
			logger.info("update sql:" + sql);
			Object[] paramters = null;
			int i = 0;
			if (updateParamtersList != null && !updateParamtersList.isEmpty()) {
				paramters = updateParamtersList.toArray();
				for (; i < paramters.length; i++) {
					preparedStatement.setObject(i + 1, paramters[i]);
				}
				logger.info("update data:" + Arrays.toString(paramters));
			}

			paramters = whereRelation.getWhereParamters();
			if (paramters != null && paramters.length > 0) {
				// int j = i > 0 ? i - 1 : i;
				for (int j = 0; j < paramters.length; j++) {
					preparedStatement.setObject(j + i + 1, paramters[j]);
				}
				logger.info("update whereCondition:" + Arrays.toString(paramters));
			}
			// this.resolveUpdateSql(preparedStatement, updateParamtersList.toArray(),
			// whereRelation.getWhereParamters());
			// 执行SQL
			int id = preparedStatement.executeUpdate();
			//this.thisWriteConnection().close();
			return id;
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
			throw new SQLException("SQL error." + sql);
		}
	}

	public int increase(WhereRelation whereRelation) throws SQLException {
		String sql = "UPDATE " + this.table_name + " SET " + whereRelation.getField() + '=' + whereRelation.getField()
				+ "+" + whereRelation.getIncrease() + whereRelation.getWhereSQL();
		try {
			PreparedStatement preparedStatement = this.thisWriteConnection().prepareStatement(sql);
			logger.info("update sql:" + sql);
			Object[] paramters = null;
			paramters = whereRelation.getWhereParamters();
			if (paramters != null && paramters.length > 0) {
				// int j = i > 0 ? i - 1 : i;
				for (int j = 0; j < paramters.length; j++) {
					preparedStatement.setObject(j + 1, paramters[j]);
				}
				logger.info("update whereCondition:" + Arrays.toString(paramters));
			}
			int id = preparedStatement.executeUpdate();
			//this.thisWriteConnection().close();
			return id;
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error." + sql);
		}
	}

	public int updateObject(Object object, WhereRelation whereRelation) throws Exception {
		return this.privateUpdateEntity(object, whereRelation);
	}

	private int privateUpdateEntity(Object object, WhereRelation whereRelation) throws Exception {
		try {
			Class<?> objectClass = object.getClass();
			this.table(objectClass);
			HashMap<String, String> fieldHashMap = whereRelation.getFieldHashMap();
			TableAnnotation tableAnnotation = getTableAnnotation(objectClass);// 取得entityClassT table的Annotation
			List<Object> valueObj = new ArrayList<Object>();
			String table_name = this.table_name;
			if(tableAnnotation.isAnnotationField) table_name = tableAnnotation.getTableName();
			StringBuilder sql = new StringBuilder("UPDATE " + table_name + " SET ");
			HashMap<String, Method> methodHashMap = entityParseMap.get(objectClass).getMethodHashMap();//
			HashMap<String, String>  updateIgnoreMap = entityParseMap.get(objectClass).getUpdateIgnoreMap();
			for (String dbFieldName : methodHashMap.keySet()) {
				if (updateIgnoreMap.containsKey(dbFieldName)) continue;
				Object tempObj = methodHashMap.get(dbFieldName).invoke(object);
				if (fieldHashMap.containsKey(dbFieldName) && tempObj != null) {
					valueObj.add(tempObj);
					sql.append(dbFieldName).append(" = ? ,");// 更新表字段
					continue;
				}
				if (tempObj != null) {// 默认值为null的不更新
					valueObj.add(tempObj);
					sql.append(dbFieldName).append(" = ? ,");// 更新表字段
				}
				//为避免entity的默认值为 0， entity的为0 的类型设置为以下的类型
				/*
				byte类型的默认值：0
				short类型的默认值：0
				int类型的默认值：0
				long类型的默认值：0
				float类型的默认值：0.0
				double类型的默认值：0.0
				char类型的默认值： 
				boolean类型的默认值：false
				//////////////////
				String 默认值：null
				Byte类型的默认值：null
				Short类型的默认值：null
				Integer类型的默认值：null
				Long类型的默认值：null
				Float类型的默认值：null
				Double类型的默认值：null
				Character类型的默认值：null
				Boolean类型的默认值：null
				*/
				
			}
			if (valueObj.size() == 0) {
				return 0;
			} else {
				// 最后一位为,，去除
				sql = this.trimChar(sql);
				sql.append(whereRelation.getWhereSQL());
				PreparedStatement preparedStatement = this.thisWriteConnection().prepareStatement(sql.toString());
				logger.info("update sql:" + sql);
				Object[] paramters = null;
				int i = 0;
				if (!valueObj.isEmpty() && valueObj.size() > 0) {
					paramters = valueObj.toArray();
					for (; i < paramters.length; i++) {
						preparedStatement.setObject(i + 1, paramters[i]);
					}
					logger.info("update data:" + Arrays.toString(paramters));
				}

				paramters = whereRelation.getWhereParamters();
				if (paramters != null && paramters.length > 0) {
					for (int j = 0; j < paramters.length; j++) {
						preparedStatement.setObject(j + i + 1, paramters[j]);
					}
					logger.info("update whereCondition:" + Arrays.toString(paramters));
				}
				logger.debug(sql.toString());
				int id = preparedStatement.executeUpdate();
				//this.thisWriteConnection().close();
				return id;
				// executeUpdate返回数据库修改行数
				// preparedStatement.execute();execute只有执行select等带有ResultSet
				// object返回结果集时它的返回值才是true
				// executeQuery则返回查询结果
			}
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql", e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error.");
		}
	}

	/*
	 * 删除数据
	 */
	public int delete(WhereRelation whereRelation) throws SQLException {
		this.table(whereRelation.getTable_clazz());
		String sql = "DELETE FROM " + this.table_name + whereRelation.getWhereSQL();
		try {
			PreparedStatement preparedStatement = this.thisWriteConnection().prepareStatement(sql);
			Object[] paramters = whereRelation.getWhereParamters();
			if (paramters != null && paramters.length > 0) {
				for (int i = 0; i < paramters.length; i++) {
					preparedStatement.setObject(i + 1, paramters[i]);
				}
			}
			logger.info("delete sql:" + sql);
			int id = preparedStatement.executeUpdate();
			//this.thisWriteConnection().close();
			return id;
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql:" + sql, e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error." + sql);
		}
	}

	/**
	 * 批量更新数据
	 *
	 * @param sqlList 一组sql
	 * @return
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public int[] batchUpdate(List<String> sqlList) throws SQLException, InterruptedException {
		int[] result = new int[] {};
		Statement statement = null;
		try {
			statement = this.thisWriteConnection().createStatement();
			for (String sql : sqlList) {
				statement.addBatch(sql);
			}
			result = statement.executeBatch();
			statement.clearBatch();
			statement.close();
			//this.thisWriteConnection().close();
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql", e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error.");
		}
		return result;
	}

	// 保存对象
	public void insertObject(Object object) throws Exception {
		this.excuteInsertObject(object, "INSERT INTO ");
	}

	public void insertIgnoreObject(Object object) throws Exception {
		this.excuteInsertObject(object, "INSERT IGNORE INTO ");
	}

	public void insertReplaceObject(Object object) throws Exception {
		this.excuteInsertObject(object, "REPLACE INTO ");
	}

	/*
	 * 保存对象 返回递增的ID
	 */
	public Object intInsertObject(Object object) throws Exception {
		return this.excuteInsertObject(object, "INSERT INTO ");
	}

	public <T> Object batchInsertObject(List<T> objectList) throws Exception {
		return this.excuteBatchInsertObject(objectList, "INSERT INTO ");
	}

	private <T> Object excuteBatchInsertObject(List<T> objectList, String insertType) throws Exception {
		int insertSize = objectList.size(), i = 0;
		if (insertSize > 0) {
			Class<?> objectClass = objectList.get(0).getClass();
			TableAnnotation tableAnnotation = getTableAnnotation(objectClass);// 取得entityClassT table的Annotation
			List<List<Object>> valueObjList = new ArrayList<>();
			List<Object> valueList = new ArrayList<>();
			StringBuilder sql = new StringBuilder(insertType + tableAnnotation.getTableName() + " (");
			StringBuilder sqlParamters = new StringBuilder("");
			HashMap<String, Method> fieldHashMap = new HashMap<>();
			List<Method> fieldMethodList = new ArrayList<>();
			for (; i < insertSize; i++) {
				T objectT = objectList.get(i);
				for (; objectClass != Object.class; objectClass = objectClass.getSuperclass()) {
					// Method[] methods = objectClass.getDeclaredMethods();
					Field[] fields = objectClass.getDeclaredFields();
					for (Field field : fields) {
						String fieldName = field.getName();
						String columnName = fieldName;
						FieldAnnotation fieldAnnotation = this.getFieldAnnotation(field);
						if (tableAnnotation.isAnnotationField) {// 如果使用了Annotation的Field
							columnName = fieldAnnotation.getColumnName();// 得到数据库表的columnName表名
						}
						if (fieldAnnotation.isIgnore() || fieldAnnotation.isAutoIncrement()) {// 如果是忽略和递增就跳过
							continue;
						}
						String methodNameBegin = fieldName.substring(0, 1).toUpperCase();
						String methodName = "get" + methodNameBegin + fieldName.substring(1);
						Method method = objectClass.getDeclaredMethod(methodName);
						fieldHashMap.put(columnName, method);
						fieldMethodList.add(method);
						sqlParamters.append("?,");
						sql.append(columnName).append(",");// 用表名构造SQL
					}
					
				}
				valueList = new ArrayList<>();
				for(Method method : fieldMethodList) {
					Object tempObj = method.invoke(objectT);// 获取值
					valueList.add(tempObj);
				}
				valueObjList.add(valueList);
			}
			// 最后一位为,，去除
			sql = this.trimChar(sql);
			sqlParamters = this.trimChar(sqlParamters);
			sql.append(") VALUES (").append(sqlParamters).append(")");
			PreparedStatement preparedStatement = this.thisWriteConnection().prepareStatement(sql.toString(),
					PreparedStatement.RETURN_GENERATED_KEYS);
			int k = 0, objSize = 0;
			i = 0;
			for (; i < insertSize; i++) {
				valueList = valueObjList.get(i);
				objSize = valueList.size();
				for (k = 0; k < objSize; k++) {
					preparedStatement.setObject(k+1, valueList.get(k));
				}
				k = 0;
				preparedStatement.addBatch();
				if (i % 100 == 0) {
					preparedStatement.executeBatch();// 执行batch
					preparedStatement.clearBatch();// 清空batch
				}
			}
			if ((i - 1) % 100 != 0) {
				preparedStatement.executeBatch();// 执行最后batch
				preparedStatement.clearBatch();// 清空batch
			}
			ResultSet rs = preparedStatement.getGeneratedKeys();
			Object object = null;
			if (rs.next()) {
				object = rs.getObject(1);
			}
			//this.thisWriteConnection().close();
			return object;
		}
		return null;

	}

	private Object excuteInsertObject(Object object, String insertType) throws Exception {
		ResultSet rs = null;
		Object result = null;
		try {
			Class<?> objectClass = object.getClass();
			this.table(objectClass);
			TableAnnotation tableAnnotation = getTableAnnotation(objectClass);// 取得objectClass table的Annotation
			String tableName = tableAnnotation.getTableName();
			List<Object> valueObj = new ArrayList<Object>();
			StringBuilder sql = new StringBuilder(insertType + tableName + " (");
			StringBuilder sqlParamters = new StringBuilder("");
			HashMap<String, Method> methodHashMap = entityParseMap.get(objectClass).getMethodHashMap();//
			for (String dbFieldName : methodHashMap.keySet()) {
				Object tempObj = methodHashMap.get(dbFieldName).invoke(object);
				if (tempObj != null) {//除了null 全部插入
					valueObj.add(tempObj);
					sql.append(dbFieldName).append(",");// 用列名构造SQL
					sqlParamters.append("?,");
				}
			}
			if (valueObj.size() == 0) {
				throw new SQLException("SQL error. valueObj.size() == 0;");
			} else {
				sql = this.trimChar(sql);// 最后一位为,，去除
				sqlParamters = this.trimChar(sqlParamters);
				sql.append(") VALUES (").append(sqlParamters).append(")");
				PreparedStatement preparedStatement = this.thisWriteConnection().prepareStatement(sql.toString(),
						PreparedStatement.RETURN_GENERATED_KEYS);
				for (int i = 1; i <= valueObj.size(); i++) {
					preparedStatement.setObject(i, valueObj.get(i - 1));
				}
				logger.debug(sql.toString());// preparedStatement.executeUpdate();
				preparedStatement.execute();
				rs = preparedStatement.getGeneratedKeys();
				if (rs.next()) {
					result = rs.getObject(1);
				}
				//logger.info("select whereCondition:" + String.join(",", (List<String>)(List)valueObj));
			}
			if (rs != null)
				rs.close();
			//this.thisWriteConnection().close();
		} catch (SQLException e) {
			MDC.put("APP_NAME", "mysql_error");
			logger.error("error sql", e);
			//this.freeAndRollBackAllConnection();
			throw new SQLException("SQL error.");
		}
		return result;
	}

	// 根据ResultSet 取得列表值 never null
	private ResultSet executeForQuery(PreparedStatement preparedStatement, Object... paramters) throws SQLException {
		if (paramters != null && paramters.length > 0) {
			MDC.put("APP_NAME", "mysql_info");
			logger.info("select whereCondition:" + Arrays.toString(paramters));
			for (int i = 0; i < paramters.length; i++) {
				preparedStatement.setObject(i + 1, paramters[i]);
			}
		}
		return preparedStatement.executeQuery();
	}
	
	private static String formatDate(Timestamp date) {
		SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		sDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0"));
		return sDateFormat.format(date);
	}
	private <T> List<T> executeResultSet(Class<T> entityClassT, ResultSet rs, List<T> list, boolean isSelectShow) throws Exception {// 将一行记录转成一个对象
		ResultSetMetaData rsm = rs.getMetaData();// 数据库 meta 总共有多少
		int columnCount = rsm.getColumnCount();// 数据库的列 总共有多少列
		HashMap<String, Field> fieldHashMap = entityParseMap.get(entityClassT).getDbFieldHashMap();
		//HashMap<String,String> annotationFileIdMap = entityParseMap.get(this.table_name).getAnnotationFileIdMap();
		HashMap<String,String> selectIgnoreMap = entityParseMap.get(entityClassT).getSelectIgnoreMap();
		/*去掉注释则默认值返回 null
		list = null; 
		if(rs.isBeforeFirst()) list = new ArrayList<T>();*/
		while (rs.next()) {// 对每一行记录进行操作
			//if(list == null) list = new ArrayList<T>();
			T objClass = entityClassT.getDeclaredConstructor().newInstance();// 构造Entity实体，每一个rs对应一个entity对象存储数据
			for (int j = 1; j <= columnCount; j++) {// 将每一个字段取出进行赋值
				String columnName = rsm.getColumnName(j).toLowerCase();
				String columnTypeName = rsm.getColumnTypeName(j);
				// if (fieldName.equalsIgnoreCase(columnName)) {//找到对应字段
				//if(annotationFileIdMap.containsKey(columnName)) 
					//columnName = annotationFileIdMap.get(columnName);
				if (fieldHashMap.containsKey(columnName)) {
					Field field = fieldHashMap.get(columnName);
					field.setAccessible(true);
					Object value = rs.getObject(j);

					if (columnTypeName.equals("DATETIME")) {// 转换java的日期格式 java的日期格式后面带有.xxx
						if(value != null) {
							new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(rs.getTimestamp(j));
							value = formatDate(rs.getTimestamp(j));//
						}
					}
					//System.out.println("=======>"+columnTypeName);
					/*if (columnTypeName.equals("DATE")) {// 转换java的日期格式 java的日期格式后面带有.xxx
						System.out.println("=======>v=>"+value);
						if(value != null) {
							System.out.println("=======>vv=>"+value);
							new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(rs.getTimestamp(j));
							value = formatDate(rs.getTimestamp(j));//
						}
					}*/
					//if(annotationFileIdMap.containsKey(columnName)) 
					//	columnName = annotationFileIdMap.get(columnName);
					if(!isSelectShow && selectIgnoreMap.containsKey(columnName)) {//
						continue;
					} 
					field.set(objClass, value);
				}
			}
			list.add(objClass);
		}
		return list;
	}
	// 根据ResultSet 取得列表值
	private List<HashMap<String, Object>> resultSetToListMap(ResultSet rs, Class<?> table_clazz, boolean isSelectShow) throws SQLException {
		List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		/*此处是返回值是 null
		 List<HashMap<String, Object>> list = null;
		 if(rs.isBeforeFirst()) list = new ArrayList<HashMap<String, Object>>();
		 */
		HashMap<String,String> annotationFileIdMap = entityParseMap.get(table_clazz).getAnnotationFileIdMap();
		HashMap<String,String> selectIgnoreMap = entityParseMap.get(table_clazz).getSelectIgnoreMap();
		ResultSetMetaData md = rs.getMetaData();
		int columnCount = md.getColumnCount();
		while (rs.next()) {
			//if(list == null) list = new ArrayList<HashMap<String, Object>>();
			HashMap<String, Object> map = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				String columnName = md.getColumnLabel(i);
				if(selectIgnoreMap.containsKey(columnName) && !isSelectShow) continue;//不输出 select_ignore 为true and select_show 为 false的数据
				if(annotationFileIdMap.containsKey(columnName)) 
					columnName = annotationFileIdMap.get(columnName);
				map.put(columnName, rs.getObject(i));
			}
			list.add(map);
		}
		return list;
	}

	private StringBuilder trimChar(StringBuilder str) {
		if (str.charAt(str.length() - 1) == ',') {
			str.deleteCharAt(str.length() - 1);
		}
		return str;
	}

	private FieldAnnotation getFieldAnnotation(Field field) {
		FieldAnnotation fieldAnnotation = new FieldAnnotation();
		boolean isAnnotationColumn = field.isAnnotationPresent(Column.class);
		if (isAnnotationColumn) {//
			Column column = field.getAnnotation(Column.class);
			String columnName = column.name();
			boolean isAutoIncrement = column.auto_increment();
			boolean isPrimaryKey = column.primary_key();
			boolean isIgnore = column.ignore();
			boolean isUpdateIgnore = column.update_ignore();
			boolean isSelectIgnore = column.select_ignore();
			fieldAnnotation.setAutoIncrement(isAutoIncrement);
			fieldAnnotation.setColumnName(columnName);
			fieldAnnotation.setIgnore(isIgnore);
			fieldAnnotation.setUpdateIgnore(isUpdateIgnore);
			fieldAnnotation.setPrimaryKey(isPrimaryKey);
			fieldAnnotation.setSelectIgnore(isSelectIgnore);
			fieldAnnotation.setAnnotationColumn(isAnnotationColumn);
		}
		return fieldAnnotation;
	}

	class FieldAnnotation {
		private String columnName;
		private boolean isAutoIncrement = false;
		private boolean isPrimaryKey = false;
		private boolean isIgnore = false;
		private boolean isUpdateIgnore = false;
		private boolean isSelectIgnore = false;
		private boolean isAnnotationColumn = false;
		
		public String getColumnName() {
			return columnName;
		}

		public void setColumnName(String columnName) {
			this.columnName = columnName;
		}

		public boolean isAutoIncrement() {
			return isAutoIncrement;
		}

		public void setAutoIncrement(boolean isAutoIncrement) {
			this.isAutoIncrement = isAutoIncrement;
		}

		public boolean isPrimaryKey() {
			return isPrimaryKey;
		}

		public void setPrimaryKey(boolean isPrimaryKey) {
			this.isPrimaryKey = isPrimaryKey;
		}

		public boolean isIgnore() {
			return isIgnore;
		}

		public void setIgnore(boolean isIgnore) {
			this.isIgnore = isIgnore;
		}
		
		public boolean isUpdateIgnore() {
			return isUpdateIgnore;
		}

		public void setUpdateIgnore(boolean isUpdateIgnore) {
			this.isUpdateIgnore = isUpdateIgnore;
		}

		public boolean isSelectIgnore() {
			return isSelectIgnore;
		}

		public void setSelectIgnore(boolean isSelectIgnore) {
			this.isSelectIgnore = isSelectIgnore;
		}

		public boolean isAnnotationColumn() {
			return isAnnotationColumn;
		}

		public void setAnnotationColumn(boolean isAnnotationColumn) {
			this.isAnnotationColumn = isAnnotationColumn;
		}
		
	}

	private TableAnnotation getTableAnnotation(Class<?> className) {
		boolean isTable = className.isAnnotationPresent(Table.class);
		TableAnnotation tableAnnotation = new TableAnnotation();
		if (isTable) {
			Table table = className.getAnnotation(Table.class);
			tableAnnotation.setTableName(table.name());
			tableAnnotation.setAnnotationField(table.isAnnotationField());
		}
		return tableAnnotation;
	}
	
	class TableAnnotation {
		private String tableName;
		private boolean isAnnotationField = false;

		public String getTableName() {
			return tableName;
		}

		public void setTableName(String tableName) {
			this.tableName = tableName;
		}

		public boolean isAnnotationField() {
			return isAnnotationField;
		}

		public void setAnnotationField(boolean isAnnotationField) {
			this.isAnnotationField = isAnnotationField;
		}
	}

	private Connection thisWriteConnection() throws SQLException {
		String key = this.dbJdbcDsn + "." + this.write;
		if (this.writeConnection == null || !this.writeConnection.isValid(1)) {
			this.writeConnection = DbcpPoolManager.instance().getConnection(key);
		}
		this.setCurrentConnHashMap(key, this.writeConnection);
		return this.writeConnection;
	}

	private Connection thisReadConnection() throws SQLException {
		String key = this.dbJdbcDsn + "." + this.read;
		if (this.readConnection == null || !this.readConnection.isValid(1)) {
			this.readConnection = DbcpPoolManager.instance().getConnection(key);
		}
		this.setCurrentConnHashMap(key, this.readConnection);
		return this.readConnection;
	}
	
	// 释放连接
	public void closeConnection() throws SQLException { 
		this.thisWriteConnection().close();
		this.thisReadConnection().close();
	}
	
	public void freeConnection() throws SQLException {
		if (this.readConnection != null) {
			DbcpPoolManager.instance().freeConnection(this.dbJdbcDsn + "." + this.read, this.readConnection);
		}

		if (this.writeConnection != null) {
			DbcpPoolManager.instance().freeConnection(this.dbJdbcDsn + "." + this.write, this.writeConnection);
		}
	}

	private void setCurrentConnHashMap(String key, Connection connection) {
		Thread thread = Thread.currentThread();
		HashMap<String, Connection> jdbcDsnMap = currentConnHashMap.get(thread);
		if (jdbcDsnMap == null)
			jdbcDsnMap = new HashMap<>();
		if (!jdbcDsnMap.containsKey(key)) {
			jdbcDsnMap.put(key, connection);
			currentConnHashMap.put(thread, jdbcDsnMap);
		}
	}

	protected void freeAllConnection() throws SQLException {
		Thread thread = Thread.currentThread();
		HashMap<String, Connection> jdbcDsnMap = currentConnHashMap.remove(thread);
		if (jdbcDsnMap != null && jdbcDsnMap.size() > 0) {
			for (String key : jdbcDsnMap.keySet()) {
				if (jdbcDsnMap.get(key) != null) {
					DbcpPoolManager.instance().freeConnection(key, jdbcDsnMap.get(key));
				}
			}
			jdbcDsnMap = new HashMap<>();
		}
		for(Thread key :  currentConnHashMap.keySet()) {
			jdbcDsnMap = currentConnHashMap.get(key);
			for (String k : jdbcDsnMap.keySet()) {
				Connection c = jdbcDsnMap.get(k);
				if (c != null) {
					//DbcpPoolManager.instance().freeConnection(k, jdbcDsnMap.get(k));
					System.out.println("=====>"+c.isValid(0));
				}
			}
		}
		if(this.readConnection != null && this.writeConnection != null) 
			System.out.println("=====>"+this.readConnection.isValid(0)+",=====>"+this.writeConnection.isValid(0));
	}

	protected void rollbackAllConnection() throws SQLException {
		Thread thread = Thread.currentThread();
		HashMap<String, Connection> jdbcDsnMap = currentConnHashMap.remove(thread);
		if (jdbcDsnMap != null && jdbcDsnMap.size() > 0) {
			for (String key : jdbcDsnMap.keySet()) {
				if (key.contains(this.write)) {
					Connection conn = jdbcDsnMap.get(key);
					if(conn != null) {
						jdbcDsnMap.get(key).rollback();
						if(!jdbcDsnMap.get(key).getAutoCommit()) jdbcDsnMap.get(key).setAutoCommit(true);// 删除事务
					}
				}
				DbcpPoolManager.instance().freeConnection(key, jdbcDsnMap.get(key));
			}
			jdbcDsnMap = new HashMap<>();
		}
	}

	/*private void freeAndRollBackAllConnection() throws SQLException {
		this.rollbackAllConnection();
		this.freeAllConnection();
	}*/
	// 释放读连接
	public void closeReadConnection() throws SQLException {
		if (this.readConnection != null) {
			DbcpPoolManager.instance().freeConnection(this.dbJdbcDsn + "." + this.read, this.readConnection);
		}
	}

	// 释放写连接
	public void closeWriteConnection() throws SQLException {
		if (this.writeConnection != null) {
			DbcpPoolManager.instance().freeConnection(this.dbJdbcDsn + "." + this.write, this.writeConnection);
		}
	}

	public void setTransaction(boolean isTransaction) throws SQLException {
		if (isTransaction) {
			if(this.writeConnection == null || this.writeConnection.isClosed()) {
				this.thisWriteConnection();//.setAutoCommit(false);
				//return;
			} 
			this.writeConnection.setAutoCommit(false);// 开始事务
		} else {
			if(this.writeConnection != null)this.writeConnection.setAutoCommit(true);// 删除事务
		}
	}

	public void commit() throws SQLException {
		this.writeConnection.commit();
	}

	/**
	 * 回滚事务并关闭数据库连接
	 *
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		if(this.writeConnection != null && this.writeConnection.isValid(0)) this.writeConnection.rollback();
	}

}

class EntityParse {
	private HashMap<String, String>  selectIgnoreMap =  new HashMap<>();
	private HashMap<String, String>  updateIgnoreMap =  new HashMap<>();
	private HashMap<String, Field> dbFieldHashMap = new HashMap<>();//和表对应的field
	private HashMap<String, Method> methodHashMap = new HashMap<>();//和表对应的method
	private HashMap<String,String> annotationFileIdMap = new HashMap<>();//把数据库的columnName 转换成fieldName 输出浏览器
	
	public HashMap<String, String> getSelectIgnoreMap() {
		return selectIgnoreMap;
	}
	public void setSelectIgnoreMap(HashMap<String, String> selectIgnoreMap) {
		this.selectIgnoreMap = selectIgnoreMap;
	}
	public HashMap<String, String> getUpdateIgnoreMap() {
		return updateIgnoreMap;
	}
	public void setUpdateIgnoreMap(HashMap<String, String> updateIgnoreMap) {
		this.updateIgnoreMap = updateIgnoreMap;
	}
	public HashMap<String, Field> getDbFieldHashMap() {
		return dbFieldHashMap;
	}
	public void setDbFieldHashMap(HashMap<String, Field> dbFieldHashMap) {
		this.dbFieldHashMap = dbFieldHashMap;
	}
	public HashMap<String, Method> getMethodHashMap() {
		return methodHashMap;
	}
	public void setMethodHashMap(HashMap<String, Method> methodHashMap) {
		this.methodHashMap = methodHashMap;
	}
	public HashMap<String, String> getAnnotationFileIdMap() {
		return annotationFileIdMap;
	}
	public void setAnnotationFileIdMap(HashMap<String, String> annotationFileIdMap) {
		this.annotationFileIdMap = annotationFileIdMap;
	}
}
