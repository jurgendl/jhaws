package org.tools.hqlbuilder.webservice.wicket;

/**
 * @see http://www.w3schools.com/jsref/dom_obj_event.asp
 */
public interface HtmlEvent {
    public static interface HtmlMouseEvent {
        /** when the user clicks on an element */
        public static final String MOUSE_CLICK = "onclick";

        /** when the user double-clicks on an element */
        public static final String MOUSE_DOUBLECLICK = "ondblclick";

        /** when a user presses a mouse button over an element */
        public static final String MOUSE_DOWN = "onmousedown";

        /** when the pointer is moving while it is over an element */
        public static final String MOUSE_MOVE = "onmousemove";

        /** when the pointer is moved onto an element */
        public static final String MOUSE_OVER = "onmouseover";

        /** when a user moves the mouse pointer out of an element */
        public static final String MOUSE_OUT = "onmouseout";

        /** when a user releases a mouse button over an element */
        public static final String MOUSE_UP = "onmouseup";
    }

    public static interface HtmlKeyboardEvent {
        /** when the user is pressing a key */
        public static final String KEY_DOWN = "onkeydown";

        /** when the user presses a key */
        public static final String KEY_PRESS = "onkeypress";

        /** when the user releases a key */
        public static final String KEY_UP = "onkeyup";
    }

    public static interface HtmlFrameEvent {
        /** when an image is stopped from loading before completely loaded (for &lt;object&gt;) */
        public static final String ABORT = "onabort";

        /** when an image does not load properly (for &lt;object&gt;, &lt;body&gt; and &lt;frameset&gt;) */
        public static final String ERROR = "onerror";

        /** when a document, frameset, or &lt;object&gt; has been loaded */
        public static final String LOAD = "onload";

        /** when a document view is resized */
        public static final String RESIZE = "onresize";

        /** when a document view is scrolled */
        public static final String SCROLL = "onscroll";

        /** once a page has unloaded (for &lt;body&gt; and &lt;frameset&gt;) */
        public static final String UNLOAD = "onunload";
    }

    public static interface HtmlFormEvent {
        /** when a form element loses focus */
        public static final String BLUR = "onblur";

        /**
         * when the content of a form element, the selection, or the checked state have changed (for &lt;input&gt;, &lt;select&gt;, and
         * &lt;textarea&gt;)
         */
        public static final String CHANGE = "onchange";

        /** when an element gets focus (for &lt;label&gt;, &lt;input&gt;, &lt;select&gt;, textarea&gt;, and &lt;button&gt;) */
        public static final String FOCUS = "onfocus";

        /** when a form is reset */
        public static final String RESET = "onreset";

        /** when a user selects some text (for &lt;input&gt; and &lt;textarea&gt;) */
        public static final String SELECT = "onselect";

        /** when a form is submitted */
        public static final String SUBMIT = "onsubmit";
    }
}
