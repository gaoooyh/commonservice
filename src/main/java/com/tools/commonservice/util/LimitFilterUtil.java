package com.tools.commonservice.util;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;

public class LimitFilterUtil {
    static Logger log = LoggerFactory.getLogger(LimitFilterUtil.class);

    /*
     * 目前是固定的key && count, 可以升级为从zk订阅获取流量限制规则, 避免频繁调整配置上线
     * 可以更进一步细分流量入口, eg: 双十一期间 增大下单流量限制, 降低退货流量限制
     */
    static {
//        LimitFilter.degradeRule("degradeRule", 100);
        //每秒100qps限制
        LimitFilterUtil.flowRule("FlowLimit", 10);
    }


    /**
     * 添加流量限制规则
     * 在{@link com.tools.commonservice.filter.ParamLogFilter} 中添加了流量限制
     */
    public static void flowRule(String name, int count) {
        //获取已经设置的限流规则
        List<FlowRule> ruleList = FlowRuleManager.getRules();
        if (ruleList == null) {
            ruleList = new ArrayList<>();
        } else {
            for(FlowRule rule : ruleList) {
                if(rule.getResource().equals(name)) {
                    log.error("Add flow rules error: Resource name already exists!");
                    return;
                }
            }
        }

        FlowRule rule = new FlowRule();
        rule.setResource(name);
        //使用qps限速
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setCount(count);

        ruleList.add(rule);
        FlowRuleManager.loadRules(ruleList);

    }

    /**
     * 按分钟内异常数量进行降级
     * @param name 对于远程服务a,b,c 分别统计
     * @param count 发生count次异常后不再请求
     */
    public static void degradeRule(String name, int count) {
        //获取已经设置的降级规则
        List<DegradeRule> ruleList = DegradeRuleManager.getRules();
        if (ruleList == null) {
            ruleList = new ArrayList<>();
        } else {
            for(DegradeRule rule : ruleList) {
                if(rule.getResource().equals(name)) {
                    log.error("Add degrade rules error: Resource name already exists!");
                    return;
                }
            }
        }

        DegradeRule rule = new DegradeRule();
        rule.setResource(name);
        //使用异常统计降级,分钟统计,滑动时间窗口
        rule.setGrade(RuleConstant.DEGRADE_GRADE_EXCEPTION_COUNT);

        rule.setCount(count);
        //降级后多久(秒) 恢复(部分恢复)
        rule.setTimeWindow(3);

        ruleList.add(rule);

        DegradeRuleManager.loadRules(ruleList);
    }

}
