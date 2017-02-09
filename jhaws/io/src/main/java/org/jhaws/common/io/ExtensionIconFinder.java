package org.jhaws.common.io;

import javax.swing.Icon;

public interface ExtensionIconFinder {

    public abstract Icon getLargeIcon(FilePath file);

    public abstract Icon getSmallIcon(FilePath file);
}
