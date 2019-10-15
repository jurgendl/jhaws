@javax.xml.bind.annotation.XmlAccessorOrder(javax.xml.bind.annotation.XmlAccessOrder.UNDEFINED)
@javax.xml.bind.annotation.XmlAccessorType(javax.xml.bind.annotation.XmlAccessType.FIELD)
// @javax.xml.bind.annotation.XmlSchema(namespace = "http://jhaws.org/lang")
@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters({ //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = java.util.Locale.class, //
				value = org.jhaws.common.jaxb.adapters.LocaleAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = java.text.DateFormat.class, //
				value = org.jhaws.common.jaxb.adapters.DateFormatAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = java.util.Date.class, //
				value = org.jhaws.common.jaxb.adapters.DateAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = java.text.NumberFormat.class, //
				value = org.jhaws.common.jaxb.adapters.NumberFormatAdapter.class), //
		//
		// @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
		// type = java.util.Collection.class, //
		// value = org.jhaws.common.jaxb.adapters.CollectionAdapter.class), //
		//
		//
		// @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
		// type = java.util.Map.class, //
		// value = org.jhaws.common.jaxb.adapters.MapAdapter.class), //
		//
		//
		// @javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
		// type = Object[].class, //
		// value = org.jhaws.common.jaxb.adapters.ArrayAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = org.joda.time.LocalDate.class, //
				value = org.jhaws.common.jaxb.adapters.JodaLocalDateAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = org.joda.time.LocalTime.class, //
				value = org.jhaws.common.jaxb.adapters.JodaLocalTimeAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = org.joda.time.LocalDateTime.class, //
				value = org.jhaws.common.jaxb.adapters.JodaLocalDateTimeAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = org.joda.time.DateTime.class, //
				value = org.jhaws.common.jaxb.adapters.JodaDateTimeAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = org.joda.time.YearMonth.class, //
				value = org.jhaws.common.jaxb.adapters.JodaYearMonthAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = java.time.LocalDate.class, //
				value = org.jhaws.common.jaxb.adapters.JavaLocalDateAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = java.time.LocalTime.class, //
				value = org.jhaws.common.jaxb.adapters.JavaLocalTimeAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = java.time.LocalDateTime.class, //
				value = org.jhaws.common.jaxb.adapters.JavaLocalDateTimeAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = java.time.ZonedDateTime.class, //
				value = org.jhaws.common.jaxb.adapters.JavaZonedDateTimeAdapter.class), //
		//
		@javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter(//
				type = java.awt.Image.class, //
				value = org.jhaws.common.jaxb.adapters.ImageXmlAdapter.class), //
		//
})
package org.jhaws.common.g11n.currency;
