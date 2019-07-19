/*package com.yem.gateway.filter;


import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.cloud.netflix.zuul.util.ZuulRuntimeException;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.yem.utils.FilterCodeUtils;
import com.yem.utils.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ErrorFilter extends ZuulFilter{

	private final String errorPath = "/yem/error";
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		try {
			RequestContext ctx = RequestContext.getCurrentContext();
			ZuulException exception = findZuulException(ctx.getThrowable());
			HttpServletRequest request = ctx.getRequest();

			request.setAttribute("javax.servlet.error.status_code", exception.nStatusCode);

			log.warn("Error during filtering", exception);
			request.setAttribute("javax.servlet.error.exception", exception);

			if (StringUtil.hasText(exception.errorCause)) {
				request.setAttribute("javax.servlet.error.message", exception.errorCause);
			}

			RequestDispatcher dispatcher = request.getRequestDispatcher(this.errorPath);
			
			if (dispatcher != null) {
				// ctx.set(SEND_ERROR_FILTER_RAN, true);
//				if (!ctx.getResponse().isCommitted()) {
					
					ctx.setResponseStatusCode(exception.nStatusCode);
					dispatcher.forward(request, ctx.getResponse());
//				}
			}
		}
		catch (Exception ex) {
			ReflectionUtils.rethrowRuntimeException(ex);
		}
		return null;
	}

	public ZuulException findZuulException(Throwable throwable) {
		if (throwable.getCause() instanceof ZuulRuntimeException) {
			// this was a failure initiated by one of the local filters
			return (ZuulException) throwable.getCause().getCause();
		}

		if (throwable.getCause() instanceof ZuulException) {
			// wrapped zuul exception
			return (ZuulException) throwable.getCause();
		}

		if (throwable instanceof ZuulException) {
			// exception thrown by zuul lifecycle
			return (ZuulException) throwable;
		}

		// fallback, should never get here
		return new ZuulException(throwable, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, null);
	}

	@Override
	public String filterType() {
		return FilterCodeUtils.ERROR_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterCodeUtils.ERROR_ORDER;
	}
}
*/