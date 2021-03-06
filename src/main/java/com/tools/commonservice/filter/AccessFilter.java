package com.tools.commonservice.filter;

import com.tools.commonservice.config.AccessConfig;
import com.tools.commonservice.config.RedisService;
import com.tools.commonservice.exception.ApiException;
import com.tools.commonservice.exception.Constants;
import com.tools.commonservice.exception.ErrorCode;
import com.tools.commonservice.util.JWTUtil;
import com.tools.commonservice.util.UserContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class AccessFilter extends GenericFilterBean {
    static Logger log = LoggerFactory.getLogger(AccessFilter.class);

    private AccessConfig accessConfig;
    private RedisService redisService;


    public AccessFilter(AccessConfig accessConfig, RedisService redisService) {
        this.redisService = redisService;
        this.accessConfig = accessConfig;
    }

    private AntPathMatcher pathMatcher = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getMethod().equalsIgnoreCase("options")) {
            chain.doFilter(request, response);
            return;
        }
//        if (accessConfig.getWhiteList() != null) {
//            for (String whiteUrlPattern : accessConfig.getWhiteList()) {
//                if (this.pathMatcher.match(whiteUrlPattern, request.getServletPath())) {
//                    String token = request.getHeader("MyToken");
//                    if(token == null) throw new ApiException(Constants.ERROR_NOT_LOGIN);
//                    String userId = JWTUtil.getUserId(token);
//                    if(userId == null) throw new ApiException(Constants.ERROR_NOT_LOGIN);
//
//                    String tokenInRedis = redisService.get(userId);
//                    if(tokenInRedis != null && token.equals(tokenInRedis)) {
//                        redisService.set(userId, token,30*60);
//                    } else {
//                        throw new ApiException(Constants.ERROR_NOT_LOGIN);
//                    }
//
//                    UserContextUtil.setCurrentUser(userId);
//
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("userId", userId);
//                    ParameterRequestWrapper wrapper = new ParameterRequestWrapper(request, map);
//
//                    chain.doFilter(wrapper, response);
//                    return;
//                }
//            }
//        }

        if (accessConfig.getOpenList() != null) {
            for (String openUrlPattern : accessConfig.getOpenList()) {
                if (this.pathMatcher.match(openUrlPattern, request.getServletPath())) {
                    //ErrorController????????????
                    //??????????????????????????????, eg: login/notExistUrl ?????? 404:????????????????????????
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        /*
        ErrorController????????????
        ?????????????????????????????????????????????????????????, ?????? 401:???????????????
        ???????????????????????????????????????, ?????? 404:????????????????????????
         */
        String token = request.getHeader("MyToken");
        if(token == null) {
            UserContextUtil.setCurrentError(new ApiException(Constants.ERROR_NOT_LOGIN));
        }
        String userId = JWTUtil.getUserId(token);
        if(userId == null) {
            UserContextUtil.setCurrentError(new ApiException(Constants.ERROR_NOT_LOGIN));
        }

        String tokenInRedis = redisService.get(userId);
        if(tokenInRedis != null && token.equals(tokenInRedis)) {
            redisService.set(userId, token,30*60);
        } else {
            UserContextUtil.setCurrentError(new ApiException(Constants.ERROR_NOT_LOGIN));
        }

        UserContextUtil.setCurrentUser(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        ParameterRequestWrapper wrapper = new ParameterRequestWrapper(request, map);

        chain.doFilter(wrapper, response);

//        throw new ApiException(ErrorCode.paramError("?????????????????????"));


    }


    class ParameterRequestWrapper extends HttpServletRequestWrapper {

        private Map<String, String[]> params = new HashMap<>();
        public ParameterRequestWrapper(HttpServletRequest request) {
            super(request);
            //?????????????????????????????????Map???????????????request????????????
            this.params.putAll(request.getParameterMap());
        }

        public ParameterRequestWrapper(HttpServletRequest request, Map<String, Object> extendParams) {
            this(request);
            //????????????????????????????????????
            addAllParameters(extendParams);
        }

        /**
         * ???????????????????????????,????????????????????????????????????????????????????????????
         *
         * @return
         */
        @Override
        public Enumeration<String> getParameterNames() {
            return new Vector(params.keySet()).elements();
        }

        @Override
        public String getParameter(String name) {
            String[] values = params.get(name);
            if (values == null || values.length == 0) {
                return null;
            }
            return values[0];
        }

        @Override
        public String[] getParameterValues(String name) {
            String[] values = params.get(name);
            if (values == null || values.length == 0) {
                return null;
            }
            return values;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return params;
        }


        public void addAllParameters(Map<String, Object> otherParams) {
            for (Map.Entry<String, Object> entry : otherParams.entrySet()) {
                addParameter(entry.getKey(), entry.getValue());
            }
        }

        public void addParameter(String name, Object value) {
            if (value != null) {
                if (value instanceof String[]) {
                    params.put(name, (String[]) value);
                } else if (value instanceof String) {
                    params.put(name, new String[]{(String) value});
                } else {
                    params.put(name, new String[]{String.valueOf(value)});
                }
            }
        }
    }
}
