package com.tools.commonservice.filter;

import com.tools.commonservice.config.AccessConfig;
import com.tools.commonservice.config.RedisService;
import com.tools.commonservice.data.constant.ConstantsKey;
import com.tools.commonservice.exception.ApiException;
import com.tools.commonservice.exception.ConstantsError;
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
import java.util.*;

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
//                    String token = request.getHeader(ConstantsKey.TOKEN_HEADER);
//                    if(token == null) throw new ApiException(ConstantsError.ERROR_NOT_LOGIN);
//                    String userId = JWTUtil.getUserId(token);
//                    if(userId == null) throw new ApiException(ConstantsError.ERROR_NOT_LOGIN);
//
//                    String tokenInRedis = redisService.get(userId);
//                    if(tokenInRedis != null && token.equals(tokenInRedis)) {
//                        redisService.set(userId, token,30*60);
//                    } else {
//                        throw new ApiException(ConstantsError.ERROR_NOT_LOGIN);
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
                    //ErrorController返回信息
                    //当请求的路径不存在时, eg: login/notExistUrl 提示 404:未找到访问的接口
                    chain.doFilter(request, response);
                    return;
                }
            }
        }

        if (accessConfig.getAuthList() != null) {
            for (String openUrlPattern : accessConfig.getAuthList()) {
                if (this.pathMatcher.match(openUrlPattern, request.getServletPath())) {
                    if (checkHeaderAuth(request, accessConfig.getAuthInfo().getUsername(), accessConfig.getAuthInfo().getPassword())) {
                        chain.doFilter(request, response);
                        return;
                    } else {
                        log.info("req uri " + request.getServletPath() + " auth empty ");
                        throw new ApiException(ConstantsError.ERROR_NOT_LOGIN);
                    }
                }
            }
        }

        /*
        ErrorController返回信息
        当未登陆用户故意请求一个不存在的路径时, 提示 401:用户未登陆
        登陆用户请求不存在的路径时, 提示 404:未找到访问的接口
         */
        String token = request.getHeader(ConstantsKey.TOKEN_HEADER);
        if(token == null) {
            UserContextUtil.setCurrentError(new ApiException(ConstantsError.ERROR_NOT_LOGIN));
        }

        String agent = JWTUtil.getKey(token, ConstantsKey.AGENT);
        if(agent == null || !agent.equals(request.getHeader(ConstantsKey.HTTP_REQUEST_HEADER_USER_AGENT))) {
            UserContextUtil.setCurrentError(new ApiException(ConstantsError.ERROR_NOT_LOGIN));
        }

        String userId = JWTUtil.getKey(token, ConstantsKey.USER_ID);
        if(userId == null) {
            UserContextUtil.setCurrentError(new ApiException(ConstantsError.ERROR_NOT_LOGIN));
        }

        String tokenInRedis = redisService.get(userId);
        if(tokenInRedis != null && token.equals(tokenInRedis)) {
            redisService.set(userId, token,30*60);
        } else {
            UserContextUtil.setCurrentError(new ApiException(ConstantsError.ERROR_NOT_LOGIN));
        }

        UserContextUtil.setCurrentUser(userId);

        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        ParameterRequestWrapper wrapper = new ParameterRequestWrapper(request, map);

        chain.doFilter(wrapper, response);

        UserContextUtil.cleanThreadLocal();
//        throw new ApiException(ErrorCode.paramError("无效的访问请求"));


    }

    private boolean checkHeaderAuth(HttpServletRequest request, String username, String password) {
        String auth = request.getHeader("Authorization");
        if ((auth != null) && (auth.length() > 6)) {
            String decodedAuth = getFromBASE64(auth.substring(6));

            String[] values = decodedAuth.split(":");
            return values.length == 2 && values[0].equals(username) && values[1].equals(password);
        } else {
            return false;
        }
    }

    private String getFromBASE64(String s) {
        if (s == null)
            return null;
        try {
            byte[] b = Base64.getDecoder().decode(s);
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }


    class ParameterRequestWrapper extends HttpServletRequestWrapper {

        private Map<String, String[]> params = new HashMap<>();
        public ParameterRequestWrapper(HttpServletRequest request) {
            super(request);
            //将参数表，赋予给当前的Map以便于持有request中的参数
            this.params.putAll(request.getParameterMap());
        }

        public ParameterRequestWrapper(HttpServletRequest request, Map<String, Object> extendParams) {
            this(request);
            //这里将扩展参数写入参数表
            addAllParameters(extendParams);
        }

        /**
         * 在获取所有的参数名,必须重写此方法，否则对象中参数值映射不上
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
