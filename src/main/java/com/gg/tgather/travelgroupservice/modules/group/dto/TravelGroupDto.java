package com.gg.tgather.travelgroupservice.modules.group.dto;

import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupRole;
import com.gg.tgather.travelgroupservice.modules.group.vo.TravelGroupSearchVo;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
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

    private List<TravelGroupMemberDto> travelGroupMemberDtoList;

    private long limitParticipantCount = 999;

    /** 총참여자수 */
    private int totalMember;

    /** 나이 제한 범위 시작 */
    private int limitAgeRangeStart = 0;

    /** 나이 제한 범위 마지노선 */
    private int limitAgeRangeEnd = 0;

    public static TravelGroupDto fromLeader(TravelGroup travelGroup) {
        Optional<TravelGroupMemberDto> optionalTravelGroupMemberDto = travelGroup.getTravelGroupMemberList().stream()
            .filter(travelGroupMember -> travelGroupMember.getTravelGroupRole() == TravelGroupRole.LEADER).map(TravelGroupMemberDto::from).findAny();

        if (optionalTravelGroupMemberDto.isEmpty()) {
            throw new OmittedRequireFieldException("여행그룹 리더를 찾을 수 없습니다.");
        }

        TravelGroupDto travelGroupDto = createTravelGroup(travelGroup);
        travelGroupDto.travelGroupMemberDtoList = List.of(optionalTravelGroupMemberDto.get());
        return travelGroupDto;
    }


    public static TravelGroupDto from(TravelGroup travelGroup) {
        TravelGroupDto travelGroupDto = createTravelGroup(travelGroup);
        travelGroupDto.travelGroupMemberDtoList = travelGroup.getTravelGroupMemberList().stream().map(TravelGroupMemberDto::from).collect(Collectors.toList());
        travelGroupDto.totalMember = travelGroup.getTravelGroupMemberList().size();
        return travelGroupDto;
    }

    private static TravelGroupDto createTravelGroup(TravelGroup travelGroup) {
        TravelGroupDto travelGroupDto = new TravelGroupDto();
        travelGroupDto.travelGroupId = travelGroup.getTravelGroupId();
        travelGroupDto.groupName = travelGroup.getGroupName();
        travelGroupDto.travelThemes = new HashSet<>(travelGroup.getTravelThemes());
        travelGroupDto.totalMember = travelGroup.getTravelGroupMemberList().size();
        travelGroupDto.description = travelGroup.getDescription();
        travelGroupDto.imageUrl = travelGroup.getImageUrl();
        travelGroupDto.limitAgeRangeStart = travelGroup.getLimitAgeRangeStart();
        travelGroupDto.limitAgeRangeEnd = travelGroup.getLimitAgeRangeEnd();
        travelGroupDto.limitParticipantCount = travelGroup.getLimitParticipantCount();
        return travelGroupDto;
    }

    public static TravelGroupDto of(String travelGroupId) {
        TravelGroupDto travelGroupDto = new TravelGroupDto();
        travelGroupDto.travelGroupId = travelGroupId;
        travelGroupDto.travelThemes = new HashSet<>();
        return travelGroupDto;
    }

    public void addTravelTheme(TravelTheme travelTheme) {
        this.travelThemes.add(travelTheme);
    }

    public void addMember(TravelGroupSearchVo member) {
        this.travelGroupMemberDtoList.add(TravelGroupMemberDto.from(member));
        this.totalMember++;
    }
}
