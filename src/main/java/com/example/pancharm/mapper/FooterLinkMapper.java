package com.example.pancharm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.example.pancharm.dto.request.footer.FooterLinkCreationRequest;
import com.example.pancharm.dto.response.footer.FooterLinkResponse;
import com.example.pancharm.entity.FooterLinks;

@Mapper(componentModel = "spring", uses = FooterLinks.class)
public interface FooterLinkMapper {
    FooterLinks toObject(FooterLinkCreationRequest request);

    FooterLinkResponse toObjectResponse(FooterLinks footerLinks);

    void updateObject(FooterLinkCreationRequest request, @MappingTarget FooterLinks footerLinks);
}
