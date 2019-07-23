package com.security.domain.dto;

public class TokenDTO {

	private String token;
	private String tipo;

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private TokenDTO dto;

		public Builder() {
			dto = new TokenDTO();
		}

		public Builder withToken(String token) {
			dto.token = token;
			return this;
		}

		public Builder withTipo(String tipo) {
			dto.tipo = tipo;
			return this;
		}

		public TokenDTO build() {
			return dto;
		}
	}

	public String getToken() {
		return token;
	}

	public String getTipo() {
		return tipo;
	}

}
