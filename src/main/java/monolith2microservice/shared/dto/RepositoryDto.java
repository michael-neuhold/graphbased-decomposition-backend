package monolith2microservice.shared.dto;

import com.google.common.collect.Iterables;
import lombok.Builder;
import lombok.Setter;

import java.util.Arrays;

@Setter
@Builder
public class RepositoryDto {

	private String uri;

	public String getUri(){
		return this.uri;
	}

	public String getName(){
		return Iterables.getLast(Arrays.asList(this.uri.split("/"))).split("\\.git")[0];
	}

}
