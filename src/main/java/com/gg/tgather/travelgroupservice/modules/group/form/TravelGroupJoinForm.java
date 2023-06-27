package com.gg.tgather.travelgroupservice.modules.group.form;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TravelGroupJoinForm {

    /** 닉네임 */
    private String nickname;
    /** 프로필 이미지 */
    private String profileImage;
}
