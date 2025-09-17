package com.base.service.BwleErp;

import java.sql.SQLException;

import com.base.model.vo.PageVo;
import com.base.service.BaseService;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;

public interface NewsService extends BaseService  {
	
	PageVo getPageList(WhereRelation whereRelation, PageVo pageVo, NeedEncrypt needEncrypt) throws Exception;
	
	int updateEntity(Object object, int member_id, int news_id) throws Exception;
	
	int increase(WhereRelation whereRelation) throws SQLException;
}
