package com.example.pancharm.mapper;

import com.example.pancharm.dto.response.configuration.ConfigurationResponse;
import com.example.pancharm.entity.Configurations;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ConfigurationMapper {
    ConfigurationResponse toConfigurationResponse(Configurations configuration);
}
