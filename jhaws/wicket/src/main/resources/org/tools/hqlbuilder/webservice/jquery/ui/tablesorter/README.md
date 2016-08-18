tablesorter is a jQuery plugin for turning a standard HTML table with THEAD and TBODY tags into a sortable table without page refreshes.
tablesorter can successfully parse and sort many types of data including linked data in a cell.

### Notice!

* Because of the change to the internal cache, the tablesorter v2.16+ core, filter widget and pager (both plugin &amp; widget) will only work with the same version or newer files.

### [Documentation](//mottie.github.io/tablesorter/docs/)

* See the [full documentation](//mottie.github.io/tablesorter/docs/).
* All of the [original document pages](//tablesorter.com/docs/) have been included.
* Information from my blog post on [undocumented options](//wowmotty.blogspot.com/2011/06/jquery-tablesorter-missing-docs.html) and lots of new demos have also been included.
* Change log moved from included text file into the [wiki documentation](//github.com/Mottie/tablesorter/wiki/Changes).

### Demos

* [Basic alpha-numeric sort Demo](//mottie.github.com/tablesorter/).
* Links to demo pages can be found within the main [documentation](//mottie.github.io/tablesorter/docs/).
* More demos & playgrounds - updated in the [wiki pages](//github.com/Mottie/tablesorter/wiki).

### Features

* Multi-column alphanumeric sorting and filtering.
* Multi-tbody sorting - see the [options](//mottie.github.io/tablesorter/docs/index.html#options) table on the main document page.
* Supports [Bootstrap v2 and 3](//mottie.github.io/tablesorter/docs/example-widget-bootstrap-theme.html)
* Parsers for sorting text, alphanumeric text, URIs, integers, currency, floats, IP addresses, dates (ISO, long and short formats) &amp; time. [Add your own easily](//mottie.github.io/tablesorter/docs/example-parsers.html).
* Inline editing - see [demo](//mottie.github.io/tablesorter/docs/example-widget-editable.html)
* Support for ROWSPAN and COLSPAN on TH elements.
* Support secondary "hidden" sorting (e.g., maintain alphabetical sort when sorting on other criteria).
* Extensibility via [widget system](//mottie.github.io/tablesorter/docs/example-widgets.html).
* Cross-browser: IE 6.0+, FF 2+, Safari 2.0+, Opera 9.0+, Chrome 5.0+.
* Small code size, starting at 25K minified
* Works with jQuery 1.2.6+ (jQuery 1.4.1+ needed with some widgets).
* Works with jQuery 1.9+ ($.browser.msie was removed; needed in the original version).

### Licensing

* Copyright (c) 2007 Christian Bach.
* Original examples and docs at: [http://tablesorter.com](//tablesorter.com).
* Dual licensed under the [MIT](//www.opensource.org/licenses/mit-license.php) and [GPL](//www.gnu.org/licenses/gpl.html) licenses.

### Related Projects

* [Plugin for Rails](//github.com/themilkman/jquery-tablesorter-rails). Maintained by [themilkman](//github.com/themilkman).
* [PHP templating engine for tablesorter](//github.com/alexweissman/bootsole) by [alexweissman](//github.com/alexweissman).

### Special Thanks

* Big shout-out to [Nick Craver](//github.com/NickCraver) for getting rid of the `eval()` function that was previously needed for multi-column sorting.
* Big thanks to [thezoggy](//github.com/thezoggy) for helping with code, themes and providing valuable feedback.
* Big thanks to [ThsSin-](//github.com/TheSin-) for taking over for a while and also providing valuable feedback.
* Also extra thanks to [christhomas](//github.com/christhomas) and [Lynesth](//github.com/Lynesth) for help with code.
* And, of course thanks to everyone else that has contributed, and continues to contribute to this forked project!

### Questions?

* Check the [FAQ](//github.com/Mottie/tablesorter/wiki/FAQ) page.
* Search the [main documentation](//mottie.github.io/tablesorter/docs/) (click the menu button in the upper left corner).
* Search the [issues](//github.com/Mottie/tablesorter/issues) to see if the question or problem has been brought up before, and hopefully resolved.
* If someone is available, ask your question in the `#tablesorter` IRC channel at freenode.net.
* Ask your question at [Stackoverflow](//stackoverflow.com/questions/tagged/tablesorter) using a tablesorter tag.
* Please don't open a [new issue](//github.com/Mottie/tablesorter/issues) unless it really is an issue with the plugin, or a feature request. Thanks!

### Change Log

View the [complete listing here](//github.com/Mottie/tablesorter/wiki/Changes).

#### <a name="v2.18.0">Version 2.18.0</a> (10/26/2014)

* Core
  * Move "ipAddress" parser into `parser-network.js` parser file.
  * Add "image" parser to core.
  * Add `widgetClass` option
      * Allows adding widgets to the table by adding a table class name.
      * Fix debug logs for applying widgets.
      * Fixes [issue #743](https://github.com/Mottie/tablesorter/issues/743).
  * Add `$cell` parameter to parser detection `is` function (`is: function(s, table, cell, $cell) { /* ... */ }`).
  * Add `$table` parameter to `onRenderHeader` function (`onRenderHeader: function (index, config, $table){ /* ... */ }`).
  * Fix ARIA caption label reference.
  * Get column index from data-attribute when sorting.
  * The `aria-labelledby` attribute is no longer to all nested captions.
  * Update `widgetClass` option matching. See [issue #743](https://github.com/Mottie/tablesorter/issues/743).
* Themes
  * Include caption element in metro theme; update various demo theme selectors to include the metro theme.
  * Fix zebra striping in nested tables.
* Parsers
  * Created `parser-network.js` parser
      * Removed "ipAddress" parser from core.
      * Move "ipAddress" parser into this file; a duplicate of the parser named "ipv4Address" is included.
      * Moved "ipv6Address" parser into this file.
      * Added new MAC parser.
  * Update all date parsers to ensure a valid date is being parsed.
  * Add named number parser & demo.
* Column Selector widget
  * Prevent adding a media query when no priorities are set.
  * `col` element will now be hidden along with the column. Fixes [issues #740](https://github.com/Mottie/tablesorter/issues/740).
* Editable widget
  * Make updatable so this widget works with the pager. Fixes [issue #732](https://github.com/Mottie/tablesorter/issues/732).
* Filter widget
  * Fix wildcard match logic to behave logically. Fixes [issue #727](https://github.com/Mottie/tablesorter/issues/727).
  * Add `filter_cellFilter` option. Fixes [issue #731](https://github.com/Mottie/tablesorter/issues/731).
  * External inputs can now target multiple columns (e.g. `data-column="0-2,4,6-7"`); see [this Stackoverflow question](http://stackoverflow.com/q/26470602/145346).
  * Any match filters now properly uses `filter_ignoreCase`. Fixes [issue #748](https://github.com/Mottie/tablesorter/issues/748).
  * Fix saved filter updates to multiple or "any" column inputs.
* Grouping widget
  * Add "monthyear" grouping to dates. Fixes [issue #744](https://github.com/Mottie/tablesorter/issues/744).
* Pager addon & widget
  * Use a sample of page number links for large collections.
      * Add `maxOptionSize` option
      * Tweak code & fix problems introduces in [pull #711](https://github.com/Mottie/tablesorter/pull/711).
      * Thanks [camallen](https://github.com/camallen)!
  * Fix ouput display not updating on initialization.
  * Add url check to allow ajax updating of table. Fixes [issue #730](https://github.com/Mottie/tablesorter/issues/730).
  * Check for dynamically changing `ajaxUrl` option.
  * Add `ajaxObject` to the `table.config.pager` variable.
  * IE requires a value attribute for every option. Fixes [issue #734](https://github.com/Mottie/tablesorter/issues/734).
  * Revert some code modified for large collections to use jQuery instead of native javascript (more IE issues).
  * Ensure `pager.filteredRows` is current on page move. See [issue #745](https://github.com/Mottie/tablesorter/issues/745).
  * Fix empty table select showing 0 & 1 pages.
  * The `fixedHeight` option is now working properly. Fixes [issue #742](https://github.com/Mottie/tablesorter/issues/742) & [issue #729](https://github.com/Mottie/tablesorter/issues/729).
  * Widget cleanup & only use the last search data.
  * Add note about including an ajax `success` function within the `ajaxObject` definition. See [issue #749](https://github.com/Mottie/tablesorter/issues/749).
* RepeatHeaders widget
  * Now works with filtered & nested tables.
* Resizable widget
  * Make it work inside of an overflow container. Fixes [issue #737](https://github.com/Mottie/tablesorter/issues/737).
* Scroller widget
  * Remove `scroller_idPrefix` in lieu of a unique namespace id.
* StickyHeaders widget
  * Now stacks when within a nested table.
  * Wrapped sticky header components (`thead` & `caption`) in a sticky div.
  * Added `stickyHeaders_xScroll` and `stickyHeaders_yScroll` options.
  * Removed jQuery UI selection from the demo to allow the accordion to be properly themed.
* CssStickyHeaders widget
  * Now stacks when within a nested table.
  * Removed `cssStickyHeaders_zIndex` option as the widget no longer uses relative positioning (it wasn't necessary)
  * Please note that **using this widget on nested tables does not work properly in ALL versions of IE** (including IE11).
* UITheme
  * Ignore nested tables.
  * Add method to remove previous theme.
  * Fix multiple header row sort icons.

#### <a name="v2.17.8">Version 2.17.8</a> (9/15/2014)

* Core
  * Fix `widthFixed` option to ignore info block tbodies.
  * Make `emptyTo` & `stringTo` settings case insensitive.
  * An empty string `headerTemplate` option will now prevent adding an inner div to the header.
* Docs
  * Fix setting active accordion from hash script.
  * Add `emptyTo` clarification.
  * Main readme: Add related projects section.
* Editable
  * Autoresort true no longer breaks the table.
  * Update demo to use row id instead of index.
  * Update validate function to allow setting it per column.
  * Add `editable_focus`, `editable_blur` and `editable_selectAll` options. See [issue #708](//github.com/Mottie/tablesorter/issues/708).
  * Add `editable_wrapContent` option.
  * Add `editable_trimContent` option.
  * Validate function now includes a contenteditable element parameter.
  * All text callback parameters are now trimmed.
  * Trim everything & revert widget changes.
  * Refocus edited element after resort only.
  * Switch form focus to focusout.
* Filter
  * Add `filter_defaultFilter` option.
      * fix issue with using class selectors.
      * Make happy with spaces within the query.
      * Will override exact/partial select filters. Fixes [issue #721](//github.com/Mottie/tablesorter/issues/721).
      * Fixes [issue #704](//github.com/Mottie/tablesorter/issues/704).
  * Add `filter_excludeFilter` option; add multiple exclusions separated by spaces, not commas.
  * Prevent multiple searches during initialization.
  * Ensure initial filter settings get applied.
  * Selects will exactly match the selected option unless "filter-match" class is set. Fixes [issue #721](//github.com/Mottie/tablesorter/issues/721).
* Pager
  * Update cache & rows copy. Fixes [issue #703](//github.com/Mottie/tablesorter/issues/703).
  * Use native javascript to populate & set the goto select. Fixes [issue #711](//github.com/Mottie/tablesorter/issues/711).
  * Custom pager controls. Fix control updates for multiple tables.
  * Fix pager widget to be properly applied after a page move.
* StickyHeaders: Fix issue so it works with jQuery v1.2.6 again.
* Zebra: Apply style to one row. Fixes [issue #715](//github.com/Mottie/tablesorter/issues/715).
* Parser
  * Duration parser - added with [demo](//mottie.github.io/tablesorter/docs/example-parsers-duration.html).
  * Select parser - Fix for IE10+ not allowing select options to be clicked.
  * IgnoreArticles parser - Added `ignoreArticlesExcept` option.
  * url parser - ensure it is used by the filter widget.

#### <a name="v2.17.7">Version 2.17.7</a> (8/9/2014)

* Core: Do not detach rows before appending to prevent ajax rows from disappearing. Fixes [issue #701](//github.com/Mottie/tablesorter/issues/701).
* Docs: Fix change log links.
* Filter: attached external select causing javascript errors. Fixes [issue #702](//github.com/Mottie/tablesorter/issues/702)
