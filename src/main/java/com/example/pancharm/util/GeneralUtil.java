package com.example.pancharm.util;

import com.example.pancharm.service.configuration.ConfigurationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
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

	/**
	 * @desc Decode a value to seperated params
	 * @param encodedParam
	 * @return Set<String>
	 */
	public Set<String> decodeToParams(String encodedParam) {
		if (encodedParam == null || encodedParam.isBlank()) {
			return Collections.emptySet();
		}

		return Arrays.stream(encodedParam.split(","))
				.map(String::trim)
				.filter(s -> !s.isEmpty())
				.collect(Collectors.toSet());
	}
}
