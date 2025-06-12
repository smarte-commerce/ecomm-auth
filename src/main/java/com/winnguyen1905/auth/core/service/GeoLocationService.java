package com.winnguyen1905.auth.core.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.winnguyen1905.auth.common.constant.RegionPartition;

@Service
public class GeoLocationService {

  private final RestTemplate restTemplate = new RestTemplate();

  public RegionPartition getRegionFromIp(String ip) {
    String url = "https://ipwho.is/" + ip;
    try {
      ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
      ObjectMapper mapper = new ObjectMapper();
      JsonNode root = mapper.readTree(response.getBody());

      String country = root.path("country").asText();
      String continent = root.path("continent").asText();

      if (continent.equalsIgnoreCase("Europe"))
        return RegionPartition.EU;
      if (continent.equalsIgnoreCase("Asia"))
        return RegionPartition.ASIA;
      if (continent.equalsIgnoreCase("North America") && country.equalsIgnoreCase("United States"))
        return RegionPartition.US;
      return RegionPartition.ASIA;
    } catch (Exception e) {
      e.printStackTrace();
      return RegionPartition.US;
    }
  }
}
