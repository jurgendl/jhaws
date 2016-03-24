package org.jhaws.common.svn;

import java.io.File;

import org.jhaws.common.svn.xml.ChangeList;
import org.jhaws.common.svn.xml.Entry;
import org.jhaws.common.svn.xml.EntryList;
import org.jhaws.common.svn.xml.LogEntry;
import org.jhaws.common.svn.xml.SvnLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SvnExecTest {
	private static final Logger logger = LoggerFactory.getLogger(SvnExecTest.class);

	public static void main(String[] args) {
		try {
			File projectdir = new File("C:/java/workspaces/GISMO/komodo");
			System.out.println(SvnExec.svn_info(projectdir));
			SvnLog svn_log = SvnExec.svn_log(projectdir, "10000", 20);
			for (LogEntry le : svn_log)
				System.out.println(le);
			for (ChangeList cl : SvnExec.svn_status(projectdir))
				System.out.println(cl);
			for (EntryList el : SvnExec.svn_ls(projectdir, "svn://savannah.ugent.be/komodo/branches/"))
				for (Entry e : el)
					System.out.println(e);
		} catch (Exception ex) {
			logger.error("{}", ex);
		}
	}
}
