package org.jhaws.common.parameters;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;

public class VariablesTest {
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement
	public static class CfgTest1 {
		@Parameter(options = { @ParameterOption("uuid"), @ParameterOption("pass") })
		@XmlAttribute
		private String type;

		@Parameter
		@XmlAttribute
		private int count = 1;

		@Parameter
		@XmlAttribute
		private int length = 30;

		public String getType() {
			return this.type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getCount() {
			return this.count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getLength() {
			return this.length;
		}

		public void setLength(int length) {
			this.length = length;
		}
	}

	@Test
	public void test1() {
		try {
			CfgTest1 config = new CfgTest1();
			new Variables<>(config).parameters(new String[] { "-type=uuid", "-count", "10" }).apply(config).log();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement
	public static class CfgTest2 {
		@Parameter
		@XmlAttribute
		private String test;

		public String getTest() {
			return this.test;
		}

		public void setTest(String test) {
			this.test = test;
		}
	}

	@Test
	public void test2() {
		try {
			CfgTest2 config = new CfgTest2();
			new Variables<>(config).parameters(new String[] { "-test=sentence with spaces and an equal = sign" })
					.apply(config).log();
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
}
