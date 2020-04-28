package org.jhaws.common.lucene;

import org.jhaws.common.io.FilePath;

public class UpdateLuceneIndexTrial78 {
    public static void main(String[] args) {
        FilePath lp = FilePath.getTempDirectory().child("luceneempty7");
        System.out.println(lp);
        try (LuceneIndex li = new LuceneIndex(lp)) {
            li.upgrade(false);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
