package cd.bnb.syprome.utilities;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "myapp")
@Configuration("appInfo")
@Data
public class AppInfo {
    private String smsUrl;
    private Integer delay;
}
