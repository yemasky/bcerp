package com.base.dao;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.base.model.vo.PageVo;
import com.base.model.vo.PageVoEntity;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;
import com.base.util.EncryptUtiliy;

public class GeneralDao extends BaseDao {
	private DBQueryDao dBQueryDao;

	public GeneralDao(String jdbcDsn) throws SQLException {
		super(jdbcDsn);
		// TODO Auto-generated constructor stub
		dBQueryDao = new DBQueryDao(jdbcDsn);
	}

	public Object getEntity(String field, int _id, Class<?> tableClass) throws SQLException {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ(field, _id);
		return dBQueryDao.setDsn(this.jdbcDsn).getEntity(tableClass, whereRelation);
	}
	
	public Object getEntity(WhereRelation whereRelation) throws SQLException {
		return dBQueryDao.setDsn(this.jdbcDsn).getEntity(whereRelation.getTable_clazz(), whereRelation);
	}

	public Object getEntityByObject(Object object) throws SQLException {
		return dBQueryDao.setDsn(this.jdbcDsn).table(object.getClass()).getEntity(object);
	}

	public int getCount(WhereRelation whereRelation) throws Exception {
		return dBQueryDao.setDsn(this.jdbcDsn).table(whereRelation.getTable_clazz()).getCount(whereRelation);
	}
	
	public HashMap<String, Object> getRow(WhereRelation whereRelation, String fieId) throws SQLException {
		List<HashMap<String, Object>> listRes = dBQueryDao.setDsn(this.jdbcDsn).table(whereRelation.getTable_clazz()).getList(whereRelation);
		if (listRes != null && !listRes.isEmpty())
			return listRes.get(0);
		return null;
	}

