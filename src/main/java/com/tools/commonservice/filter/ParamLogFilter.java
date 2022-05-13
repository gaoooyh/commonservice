package com.tools.commonservice.filter;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.tools.commonservice.exception.ApiException;
import com.tools.commonservice.exception.ErrorCode;
import com.tools.commonservice.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 打印参数
 */
public class ParamLogFilter extends GenericFilterBean {
    static Logger log = LoggerFactory.getLogger(ParamLogFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;

        //这里增加流量控制并不规范
        try (Entry ignored = SphU.entry("FlowLimit")) {
            log.info("RequestUri: {}, Param: {}", request.getRequestURI(), JsonUtils.write(request.getParameterMap()));
            chain.doFilter(req, res);
        } catch (BlockException ex) {
            log.error("Service Busy, RequestUri: {}, Param: {}", request.getRequestURI(), JsonUtils.write(request.getParameterMap()));
            throw new ApiException(ErrorCode.paramError("服务器繁忙, 请稍后重试"));
        }
    }
}
