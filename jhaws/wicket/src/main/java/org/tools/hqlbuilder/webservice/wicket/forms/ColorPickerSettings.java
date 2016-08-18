package org.tools.hqlbuilder.webservice.wicket.forms;

import java.util.List;

/**
 * palette: [<br>
 * ['black', 'white', 'blanchedalmond'],<br>
 * ['rgb(255, 128, 0);', 'hsv 100 70 50', 'lightyellow']<br>
 * ]<br>
 * containerClassName: 'x'<br>
 * replacerClassName: 'x'<br>
 */
public class ColorPickerSettings extends AbstractFormElementSettings<ColorPickerSettings> {
    private static final long serialVersionUID = 6407342468858453586L;

    public static enum ColorFormat {
        hex, hex3, hsl, rgb, name, none;
    }

    protected String color;

    protected ColorFormat preferredFormat = ColorFormat.hsl;

    protected Boolean allowEmpty;

    protected Boolean showInput = true;

    protected Boolean showAlpha = true;

    protected Boolean disabled;

    protected Boolean showPalette = true;

    protected Boolean showPaletteOnly;

    protected Boolean showSelectionPalette = true;

    protected List<List<String>> palette;

    protected List<String> selectionPalette;

    protected Boolean clickoutFiresChange;

    protected Boolean showInitial;

    protected String chooseText;

    protected String cancelText;

    protected Boolean showButtons = true;

    protected Boolean flat;

    public ColorPickerSettings() {
        super();
    }

    public ColorPickerSettings(String color) {
        this.color = color;
    }

    public ColorFormat getPreferredFormat() {
        return this.preferredFormat;
    }

    public Boolean isAllowEmpty() {
        return this.allowEmpty;
    }

    public Boolean isShowInput() {
        return this.showInput;
    }

    public Boolean isShowAlpha() {
        return this.showAlpha;
    }

    public Boolean isDisabled() {
        return this.disabled;
    }

    public Boolean isShowPalette() {
        return this.showPalette;
    }

    public Boolean isShowPaletteOnly() {
        return this.showPaletteOnly;
    }

    public Boolean isShowSelectionPalette() {
        return this.showSelectionPalette;
    }

    public List<List<String>> getPalette() {
        return this.palette;
    }

    public Boolean isClickoutFiresChange() {
        return this.clickoutFiresChange;
    }

    public Boolean isShowInitial() {
        return this.showInitial;
    }

    public String getChooseText() {
        return this.chooseText;
    }

    public String getCancelText() {
        return this.cancelText;
    }

    public Boolean isShowButtons() {
        return this.showButtons;
    }

    public void setPreferredFormat(ColorFormat preferredFormat) {
        this.preferredFormat = preferredFormat;
    }

    public void setAllowEmpty(Boolean allowEmpty) {
        this.allowEmpty = allowEmpty;
    }

    public void setShowInput(Boolean showInput) {
        this.showInput = showInput;
    }

    public void setShowAlpha(Boolean showAlpha) {
        this.showAlpha = showAlpha;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public void setShowPalette(Boolean showPalette) {
        this.showPalette = showPalette;
    }

    public void setShowPaletteOnly(Boolean showPaletteOnly) {
        this.showPaletteOnly = showPaletteOnly;
    }

    public void setShowSelectionPalette(Boolean showSelectionPalette) {
        this.showSelectionPalette = showSelectionPalette;
    }

    public void setPalette(List<List<String>> palette) {
        this.palette = palette;
    }

    public void setClickoutFiresChange(Boolean clickoutFiresChange) {
        this.clickoutFiresChange = clickoutFiresChange;
    }

    public void setShowInitial(Boolean showInitial) {
        this.showInitial = showInitial;
    }

    public void setChooseText(String chooseText) {
        this.chooseText = chooseText;
    }

    public void setCancelText(String cancelText) {
        this.cancelText = cancelText;
    }

    public void setShowButtons(Boolean showButtons) {
        this.showButtons = showButtons;
    }

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean isFlat() {
        return this.flat;
    }

    public void setFlat(Boolean flat) {
        this.flat = flat;
    }

    public Boolean getAllowEmpty() {
        return this.allowEmpty;
    }

    public Boolean getShowInput() {
        return this.showInput;
    }

    public Boolean getShowAlpha() {
        return this.showAlpha;
    }

    public Boolean getDisabled() {
        return this.disabled;
    }

    public Boolean getShowPalette() {
        return this.showPalette;
    }

    public Boolean getShowPaletteOnly() {
        return this.showPaletteOnly;
    }

    public Boolean getShowSelectionPalette() {
        return this.showSelectionPalette;
    }

    public List<String> getSelectionPalette() {
        return this.selectionPalette;
    }

    public Boolean getClickoutFiresChange() {
        return this.clickoutFiresChange;
    }

    public Boolean getShowInitial() {
        return this.showInitial;
    }

    public Boolean getShowButtons() {
        return this.showButtons;
    }

    public Boolean getFlat() {
        return this.flat;
    }

    public void setSelectionPalette(List<String> selectionPalette) {
        this.selectionPalette = selectionPalette;
    }
}