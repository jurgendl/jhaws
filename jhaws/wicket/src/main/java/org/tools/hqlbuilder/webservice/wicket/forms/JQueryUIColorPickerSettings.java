package org.tools.hqlbuilder.webservice.wicket.forms;

import java.util.Locale;

public class JQueryUIColorPickerSettings extends AbstractFormElementSettings<JQueryUIColorPickerSettings> {
    private static final long serialVersionUID = 1732670371494903296L;

    public static enum JQueryColorPickerLocale {
        de, en, fr, nl, pt_BR, ru;

        @Override
        public String toString() {
            return name().toLowerCase().replace('_', '-');
        };
    }

    /**
     * Regional (in the i18n directory) plugins contain localized texts (a.k.a. translations). A number of languages is provided. Included regionals:
     * de German (Deutsch). en English (default). fr French (Francais). nl Dutch (Nederlands). pt-br Brazilian Portuguese.
     */
    protected JQueryColorPickerLocale regional;

    /**
     * Determine which parts to display. Use any of the preset names ('full', 'popup' or 'inline') or specify an array of part names (i.e. ['header',
     * 'map', 'bar', 'hex', 'hsv', 'rgb', 'alpha', 'lab', 'cmyk', 'preview', 'swatches', 'footer']). If an empty string is given, the parts will be
     * automatically chosen as preset 'popup' or 'inline' depending on the context in which the colorpicker is used.
     */
    protected String parts = "full";

    /**
     * null > false<br>
     * Whether or not to show the inputs for alpha.
     */
    protected Boolean alpha = true;

    /**
     * null > true<br>
     * Change the opacity of the altField element(s) according to the alpha setting.
     */
    protected Boolean altAlpha;

    /**
     * null > true<br>
     * If true, the altField element(s) are updated on every change, otherwise only upon closing.
     */
    protected Boolean altOnChange;

    /**
     * null > false<br>
     * If true, the dialog opens automatically upon page load.
     */
    protected Boolean autoOpen;

    /** If this option is set to a string, the button will be assigned the class specified. */
    protected String buttonClass = "jquerycolorpickerbuttonimage";

    /**
     * null > false<br>
     * If a buttonimage is specified, change the background color of the image when the color is changed.
     */
    protected Boolean buttonColorize = true;

    /** null > 'images/ui-colorpicker.png' */
    protected String buttonImage = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7";

    /** null > false */
    protected Boolean buttonImageOnly;

    /** Same as jQueryUI DatePicker. If null, use language default. */
    protected String buttonText;

    /**
     * null > true<br>
     * Close the window when pressing the Escape key on the keyboard.
     */
    protected Boolean closeOnEscape;

    /**
     * null > true<br>
     * Close the window when clicking outside the colorpicker display.
     */
    protected Boolean closeOnOutside;

    /**
     * null > 'HEX'<br>
     * <br>
     * Specifies the format of the color string returned in callbacks. You can either specify one of the predefined formats:<br>
     * #HEX #112233<br>
     * #HEX3 #123 if possible, otherwise false.<br>
     * HEX 112233<br>
     * HEX3 123 if possible, otherwise false.<br>
     * RGB rgb(123,45,67) if opaque, otherwise false.<br>
     * RGBA rgba(123,45,67,0.123%)<br>
     * RGB% rgb(12%,34%,56%) if opaque, otherwise false.<br>
     * RGBA% rgba(12%,34%,56%,0.123%)<br>
     * HSL hsl(123,45,67) if opaque, otherwise false.<br>
     * HSLA hsla(123,45,67,0.123%)<br>
     * HSL% hsl(12%,34%,56%) if opaque, otherwise false.<br>
     * HSLA% hsla(12%,34%,56%,0.123%)<br>
     * NAME Closest color name<br>
     * EXACT Exact name if possible, otherwise false.<br>
     */
    protected String colorFormat = "#HEX";

    /**
     * null > true<br>
     * Make the dialog draggable if the header is visible and the dialog is not inline.
     */
    protected Boolean draggable;

    /** null > 'fast' */
    protected String duration;

    /**
     * null > true<br>
     * Whether or not to show the inputs for HSV.
     */
    protected Boolean hsv;

    /**
     * null > true<br>
     * If enabled, shows a border and background when inline. Disabling may allow closer integration.
     */
    protected Boolean inlineFrame;

    /**
     * null > ''<br>
     * <br>
     * Limit the selectable colors to any of the predefined limits:<br>
     * '' No limitations, allow 8bpp color for a palette of all 16 million colors.<br>
     * websafe Set of 216 colors composed of 00, 33, 66, 99, cc and ff color channel values in #rrggbb.<br>
     * nibble 4 bits per color, can be easily converted to #rgb format. The palette is limited to 4096 colors.<br>
     * binary Allow only #00 or #ff as color channel values for primary colors only; only 8 colors are available with this limit.<br>
     * name Limit to closest color name.<br>
     */
    protected String limit;

