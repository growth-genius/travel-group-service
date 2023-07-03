package com.gg.tgather.travelgroupservice.modules.group.service;

import com.gg.tgather.commonservice.advice.exceptions.OmittedRequireFieldException;
import com.gg.tgather.commonservice.annotation.BaseServiceAnnotation;
import com.gg.tgather.commonservice.enums.EnumMapperValue;
import com.gg.tgather.commonservice.enums.TravelTheme;
import com.gg.tgather.commonservice.security.JwtAuthentication;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupDto;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupRegisterInitDto;
import com.gg.tgather.travelgroupservice.modules.group.dto.TravelGroupWithPageable;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroup;
import com.gg.tgather.travelgroupservice.modules.group.entity.TravelGroupMember;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupModifyForm;
import com.gg.tgather.travelgroupservice.modules.group.form.TravelGroupSaveForm;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupMemberRepository;
import com.gg.tgather.travelgroupservice.modules.group.repository.TravelGroupRepository;
import com.gg.tgather.travelgroupservice.modules.group.vo.TravelGroupSearchVo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

/**
 * 여행그룹 CRUD 서비스
 *
 * @author joyeji
 * @since 2023.06.04
 */
@Slf4j
@BaseServiceAnnotation
@RequiredArgsConstructor
public class TravelGroupService {

    private final TravelGroupRepository travelGroupRepository;

    private final TravelGroupMemberRepository travelGroupMemberRepository;

    /**
     * 여행그룹 생성
     *
     * @param travelGroupSaveForm travelGroup 입력폼
     * @param authentication      인증정보
     * @return travelGroupDto travelGroup 생성 결과값
     */
    public TravelGroupDto createTravelGroup(TravelGroupSaveForm travelGroupSaveForm, JwtAuthentication authentication) {
        validTravelGroup(travelGroupSaveForm.getGroupName());
        TravelGroup travelGroup = TravelGroup.from(travelGroupSaveForm);
        travelGroupRepository.save(travelGroup);
        TravelGroupMember travelGroupMember = TravelGroupMember.createTravelGroupLeader(travelGroup, authentication.accountId(),
            travelGroupSaveForm.getNickname(), travelGroupSaveForm.getProfileImage());
        travelGroupMemberRepository.save(travelGroupMember);
        return TravelGroupDto.from(travelGroup);
    }

    /**
     * 여행그룹명 유효여부 확인
     *
     * @param travelGroupName 여행그룹명
     */
    private void validTravelGroup(String travelGroupName) {
        travelGroupRepository.findByGroupName(travelGroupName).ifPresent(m -> {
            throw new OmittedRequireFieldException("동일한 여행그룹명이 있습니다.");
        });
    }

    /**
     * 여행그룹 검색하기
     *
     * @param travelThemes travelGroup 검색 폼
     * @return TravelGroupDto travelGroup 검색 결과
     */
    public List<TravelGroupDto> findTravelGroupByTheme(Set<TravelTheme> travelThemes) {
        List<TravelGroup> travelGroups = travelGroupRepository.searchTravelGroupAllByTravelThemes(travelThemes);
        return travelGroups.stream().map(TravelGroupDto::from).toList();
    }

    /**
     * 여행그룹 정보 수정하기
     *
     * @param travelGroupModifyForm travelGroup 수정 폼
     * @param authentication        인증정보
     * @return TravelGroupDto travelGroup 수정 결과
     */
    public TravelGroupDto modifyTravelGroup(String travelGroupId, TravelGroupModifyForm travelGroupModifyForm, JwtAuthentication authentication) {
        TravelGroup travelGroup = travelGroupRepository.searchByTravelGroupAndLeader(travelGroupId, authentication.accountId())
            .orElseThrow(() -> new OmittedRequireFieldException("여행그룹명을 찾을 수 없습니다."));
        validTravelGroupWithoutOwn(travelGroupModifyForm.getGroupName(), travelGroup.getTravelGroupId());
        travelGroup.modifyTravelGroup(travelGroupModifyForm);
        return TravelGroupDto.from(travelGroup);
    }

