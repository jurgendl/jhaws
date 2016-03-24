package org.jhaws.common.svn;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.jhaws.common.svn.xml.Svn;
import org.jhaws.common.svn.xml.SvnInfo;
import org.jhaws.common.svn.xml.SvnList;
import org.jhaws.common.svn.xml.SvnLog;
import org.jhaws.common.svn.xml.SvnStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SvnExec {
	private static final Logger logger = LoggerFactory.getLogger(SvnExec.class);

	public static void replace(ValueHolder<String> v, Map.Entry<String, String> entry) {
		v.setValue(v.getValue().replaceAll("\\{" + entry.getKey() + "\\}", String.valueOf(entry.getValue())));
	}

	public static byte[] exec(boolean print, File projectdir, IMap<String, String> map, String... cmd) {
		logger.info("{}", projectdir);
		logger.info("{}", Arrays.stream(cmd).collect(Collectors.joining(" ")));
		if (map != null) {
			map.forEach((k, v) -> logger.info("{}={}", k, v));
			cmd = Arrays.stream(cmd).map((String cmdri) -> {
				ValueHolder<String> v = new ValueHolder<>(cmdri);
				map.entrySet().stream().forEach(entry -> replace(v, entry));
				return v.getValue();
			}).collect(Collectors.toList()).toArray(new String[0]);
		}
		logger.info("{}", Arrays.stream(cmd).collect(Collectors.joining(" ")));
		final ByteArrayOutputStream bout = new ByteArrayOutputStream();
		Executor.ParseProcessOutput boutppo = line -> {
			try {
				bout.write(line.getBytes());
			} catch (IOException ex) {
				logger.error("{}", ex);
				throw new RuntimeException(ex);
			}
		};
		try {
			Executor.parseProcessOutput(Executor.create(projectdir, null, cmd).start(),
					print ? new Executor.MultiParseProcessOutput(boutppo, line -> logger.info("{}", line)) : boutppo);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
		byte[] byteArray = bout.toByteArray();
		logger.info(">> {}", new String(byteArray));
		return byteArray;
	}

	public static InputStream call(File projectRoot, IMap<String, String> prms, String... cmd) {
		return new ByteArrayInputStream(exec(false, projectRoot, prms, cmd));
	}

	static String[] cmds_svn_revert;

	static String[] cmds_svn_ls;

	static String[] cmds_svn_update;

	static String[] cmds_svn_status;

	static String[] cmds_svn_commit;

	static String[] cmds_svn_changelist;

	static String[] cmds_svn_changelist_clear;

	static String[] cmds_svn_info;

	static String[] cmds_svn_switch;

	static String[] cmds_svn_log;

	static String[] cmds_svn_copy;

	static String[] cmds_svn_log_tag;

	static String[] cmds_svn_ls_tag;

	static {
		Properties settings = new Properties();
		try {
			settings.load(Svn.class.getClassLoader().getResourceAsStream("svn.properties"));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
		cmds_svn_ls = settings.getProperty("cmds.svn.ls").split(" ");
		cmds_svn_revert = settings.getProperty("cmds.svn.revert").split(" ");
		cmds_svn_update = settings.getProperty("cmds.svn.update").split(" ");
		cmds_svn_status = settings.getProperty("cmds.svn.status").split(" ");
		cmds_svn_commit = settings.getProperty("cmds.svn.commit").split(" ");
		cmds_svn_changelist = settings.getProperty("cmds.svn.changelist").split(" ");
		cmds_svn_changelist_clear = settings.getProperty("cmds.svn.changelist.clear").split(" ");
		cmds_svn_info = settings.getProperty("cmds.svn.info").split(" ");
		cmds_svn_switch = settings.getProperty("cmds.svn.switch").split(" ");
		cmds_svn_log = settings.getProperty("cmds.svn.log").split(" ");
		cmds_svn_copy = settings.getProperty("cmds.svn.copy").split(" ");
		cmds_svn_log_tag = settings.getProperty("cmds.svn.log.tag").split(" ");
		cmds_svn_ls_tag = settings.getProperty("cmds.svn.ls.tag").split(" ");
	}

	/**
	 * svn ls --xml {path}
	 */
	public static SvnList svn_ls(File projectdir, String branchroot) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", branchroot);
		return Svn.lists(new ByteArrayInputStream(exec(false, projectdir, parameters, cmds_svn_ls)));
	}

	/**
	 * svn --quiet revert {path}
	 */
	public static void svn_revert(File projectdir) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".");
		exec(true, projectdir, parameters, cmds_svn_revert);
	}

	/**
	 * svn update {path} --non-interactive --accept postpone
	 */
	public static void svn_update(File projectdir) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".");
		exec(true, projectdir, parameters, cmds_svn_update);
	}

	/**
	 * svn status {path} --non-interactive --xml --show-updates
	 */
	public static SvnStatus svn_status(File projectdir) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".");
		return Svn.status(new ByteArrayInputStream(exec(false, projectdir, parameters, cmds_svn_status)));
	}

	/**
	 * svn commit {path} --non-interactive --file {log} --changelist {cl}
	 */
	public static void svn_commit(File projectdir, String clname, File logfile) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".").add("cl", clname).add("log", logfile.getAbsolutePath());
		exec(true, projectdir, parameters, cmds_svn_commit);
	}

	/**
	 * svn changelist {cl} {files}
	 */
	public static void svn_changelist(File projectdir, String clname, String el) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".").add("cl", clname).add("files", el);
		exec(true, projectdir, parameters, cmds_svn_changelist);
	}

	/**
	 * svn changelist --remove --changelist {cl} --depth infinity {path}
	 */
	public static void svn_changelist_clear(File projectdir, String clname) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".").copy().add("cl", clname);
		exec(true, projectdir, parameters, cmds_svn_changelist_clear);
	}

	/**
	 * svn info {path} --non-interactive --xml
	 */
	public static SvnInfo svn_info(File projectdir) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".");
		return Svn.info(new ByteArrayInputStream(exec(false, projectdir, parameters, cmds_svn_info)));
	}

	/**
	 * svn switch {url} {path}
	 */
	public static void svn_switch(File projectdir, String latest) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".").add("url", latest);
		exec(true, projectdir, parameters, cmds_svn_switch);
	}

	/**
	 * svn log -r {r}:HEAD --stop-on-copy --limit {limit} --xml {path}
	 */
	public static SvnLog svn_log(File projectdir, String r, int limit) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".").add("r", r).add("limit", String.valueOf(limit));
		return Svn.log(new ByteArrayInputStream(exec(false, projectdir, parameters, cmds_svn_log)));
	}

	/**
	 * svn copy {branchurl} {tag} -m [release]
	 */
	public static void svn_copy(File projectdir, String branchurl, String tag) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".").add("branchurl", branchurl).add("tag", tag);
		exec(false, projectdir, parameters, cmds_svn_log);
	}

	/**
	 * svn log -v -q --stop-on-copy --xml {tag} {path}
	 */
	public static SvnLog svn_log_tag(File projectdir, String tag) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".").add("tag", tag);
		return Svn.log(new ByteArrayInputStream(exec(false, projectdir, parameters, cmds_svn_log_tag)));
	}

	/**
	 * svn ls --xml {path} "^/tag"
	 */
	public static SvnList svn_ls_tag(File projectdir) {
		IMap<String, String> parameters = new IMap<String, String>().add("path", ".");
		return Svn.lists(new ByteArrayInputStream(exec(false, projectdir, parameters, cmds_svn_ls_tag)));
	}
}
