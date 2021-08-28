package br.com.lutadeclasses.jornadaservice.exception.handler;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorHandler implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
	private String method;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
