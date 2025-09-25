package com.BwleErp.controller.SystemSetting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.base.controller.AbstractAction;
import com.base.model.entity.BwleErp.company.CompanySector;
import com.base.service.GeneralService;
import com.base.type.CheckedStatus;

import core.jdbc.mysql.WhereRelation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class EmployeeAction extends AbstractAction {
	@Autowired
	private GeneralService generalService;
	
	@Override
	public CheckedStatus check(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return this.status;
	}

	@Override
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		String method = (String) request.getAttribute("method");
		if(method == null) method = "";
		//
		switch (method) {
		case "getSector":
			this.doGetSector(request, response);
			break;	
		default:
			this.doDefault(request, response);
			break;
		}		
	}

	@Override
	public void release(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void rollback(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public void doGetSector(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		WhereRelation whereRelation = new WhereRelation();
		whereRelation.EQ("sector_valid", 1).setTable_clazz(CompanySector.class);
		List<CompanySector> companySectorList = generalService.getEntityList(whereRelation);
		this.success.setItem("companySectorList", companySectorList); 
	}
}
