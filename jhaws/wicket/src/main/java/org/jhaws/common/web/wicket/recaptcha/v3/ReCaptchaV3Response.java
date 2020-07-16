package org.jhaws.common.web.wicket.recaptcha.v3;

import java.time.LocalDateTime;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ReCaptchaV3Response {
	// 2020-07-15T09:15:30Z
	@JsonProperty("challenge_ts")
	protected LocalDateTime challengeTimestamp;

	protected String hostname;

	protected Boolean success;

	protected String action;

	protected Double score;

	@JsonProperty("error-codes")
	protected String[] errorCodes;

	public ReCaptchaV3Response() {
		super();
	}

	@Override
	public String toString() {
		return "ReCaptchaV3Response ["
				+ (this.challengeTimestamp != null ? "challengeTimestamp=" + this.challengeTimestamp + ", " : "")
				+ (this.hostname != null ? "hostname=" + this.hostname + ", " : "") + "success=" + this.success + ", "
				+ (this.action != null ? "action=" + this.action + ", " : "")
				+ (this.score != null ? "score=" + this.score + ", " : "")
				+ (this.errorCodes != null ? "errorCodes=" + Arrays.toString(this.errorCodes) : "") + "]";
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getAction() {
		return this.action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public Double getScore() {
		return this.score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String[] getErrorCodes() {
		return this.errorCodes;
	}

	public void setErrorCodes(String[] errorCodes) {
		this.errorCodes = errorCodes;
	}

	public LocalDateTime getChallengeTimestamp() {
		return this.challengeTimestamp;
	}

	public void setChallengeTimestamp(LocalDateTime challengeTimestamp) {
		this.challengeTimestamp = challengeTimestamp;
	}
}