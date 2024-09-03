package tr.gov.gib.iade;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = "tr.gov.gib")
@OpenAPIDefinition(info =
@Info(
		title = "Iade Service API",
		version = "1.0",
		description = "Documentation Iade Service API v1.0",
		contact = @Contact(
				name = "Busra Evin ",
				email = "busra.evin@gelirler.gov.tr",
				url = "https://www.gib.gov.tr/"
		),
		license = @License(
				url = "https://www.gib.gov.tr/",
				name = "Gelir İdaresi Başkanlığı Lisanslıdır"
		)
)
)
public class IadeServisApplication {

	public static void main(String[] args) {
		SpringApplication.run(IadeServisApplication.class, args);
	}

}
