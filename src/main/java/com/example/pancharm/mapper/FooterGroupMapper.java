package com.example.pancharm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.pancharm.dto.request.footer.FooterGroupCreationRequest;
import com.example.pancharm.dto.response.footer.FooterGroupResponse;
import com.example.pancharm.entity.FooterGroups;

@Mapper(componentModel = "spring", uses = FooterGroups.class)
public interface FooterGroupMapper {
    FooterGroups toObject(FooterGroupCreationRequest request);

    FooterGroupResponse toObjectResponse(FooterGroups footerGroups);

    void updateObject(FooterGroupCreationRequest request, @MappingTarget FooterGroups footerGroups);
}
