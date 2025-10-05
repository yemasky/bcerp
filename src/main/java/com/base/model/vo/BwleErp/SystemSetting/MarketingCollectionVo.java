package com.base.model.vo.BwleErp.SystemSetting;

import com.base.model.entity.BwleErp.SystemSetting.BaseAbstract.MarketingCollectionBaseAbstract;

import core.custom_interface.Column;
import core.custom_interface.Table;
@Table(name = "marketing_collection", isAnnotationField = true)
public class MarketingCollectionVo extends MarketingCollectionBaseAbstract {
	@Column(name = "collection_id", primary_key = true, auto_increment = true)
	private String collection_id;

	public String getCollection_id() {
		return collection_id;
	}

	public void setCollection_id(String collection_id) {
		this.collection_id = collection_id;
	}
}
