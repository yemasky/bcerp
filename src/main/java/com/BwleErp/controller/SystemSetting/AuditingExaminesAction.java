package com.BwleErp.controller.SystemSetting;

import java.sql.SQLException;

import com.base.model.entity.BwleErp.SystemSetting.CurrencyRate;
import com.base.service.GeneralService;

import core.jdbc.mysql.WhereRelation;

public class AuditingExaminesAction {
	private GeneralService generalService;
	
	protected AuditingExaminesAction(GeneralService generalService) {
		this.generalService = generalService;
	}
	
	public int CurrencyRate(Integer currency_id, String auditingStatus) throws SQLException {
		WhereRelation whereRelation = new WhereRelation();//取得所有员工
		whereRelation.EQ("currency_id", currency_id).setTable_clazz(CurrencyRate.class).setUpdate("auditing_state", auditingStatus);
		return this.generalService.update(whereRelation);
	}
}
