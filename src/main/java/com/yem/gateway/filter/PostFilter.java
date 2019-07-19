package com.yem.gateway.filter;
/*package com.yem.gateway.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.yem.constant.Constants;
import com.yem.utils.FilterCodeUtils;
import com.yem.utils.JsonUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PostInterceptor extends ZuulFilter{

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		try {
			RequestContext context = RequestContext.getCurrentContext();
			ZuulException exception = this.findZuulException(context.getThrowable());

			log.error("进入PostFilter异常拦截", exception);

			HttpServletResponse response = context.getResponse();
			response.setContentType("application/json; charset=utf8");
			response.setStatus(exception.nStatusCode);

			Map<String,String> map = new HashMap<>(3);
			map.put("code", Constants.RESP_BIZ_ERR_CODE);
			map.put("message", exception.getMessage());

			JsonUtils.sendJsonMessage(response,map);
		} catch (Exception var5) {

			ReflectionUtils.rethrowRuntimeException(var5);
		}

		return null;
	}

	@Override
	public String filterType() {
		return FilterCodeUtils.POST_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterCodeUtils.POST_ORDER;
	}

	private ZuulException findZuulException(Throwable throwable) {
		if (ZuulRuntimeException.class.isInstance(throwable.getCause())) {
			return (ZuulException)throwable.getCause().getCause();
		} else if (ZuulException.class.isInstance(throwable.getCause())) {
			return (ZuulException)throwable.getCause();
		}
		else {
			return ZuulException.class.isInstance(throwable) ? (ZuulException)throwable : new ZuulException(throwable, 500, (String)null);
		}
	}
}
*/