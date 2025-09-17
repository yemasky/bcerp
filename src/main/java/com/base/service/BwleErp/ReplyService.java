package com.base.service.BwleErp;

import com.base.model.vo.PageVo;
import com.base.service.BaseService;

import core.jdbc.mysql.NeedEncrypt;
import core.jdbc.mysql.WhereRelation;

public interface ReplyService extends BaseService {
	PageVo getPageList(WhereRelation whereRelation, PageVo pageVo, NeedEncrypt needEncrypt) throws Exception;
}
