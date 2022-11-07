import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.Collectors;

// https://stackoverflow.com/questions/189094/how-to-scan-a-folder-in-java
// https://wiki.ugent.be/pages/viewpage.action?pageId=141066740
// https://www.baeldung.com/run-shell-command-in-java
// https://blog.termian.dev/posts/nodejs-in-java/
// https://lisperator.net/uglifyjs/
// https://cwiki.apache.org/confluence/display/MAVEN/Maven+Properties+Guide
// https://javapapers.com/java/glob-with-java-nio/
//
// Windows XP - %USERPROFILE%\Application Data\npm\node_modules
// Newer Windows Versions - %AppData%\npm\node_modules
// or - %AppData%\roaming\npm\node_modules
// https://getridbug.com/node-js/how-to-minify-multiple-javascript-files-in-a-folder-with-uglifyjs/
public class Minify {
    private static ExecutorService T;

    private static List<PathMatcher> pathIncludes;

    private static List<PathMatcher> pathExcludes;

    private static List<PathMatcher> fileIncludes;

    private static List<PathMatcher> fileIncludesBasic;

    private static List<PathMatcher> fileExcludes;

    private static boolean isWindows;

    private static boolean alwaysNew = true;

    private static List<String> processed = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("===========");
        T = Executors.newSingleThreadExecutor();
        try {
            for (int i = 0; i < args.length; i++) {
                System.out.println(i + "\t\t" + args[i]);
            }
            Path path;
            Path module;
            if (args.length > 0) {
                module = Paths.get(args[0]);
                path = module.resolve(args[3]);
            } else {
                module = Paths.get(System.getProperty("user.dir"));
                path = module.resolve("src").resolve("main").resolve("resources");
            }
            Path config = module.resolve("Minify.config");
            Properties props = new Properties();
            String globPrefix = "glob:**/";
            fileIncludesBasic = Arrays.asList(FileSystems.getDefault().getPathMatcher(globPrefix + "*.js"), FileSystems.getDefault().getPathMatcher(globPrefix + "*.css"));
            pathIncludes = Arrays.asList(FileSystems.getDefault().getPathMatcher(globPrefix + "*"));
            pathExcludes = new ArrayList<>();
            fileIncludes = new ArrayList<>();
            fileExcludes = new ArrayList<>(Arrays.asList(FileSystems.getDefault().getPathMatcher(globPrefix + "*.min.js"), FileSystems.getDefault().getPathMatcher(globPrefix + "*.min.css")));
            if (Files.exists(config)) {
                System.err.println(config);
                props.load(Files.newInputStream(config));
                if (props.getProperty("pathIncludes") != null) Arrays.stream(props.getProperty("pathIncludes").split(";")).forEach(pathInclude -> System.out.println("pathInclude: " + pathInclude));
                if (props.getProperty("fileIncludes") != null) Arrays.stream(props.getProperty("fileIncludes").split(";")).forEach(fileInclude -> System.out.println("fileInclude: " + fileInclude));
                if (props.getProperty("pathExcludes") != null) Arrays.stream(props.getProperty("pathExcludes").split(";")).forEach(pathExclude -> System.out.println("pathExclude: " + pathExclude));
                if (props.getProperty("fileExcludes") != null) Arrays.stream(props.getProperty("fileExcludes").split(";")).forEach(fileExclude -> System.out.println("fileExclude: " + fileExclude));
                if (props.getProperty("new") != null) {
                    alwaysNew = "true".equals(props.getProperty("new")) || "1".equals(props.getProperty("new"));
                }
                if (props.getProperty("pathIncludes") != null) {
                    pathIncludes = Arrays.stream(props.getProperty("pathIncludes").split(";")).map(p -> p.endsWith("/") ? p.substring(0, p.length() - 1) : p).map(glob -> FileSystems.getDefault().getPathMatcher(globPrefix + glob)).collect(Collectors.toList());
                }
                if (props.getProperty("pathExcludes") != null) {
                    pathExcludes.addAll(
                            Arrays.stream(props.getProperty("pathExcludes").split(";")).map(p -> p.endsWith("/") ? p.substring(0, p.length() - 1) : p).map(glob -> FileSystems.getDefault().getPathMatcher(globPrefix + glob)).collect(Collectors.toList()));
                }
                if (props.getProperty("fileIncludes") != null) {
                    fileIncludes = Arrays.stream(props.getProperty("fileIncludes").split(";")).map(glob -> FileSystems.getDefault().getPathMatcher(globPrefix + glob)).collect(Collectors.toList());
                }
                if (props.getProperty("fileExcludes") != null) {
                    fileExcludes.addAll(Arrays.stream(props.getProperty("fileExcludes").split(";")).map(glob -> FileSystems.getDefault().getPathMatcher(globPrefix + glob)).collect(Collectors.toList()));
                }
            }
            isWindows = System.getProperty("os.name").toLowerCase().contains("windows");
            // System.out.println("?windows " + isWindows);
            Path node_modules;
            if (isWindows) {
                node_modules = Paths.get(System.getenv("APPDATA"), "npm", "node_modules");
                if (Files.notExists(node_modules)) node_modules = Paths.get(System.getenv("APPDATA"), "roaming", "npm", "node_modules");
                if (Files.notExists(node_modules)) node_modules = Paths.get(System.getenv("NVM_BIN"), "node_modules");
            } else {
                node_modules = Paths.get(Paths.get(System.getenv("NVM_BIN")).toFile().getCanonicalPath()).getParent().resolve("lib").resolve("node_modules");
            }
            if (Files.notExists(node_modules)) throw new IllegalArgumentException("node_modules");
            System.out.println(node_modules);
            System.out.println(path);
            System.out.println();
            Path _node_modules = node_modules;
            Files.walkFileTree(path, new HashSet<FileVisitOption>(), 20, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path p, BasicFileAttributes attrs) throws IOException {
                    if (pathIncludes.stream().anyMatch(i -> i.matches(p)) && pathExcludes.stream().noneMatch(i -> i.matches(p))) {
                        // System.out.println("?dir accept " + p);
                        return FileVisitResult.CONTINUE;
                    }
                    // System.out.println("?dir skip " + p);
                    return FileVisitResult.SKIP_SIBLINGS;
                }

                @Override
                public FileVisitResult visitFile(Path p, BasicFileAttributes attrs) throws IOException {
                    if (fileIncludesBasic.stream().anyMatch(i -> i.matches(p)) && fileExcludes.stream().noneMatch(i -> i.matches(p))) {
                        // System.out.println("?file " + p);
                        String shortname = p.toString().replace(path.toString(), "").substring(1).replace("\\", "/");
                        try {
                            if (resource(_node_modules, path, p)) {
                                processed.add(shortname);
                            }
                        } catch (IOException ex) {
                            System.out.println(shortname);
                            ex.printStackTrace(System.out);
                        } catch (InterruptedException ex) {
                            //
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
            Files.walkFileTree(path, new HashSet<FileVisitOption>(), 20, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path p, BasicFileAttributes attrs) throws IOException {
                    if (fileIncludesBasic.stream().anyMatch(i -> i.matches(p)) && fileIncludes.stream().anyMatch(i -> i.matches(p)) && fileExcludes.stream().noneMatch(i -> i.matches(p))) {
                        // System.out.println("?file " + p);
                        String shortname = p.toString().replace(path.toString(), "").substring(1).replace("\\", "/");
                        try {
                            if (resource(_node_modules, path, p)) {
                                processed.add(shortname);
                            }
                        } catch (IOException ex) {
                            System.out.println(shortname);
                            ex.printStackTrace(System.out);
                        } catch (InterruptedException ex) {
                            //
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }
            });
            System.out.println("-----------");
            processed.stream().map(s -> s + ";").forEach(System.out::println);
        } catch (Exception ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                T.shutdownNow();
            } catch (Exception ex2) {
                //
            }
        }
        System.out.println("===========");
    }

    private static void run(File dir, Consumer<String> consumer, String... cmd) throws IOException, InterruptedException {
        System.out.println(dir + "> ");
        System.out.println(Arrays.stream(cmd).collect(Collectors.joining(" ")));
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(cmd);
        builder.directory(dir);
        Process process = builder.start();
        InputStream in = process.getInputStream();
        T.submit(() -> new BufferedReader(new InputStreamReader(in)).lines().forEach(consumer));
        int exitCode = process.waitFor();
        System.out.println(exitCode);
        assert exitCode == 0;
    }

    protected static boolean resource(Path node_modules, Path root, Path child) throws IOException, InterruptedException {
        String filename = child.getFileName().toString();
        boolean b = false;
        if (filename.endsWith(".js") && !filename.endsWith(".min.js") && Files.notExists(child.getParent().resolve(filename + ".no-min.txt"))) {
            Path min = child.getParent().resolve(filename.replaceAll("\\.js$", "") + ".min.js");
            System.out.println(child.toString().replace(root.toString(), "").replace("\\", "/"));
            System.out.println("\t" + min.getFileName().toString().replace("\\", "/"));
            System.out.print("\t" + Files.getLastModifiedTime(child));
            if (Files.exists(min))
                System.out.print(" < " + Files.getLastModifiedTime(min) + " ? " + (Files.getLastModifiedTime(min).toMillis() < Files.getLastModifiedTime(child).toMillis()));
            else if (alwaysNew) System.out.print(" < - ? true");
            System.out.println();
            if ((alwaysNew && Files.notExists(min)) || (Files.exists(min) && Files.getLastModifiedTime(min).toMillis() < Files.getLastModifiedTime(child).toMillis())) {
                run(isWindows ? node_modules.getParent().toFile() : //
                        node_modules.resolve("uglify-js").resolve("bin").toFile(), //
                        System.out::println, "uglifyjs" + (isWindows ? ".cmd" : ""), child.toString().replace("\\", "/"), "-o", min.toString().replace("\\", "/"));
                b = true;
            }
            System.out.println();
        } else if (filename.endsWith(".css") && !filename.endsWith(".min.css") && Files.notExists(child.getParent().resolve(filename + ".no-min.txt"))) {
            Path min = child.getParent().resolve(filename.replaceAll("\\.css$", "") + ".min.css");
            System.out.println(child.toString().replace(root.toString(), "").replace("\\", "/"));
            System.out.println("\t" + min.getFileName().toString().replace("\\", "/"));
            System.out.print("\t" + Files.getLastModifiedTime(child));
            if (Files.exists(min))
                System.out.print(" < " + Files.getLastModifiedTime(min) + " ? " + (Files.getLastModifiedTime(min).toMillis() < Files.getLastModifiedTime(child).toMillis()));
            else if (alwaysNew) System.out.print(" < - ? true");
            System.out.println();
            if ((alwaysNew && Files.notExists(min)) || (Files.exists(min) && Files.getLastModifiedTime(min).toMillis() < Files.getLastModifiedTime(child).toMillis())) {
                run(isWindows ? node_modules.getParent().toFile() : //
                        node_modules.resolve("csso-cli").resolve("bin").toFile(), //
                        System.out::println, "csso" + (isWindows ? ".cmd" : ""), "-i", child.toString().replace("\\", "/"), "-o", min.toString().replace("\\", "/"));
                b = true;
            }
            System.out.println();
        }
        return b;
    }
}
