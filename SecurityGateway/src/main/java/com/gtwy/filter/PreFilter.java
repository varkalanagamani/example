package com.gtwy.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PreFilter extends ZuulFilter {

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		System.out.println("PreFilter.run()");
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String url = request.getRequestURI();
		System.out.println("Request url is ------ >>> " + url);

		String accessToken = request.getHeader("Authorization");
		System.out.println("accessToken :: " + accessToken);

		if (!request.getRequestURI().toString().contains("/get")) {

			if (!StringUtils.isEmpty(accessToken)) {
				System.out.println("Zuul if ------>>>");
				ctx.setSendZuulResponse(true);
				ctx.setResponseStatusCode(200);
				// Some values can be set
				ctx.set("isSuccess", true);
				return null;
			} else {
				System.out.println("Zuul else ------>>>");
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(401);
				ctx.setResponseBody("{\"result\":\"accessToken is not correct!\"}");
				// Some values can be set
				ctx.set("isSuccess", false);
				return null;
			}
		} else {
			ctx.setSendZuulResponse(true);
			ctx.setResponseStatusCode(200);
			// Some values can be set
			ctx.set("isSuccess", true);
			return null;
		}

	}

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 1;
	}
}