    /**
     * null > 'h'<br>
     * <br>
     * Determines the functionality of the map and bar components. Allowed values are; 'h', 's', 'l', 'r', 'g', 'b' or 'a', for hue, saturation,
     * luminosity, red, green, blue and alpha respectively.
     */
    protected String mode;

    /**
     * null > false<br>
     * Ensures no other controls on screen can be used while the dialog is opened. Also look at showCancelButton and closeOnEscape to use in
     * combination with the modal option. closeOnOutside is redundant when used with modal.
     */
    protected Boolean modal;

    /**
     * null > false<br>
     * Close the window when pressing the Enter key on the keyboard, keeping the selected color.
     */
    protected Boolean okOnEnter;

    /**
     * null > false<br>
     * If enabled, closing the dialog through any means but the OK button will revert the color back to the previous state, as if pressing the Cancel
     * button. The revert option changes the behaviour of the [X] button in the header, the Escape keyboard button and clicking outside the dialog,
     * when any of these features are enabled.
     */
    protected Boolean revert;

    /**
     * null > false<br>
     * Whether or not to show the inputs for RGB.
     */
    protected Boolean rgb;

    /** null > 'fadeIn' */
    protected String showAnim;

    /**
     * null > true<br>
     * Show the Cancel button if buttonpane is visible.
     */
    protected Boolean showCancelButton;

    /**
     * null > true<br>
     * Show the Close button if the header is visible. If the dialog is inline, the close button is never shown.
     */
    protected Boolean showCloseButton;

    /**
     * null > false<br>
     * Show the None/Revert button if buttonpane is visible.
     */
    protected Boolean showNoneButton = true;

    /**
     * null > 'focus click alt''<br>
     * Specifies what user events will show the colorpicker if not inline. Specify multiple events by seperating with space. Optionally 'focus',
     * 'click', 'alt', 'button' and/or 'both'<br>
     * focus When the element comes into focus (either tab or click)<br>
     * click When the element is clicked (for non-inputs)<br>
     * alt When clicking on an element specified with as altField<br>
     * button When clicking on the button created if this event is specified.<br>
     * both selects all possible triggers<br>
     */
    protected String showOn = "both";

    /**
     * null > 84<br>
     * Width of the swatches display in pixels.
     */
    protected Integer swatchesWidth;

    /** Title to display in the header. If null, use language default. */
    protected String title;

    public Boolean getAlpha() {
        return this.alpha;
    }

    public Boolean getAltAlpha() {
        return this.altAlpha;
    }

    public Boolean getAltOnChange() {
        return this.altOnChange;
    }

    public Boolean getAutoOpen() {
        return this.autoOpen;
    }

    public String getButtonClass() {
        return this.buttonClass;
    }

    public Boolean getButtonColorize() {
        return this.buttonColorize;
    }

    public String getButtonImage() {
        return this.buttonImage;
    }

    public Boolean getButtonImageOnly() {
        return this.buttonImageOnly;
    }

    public String getButtonText() {
        return this.buttonText;
    }

    public Boolean getCloseOnEscape() {
        return this.closeOnEscape;
    }

    public Boolean getCloseOnOutside() {
        return this.closeOnOutside;
    }

    public String getColorFormat() {
        return this.colorFormat;
    }

    public Boolean getDraggable() {
        return this.draggable;
    }

    public String getDuration() {
        return this.duration;
    }

    public Boolean getHsv() {
        return this.hsv;
    }

    public Boolean getInlineFrame() {
        return this.inlineFrame;
    }

    public String getLimit() {
        return this.limit;
    }

    public String getMode() {
        return this.mode;
    }

    public Boolean getModal() {
        return this.modal;
    }

    public Boolean getOkOnEnter() {
        return this.okOnEnter;
    }

    public Boolean getRevert() {
        return this.revert;
    }

    public Boolean getRgb() {
        return this.rgb;
    }

    public String getShowAnim() {
        return this.showAnim;
    }

    public Boolean getShowCancelButton() {
        return this.showCancelButton;
    }

    public Boolean getShowCloseButton() {
        return this.showCloseButton;
    }

    public Boolean getShowNoneButton() {
        return this.showNoneButton;
    }

    public String getShowOn() {
        return this.showOn;
    }

    public Integer getSwatchesWidth() {
        return this.swatchesWidth;
    }

    public String getTitle() {
        return this.title;
    }

    public JQueryUIColorPickerSettings setAlpha(Boolean alpha) {
        this.alpha = alpha;
        return this;
    }

    public JQueryUIColorPickerSettings setAltAlpha(Boolean altAlpha) {
        this.altAlpha = altAlpha;
        return this;
    }

