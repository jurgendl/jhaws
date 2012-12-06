package org.jhaws.common.lucene.doctype;

import java.io.InputStream;

import junit.framework.TestCase;

import org.jhaws.common.io.IOFile;
import org.jhaws.common.lucene.Analyzer;
import org.jhaws.common.lucene.DocumentFactory;


/**
 * testConversion
 * 
 * @author Jurgen De Landsheer
 */
public class TestConversion extends TestCase {
    /**
     * testConversion
     */
    public void testConversion() {
        System.out.println("start of test"); //$NON-NLS-1$

        Analyzer analyzer = Analyzer.en;

        for (String ext : DocumentFactory.getSupportedExtensions()) {
            System.out.println(ext);

            try {
                InputStream in = TestConversion.class.getClassLoader().getResourceAsStream("tekst." + ext); //$NON-NLS-1$

                if (in != null) {
                    byte[] buffer = new byte[in.available()];
                    in.read(buffer);
                    in.close();

                    IOFile tmpfile = IOFile.newTmpFileExt(ext);
                    tmpfile.writeBytes(buffer);
                    DocumentFactory.getConvertor(tmpfile).convert(analyzer, tmpfile, tmpfile.toURL(), "keywords".split(","), "description"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                } else {
                    System.err.println("example for " + ext + " not found"); //$NON-NLS-1$ //$NON-NLS-2$
                }
            } catch (Throwable e) {
                e.printStackTrace(System.out);
            }

            System.out.println("completed: " + ext); //$NON-NLS-1$
        }

        System.out.println("end of test"); //$NON-NLS-1$
    }
}
