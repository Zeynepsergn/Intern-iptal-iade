package tr.gov.gib.iade.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Iade Service")
                        .version("1.0")
                        .description("Iade service ile ilgili işlemleri yapan servistir.")
                        .termsOfService("https://gib.gov.tr")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")
                        )
                        .contact(new Contact()
                                .email("busra.evin@gelirler.gov.tr")
                                .name("Geliştirici")
                                .url("https://gelirler.gov.tr")
                        )
                );
    }
}
