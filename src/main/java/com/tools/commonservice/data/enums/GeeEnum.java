package com.tools.commonservice.data.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum GeeEnum implements IEnum<Integer> {
    GEE_ENUM(1,"GEE"),
    PINK_ENUM(2,"PINK"),
    ROCK_STAR_ENUM(3,"ROCK"),
    ;

    private Integer value;
    private String name;

    @JsonCreator
    public static GeeEnum from(String value) {
        for(GeeEnum type : GeeEnum.values()) {
            if(type.toString().equals(value)) {
                return type;
            }
        }
        try {
            Integer intValue = Integer.parseInt(value);
            for (GeeEnum type : GeeEnum.values()) {
                if (type.getValue() == intValue) {
                    return type;
                }
            }
        } catch (NumberFormatException ignored) {

        }

        return null;
    }
}
