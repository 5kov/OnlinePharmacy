package bg.softuni.onlinepharmacy.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "news.api")
public class NewsApiConfig {
  private String baseUrl;

  public String getBaseUrl() {
    return baseUrl;
  }

  public NewsApiConfig setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
    return this;
  }
}
