package monolith2microservice.shared.dto;

import com.google.common.collect.Iterables;

import java.util.Arrays;

public class RepositoryDto {

	private String uri;

	public String getUri(){
		return this.uri;
	}
	
	public void setUri(String uri){
		this.uri = uri;
	}
	
	public String getName(){
		return Iterables.getLast(Arrays.asList(this.uri.split("/"))).split("\\.git")[0];
	}

	@Override
	public String toString() {
		return "RepositoryDTO [uri=" + uri + ", name=" + getName() + "]";
	}

}
