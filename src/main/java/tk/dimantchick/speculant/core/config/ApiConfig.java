package tk.dimantchick.speculant.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.tinkoff.invest.openapi.OpenApi;
import ru.tinkoff.invest.openapi.okhttp.OkHttpOpenApi;

@Configuration
@ComponentScan("tk.dimantchick.speculant")
@PropertySource("file:./config.properties")
@PropertySource("file:./application.properties")
public class ApiConfig {

    @Value("${tk.dimantchick.speculant.trader.token}")
    private String token;

    @Bean
    public OpenApi openApi() {
        return new OkHttpOpenApi(token, false);
    }
}
