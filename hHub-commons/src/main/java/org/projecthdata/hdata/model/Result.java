/*
 * Copyright 2011 The MITRE Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.projecthdata.hdata.model;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Namespace;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@SuppressWarnings("serial")
@Root(name = "result", strict = false)
@Namespace(reference = "http://projecthdata.org/hdata/schemas/2009/06/result")
public class Result implements Comparable<Result>, Serializable {

	private String resultDateTimeRaw;
	@Transient
	private DateTime resultDateTime = null;
	@Transient
	private DateTimeFormatter formatter = DateTimeFormat
			.forPattern("MMMM dd, yyyy");

	@Attribute(name = "code")
	@Path("resultStatus")
	private String resultStatusCode;

	@Element
	private String narrative;

	@Attribute(name = "value")
	@Path("resultValue")
	private String resultValue;

	@Attribute(name = "unit")
	@Path("resultValue")
	private String resultValueUnit;

	@Attribute(name = "root")
	@Path("resultId")
	private String resultId;

	@Attribute(name = "low")
	@Path("resultDateTime")
	public String getResultDateTime() {
		return resultDateTimeRaw;
	}

	@Attribute(name = "low")
	@Path("resultDateTime")
	public void setResultDateTime(String resultDateTime) {
		this.resultDateTimeRaw = resultDateTime;
		this.resultDateTime = new DateTime(this.resultDateTimeRaw);
	}

	@Element
	private ResultType resultType = null;

	public Result() {
		this.resultType = new ResultType();
	}

	public ResultType getResultType() {
		return resultType;
	}

	public void setResultType(ResultType resultType) {
		this.resultType = resultType;
	}

	public String getFormattedDateTime() {
		return formatter.print(this.resultDateTime);
	}

	public DateTime getDateTimeObj() {
		return this.resultDateTime;
	}

	public String getResultStatusCode() {
		return resultStatusCode;
	}

	public void setResultStatusCode(String resultStatusCode) {
		this.resultStatusCode = resultStatusCode;
	}

	public String getNarrative() {
		return narrative;
	}

	public void setNarrative(String narrative) {
		this.narrative = narrative;
	}

	public String getResultValue() {
		return resultValue;
	}

	public void setResultValue(String resultValue) {
		this.resultValue = resultValue;
	}

	public String getResultValueUnit() {
		return resultValueUnit;
	}

	public void setResultValueUnit(String resultValueUnit) {
		this.resultValueUnit = resultValueUnit;
	}

	public String getResultId() {
		return resultId;
	}

	public void setResultId(String resultId) {
		this.resultId = resultId;
	}

	@Override
	public int compareTo(Result other) {
		return this.getDateTimeObj().compareTo(other.getDateTimeObj());
	}

	@SuppressWarnings("serial")
	public static class ResultType implements Serializable {
		@Attribute
		private String code = null;
		@Attribute
		private String codeSystem = null;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getCodeSystem() {
			return codeSystem;
		}

		public void setCodeSystem(String codeSystem) {
			this.codeSystem = codeSystem;
		}

	}

}
