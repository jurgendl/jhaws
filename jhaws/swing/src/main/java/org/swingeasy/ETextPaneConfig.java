package org.swingeasy;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

public class ETextPaneConfig extends EComponentConfig<ETextPaneConfig> {
    protected StyledEditorKit kit;

    public ETextPaneConfig() {
        super();
    }

    public StyledEditorKit getKit() {
        if (kit == null) {
            kit = new RTFEditorKit();
        }
        return kit;
    }

    public ETextPaneConfig setKit(StyledEditorKit kit) {
        this.kit = kit;
        return this;
    }
}
