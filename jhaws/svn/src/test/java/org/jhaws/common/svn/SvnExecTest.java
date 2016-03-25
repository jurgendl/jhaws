package org.jhaws.common.svn;

import java.io.File;

import org.jhaws.common.svn.xml.ChangeList;
import org.jhaws.common.svn.xml.Entry;
import org.jhaws.common.svn.xml.EntryList;
import org.jhaws.common.svn.xml.LogEntry;
import org.jhaws.common.svn.xml.SvnInfo;
import org.jhaws.common.svn.xml.SvnList;
import org.jhaws.common.svn.xml.SvnLog;
import org.jhaws.common.svn.xml.SvnStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SvnExecTest {
	private static final Logger logger = LoggerFactory.getLogger(SvnExecTest.class);

	public static void main(String[] args) {
		try {
			File projectdir = new File(args[0]);
			System.out.println("=========================");
			SvnInfo svn_info = SvnExec.svn_info(projectdir);
			System.out.println(svn_info);
			System.out.println("=========================");
			SvnLog svn_log = SvnExec.svn_log(projectdir, "10000", 20);
			for (LogEntry le : svn_log)
				System.out.println(le);
			System.out.println("=========================");
			SvnStatus svn_status = SvnExec.svn_status(projectdir);
			for (ChangeList cl : svn_status)
				System.out.println(cl);
			System.out.println("=========================");
			SvnList svn_ls = SvnExec.svn_ls(projectdir, svn_info.getEntry().getUrl().replaceAll("trunk", "tags"));
			for (EntryList el : svn_ls)
				for (Entry e : el)
					System.out.println(e);
			System.out.println("=========================");
			SvnList svn_ls_tag = SvnExec.svn_ls_tag(projectdir, svn_info.getEntry().getUrl().replaceAll("trunk", "tags"));
			for (EntryList el : svn_ls_tag)
				for (Entry e : el)
					System.out.println(e);
			System.out.println("=========================");
		} catch (Exception ex) {
			logger.error("{}", ex);
		}
	}
}
