package com.example.pancharm.service.configuration;

import com.example.pancharm.constant.ConfigurationName;
import com.example.pancharm.constant.LangConfiguration;
import com.example.pancharm.entity.Configurations;
import com.example.pancharm.repository.ConfigurationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigurationService {
	ConfigurationRepository configurationRepository;

	public Configurations getCompanyConfiguration() {
		return configurationRepository.findByName(ConfigurationName.COMPANY_CONFIG)
				.orElse(
						Configurations.builder()
								.name(ConfigurationName.COMPANY_CONFIG)
								.lang(LangConfiguration.VI)
								.build()
				);
	}
}
