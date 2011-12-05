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
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="allergy", strict=false)
public class Allergy {
	
	@Element
	private Information adverseEventType;
	@Element
	private Information product;
	@Element
	private Information reaction;
	@Element
	private Information severity;
	@Element
	private Information alertStatus;
	

	public Information getAdverseEventType() {
		return adverseEventType;
	}

	public Information getProduct() {
		return product;
	}

	public void setProduct(Information product) {
		this.product = product;
	}

	public Information getReaction() {
		return reaction;
	}

	public void setReaction(Information reaction) {
		this.reaction = reaction;
	}

	public Information getSeverity() {
		return severity;
	}

	public void setSeverity(Information severity) {
		this.severity = severity;
	}

	public Information getAlertStatus() {
		return alertStatus;
	}

	public void setAlertStatus(Information alertStatus) {
		this.alertStatus = alertStatus;
	}

	public void setAdverseEventType(Information adverseEventType) {
		this.adverseEventType = adverseEventType;
	}


	public static class Information{
		@Attribute(required=false)
		private String code;
		@Attribute(required=false)
		private String codeSystem;
		@Attribute(required=false)
		private String displayName;
		@Attribute(required=false)
		private String codeSystemName;
		
		
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
		public String getDisplayName() {
			return displayName;
		}
		public void setDisplayName(String displayName) {
			this.displayName = displayName;
		}
		public String getCodeSystemName() {
			return codeSystemName;
		}
		public void setCodeSystemName(String codeSystemName) {
			this.codeSystemName = codeSystemName;
		}
		
		
	}
	
	@Override
	public String toString() {
		return this.product.displayName;
	}
}
