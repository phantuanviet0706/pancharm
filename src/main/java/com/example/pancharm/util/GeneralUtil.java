package com.example.pancharm.util;

import com.example.pancharm.service.configuration.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GeneralUtil {
	ConfigurationService configurationService;

	/**
	 * @desc Generate Slug for Product
	 * @param objectTypeKey
	 * @param slugId
	 * @return String
	 */
	public String generateSlug(String objectTypeKey, int slugId) {
		String defaultPrefix = objectTypeKey.toUpperCase() + "-";

		var companyConfig = configurationService.getCompanyConfiguration();
		var objectConfig = JsonConfigUtil.getString(companyConfig.getConfig(), objectTypeKey);
		var slugPrefix = (objectConfig != null)
				? JsonConfigUtil.getString(objectConfig, "slug")
				: null;

		if (slugPrefix != null && !slugPrefix.isBlank()) {
			defaultPrefix = slugPrefix;
		}

		return defaultPrefix + slugId;
	}
}
