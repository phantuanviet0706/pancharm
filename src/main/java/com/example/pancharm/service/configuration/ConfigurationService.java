package com.example.pancharm.service.configuration;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.constant.FileConstants;
import com.example.pancharm.dto.response.company.CompanyResponse;
import com.example.pancharm.dto.response.configuration.ConfigurationResponse;
import com.example.pancharm.entity.ProductImages;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.ConfigurationMapper;
import com.example.pancharm.util.ImageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ConfigurationName;
import com.example.pancharm.constant.LangConfiguration;
import com.example.pancharm.entity.Configurations;
import com.example.pancharm.repository.ConfigurationRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigurationService {
    ConfigurationRepository configurationRepository;
    ConfigurationMapper  configurationMapper;

    ImageUtil imageUtil;

    public Configurations getCompanyConfiguration() {
        return configurationRepository
                .findByName(ConfigurationName.COMPANY_CONFIG)
                .orElse(Configurations.builder()
                        .name(ConfigurationName.COMPANY_CONFIG)
                        .lang(LangConfiguration.VI)
                        .build());
    }

    /**
     * @desc Get Default Configuration
     * @return ConfigurationResponse
     */
    public ConfigurationResponse getConfiguration() {
        var configuration = configurationRepository.findAll().getFirst();
        if (configuration == null) {
            configuration = Configurations.builder()
                    .name(ConfigurationName.COMPANY_CONFIG)
                    .build();

            configurationRepository.save(configuration);
        }
        return configurationMapper.toConfigurationResponse(configuration);
    }

    public ConfigurationResponse updateConfigSource(MultipartFile file, String type) {
        System.out.println("Multipart File:" + file.toString());
        ConfigurationName name = ConfigurationName.COMPANY_CONFIG;
        Configurations configuration = configurationRepository.findByName(name).orElse(null);
        if (configuration == null) {
            configuration = Configurations.builder().name(name).build();
            configurationRepository.save(configuration);
        }

        Set<MultipartFile> files = new HashSet<>();
        files.add(file);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode configNode = null;
        String currentVideoPath = "";
        try {
            configNode = objectMapper.readTree(configuration.getConfig());
            // Kiểm tra key có tồn tại trong config hay không
            currentVideoPath = configNode.path(type + "Url").asText();
        } catch (JsonProcessingException e) {
            currentVideoPath = ""; // Nếu có lỗi thì khởi tạo chuỗi trống
        }

        // Lấy URL của file tải lên
        String fileUrl = imageUtil.upsertSingleFile(
                file,
                configuration,
                c -> "configurations/video",
                currentVideoPath,
                configuration::setConfig,
                false
        );

        if (fileUrl != null) {
            try {
                if (configNode == null || configNode.isObject()) {
                    ((ObjectNode) configNode).put(type + "Url", fileUrl);
                } else {
                    configNode = objectMapper.createObjectNode();
                    ((ObjectNode) configNode).put(type + "Url", fileUrl);
                }

                String configJson = objectMapper.writeValueAsString(configNode);

                configuration.setConfig(configJson);
                configurationRepository.save(configuration);
            } catch (Exception e) {
                throw new AppException(ErrorCode.JSON_PROCESSING_ERROR);
            }
        }

        return configurationMapper.toConfigurationResponse(configuration);
    }

}
