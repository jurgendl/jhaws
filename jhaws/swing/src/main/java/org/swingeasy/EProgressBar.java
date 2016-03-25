package org.swingeasy;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;
import javax.swing.ToolTipManager;

// ProgressBar.background Color
// ProgressBar.border Border
// ProgressBar.cellLength Integer
// ProgressBar.cellSpacing Integer
// ProgressBar.cycleTime Integer
// ProgressBar.font Font
// ProgressBar.foreground Color
// ProgressBar.highlight Color
// ProgressBar.horizontalSize Dimension
// ProgressBar.repaintInterval Integer
// ProgressBar.selectionBackground Color
// ProgressBar.selectionForeground Color
// ProgressBar.shadow Color
// ProgressBar.verticalSize Dimension
// ProgressBarUI String

/**
 * @author Jurgen
 */
public class EProgressBar extends JProgressBar {
    private static final long serialVersionUID = -4208232590900954548L;

    public EProgressBar() {
        this.init();
    }

    public EProgressBar(BoundedRangeModel newModel) {
        super(newModel);
        this.init();
    }

    public EProgressBar(int orient) {
        super(orient);
        this.init();
    }

    public EProgressBar(int min, int max) {
        super(min, max);
        this.init();
    }

    public EProgressBar(int orient, int min, int max) {
        super(orient, min, max);
        this.init();
    }

    /**
     * 
     * @see javax.swing.JComponent#getToolTipText()
     */
    @Override
    public String getToolTipText() {
        String toolTipText = super.getToolTipText();
        if (toolTipText == null) {
            String text = this.getString();
            if (text.trim().length() == 0) {
                text = null;
            }
            return text;
        }
        return toolTipText;
    }

    protected void init() {
        // if (cfg.isTooltips()) {
        ToolTipManager.sharedInstance().registerComponent(this);
        // }
    }
}
