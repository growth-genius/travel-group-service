package com.gg.tgather.travelgroupservice.modules.group.enums;

import com.gg.tgather.commonservice.enums.EnumMapperType;

public enum GroupJoinStatus implements EnumMapperType {
    ALL("전체", false),
    APPROVED("승인", true),
    NO_APPROVED("미승인", false),
    ;

    private GroupJoinStatus(String status, boolean active) {
        this.status = status;
        this.active = active;
    }

    private final String status;

    private final boolean active;


    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public String getTitle() {
        return this.status;
    }

    @Override
    public boolean isDefault() {
        return this.active;
    }
}
