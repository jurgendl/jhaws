package org.jhaws.common.documents;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.lang3.StringUtils;
import org.jhaws.common.io.FilePath;
import org.jhaws.common.io.console.Processes;
import org.jhaws.common.system.SystemSettings;

public class XPdfFileTextExtracter implements FileTextExtracter {
    public static final String VERSION = "4.03";

    protected FilePath xpdfExecutable;

    protected String version = VERSION;

    public XPdfFileTextExtracter() {
        super();
    }

    @Override
    public List<String> accepts() {
        return Arrays.asList("pdf");
    }

    @Override
    public void extract(InputStream stream, FilePath target) throws IOException {
        FilePath tmp = FilePath.createTempFile(target.getFileNameString(), "pdf");
        tmp.write(stream);
        extract(tmp, target);
    }

    @Override
    public void extract(FilePath pdf, FilePath txt) {
        if (txt.notExists() || pdf.getLastModifiedDateTime().isAfter(txt.getLastModifiedDateTime())) {
            System.out.println(pdf);
            extractpdftext(getXpdfExecutable(), pdf, txt);
            System.out.println(txt);
        }
    }

    public static void extractpdftext(FilePath xpdfexe, FilePath pdf, FilePath txt) {
        if (txt == null) {
            txt = pdf.appendExtension("txt");
        } else {
            txt.getParentPath().createDirectory();
        }
        Processes.callProcess(null, true, Arrays.asList("\"" + xpdfexe.getAbsolutePath() + "\"", "-enc", "Latin1", "-eol", "dos",
                // "-nopgbrk", // "\u000C" // FORM FEED (FF)
                "\"" + pdf.getAbsolutePath() + "\"", "\"" + txt.getAbsolutePath() + "\""), txt.getParentPath(), new Processes.Log());
    }

    public static FilePath xpdf(String version) throws MalformedURLException, IOException {
        if (version == null) version = VERSION;

        // https://dl.xpdfreader.com/xpdf-tools-linux-4.03.tar.gz
        // https://dl.xpdfreader.com/xpdf-tools-win-4.03.zip
        // https://dl.xpdfreader.com/xpdf-tools-mac-4.03.tar.gz
        FilePath xpdf = new FilePath(System.getProperty("user.home")).child("xpdf" + "-" + version).createDirectory();
        FilePath xpdfarchive;
        String os;
        String file;
        String ext;
        switch (SystemSettings.osgroup) {
            case Mac:
                os = "mac";
                file = "xpdf-tools-" + os + "-" + version + ".tar.gz";
                xpdfarchive = xpdf.child(file);
                ext = "";
                break;
            case Nix:
                os = "linux";
                file = "xpdf-tools-" + os + "-" + version + ".tar.gz";
                xpdfarchive = xpdf.child(file);
                ext = "";
                break;
            case Windows:
                os = "win";
                file = "xpdf-tools-" + os + "-" + version + ".zip";
                xpdfarchive = xpdf.child(file);
                ext = "exe";
                break;
            default:
                throw new RuntimeException("unknow os");
        }

        if (xpdfarchive.notExists()) {
            FilePath tmp = xpdfarchive.appendExtension("partial");
            tmp.download(new URL("https://dl.xpdfreader.com/" + file));
            tmp.renameTo(xpdfarchive);
        }

        // http://www.foolabs.com/xpdf/
        FilePath xpdfexe = xpdf.child("xpdf-tools-" + os + "-" + version).child("bin64").child("pdftotext" + (StringUtils.isBlank(ext) ? "" : "." + ext));
        if (xpdfexe.notExists()) {
            if (xpdfarchive.getName().equals(".zip")) {
                xpdfarchive.unzip(xpdf);
            } else if (xpdfarchive.getName().equals(".tar.gz")) {
                extractTarGZ(xpdfarchive.newInputStream(), xpdf);
            } else {
                throw new IllegalArgumentException("archive not supported " + file);
            }
        }

        return xpdfexe;
    }

    public FilePath getXpdfExecutable() {
        if (xpdfExecutable == null) {
            try {
                xpdfExecutable = xpdf(version);
            } catch (IOException ex) {
                throw new UncheckedIOException(ex);
            }
        }
        return xpdfExecutable;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    // https://stackoverflow.com/questions/7128171/how-to-compress-decompress-tar-gz-files-in-java
    static public void extractTarGZ(InputStream in, FilePath target) {
        // Files.createDirectories(Paths.get(target));
        // ProcessBuilder builder = new ProcessBuilder();
        // builder.command("sh", "-c", String.format("tar xfz %s -C %s", tarGzPathLocation, target));
        // builder.directory(new File("/tmp"));
        // Process process = builder.start();
        // int exitCode = process.waitFor();
        // assert exitCode == 0;

        try {
            int BUFFER_SIZE = 1024 * 64;
            byte data[] = new byte[BUFFER_SIZE];
            try (GzipCompressorInputStream gzipIn = new GzipCompressorInputStream(in);) {
                try (TarArchiveInputStream tarIn = new TarArchiveInputStream(gzipIn)) {
                    TarArchiveEntry entry;
                    while ((entry = (TarArchiveEntry) tarIn.getNextEntry()) != null) {
                        FilePath f = target.child(entry.getName());
                        if (entry.isDirectory()) {
                            f.createDirectory();
                        } else {
                            int count;
                            try (OutputStream fos = f.newBufferedOutputStream();) {
                                while ((count = tarIn.read(data, 0, BUFFER_SIZE)) != -1) {
                                    fos.write(data, 0, count);
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }
}
