package com.base.model.vo.upload;

import core.custom_interface.Table;

@Table(name = "upload_file", isAnnotationField = true)
public class UploadFileVo {
	private String file_url;
	private String file_name;
	private String file_extend;
	private String file_type;
	public String getFile_url() {
		return file_url;
	}
	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getFile_extend() {
		return file_extend;
	}
	public void setFile_extend(String file_extend) {
		this.file_extend = file_extend;
	}
	public String getFile_type() {
		return file_type;
	}
	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}
}
