package monolith2microservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Configs {
	
	@Value("${git.localrepo}")
	public String localRepositoryDirectory;

	public final String DEV_NULL = "/dev/null";

}
