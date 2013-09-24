package org.jhaws.common.lucene.doctype.conversion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ProtocolCommandEvent;
import org.apache.commons.net.ProtocolCommandListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.io.CopyStreamEvent;
import org.apache.commons.net.io.CopyStreamListener;
import org.jhaws.common.io.IODirectory;
import org.jhaws.common.io.IOFile;

/**
 * uses xpdf as command line utility to convert pdf to text (very performant), needs connection to internet and library ftp server to be up to work
 * 
 * @author Jurgen
 * 
 * @see http://www.foolabs.com/xpdf/download.html
 * @see ftp://ftp.foolabs.com/pub/xpdf
 */
public class XPdfExtractor {
    private static final String VERSION = "3.03"; //$NON-NLS-1$

    private static final IODirectory runners_root = new IODirectory(IODirectory.getUserDir(), "/.jhaws/xpdf/").create(); //$NON-NLS-1$

    private static IOFile runner;

    static {
        try {
            XPdfExtractor.init();
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        }
    }

    public static IOFile extract(IOFile pdff) throws IOException {
        pdff.checkExistence();
        XPdfExtractor.runner.checkExistence();

        IOFile txt = new IOFile(XPdfExtractor.runners_root, "123"/* StringTool.randomAlphaNumeric(36) */);
        List<String> cmdl = new ArrayList<String>();
        String cmd = XPdfExtractor.runner.getName() + " " + pdff.getAbsolutePath() + " " + txt.getAbsolutePath(); //$NON-NLS-1$//$NON-NLS-2$
        System.out.println(cmd);

        if (System.getProperty("os.name").toLowerCase().indexOf("win") != -1) { //$NON-NLS-1$ //$NON-NLS-2$
            cmdl.add("\"" + XPdfExtractor.runner.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            cmdl.add("\"" + pdff.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
            cmdl.add("\"" + txt.getAbsolutePath() + "\""); //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            cmdl.add(XPdfExtractor.runner.getAbsolutePath().replaceAll(" ", "\\ ")); //$NON-NLS-1$ //$NON-NLS-2$
            cmdl.add(pdff.getAbsolutePath().replaceAll(" ", "\\ ")); //$NON-NLS-1$ //$NON-NLS-2$
            cmdl.add(txt.getAbsolutePath().replaceAll(" ", "\\ ")); //$NON-NLS-1$ //$NON-NLS-2$
        }

        Process p = new ProcessBuilder(cmdl).directory(XPdfExtractor.runner.getParentDirectory()).redirectErrorStream(true).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        reader.close();

        return txt;
    }

    private static void init() throws SocketException, IOException {
        String name;
        String shortName;
        String suffix = ""; //$NON-NLS-1$

        // xpdfbin-win-3.03.zip
        // xpdfbin-linux-3.03.tar.gz

        if (System.getProperty("os.name").toLowerCase().indexOf("win") != -1) { //$NON-NLS-1$ //$NON-NLS-2$
            shortName = "xpdfbin-win-" + XPdfExtractor.VERSION + ""; //$NON-NLS-1$ //$NON-NLS-2$
            name = "xpdfbin-win-" + XPdfExtractor.VERSION + ".zip"; //$NON-NLS-1$ //$NON-NLS-2$
            suffix = ".exe"; //$NON-NLS-1$
        } else {
            shortName = "xpdfbin-linux-" + XPdfExtractor.VERSION + ""; //$NON-NLS-1$ //$NON-NLS-2$
            name = "xpdfbin-linux-" + XPdfExtractor.VERSION + ".tar.gz"; //$NON-NLS-1$ //$NON-NLS-2$
        }

        IOFile archivef = new IOFile(XPdfExtractor.runners_root, name);
        archivef.erase();
        System.out.println("*" + archivef);
        System.out.println("*" + archivef.exists());

        XPdfExtractor.runner = new IOFile(XPdfExtractor.runners_root, shortName + "/bin32/pdftotext" + suffix); //$NON-NLS-1$

        if (XPdfExtractor.runner.exists()) {
            return;
        }

        FTPClient ftp = new FTPClient();
        ftp.setCopyStreamListener(new CopyStreamListener() {
            @Override
            public void bytesTransferred(CopyStreamEvent event) {
                System.out.println(event.getBytesTransferred());
            }

            @Override
            public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
                System.out.println(totalBytesTransferred + " / " + bytesTransferred + " / " + streamSize);
            }
        });
        ftp.addProtocolCommandListener(new ProtocolCommandListener() {
            @Override
            public void protocolCommandSent(ProtocolCommandEvent event) {
                System.out.println("protocolCommandSent: " + event.getCommand() + " - " + event.getMessage() + " - " + event.getReplyCode());
            }

            @Override
            public void protocolReplyReceived(ProtocolCommandEvent event) {
                System.out.println("protocolReplyReceived: " + event.getCommand() + " - " + event.getMessage() + " - " + event.getReplyCode());
            }
        });

        ftp.connect("ftp.foolabs.com");
        ftp.login("anonymous", "");

        if (FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            ftp.retrieveFile("/pub/xpdf/" + name, new FileOutputStream(archivef));
            System.out.println("*" + archivef.exists());
            ftp.disconnect();
        }

        //
        // FTPWrapper download = new FTPWrapper();
        //        download.connectAndLogin("ftp.foolabs.com"); //$NON-NLS-1$
        // download.setPassiveMode();
        //        download.changeWorkingDirectory("pub"); //$NON-NLS-1$
        //        download.changeWorkingDirectory("xpdf"); //$NON-NLS-1$
        // download.setBinary();
        //
        // archivef.checkExistence().exists();

        // Archive archive = new Archive(archivef);
        // archive.extract(XPdfExtractor.runners_root);
    }

    public static void main(String[] args) {
        //
    }

    public static String read(IOFile txt) {
        try {
            if ((txt == null) || !txt.exists()) {
                return null;
            }

            FileInputStream in = new FileInputStream(txt);
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            txt.erase();

            return new String(buffer);
        } catch (Exception e) {
            return ""; //$NON-NLS-1$
        }
    }
}
