package com.BwleErp.controller.SystemSetting;

import java.sql.SQLException;

import com.base.model.dto.BwleErp.AuditingExaminesDTO;
import com.base.model.entity.BwleErp.SystemSetting.CurrencyRate;
import com.base.service.GeneralService;

import core.jdbc.mysql.WhereRelation;

public class AuditingExaminesAction {
	private GeneralService generalService;
	
	protected AuditingExaminesAction(GeneralService generalService) {
		this.generalService = generalService;
	}
	
	public int CurrencyRate(AuditingExaminesDTO update) throws SQLException {
		WhereRelation whereRelation = new WhereRelation();//取得所有员工
		whereRelation.EQ("currency_id", update.getUpdate_id());
		if(update.getStatus() != null) whereRelation.setUpdate("auditing_state", update.getStatus());
		if(update.getAuditing_id() != null) whereRelation.setUpdate("auditing_id", update.getAuditing_id());
		if(update.getAuditing_date() != null) whereRelation.setUpdate("auditing_date", update.getAuditing_date());
		if(update.getAuditing_employee_id() != null) whereRelation.setUpdate("auditing_employee_id", update.getAuditing_employee_id());
		whereRelation.setTable_clazz(CurrencyRate.class);
		return this.generalService.update(whereRelation);
	}
}