    public JQueryUIColorPickerSettings setAltOnChange(Boolean altOnChange) {
        this.altOnChange = altOnChange;
        return this;
    }

    public JQueryUIColorPickerSettings setAutoOpen(Boolean autoOpen) {
        this.autoOpen = autoOpen;
        return this;
    }

    public JQueryUIColorPickerSettings setButtonClass(String buttonClass) {
        this.buttonClass = buttonClass;
        return this;
    }

    public JQueryUIColorPickerSettings setButtonColorize(Boolean buttonColorize) {
        this.buttonColorize = buttonColorize;
        return this;
    }

    public JQueryUIColorPickerSettings setButtonImage(String buttonImage) {
        this.buttonImage = buttonImage;
        return this;
    }

    public JQueryUIColorPickerSettings setButtonImageOnly(Boolean buttonImageOnly) {
        this.buttonImageOnly = buttonImageOnly;
        return this;
    }

    public JQueryUIColorPickerSettings setButtonText(String buttonText) {
        this.buttonText = buttonText;
        return this;
    }

    public JQueryUIColorPickerSettings setCloseOnEscape(Boolean closeOnEscape) {
        this.closeOnEscape = closeOnEscape;
        return this;
    }

    public JQueryUIColorPickerSettings setCloseOnOutside(Boolean closeOnOutside) {
        this.closeOnOutside = closeOnOutside;
        return this;
    }

    public JQueryUIColorPickerSettings setColorFormat(String colorFormat) {
        this.colorFormat = colorFormat;
        return this;
    }

    public JQueryUIColorPickerSettings setDraggable(Boolean draggable) {
        this.draggable = draggable;
        return this;
    }

    public JQueryUIColorPickerSettings setDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public JQueryUIColorPickerSettings setHsv(Boolean hsv) {
        this.hsv = hsv;
        return this;
    }

    public JQueryUIColorPickerSettings setInlineFrame(Boolean inlineFrame) {
        this.inlineFrame = inlineFrame;
        return this;
    }

    public JQueryUIColorPickerSettings setLimit(String limit) {
        this.limit = limit;
        return this;
    }

    public JQueryUIColorPickerSettings setMode(String mode) {
        this.mode = mode;
        return this;
    }

    public JQueryUIColorPickerSettings setModal(Boolean modal) {
        this.modal = modal;
        return this;
    }

    public JQueryUIColorPickerSettings setOkOnEnter(Boolean okOnEnter) {
        this.okOnEnter = okOnEnter;
        return this;
    }

    public JQueryUIColorPickerSettings setRevert(Boolean revert) {
        this.revert = revert;
        return this;
    }

    public JQueryUIColorPickerSettings setRgb(Boolean rgb) {
        this.rgb = rgb;
        return this;
    }

    public JQueryUIColorPickerSettings setShowAnim(String showAnim) {
        this.showAnim = showAnim;
        return this;
    }

    public JQueryUIColorPickerSettings setShowCancelButton(Boolean showCancelButton) {
        this.showCancelButton = showCancelButton;
        return this;
    }

    public JQueryUIColorPickerSettings setShowCloseButton(Boolean showCloseButton) {
        this.showCloseButton = showCloseButton;
        return this;
    }

    public JQueryUIColorPickerSettings setShowNoneButton(Boolean showNoneButton) {
        this.showNoneButton = showNoneButton;
        return this;
    }

    public JQueryUIColorPickerSettings setShowOn(String showOn) {
        this.showOn = showOn;
        return this;
    }

    public JQueryUIColorPickerSettings setSwatchesWidth(Integer swatchesWidth) {
        this.swatchesWidth = swatchesWidth;
        return this;
    }

    public JQueryUIColorPickerSettings setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getParts() {
        return this.parts;
    }

    public JQueryUIColorPickerSettings setParts(String parts) {
        this.parts = parts;
        return this;
    }

    public JQueryColorPickerLocale getRegional() {
        return this.regional;
    }

    public JQueryUIColorPickerSettings setRegional(JQueryColorPickerLocale regional) {
        this.regional = regional;
        return this;
    }

    public JQueryUIColorPickerSettings setRegional(Locale locale) {
        for (JQueryColorPickerLocale it : JQueryColorPickerLocale.values()) {
            if (it.name().toLowerCase().replace('-', '_').equals(locale.toString().toLowerCase().replace('-', '_'))) {
                return setRegional(it);
            }
        }
        for (JQueryColorPickerLocale it : JQueryColorPickerLocale.values()) {
            if (it.name().toLowerCase().equals(locale.getLanguage().toLowerCase())) {
                return setRegional(it);
            }
        }
        return setRegional(JQueryColorPickerLocale.en);
    }
}
