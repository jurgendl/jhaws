package org.swingeasy;

import javax.swing.text.StyledEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

public class ETextPaneConfig extends EComponentConfig<ETextPaneConfig> {
    protected StyledEditorKit kit;

    public ETextPaneConfig() {
        super();
    }

    public StyledEditorKit getKit() {
        if (this.kit == null) {
            this.kit = new RTFEditorKit();
        }
        return this.kit;
    }

    public ETextPaneConfig setKit(StyledEditorKit kit) {
        this.kit = kit;
        return this;
    }
}
