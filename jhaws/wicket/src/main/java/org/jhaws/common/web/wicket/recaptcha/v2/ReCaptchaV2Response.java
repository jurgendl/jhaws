package org.jhaws.common.web.wicket.recaptcha.v2;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReCaptchaV2Response {
	@JsonProperty("challenge_ts")
	protected LocalDateTime challengeTimestamp;

	protected String hostname;

	protected Boolean success;

	public ReCaptchaV2Response() {
		super();
	}

	@Override
	public String toString() {
		return "ReCaptchaV2Response ["
				+ (this.challengeTimestamp != null ? "challengeTimestamp=" + this.challengeTimestamp + ", " : "")
				+ (this.hostname != null ? "hostname=" + this.hostname + ", " : "")
				+ (this.success != null ? "success=" + this.success : "") + "]";
	}

	public LocalDateTime getChallengeTimestamp() {
		return this.challengeTimestamp;
	}

	public void setChallengeTimestamp(LocalDateTime challengeTimestamp) {
		this.challengeTimestamp = challengeTimestamp;
	}

	public String getHostname() {
		return this.hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
}