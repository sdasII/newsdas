package com.iscas.sdas.dto.result;

import com.iscas.sdas.dto.common.TotalDto;

/**
 * 小区单个时刻的判别结果
 * 
 * @author dongqun 2018年1月19日上午10:02:09
 */
public class TotalResultInfoDto extends TotalDto{
	
	
	private Integer result;

	public Integer getResult() {
		return result;
	}

	public void setResult(Integer result) {
		this.result = result;
	}

}
