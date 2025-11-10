package com.example.pancharm.mapper;

import org.mapstruct.Mapper;

import com.example.pancharm.entity.FooterGroups;

@Mapper(componentModel = "spring", uses = FooterGroups.class)
public interface FooterGroupMapper {}
