package com.yem.gateway.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yem.constant.Constants;
import com.yem.response.BaseMsgResponse;

@ControllerAdvice
@RequestMapping("/yem")
public class ErrorController {

	@PostMapping("/error")
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public BaseMsgResponse error(Exception e) {
		BaseMsgResponse resp = new BaseMsgResponse();
		resp.setRespCode(Constants.RESP_BIZ_ERR_CODE);
		resp.setRespMsg(e.getMessage());
		return resp;
		
	}
}
