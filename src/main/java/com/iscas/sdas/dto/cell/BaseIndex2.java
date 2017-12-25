package com.iscas.sdas.dto.cell;
/**
 * 小区指标-月份模型
 * @author Administrator
 *
 */
public class BaseIndex2 extends Base{

	private String indeicator_code;
	
	private String cell_code;
	
	private Integer model_type;
	
	private String yyyyMM;

	public String getIndeicator_code() {
		return indeicator_code;
	}

	public void setIndeicator_code(String indeicator_code) {
		this.indeicator_code = indeicator_code;
	}

	public String getCell_code() {
		return cell_code;
	}

	public void setCell_code(String cell_code) {
		this.cell_code = cell_code;
	}

	public Integer getModel_type() {
		return model_type;
	}

	public void setModel_type(Integer model_type) {
		this.model_type = model_type;
	}

	public String getYyyyMM() {
		return yyyyMM;
	}

	public void setYyyyMM(String yyyyMM) {
		this.yyyyMM = yyyyMM;
	}

	
}
