package org.jhaws.common.svn;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Properties;

import org.jhaws.common.lang.Executor;
import org.jhaws.common.lang.IMap;
import org.jhaws.common.lang.Value;
import org.jhaws.common.svn.xml.SvnInfo;
import org.jhaws.common.svn.xml.SvnList;
import org.jhaws.common.svn.xml.SvnLog;
import org.jhaws.common.svn.xml.SvnStatus;

public class SvnExec {
	private static String[] cmds_svn_revert;

	private static String[] cmds_svn_ls;

	private static String[] cmds_svn_update;

	private static String[] cmds_svn_status;

	private static String[] cmds_svn_commit;

	private static String[] cmds_svn_changelist;

	private static String[] cmds_svn_changelist_clear;

	private static String[] cmds_svn_info;

	private static String[] cmds_svn_switch;

	private static String[] cmds_svn_log;

	private static String[] cmds_svn_copy;

	private static String[] cmds_svn_log2;

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
		cmds_svn_log2 = settings.getProperty("cmds.svn.log2").split(" ");
	}

	public static IMap<String, String> createParameters() {
		return new IMap<String, String>().add("path", ".");
	}

	/**
	 * svn ls --xml {path} --non-interactive
	 */
	public static SvnList list(File projectdir, String path) {
		return Svn.lists(new ByteArrayInputStream(Executor.exec(new Value<>(-1), false, projectdir, new IMap<String, String>().add("path", path), cmds_svn_ls)));
	}

	/**
	 * svn --quiet revert {path} --non-interactive --depth infinity
	 */
	public static int revert(File projectdir) {
		Value<Integer> returnValue = new Value<>(-1);
		Executor.exec(returnValue, true, projectdir, createParameters(), cmds_svn_revert);
		return returnValue.getValue();
	}

	/**
	 * svn update {path} --non-interactive --accept postpone
	 */
	public static int update(File projectdir) {
		Value<Integer> returnValue = new Value<>(-1);
		Executor.exec(returnValue, true, projectdir, createParameters(), cmds_svn_update);
		return returnValue.getValue();
	}

	/**
	 * svn status {path} {noignore} --non-interactive --xml --show-updates
	 */
	public static SvnStatus status(File projectdir, boolean noIgnore) {
		return Svn.status(
				new ByteArrayInputStream(Executor.exec(new Value<>(-1), false, projectdir, createParameters().add("noignore", noIgnore ? "--no-ignore" : ""), cmds_svn_status)));
	}

	/**
	 * svn commit {path} --non-interactive --file {log} --changelist {changelistName}
	 */
	public static int commit(File projectdir, String changelistName, File logfile) {
		Value<Integer> returnValue = new Value<>(-1);
		Executor.exec(returnValue, true, projectdir, createParameters().add("changelistName", changelistName).add("log", logfile.getAbsolutePath()), cmds_svn_commit);
		return returnValue.getValue();
	}

	/**
	 * svn changelist {changelistName} {files} --non-interactive
	 */
	public static int changelist(File projectdir, String changelistName, String files) {
		Value<Integer> returnValue = new Value<>(-1);
		Executor.exec(returnValue, true, projectdir, createParameters().add("changelistName", changelistName).add("files", files), cmds_svn_changelist);
		return returnValue.getValue();
	}

	/**
	 * svn changelist --remove --changelist {changelistName} --depth infinity {path} --non-interactive
	 */
	public static int changelistClear(File projectdir, String changelistName) {
		Value<Integer> returnValue = new Value<>(-1);
		Executor.exec(returnValue, true, projectdir, createParameters().copy().add("changelistName", changelistName), cmds_svn_changelist_clear);
		return returnValue.getValue();
	}

	/**
	 * svn info {path} --non-interactive --xml
	 */
	public static SvnInfo info(File projectdir) {
		return info(projectdir, ".");
	}

	/**
	 * svn info {path} --non-interactive --xml
	 */
	public static SvnInfo info(File projectdir, String path) {
		return Svn.info(new ByteArrayInputStream(Executor.exec(new Value<>(-1), false, projectdir, new IMap<String, String>().add("path", path), cmds_svn_info)));
	}

	/**
	 * svn switch {url} {path} --non-interactive
	 */
	public static int switchBranch(File projectdir, String latest) {
		Value<Integer> returnValue = new Value<>(-1);
		Executor.exec(returnValue, true, projectdir, createParameters().add("url", latest), cmds_svn_switch);
		return returnValue.getValue();
	}

	/**
	 * svn log -r {r}:HEAD --stop-on-copy --limit {limit} --xml {path} --non-interactive
	 */
	public static SvnLog log(File projectdir, String r, int limit) {
		return Svn
				.log(new ByteArrayInputStream(Executor.exec(new Value<>(-1), false, projectdir, createParameters().add("r", r).add("limit", String.valueOf(limit)), cmds_svn_log)));
	}

	/**
	 * svn log -v -q --stop-on-copy --xml {path} --non-interactive
	 */
	public static SvnLog log2(File projectdir, String path) {
		return Svn.log(new ByteArrayInputStream(Executor.exec(new Value<>(-1), false, projectdir, new IMap<String, String>().add("path", path), cmds_svn_log2)));
	}

	/**
	 * svn copy {branchurl} {path} -m [release] --non-interactive
	 */
	public static int copy(File projectdir, String branchurl, String path) {
		Value<Integer> returnValue = new Value<>(-1);
		Executor.exec(returnValue, false, projectdir, createParameters().add("branchurl", branchurl).add("path", path), cmds_svn_copy);
		return returnValue.getValue();
	}
}
