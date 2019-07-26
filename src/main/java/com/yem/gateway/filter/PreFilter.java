package com.yem.gateway.filter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.yem.common.BaseMsg;
import com.yem.common.BaseMsgHeader;
import com.yem.constant.ApiConstant;
import com.yem.exception.BaseException;
import com.yem.exception.ParamException;
import com.yem.utils.FilterCodeUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PreFilter extends ZuulFilter{

	@Override
	public boolean shouldFilter() {
		RequestContext context = RequestContext.getCurrentContext();
		String uri = context.getRequest().getRequestURI().toString();
		
		log.info("gatway------是否拦截请求：{}",uri);
		if (ApiConstant.TOKEN_URI.equals(uri) || ApiConstant.AUTH_URI.equals(uri)) {
			return false;
		}
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		try {
			log.info("gatway------PreFilter 预处理请求");
			
			RequestContext context = RequestContext.getCurrentContext();
			BaseMsg baseMsg = new BaseMsg();
			
			JSONObject json = parseHanlderRequest(preHandlerRequest(context.getRequest()));
			checkBaseMsg(json, baseMsg);

			log.info("gatway------PreFilter 预处理请求，请求信息：{}", JSONObject.toJSONString(baseMsg));
			
			context.addZuulRequestHeader(ApiConstant.ROUTE_KEY, JSONObject.toJSONString(baseMsg.getRequestBody()));
			
		} catch (Exception var5) {

			ReflectionUtils.rethrowRuntimeException(var5);
		}

		return null;
	}

	@Override
	public String filterType() {
		return FilterCodeUtils.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterCodeUtils.PRE_ORDER;
	}

	/**
	 * 预处理post请求数据流
	 * (这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author <a href="mailto:hbszguchuang@163.com">guchuang</a>
	 * @param request
	 * @return
	 * @throws BaseException
	 * @since JDK 1.8
	 */
	public String preHandlerRequest(HttpServletRequest request) throws BaseException{
		StringBuilder sb = new StringBuilder();
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			
			br.close();
		} catch (IOException e) {
			log.error("参数解析失败");
			throw new ParamException("参数解析失败");
		}
		return sb.toString();
	}
	
	/**
	 * 转换成json并校验报文合法性
	 * (这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author <a href="mailto:hbszguchuang@163.com">guchuang</a>
	 * @param json
	 * @return
	 * @throws BaseException
	 * @since JDK 1.8
	 */
	private JSONObject parseHanlderRequest(String json) throws BaseException{
		JSONObject param;
		try {
			
			param = JSONObject.parseObject(json);
		} catch (Exception e) {
			log.error("参数转换异常");
			throw new ParamException(e.getMessage());
		}
		
		return param;
	}
	
	/**
	 * 校验请求信息
	 * (这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法的使用方法 – 可选).<br/>
	 * TODO(这里描述这个方法的注意事项 – 可选).<br/>
	 *
	 * @author <a href="mailto:hbszguchuang@163.com">guchuang</a>
	 * @param json
	 * @param msg
	 * @throws BaseException
	 * @since JDK 1.8
	 */
	private void checkBaseMsg(JSONObject json, BaseMsg msg) throws BaseException {
		if (json == null) {
			throw new ParamException("请求信息缺失");
		}
		if (!json.containsKey(ApiConstant.API_MSG_HEADER)) {
			throw new ParamException("请求头缺失");
		}
		if (!json.containsKey(ApiConstant.API_MSG_BODY)) {
			throw new ParamException("请求体缺失");
		}

		JSONObject body = json.getJSONObject(ApiConstant.API_MSG_BODY);
		msg.setRequestBody(body);
		
		JSONObject header = json.getJSONObject(ApiConstant.API_MSG_HEADER);
		if (!header.containsKey(ApiConstant.API_MSG_SOURCE)) {
			throw new ParamException("请求来源缺失");
		}
		if (!header.containsKey(ApiConstant.API_MSG_TIME)) {
			throw new ParamException("请求时间缺失");
		}
		if (!header.containsKey(ApiConstant.API_MSG_ACT)) {
			throw new ParamException("请求动作缺失");
		}
		msg.setRequestHeader(JSONObject.toJavaObject(header, BaseMsgHeader.class));

	}
}
