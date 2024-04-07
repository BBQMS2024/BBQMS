package ba.unsa.etf.si.bbqms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestRequestConfig {
    @Bean
    public RestTemplate restTemplate(final List<HttpMessageConverter<?>> messageConverters) {
        return new RestTemplate(messageConverters);
    }

    @Bean
    public ByteArrayHttpMessageConverter byteArrayHttpMessageConverter() {
        return new ByteArrayHttpMessageConverter();
    }
}
