package com.BwleErp.service.impl;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.BwleErp.config.Config;
import com.base.dao.GeneralDao;
import com.base.model.vo.PageVo;
import com.base.model.vo.PageVoEntity;
import com.base.service.GeneralService;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;

@Service
public class GeneralServiceImpl  implements GeneralService{
	private GeneralDao genernalDao;
	
	public GeneralServiceImpl() throws SQLException {
		this.genernalDao = new GeneralDao(Config.erpDSN);
	}

	@Override
	public int save(Object object) throws Exception {
		// TODO Auto-generated method stub
		return this.genernalDao.saveEntity(object);
	}
	
	@Override
	public Object getEntityByObject(Object object) throws SQLException {
		// TODO Auto-generated method stub
		return this.genernalDao.getEntityByObject(object);
	}

	@Override
	public <T> List<T> getEntityList(Object object) throws SQLException {
		// TODO Auto-generated method stub
		return this.genernalDao.getEntityListByObject(object);
	}

	@Override
	public List<HashMap<String, Object>> getList(Object object, String fieId) throws SQLException {
		// TODO Auto-generated method stub
		return this.genernalDao.getList(object, fieId, null);
	}

	@Override
	public int increase(String field, String increase_key, int increase_key_id, int member_id, Class<?> clazz) throws SQLException {
		// TODO Auto-generated method stub
		/*HashMap<String, Object> conditionMap = new HashMap<>();
		conditionMap.put(increase_key, increase_key_id);
		conditionMap.put("member_id", member_id);*/
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.setField(field).EQ("member_id", member_id).EQ(increase_key,increase_key_id);
		return genernalDao.increase(whereRelation);//(field, conditionMap);
	}

	@Override
	public <T> List<T> getEntityList(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return this.genernalDao.getEntityList(whereRelation);
	}

	@Override
	public int update(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return this.genernalDao.update(whereRelation);
	}

	@Override
	public int updateEntity(Object object, WhereRelation whereRelation) throws Exception {
		// TODO Auto-generated method stub
		return this.genernalDao.updateEntity(object, whereRelation);
	}

	@Override
	public List<HashMap<String, Object>> getList(WhereRelation whereRelation, NeedEncrypt needEncrypt)
			throws SQLException {
		// TODO Auto-generated method stub
		return this.genernalDao.getList(whereRelation, needEncrypt);
	}

	@Override
	public Object getEntity(WhereRelation whereRelation) throws SQLException {
		// TODO Auto-generated method stub
		return this.genernalDao.getEntity(whereRelation);
	}

	@Override
	public <T> PageVoEntity<T> getPageEntityList(WhereRelation whereRelation, PageVoEntity<T> pageVo) throws Exception {
		// TODO Auto-generated method stub
		return this.genernalDao.getPageEntityList(whereRelation, pageVo);
	}

	@Override
	public PageVo getPageList(WhereRelation whereRelation, PageVo pageVo, NeedEncrypt needEncrypt) throws Exception {
		// TODO Auto-generated method stub
		return this.genernalDao.getPageList(whereRelation, pageVo, needEncrypt);
	}
	
	@Override
	public <T> int batchSave(List<T> objectList) throws Exception {
		// TODO Auto-generated method stub
		return this.genernalDao.batchSave(objectList);
	}
	
	@Override
	public <T> int batchSave(List<T> objectList, String insertType) throws Exception {
		// TODO Auto-generated method stub
		return this.genernalDao.batchSave(objectList, insertType);
	}
	
	@Override
	public void delete(WhereRelation whereRelation) throws Exception {
		// TODO Auto-generated method stub
		this.genernalDao.delete(whereRelation);
	}
	
	@Override
	public void closeConnection() throws SQLException {
		this.genernalDao.closeConnection();
	}

	@Override
	public void setTransaction(boolean isTransaction) throws SQLException {
		// TODO Auto-generated method stub
		this.genernalDao.setTransaction(isTransaction);
	}

	@Override
	public void commit() throws SQLException {
		// TODO Auto-generated method stub
		this.genernalDao.commit();
	}

	@Override
	public void rollback() throws SQLException {
		// TODO Auto-generated method stub
		this.genernalDao.rollback();
	}

}