    /**
     * 본인 그룹을 제외한 그룹명 있는지 확인
     *
     * @param travelGroupName 여행 그룹명
     * @param travelGroupId   고유 그룹 id
     */
    private void validTravelGroupWithoutOwn(String travelGroupName, String travelGroupId) {
        travelGroupRepository.searchByTravelGroupNameWithoutOwn(travelGroupName, travelGroupId).ifPresent(m -> {
            throw new OmittedRequireFieldException("동일한 여행그룹명이 있습니다.");
        });
    }

    /**
     * 여행그룹 삭제
     *
     * @param travelGroupId  여행 그룹명
     * @param authentication 인증정보
     * @return boolean 삭제 여부
     */
    public Boolean deleteTravelGroup(String travelGroupId, JwtAuthentication authentication) {
        TravelGroup travelGroup = travelGroupRepository.searchByTravelGroupAndLeader(travelGroupId, authentication.accountId())
            .orElseThrow(() -> new OmittedRequireFieldException("여행그룹명을 찾을 수 없습니다."));
        travelGroup.deleteTravelGroup();
        return true;
    }

    /**
     * 사용자 별 여행그룹 조회
     *
     * @param accountId 사용자 아이디
     * @return List<TravelGroupDto>
     */
    public List<TravelGroupDto> findAllTravelGroupByOwn(String accountId) {
        TravelGroupSearch travelGroupSearch = getTravelGroupSearch(accountId);

        return makeTravelGroupDto(travelGroupSearch);
    }

    private static List<TravelGroupDto> makeTravelGroupDto(TravelGroupSearch travelGroupSearch) {
        List<TravelGroupDto> travelGroupDtoList = new ArrayList<>();
        for (String travelGroupId : travelGroupSearch.travelGroupIds()) {
            TravelGroupDto travelGroupDto = TravelGroupDto.of(travelGroupId);
            Map<TravelTheme, List<TravelGroupSearchVo>> themeListMap = travelGroupSearch.travelGroupSearchVoList().stream()
                .filter(t -> t.getTravelGroupId().equals(travelGroupId)).collect(Collectors.groupingBy(TravelGroupSearchVo::getTravelTheme));
            for (Entry<TravelTheme, List<TravelGroupSearchVo>> travelThemeListEntry : themeListMap.entrySet()) {
                travelGroupDto.addTravelTheme(travelThemeListEntry.getKey());
                for (List<TravelGroupSearchVo> members : themeListMap.values()) {
                    for (TravelGroupSearchVo member : members) {
                        travelGroupDto.addMember(member);
                    }
                }
            }
            travelGroupDtoList.add(travelGroupDto);
        }
        return travelGroupDtoList;
    }

    @NotNull
    private TravelGroupSearch getTravelGroupSearch(String accountId) {
        List<TravelGroupSearchVo> travelGroupSearchVoList = travelGroupRepository.searchTravelGroupAllByMe(accountId);
        List<String> travelGroupIds = travelGroupSearchVoList.stream().distinct().map(TravelGroupSearchVo::getTravelGroupId).toList();
        return TravelGroupSearch.of(travelGroupSearchVoList, travelGroupIds);
    }

    public TravelGroupRegisterInitDto findRegisterInitData() {
        return TravelGroupRegisterInitDto.builder().travelThemes(Arrays.stream(TravelTheme.values()).map(EnumMapperValue::new).toList()).build();
    }

    /**
     * 여행목록 전체 조회
     *
     * @param pageable 페이지
     * @return 여행그룹 전체 결과값
     */
    @Transactional(readOnly = true)
    public TravelGroupWithPageable findAllTravelGroupsWithPageable(Pageable pageable) {
        Page<TravelGroup> travelGroupList = travelGroupRepository.findAll(pageable);
        List<TravelGroupDto> travelGroupDtoList = travelGroupList.stream().map(TravelGroupDto::fromLeader).toList();
        return TravelGroupWithPageable.of(travelGroupDtoList, pageable.getPageNumber(), pageable.getPageSize(), travelGroupList.getTotalElements(),
            travelGroupList.getTotalPages(), travelGroupList.isLast());
    }

    private record TravelGroupSearch(List<TravelGroupSearchVo> travelGroupSearchVoList, List<String> travelGroupIds) {

        public static TravelGroupSearch of(List<TravelGroupSearchVo> travelGroupSearchVoList, List<String> travelGroupIds) {
            return new TravelGroupSearch(travelGroupSearchVoList, travelGroupIds);
        }
    }

}
