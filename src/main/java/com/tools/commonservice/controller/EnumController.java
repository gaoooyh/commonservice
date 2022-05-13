package com.tools.commonservice.controller;

import com.tools.commonservice.common.HttpResult;
import com.tools.commonservice.data.enums.GeeEnum;
import com.tools.commonservice.annotation.ReqParam;
import com.tools.commonservice.util.JsonUtils;
import io.swagger.annotations.*;
import lombok.Data;
import org.springframework.web.bind.annotation.*;
import javax.validation.constraints.NotNull;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * 测试Enum类型参数获取
 * 使用Spring默认解析和使用自定义解析获取Enum类型参数的对比
 */
@RestController
@Api("Enum测试")
@RequestMapping("enum")
public class EnumController {

    /**
     * get1接口通过 @ReqParam 可以调用枚举类型的 JsonCreator  {@link GeeEnum#from(String)}
     * localhost:8080/enum/get1?geeEnum1=1&longValue=100&geeEnum2=GEE_ENUM&geeEnumList=1&geeEnumList=PINK_ENUM
      */
    @ApiOperation("测试get请求传入枚举, 使用@ReqParam")
    @GetMapping("get1")
    public HttpResult<DParam> get1(@ReqParam DParam param) {
        System.out.println(param);
        return HttpResult.success().setData(param);
    }

    /**
     * get2 通过springboot对参数的默认解析 (@RequestParam) 只能用枚举的toString eg: GEE_ENUM, 使用value会报错
     *    localhost:8080/enum/get1?geeEnum1=1&longValue=100&geeEnum2=GEE_ENUM&geeEnumList=1&geeEnumList=PINK_ENUM
      */
    @ApiOperation("测试get请求传入枚举")
    @GetMapping("get2")
    public HttpResult<DParam> get2(DParam param) {
        System.out.println(param);
        return HttpResult.success().setData(param);
    }

    @ApiOperation("测试JsonEnum, 直接用了固定数据")
    @GetMapping("test")
    public HttpResult<EnumData> testEnum( ) {

        String str  = "{\n" +
                "    \"geeEnumList\" : [\n" +
                "        \"GEE_ENUM\",\"3\"\n" +
                "    ]\n" +
                "}";

        EnumData data = JsonUtils.read(str, EnumData.class);
        System.out.println(data);

        return HttpResult.success().setData(data);
    }

    @Data
    static class EnumData{
        List<GeeEnum> geeEnumList;
    }

    @Data
    static class DParam {
        @ApiParam(value ="GeeEnum", required = true )
        @NotNull(message = "GeeEnum1不能为空")
        GeeEnum geeEnum1;

        @ApiParam(value ="GeeEnum", required = true )
        @NotNull(message = "GeeEnum2不能为空")
        GeeEnum geeEnum2;

        @ApiParam(value ="这是一个long", required = true )
        @NotNull(message = "longValue不能为空")
        Long longValue;
        @ApiParam(value ="枚举GeeEnum list", required = true )
        @NotNull(message = "geeEnumList不能为空")
        List<GeeEnum> geeEnumList;
    }




}
