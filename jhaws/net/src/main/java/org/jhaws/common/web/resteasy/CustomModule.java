package org.jhaws.common.web.resteasy;

import com.fasterxml.jackson.core.Version;

@SuppressWarnings("serial")
public class CustomModule extends com.fasterxml.jackson.databind.module.SimpleModule {
	@Override
	public String getModuleName() {
		return "CustomModule";
	}

	@Override
	public Version version() {
		return new Version(1, 0, 0, null, null, null);
	}

	@Override
	public void setupModule(SetupContext context) {
		context.addSerializers(new CustomModuleSerializers());
		context.addDeserializers(new CustomDeserializers());
	}
}