	public List<HashMap<String, Object>> getList(WhereRelation whereRelation,NeedEncrypt needEncrypt) throws SQLException {
		List<HashMap<String, Object>> objectList = dBQueryDao.setDsn(this.jdbcDsn).table(whereRelation.getTable_clazz()).getList(whereRelation);
		return this.needEncrypt(objectList, needEncrypt);
	}
	//fieId 数据库的fieId
	public List<HashMap<String, Object>> getList(Object object, String fieId, NeedEncrypt needEncrypt) throws SQLException {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.setField(fieId);
		List<HashMap<String, Object>> objectList = dBQueryDao.setDsn(this.jdbcDsn).table(object.getClass())
				.getList(object, whereRelation);
		return this.needEncrypt(objectList, needEncrypt);
	}
	//needEncrypt 不一定是数据库的fieId有可能是伪装了
	public List<HashMap<String, Object>> needEncrypt(List<HashMap<String, Object>> objectList, NeedEncrypt needEncrypt) {
		if (needEncrypt != null && needEncrypt.isNeedEncrypt() && objectList != null && !objectList.isEmpty()) {
			HashMap<String, String> needEncryptMap = needEncrypt.getNeedEncrypt();
			for (int i = 0; i < objectList.size(); i++) {
				HashMap<String, Object> mapValue = objectList.get(i);
				if(needEncryptMap != null) {
					for (String key : needEncryptMap.keySet()) {
						String val = needEncryptMap.get(key);
						if (mapValue.containsKey(key)) {
							Object value = mapValue.get(key);
							if (val.equals(NeedEncrypt._ENCRYPT)) {
								String type = EncryptUtiliy.instance().getType(value);
								if (type.equals("Integer")) {
									String encryptId = EncryptUtiliy.instance().intIDEncrypt((int) value);
									mapValue.put(key, encryptId);
								} 
							} else if(val.contains(needEncrypt.get_UNIQUE_KEY())) {// m_unique_id
								//unique_id 需要加密成unique_id的必须包含有 “unique_id”字样
								try {//把val 当键值
									String unique_id = EncryptUtiliy.instance().getUniqueId((int) value);
									mapValue.put(val, unique_id);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							} else if(mapValue.containsKey(val)) {//把key的value加密后放到val
								String type = EncryptUtiliy.instance().getType(value);
								if (type.equals("Integer")) {
									String encryptId = EncryptUtiliy.instance().intIDEncrypt((int) value);
									mapValue.put(val, encryptId);
								}
							}
						}
					}
				}
				objectList.set(i, mapValue);
			}
		}
		return objectList;
	}

	// 含 Entity 表示返回的是 实体类 没有则是 hashMap数据
	@SuppressWarnings("unchecked")
	public <T> List<T> getEntityListByObject(Object object) throws SQLException {
		return (List<T>) dBQueryDao.setDsn(this.jdbcDsn).getEntityList(object.getClass(), object);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getEntityList(String field, int[] _ids) throws SQLException {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.IN(field, _ids);
		return (List<T>) dBQueryDao.setDsn(this.jdbcDsn).getEntityList(whereRelation.getTable_clazz(), whereRelation);
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> getEntityList(String field, List<Integer> _ids) throws SQLException {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.IN(field, _ids);
		return (List<T>) dBQueryDao.setDsn(this.jdbcDsn).getEntityList(whereRelation.getTable_clazz(), whereRelation);
	}

	@SuppressWarnings("unchecked")
	public <T> List<T> getEntityList(WhereRelation whereRelation) throws SQLException {
		return (List<T>) dBQueryDao.table(whereRelation.getTable_clazz()).getEntityList(whereRelation.getTable_clazz(), whereRelation);
	}

	
	public <T> PageVoEntity<T> getPageEntityList(WhereRelation whereRelation, PageVoEntity<T> pageVo) throws Exception {
		String field = whereRelation.getField();
		whereRelation.emptyField();
		Class<? extends Object> _tableClass = whereRelation.getTable_clazz();
		_tableClass = whereRelation.getTable_clazz() == null ? _tableClass : whereRelation.getTable_clazz();
		int allRows = dBQueryDao.table(_tableClass).getCount(whereRelation);
		Double allPage = Math.ceil((float) allRows / (float) pageVo.getPerPage());
		int page = pageVo.getCurrentPage();
		//System.out.println("page2=======>"+page);
		if (page > allPage.intValue())
			page = allPage.intValue();
		if (page < 1)
			page = 1;
		pageVo.setCurrentPage(page);
		pageVo.setAllPages(allPage.intValue());
		pageVo.setAllNum(allRows);
		whereRelation.LIMIT((page - 1) * pageVo.getPerPage(), pageVo.getPerPage());
		whereRelation.emptyField().setField(field);
		@SuppressWarnings("unchecked")
		List<T> objectList = (List<T>) dBQueryDao.getEntityList(whereRelation.getTable_clazz(), whereRelation);
		pageVo.setPageList(objectList);
		return pageVo;
	}

	public PageVo getPageList(WhereRelation whereRelation, PageVo pageVo, NeedEncrypt needEncrypt)
			throws Exception {
		String field = whereRelation.getField();
		whereRelation.emptyField();
		int allRows = dBQueryDao.table(whereRelation.getTable_clazz()).getCount(whereRelation);
		Double allPage = Math.ceil((float) allRows / (float) pageVo.getPerPage());
		int page = pageVo.getCurrentPage();
		//System.out.println("page3=======>"+page);
		if (page > allPage.intValue())
			page = allPage.intValue();
		if (page < 1)
			page = 1;
		pageVo.setCurrentPage(page);
		pageVo.setAllPages(allPage.intValue());
		pageVo.setAllNum(allRows);
		whereRelation.LIMIT((page - 1) * pageVo.getPerPage(), pageVo.getPerPage());
		whereRelation.emptyField().setField(field);
		//List<HashMap<String, Object>> objectList = dBQueryDao.getListByEntity(whereRelation.getTable_clazz(), whereRelation);
		List<HashMap<String, Object>> objectList = dBQueryDao.getList(whereRelation);
		objectList = this.needEncrypt(objectList, needEncrypt);
		pageVo.setPageList(objectList);
		return pageVo;
	}

	public int saveEntity(Object object) throws Exception {
		// 需转换成long java 默认从mysql int取出来是 BigInteger
		BigInteger id = (BigInteger) dBQueryDao.table(object.getClass()).intInsertObject(object);
		if(id != null) return id.intValue();
		return 0;
	}
	
	public void saveIgnoreEntity(Object object) throws Exception {
		// 需转换成long java 默认从mysql int取出来是 BigInteger
		dBQueryDao.table(object.getClass()).insertIgnoreObject(object);
	}

	public <T> int batchSave(List<T> objectList) throws Exception {
		//需转换成long java 默认从mysql int取出来是 BigInteger
		BigInteger _id = (BigInteger) dBQueryDao.setDsn(this.jdbcDsn).batchInsertObject(objectList);
		if(_id != null) return _id.intValue();
		return 0;
	}
	
	public <T> int batchSave(List<T> objectList, String insertType) throws Exception {
		//需转换成long java 默认从mysql int取出来是 BigInteger
		BigInteger _id = (BigInteger) dBQueryDao.setDsn(this.jdbcDsn).batchInsertObject(objectList, insertType);
		if(_id != null) return _id.intValue();
		return 0;
	}
	
	public int increase(WhereRelation whereRelation) throws SQLException {
		return dBQueryDao.table(whereRelation.getTable_clazz()).increase(whereRelation);
	}

	public int update(WhereRelation whereRelation) throws SQLException {
		return dBQueryDao.table(whereRelation.getTable_clazz()).update(whereRelation);
	}

	public int updateEntity(Object object, WhereRelation whereRelation) throws Exception {
		return dBQueryDao.updateObject(object, whereRelation);
	}
	
	public int updateEntity(Object object, String field, int _id) throws Exception {
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ(field, _id);
		return dBQueryDao.updateObject(object, whereRelation);
	}

	public int delete(WhereRelation whereRelation) throws Exception {
		return dBQueryDao.delete(whereRelation);
	}
	
	public void setTransaction(boolean isTransaction) throws SQLException {
		this.dBQueryDao.setTransaction(isTransaction);
	}

	public void commit() throws SQLException {
		this.dBQueryDao.commit();
	}

	public void rollback() throws SQLException {
		this.dBQueryDao.rollback();
	}
	
	public void closeConnection() throws SQLException {
		this.dBQueryDao.closeConnection();
	}
}
