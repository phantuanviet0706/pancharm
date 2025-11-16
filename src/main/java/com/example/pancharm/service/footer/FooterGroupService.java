package com.example.pancharm.service.footer;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.footer.FooterGroupCreationRequest;
import com.example.pancharm.dto.response.footer.FooterGroupResponse;
import com.example.pancharm.entity.FooterGroups;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.FooterGroupMapper;
import com.example.pancharm.mapper.PageMapper;
import com.example.pancharm.repository.FooterGroupRepository;
import com.example.pancharm.util.GeneralUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FooterGroupService {
    FooterGroupMapper footerGroupMapper;
    FooterGroupRepository footerGroupRepository;

    PageMapper pageMapper;
    GeneralUtil generalUtil;

    @Transactional
    public FooterGroupResponse create(FooterGroupCreationRequest request) {
        FooterGroups groups = footerGroupMapper.toObject(request);

        try {
            groups = footerGroupRepository.save(groups);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        return footerGroupMapper.toObjectResponse(groups);
    }
}
