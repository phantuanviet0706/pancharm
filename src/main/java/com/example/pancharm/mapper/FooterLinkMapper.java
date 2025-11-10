package com.example.pancharm.mapper;

import org.mapstruct.Mapper;

import com.example.pancharm.entity.FooterLinks;

@Mapper(componentModel = "spring", uses = FooterLinks.class)
public interface FooterLinkMapper {}
