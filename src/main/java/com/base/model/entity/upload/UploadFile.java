package com.base.model.entity.upload;

import com.base.model.dto.FileDTO;

import core.custom_interface.Column;
import core.custom_interface.Table;

@Table(name = "upload_file", isAnnotationField = false)
public class UploadFile extends FileDTO {
	@Column(name = "file_id", primary_key = true, auto_increment = true)
	private Integer file_id;
	private Integer category_id;
	private Integer file_linked_id;
	private Integer employee_id;
	private Integer file_valid;

	public Integer getFile_id() {
		return file_id;
	}

	public void setFile_id(int file_id) {
		this.file_id = file_id;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public Integer getFile_linked_id() {
		return file_linked_id;
	}

	public void setFile_linked_id(int file_linked_id) {
		this.file_linked_id = file_linked_id;
	}

	public Integer getEmployee_id() {
		return employee_id;
	}

	public void setEmployee_id(Integer employee_id) {
		this.employee_id = employee_id;
	}

	public Integer getFile_valid() {
		return file_valid;
	}

	public void setFile_valid(int file_valid) {
		this.file_valid = file_valid;
	}
}
