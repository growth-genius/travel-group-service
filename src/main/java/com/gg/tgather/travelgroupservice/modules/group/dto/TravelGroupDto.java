package com.gg.tgather.travelgroupservice.modules.group.dto;

import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.vo.TravelGroupSearchVo;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TravelGroupDto {

    /** 여행그룹 아이디 */
    private String travelGroupId;

    /** 여행그룹명 */
    private String groupName;

    /** 여행그룹 설명 */
    private String description;

    /** 여행그룹 이미지 */
    private String imageUrl;

    /** 여행테마 */
    private Set<TravelTheme> travelThemes;

    private List<TravelGroupMemberDto> travelGroupMemberList;

    /** 총참여자수 */
    private int totalMember;

    /** 나이 제한 범위 시작 */
    private int limitAgeRangeStart = 0;

    /** 나이 제한 범위 마지노선 */
    private int limitAgeRangeEnd = 0;

    public static TravelGroupDto from(TravelGroup travelGroup) {
        TravelGroupDto travelGroupDTO = new TravelGroupDto();
        travelGroupDTO.travelGroupId = travelGroup.getTravelGroupId();
        travelGroupDTO.groupName = travelGroup.getGroupName();
        travelGroupDTO.travelThemes = new HashSet<>(travelGroup.getTravelThemes());
        travelGroupDTO.totalMember = travelGroup.getTravelGroupMemberList().size();
        travelGroupDTO.description = travelGroup.getDescription();
        travelGroupDTO.imageUrl = travelGroup.getImageUrl();
        travelGroupDTO.limitAgeRangeStart = travelGroup.getLimitAgeRangeStart();
        travelGroupDTO.limitAgeRangeEnd = travelGroup.getLimitAgeRangeEnd();
        return travelGroupDTO;
    }

    public static TravelGroupDto of(String travelGroupId) {
        TravelGroupDto travelGroupDto = new TravelGroupDto();
        travelGroupDto.travelGroupId = travelGroupId;
        travelGroupDto.travelThemes = new HashSet<>();
        travelGroupDto.travelGroupMemberList = new ArrayList<>();
        return travelGroupDto;
    }

    public void addTravelTheme(TravelTheme travelTheme) {
        this.travelThemes.add(travelTheme);
    }

    public void addMember(TravelGroupSearchVo member) {
        this.travelGroupMemberList.add(TravelGroupMemberDto.from(member));
        this.totalMember++;
    }
}
