package org.jhaws.common.lucene;


import java.util.Date;

import org.apache.lucene.document.Document;
import org.jhaws.common.lucene.Indexable.IndexableAdapter;
import org.junit.Assert;
import org.junit.Test;

public class LuceneDocumentBuilderTest {
	public static class It extends IndexableAdapter<It> {
		@IndexField
		private String field1;

		@IndexField
		private String field2;

		@Override
		public It retrieve(Document d) {
			retrieveBase(d);
			return this;
		}

		@Override
		public Document indexable() {
			Document d = super.indexable();
			return d;
		}

		public String getField1() {
			return field1;
		}

		public void setField1(String field1) {
			this.field1 = field1;
		}

		public String getField2() {
			return field2;
		}

		public void setField2(String field2) {
			this.field2 = field2;
		}
	}

	@Test
	public void test() {
		It it = new It();
		it.setLastmodified(new Date());
		it.setUuid("uuid");
		it.setVersion(1);
		LuceneDocumentBuilder<It> b = new LuceneDocumentBuilder<It>() {};
		Document d = b.buildDocument(it);
		It it2 = b.retrieveFromDocument(d);
		Assert.assertFalse(it == it2);
		Assert.assertEquals(it.getUuid(), it2.getUuid());
		Assert.assertEquals(it.getVersion(), it2.getVersion());
		Assert.assertEquals(it.getLastmodified(), it2.getLastmodified());
	}
}
