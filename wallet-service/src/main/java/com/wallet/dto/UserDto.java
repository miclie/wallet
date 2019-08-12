package com.wallet.dto;

import java.io.Serializable;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wallet.entity.User;

import io.swagger.annotations.ApiModel;

@ApiModel(description = "Represents a a family house within the Game of Thrones fantasy world")
@JsonInclude(Include.NON_NULL)
public class UserDto  extends ResourceSupport implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	public UserDto(User user) {
		super();
		this.name = user.getUsername();
		this.id = user.getId();
	}


	public UserDto withLink(Link link) {
		this.add(link);
		return this;
	}
	
	public UserDto withLink(String href, String rel) {
		add(new Link(href, rel));
		return this;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUserId() {
		return id;
	}

}
