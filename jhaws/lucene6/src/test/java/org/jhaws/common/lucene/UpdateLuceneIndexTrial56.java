package org.jhaws.common.lucene;

import java.io.IOException;

import org.jhaws.common.io.FilePath;

public class UpdateLuceneIndexTrial56 {
	public static void main(String[] args) {
		FilePath lp = FilePath.getTempDirectory().child("luceneempty5");
		System.out.println(lp);
		try (LuceneIndex li = new LuceneIndex(lp)) {
			li.upgrade(false);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}