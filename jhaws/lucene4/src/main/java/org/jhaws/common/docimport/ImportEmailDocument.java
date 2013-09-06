package org.jhaws.common.docimport;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.text.html.HTMLEditorKit;

import org.jhaws.common.io.IOFile;

/**
 * email document conversion
 * 
 * @author Jurgen
 * @version 1.0.0 - 22 June 2006
 * 
 * @since 1.5
 */
public class ImportEmailDocument implements ImportDocument {
    /**
     * Creates a new EmailToLuceneDocument object.
     */
    public ImportEmailDocument() {
        super();
    }

    /**
     * na
     * 
     * @param body na
     * 
     * @return
     * 
     * @throws MessagingException na
     * @throws IOException na
     * @throws BadLocationException na
     */
    private String bodyPartToString(final BodyPart body) throws MessagingException, IOException, BadLocationException {
        if (body.getContentType().startsWith("text/plain")) {

            return body.getContent().toString();
        }

        if (body.getContentType().startsWith("text/htm")) {

            HTMLEditorKit kit = new HTMLEditorKit();
            PlainDocument tdoc = new PlainDocument();
            ByteArrayInputStream is = new ByteArrayInputStream(body.getContent().toString().getBytes());
            kit.read(is, tdoc, 0);

            return tdoc.getText(0, tdoc.getLength());
        }

        return "";
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(java.io.InputStream)
     */
    @Override
    public String getText(final InputStream file) throws IOException {
        try {
            MimeMessage message = new MimeMessage(null, file);
            Object content = message.getContent();

            StringBuilder sb = new StringBuilder(2);

            if (content instanceof MimeMultipart) {
                MimeMultipart multi = (MimeMultipart) message.getContent();

                for (int i = 0; i < multi.getCount(); i++) {
                    try {
                        BodyPart body = multi.getBodyPart(i);
                        sb.append(this.bodyPartToString(body));
                        sb.append("\n\n");
                    } catch (final BadLocationException e) {
                        throw new IOException(e.getMessage());
                    }
                }
            } else {
                if (content instanceof String) {
                    sb.append(content.toString());
                    sb.append("\n\n");
                } else {
                    if (content instanceof BodyPart) {
                        try {
                            BodyPart body = (BodyPart) content;
                            sb.append(this.bodyPartToString(body));
                            sb.append("\n\n");
                        } catch (final BadLocationException e) {
                            throw new IOException(e.getMessage());
                        }
                    } else {
                        throw new RuntimeException("not implemented TODO");
                    }
                }
            }

            return sb.toString();
        } catch (final MessagingException e) {
            throw new IOException(e.getMessage());
        }
    }

    /**
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "eml";
    }

    /**
     * @see org.jhaws.common.docimport.ImportDocument#getText(org.jhaws.common.io.IOFile)
     */
    @Override
    public String getText(IOFile file) throws IOException {
        return getText(new FileInputStream(file));
    }
}
