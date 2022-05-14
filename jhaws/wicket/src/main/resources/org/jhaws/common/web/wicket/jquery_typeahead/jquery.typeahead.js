/* base */
/*!
 * jQuery Typeahead
 * Copyright (C) 2020 RunningCoder.org
 * Licensed under the MIT license
 *
 * @author Tom Bertrand
 * @version 2.11.1 (2020-5-18)
 * @link http://www.runningcoder.org/jquerytypeahead/
 */
! function(e) {
    var t;
    "function" == typeof define && define.amd ? define("jquery-typeahead", ["jquery"], function(t) {
        return e(t)
    }) : "object" == typeof module && module.exports ? module.exports = (void 0 === t && (t = "undefined" != typeof window ? require("jquery") : require("jquery")(void 0)), e(t)) : e(jQuery)
}(function(j) {
    "use strict";

    function r(t, e) {
        this.rawQuery = t.val() || "", this.query = t.val() || "", this.selector = t[0].selector, this.deferred = null, this.tmpSource = {}, this.source = {}, this.dynamicGroups = [], this.hasDynamicGroups = !1, this.generatedGroupCount = 0, this.groupBy = "group", this.groups = [], this.searchGroups = [], this.generateGroups = [], this.requestGroups = [], this.result = [], this.tmpResult = {}, this.groupTemplate = "", this.resultHtml = null, this.resultCount = 0, this.resultCountPerGroup = {}, this.options = e, this.node = t, this.namespace = "." + this.helper.slugify.call(this, this.selector) + ".typeahead", this.isContentEditable = void 0 !== this.node.attr("contenteditable") && "false" !== this.node.attr("contenteditable"), this.container = null, this.resultContainer = null, this.item = null, this.items = null, this.comparedItems = null, this.xhr = {}, this.hintIndex = null, this.filters = {
            dropdown: {},
            dynamic: {}
        }, this.dropdownFilter = {
            static: [],
            dynamic: []
        }, this.dropdownFilterAll = null, this.isDropdownEvent = !1, this.requests = {}, this.backdrop = {}, this.hint = {}, this.label = {}, this.hasDragged = !1, this.focusOnly = !1, this.displayEmptyTemplate, this.__construct()
    }
    var i, s = {
            input: null,
            minLength: 2,
            maxLength: !(window.Typeahead = {
                version: "2.11.1"
            }),
            maxItem: 8,
            dynamic: !1,
            delay: 300,
            order: null,
            offset: !1,
            hint: !1,
            accent: !1,
            highlight: !0,
            multiselect: null,
            group: !1,
            groupOrder: null,
            maxItemPerGroup: null,
            dropdownFilter: !1,
            dynamicFilter: null,
            backdrop: !1,
            backdropOnFocus: !1,
            cache: !1,
            ttl: 36e5,
            compression: !1,
            searchOnFocus: !1,
            blurOnTab: !0,
            resultContainer: null,
            generateOnLoad: null,
            mustSelectItem: !1,
            href: null,
            display: ["display"],
            template: null,
            templateValue: null,
            groupTemplate: null,
            correlativeTemplate: !1,
            emptyTemplate: !1,
            cancelButton: !0,
            loadingAnimation: !0,
            asyncResult: !1,
            filter: !0,
            matcher: null,
            source: null,
            callback: {
                onInit: null,
                onReady: null,
                onShowLayout: null,
                onHideLayout: null,
                onSearch: null,
                onResult: null,
                onLayoutBuiltBefore: null,
                onLayoutBuiltAfter: null,
                onNavigateBefore: null,
                onNavigateAfter: null,
                onEnter: null,
                onLeave: null,
                onClickBefore: null,
                onClickAfter: null,
                onDropdownFilter: null,
                onSendRequest: null,
                onReceiveRequest: null,
                onPopulateSource: null,
                onCacheSave: null,
                onSubmit: null,
                onCancel: null
            },
            selector: {
                container: "typeahead__container",
                result: "typeahead__result",
                list: "typeahead__list",
                group: "typeahead__group",
                item: "typeahead__item",
                empty: "typeahead__empty",
                display: "typeahead__display",
                query: "typeahead__query",
                filter: "typeahead__filter",
                filterButton: "typeahead__filter-button",
                dropdown: "typeahead__dropdown",
                dropdownItem: "typeahead__dropdown-item",
                labelContainer: "typeahead__label-container",
                label: "typeahead__label",
                button: "typeahead__button",
                backdrop: "typeahead__backdrop",
                hint: "typeahead__hint",
                cancelButton: "typeahead__cancel-button"
            },
            debug: !1
        },
        o = {
            from: "ãàáäâẽèéëêìíïîõòóöôùúüûñç",
            to: "aaaaaeeeeeiiiiooooouuuunc"
        },
        n = ~window.navigator.appVersion.indexOf("MSIE 9."),
        a = ~window.navigator.appVersion.indexOf("MSIE 10"),
        l = !!~window.navigator.userAgent.indexOf("Trident") && ~window.navigator.userAgent.indexOf("rv:11");
    r.prototype = {
        _validateCacheMethod: function(t) {
            var e;
            if (!0 === t) t = "localStorage";
            else if ("string" == typeof t && !~["localStorage", "sessionStorage"].indexOf(t)) return !1;
            e = void 0 !== window[t];
            try {
                window[t].setItem("typeahead", "typeahead"), window[t].removeItem("typeahead")
            } catch (t) {
                e = !1
            }
            return e && t || !1
        },
        extendOptions: function() {
            if (this.options.cache = this._validateCacheMethod(this.options.cache), this.options.compression && ("object" == typeof LZString && this.options.cache || (this.options.compression = !1)), this.options.maxLength && !isNaN(this.options.maxLength) || (this.options.maxLength = 1 / 0), void 0 !== this.options.maxItem && ~[0, !1].indexOf(this.options.maxItem) && (this.options.maxItem = 1 / 0), this.options.maxItemPerGroup && !/^\d+$/.test(this.options.maxItemPerGroup) && (this.options.maxItemPerGroup = null), this.options.display && !Array.isArray(this.options.display) && (this.options.display = [this.options.display]), this.options.multiselect && (this.items = [], this.comparedItems = [], "string" == typeof this.options.multiselect.matchOn && (this.options.multiselect.matchOn = [this.options.multiselect.matchOn])), this.options.group && (Array.isArray(this.options.group) || ("string" == typeof this.options.group ? this.options.group = {
                    key: this.options.group
                } : "boolean" == typeof this.options.group && (this.options.group = {
                    key: "group"
                }), this.options.group.key = this.options.group.key || "group")), this.options.highlight && !~["any", !0].indexOf(this.options.highlight) && (this.options.highlight = !1), this.options.dropdownFilter && this.options.dropdownFilter instanceof Object) {
                Array.isArray(this.options.dropdownFilter) || (this.options.dropdownFilter = [this.options.dropdownFilter]);
                for (var t = 0, e = this.options.dropdownFilter.length; t < e; ++t) this.dropdownFilter[this.options.dropdownFilter[t].value ? "static" : "dynamic"].push(this.options.dropdownFilter[t])
            }
            this.options.dynamicFilter && !Array.isArray(this.options.dynamicFilter) && (this.options.dynamicFilter = [this.options.dynamicFilter]), this.options.accent && ("object" == typeof this.options.accent ? this.options.accent.from && this.options.accent.to && (this.options.accent.from.length, this.options.accent.to.length) : this.options.accent = o), this.options.groupTemplate && (this.groupTemplate = this.options.groupTemplate), this.options.resultContainer && ("string" == typeof this.options.resultContainer && (this.options.resultContainer = j(this.options.resultContainer)), this.options.resultContainer instanceof j && this.options.resultContainer[0] && (this.resultContainer = this.options.resultContainer)), this.options.group && this.options.group.key && (this.groupBy = this.options.group.key), this.options.callback && this.options.callback.onClick && (this.options.callback.onClickBefore = this.options.callback.onClick, delete this.options.callback.onClick), this.options.callback && this.options.callback.onNavigate && (this.options.callback.onNavigateBefore = this.options.callback.onNavigate, delete this.options.callback.onNavigate), this.options = j.extend(!0, {}, s, this.options)
        },
        unifySourceFormat: function() {
            var t, e, i;
            for (t in this.dynamicGroups = [], Array.isArray(this.options.source) && (this.options.source = {
                    group: {
                        data: this.options.source
                    }
                }), "string" == typeof this.options.source && (this.options.source = {
                    group: {
                        ajax: {
                            url: this.options.source
                        }
                    }
                }), this.options.source.ajax && (this.options.source = {
                    group: {
                        ajax: this.options.source.ajax
                    }
                }), (this.options.source.url || this.options.source.data) && (this.options.source = {
                    group: this.options.source
                }), this.options.source)
                if (this.options.source.hasOwnProperty(t)) {
                    if ("string" == typeof(e = this.options.source[t]) && (e = {
                            ajax: {
                                url: e
                            }
                        }), i = e.url || e.ajax, Array.isArray(i) ? (e.ajax = "string" == typeof i[0] ? {
                            url: i[0]
                        } : i[0], e.ajax.path = e.ajax.path || i[1] || null) : "object" == typeof e.url ? e.ajax = e.url : "string" == typeof e.url && (e.ajax = {
                            url: e.url
                        }), delete e.url, !e.data && !e.ajax) return !1;
                    e.display && !Array.isArray(e.display) && (e.display = [e.display]), e.minLength = "number" == typeof e.minLength ? e.minLength : this.options.minLength, e.maxLength = "number" == typeof e.maxLength ? e.maxLength : this.options.maxLength, e.dynamic = "boolean" == typeof e.dynamic || this.options.dynamic, e.minLength > e.maxLength && (e.minLength = e.maxLength), this.options.source[t] = e, this.options.source[t].dynamic && this.dynamicGroups.push(t), e.cache = void 0 !== e.cache ? this._validateCacheMethod(e.cache) : this.options.cache, e.compression && ("object" == typeof LZString && e.cache || (e.compression = !1))
                } return this.hasDynamicGroups = this.options.dynamic || !!this.dynamicGroups.length, !0
        },
        init: function() {
            this.helper.executeCallback.call(this, this.options.callback.onInit, [this.node]), this.container = this.node.closest("." + this.options.selector.container)
        },
        delegateEvents: function() {
            var i = this,
                t = ["focus" + this.namespace, "input" + this.namespace, "propertychange" + this.namespace, "keydown" + this.namespace, "keyup" + this.namespace, "search" + this.namespace, "generate" + this.namespace];
            j("html").on("touchmove", function() {
                i.hasDragged = !0
            }).on("touchstart", function() {
                i.hasDragged = !1
            }), this.node.closest("form").on("submit", function(t) {
                if (!i.options.mustSelectItem || !i.helper.isEmpty(i.item)) return i.options.backdropOnFocus || i.hideLayout(), i.options.callback.onSubmit ? i.helper.executeCallback.call(i, i.options.callback.onSubmit, [i.node, this, i.item || i.items, t]) : void 0;
                t.preventDefault()
            }).on("reset", function() {
                setTimeout(function() {
                    i.node.trigger("input" + i.namespace), i.hideLayout()
                })
            });
            var s = !1;
            if (this.node.attr("placeholder") && (a || l)) {
                var e = !0;
                this.node.on("focusin focusout", function() {
                    e = !(this.value || !this.placeholder)
                }), this.node.on("input", function(t) {
                    e && (t.stopImmediatePropagation(), e = !1)
                })
            }
            this.node.off(this.namespace).on(t.join(" "), function(t, e) {
                switch (t.type) {
                    case "generate":
                        i.generateSource(Object.keys(i.options.source));
                        break;
                    case "focus":
                        if (i.focusOnly) {
                            i.focusOnly = !1;
                            break
                        }
                        i.options.backdropOnFocus && (i.buildBackdropLayout(), i.showLayout()), i.options.searchOnFocus && !i.item && (i.deferred = j.Deferred(), i.assignQuery(), i.generateSource());
                        break;
                    case "keydown":
                        8 === t.keyCode && i.options.multiselect && i.options.multiselect.cancelOnBackspace && "" === i.query && i.items.length ? i.cancelMultiselectItem(i.items.length - 1, null, t) : t.keyCode && ~[9, 13, 27, 38, 39, 40].indexOf(t.keyCode) && (s = !0, i.navigate(t));
                        break;
                    case "keyup":
                        n && i.node[0].value.replace(/^\s+/, "").toString().length < i.query.length && i.node.trigger("input" + i.namespace);
                        break;
                    case "propertychange":
                        if (s) {
                            s = !1;
                            break
                        }
                    case "input":
                        i.deferred = j.Deferred(), i.assignQuery(), "" === i.rawQuery && "" === i.query && (t.originalEvent = e || {}, i.helper.executeCallback.call(i, i.options.callback.onCancel, [i.node, i.item, t]), i.item = null), i.options.cancelButton && i.toggleCancelButtonVisibility(), i.options.hint && i.hint.container && "" !== i.hint.container.val() && 0 !== i.hint.container.val().indexOf(i.rawQuery) && (i.hint.container.val(""), i.isContentEditable && i.hint.container.text("")), i.hasDynamicGroups ? i.helper.typeWatch(function() {
                            i.generateSource()
                        }, i.options.delay) : i.generateSource();
                        break;
                    case "search":
                        i.searchResult(), i.buildLayout(), i.result.length || i.searchGroups.length && i.displayEmptyTemplate ? i.showLayout() : i.hideLayout(), i.deferred && i.deferred.resolve()
                }
                return i.deferred && i.deferred.promise()
            }), this.options.generateOnLoad && this.node.trigger("generate" + this.namespace)
        },
        assignQuery: function() {
            this.isContentEditable ? this.rawQuery = this.node.text() : this.rawQuery = this.node.val().toString(), this.rawQuery = this.rawQuery.replace(/^\s+/, ""), this.rawQuery !== this.query && (this.query = this.rawQuery)
        },
        filterGenerateSource: function() {
            if (this.searchGroups = [], this.generateGroups = [], !this.focusOnly || this.options.multiselect)
                for (var t in this.options.source)
                    if (this.options.source.hasOwnProperty(t) && this.query.length >= this.options.source[t].minLength && this.query.length <= this.options.source[t].maxLength) {
                        if (this.filters.dropdown && "group" === this.filters.dropdown.key && this.filters.dropdown.value !== t) continue;
                        if (this.searchGroups.push(t), !this.options.source[t].dynamic && this.source[t]) continue;
                        this.generateGroups.push(t)
                    }
        },
        generateSource: function(t) {
            if (this.filterGenerateSource(), this.generatedGroupCount = 0, Array.isArray(t) && t.length) this.generateGroups = t;
            else if (!this.generateGroups.length) return void this.node.trigger("search" + this.namespace);
            if (this.requestGroups = [], this.options.loadingAnimation && this.container.addClass("loading"), !this.helper.isEmpty(this.xhr)) {
                for (var e in this.xhr) this.xhr.hasOwnProperty(e) && this.xhr[e].abort();
                this.xhr = {}
            }
            for (var i, s, o, n, r, a, l, h = this, c = (e = 0, this.generateGroups.length); e < c; ++e) {
                if (i = this.generateGroups[e], n = (o = this.options.source[i]).cache, r = o.compression, this.options.asyncResult && delete this.source[i], n && (a = window[n].getItem("TYPEAHEAD_" + this.selector + ":" + i))) {
                    r && (a = LZString.decompressFromUTF16(a)), l = !1;
                    try {
                        (a = JSON.parse(a + "")).data && a.ttl > (new Date).getTime() ? (this.populateSource(a.data, i), l = !0) : window[n].removeItem("TYPEAHEAD_" + this.selector + ":" + i)
                    } catch (t) {}
                    if (l) continue
                }!o.data || o.ajax ? o.ajax && (this.requests[i] || (this.requests[i] = this.generateRequestObject(i)), this.requestGroups.push(i)) : "function" == typeof o.data ? (s = o.data.call(this), Array.isArray(s) ? h.populateSource(s, i) : "function" == typeof s.promise && function(e) {
                    j.when(s).then(function(t) {
                        t && Array.isArray(t) && h.populateSource(t, e)
                    })
                }(i)) : this.populateSource(j.extend(!0, [], o.data), i)
            }
            return this.requestGroups.length && this.handleRequests(), this.options.asyncResult && this.searchGroups.length !== this.generateGroups && this.node.trigger("search" + this.namespace), !!this.generateGroups.length
        },
        generateRequestObject: function(s) {
            var o = this,
                n = this.options.source[s],
                t = {
                    request: {
                        url: n.ajax.url || null,
                        dataType: "json",
                        beforeSend: function(t, e) {
                            o.xhr[s] = t;
                            var i = o.requests[s].callback.beforeSend || n.ajax.beforeSend;
                            "function" == typeof i && i.apply(null, arguments)
                        }
                    },
                    callback: {
                        beforeSend: null,
                        done: null,
                        fail: null,
                        then: null,
                        always: null
                    },
                    extra: {
                        path: n.ajax.path || null,
                        group: s
                    },
                    validForGroup: [s]
                };
            if ("function" != typeof n.ajax && (n.ajax instanceof Object && (t = this.extendXhrObject(t, n.ajax)), 1 < Object.keys(this.options.source).length))
                for (var e in this.requests) this.requests.hasOwnProperty(e) && (this.requests[e].isDuplicated || t.request.url && t.request.url === this.requests[e].request.url && (this.requests[e].validForGroup.push(s), t.isDuplicated = !0, delete t.validForGroup));
            return t
        },
        extendXhrObject: function(t, e) {
            return "object" == typeof e.callback && (t.callback = e.callback, delete e.callback), "function" == typeof e.beforeSend && (t.callback.beforeSend = e.beforeSend, delete e.beforeSend), t.request = j.extend(!0, t.request, e), "jsonp" !== t.request.dataType.toLowerCase() || t.request.jsonpCallback || (t.request.jsonpCallback = "callback_" + t.extra.group), t
        },
        handleRequests: function() {
            var t, h = this,
                c = this.requestGroups.length;
            if (!1 !== this.helper.executeCallback.call(this, this.options.callback.onSendRequest, [this.node, this.query]))
                for (var e = 0, i = this.requestGroups.length; e < i; ++e) t = this.requestGroups[e], this.requests[t].isDuplicated || function(t, r) {
                    if ("function" == typeof h.options.source[t].ajax) {
                        var e = h.options.source[t].ajax.call(h, h.query);
                        if ("object" != typeof(r = h.extendXhrObject(h.generateRequestObject(t), "object" == typeof e ? e : {})).request || !r.request.url) return h.populateSource([], t);
                        h.requests[t] = r
                    }
                    var a, i = !1,
                        l = {};
                    if (~r.request.url.indexOf("{{query}}") && (i || (r = j.extend(!0, {}, r), i = !0), r.request.url = r.request.url.replace("{{query}}", encodeURIComponent(h.query))), r.request.data)
                        for (var s in r.request.data)
                            if (r.request.data.hasOwnProperty(s) && ~String(r.request.data[s]).indexOf("{{query}}")) {
                                i || (r = j.extend(!0, {}, r), i = !0), r.request.data[s] = r.request.data[s].replace("{{query}}", h.query);
                                break
                            } j.ajax(r.request).done(function(t, e, i) {
                        for (var s, o = 0, n = r.validForGroup.length; o < n; o++) s = r.validForGroup[o], "function" == typeof(a = h.requests[s]).callback.done && (l[s] = a.callback.done.call(h, t, e, i))
                    }).fail(function(t, e, i) {
                        for (var s = 0, o = r.validForGroup.length; s < o; s++)(a = h.requests[r.validForGroup[s]]).callback.fail instanceof Function && a.callback.fail.call(h, t, e, i)
                    }).always(function(t, e, i) {
                        for (var s, o = 0, n = r.validForGroup.length; o < n; o++) {
                            if (s = r.validForGroup[o], (a = h.requests[s]).callback.always instanceof Function && a.callback.always.call(h, t, e, i), "abort" === e) return;
                            h.populateSource(null !== t && "function" == typeof t.promise && [] || l[s] || t, a.extra.group, a.extra.path || a.request.path), 0 === (c -= 1) && h.helper.executeCallback.call(h, h.options.callback.onReceiveRequest, [h.node, h.query])
                        }
                    }).then(function(t, e) {
                        for (var i = 0, s = r.validForGroup.length; i < s; i++)(a = h.requests[r.validForGroup[i]]).callback.then instanceof Function && a.callback.then.call(h, t, e)
                    })
                }(t, this.requests[t])
        },
        populateSource: function(i, t, e) {
            var s = this,
                o = this.options.source[t],
                n = o.ajax && o.data;
            e && "string" == typeof e && (i = this.helper.namespace.call(this, e, i)), Array.isArray(i) || (i = []), n && ("function" == typeof n && (n = n()), Array.isArray(n) && (i = i.concat(n)));
            for (var r, a = o.display ? "compiled" === o.display[0] ? o.display[1] : o.display[0] : "compiled" === this.options.display[0] ? this.options.display[1] : this.options.display[0], l = 0, h = i.length; l < h; l++) null !== i[l] && "boolean" != typeof i[l] && ("string" == typeof i[l] && ((r = {})[a] = i[l], i[l] = r), i[l].group = t);
            if (!this.hasDynamicGroups && this.dropdownFilter.dynamic.length) {
                var c, p, u = {};
                for (l = 0, h = i.length; l < h; l++)
                    for (var d = 0, f = this.dropdownFilter.dynamic.length; d < f; d++) c = this.dropdownFilter.dynamic[d].key, (p = i[l][c]) && (this.dropdownFilter.dynamic[d].value || (this.dropdownFilter.dynamic[d].value = []), u[c] || (u[c] = []), ~u[c].indexOf(p.toLowerCase()) || (u[c].push(p.toLowerCase()), this.dropdownFilter.dynamic[d].value.push(p)))
            }
            if (this.options.correlativeTemplate) {
                var m = o.template || this.options.template,
                    g = "";
                if ("function" == typeof m && (m = m.call(this, "", {})), m) {
                    if (Array.isArray(this.options.correlativeTemplate))
                        for (l = 0, h = this.options.correlativeTemplate.length; l < h; l++) g += "{{" + this.options.correlativeTemplate[l] + "}} ";
                    else g = m.replace(/<.+?>/g, " ").replace(/\s{2,}/, " ").trim();
                    for (l = 0, h = i.length; l < h; l++) i[l].compiled = j("<textarea />").html(g.replace(/\{\{([\w\-\.]+)(?:\|(\w+))?}}/g, function(t, e) {
                        return s.helper.namespace.call(s, e, i[l], "get", "")
                    }).trim()).text();
                    o.display ? ~o.display.indexOf("compiled") || o.display.unshift("compiled") : ~this.options.display.indexOf("compiled") || this.options.display.unshift("compiled")
                } else;
            }
            this.options.callback.onPopulateSource && (i = this.helper.executeCallback.call(this, this.options.callback.onPopulateSource, [this.node, i, t, e])), this.tmpSource[t] = Array.isArray(i) && i || [];
            var y = this.options.source[t].cache,
                v = this.options.source[t].compression,
                b = this.options.source[t].ttl || this.options.ttl;
            if (y && !window[y].getItem("TYPEAHEAD_" + this.selector + ":" + t)) {
                this.options.callback.onCacheSave && (i = this.helper.executeCallback.call(this, this.options.callback.onCacheSave, [this.node, i, t, e]));
                var k = JSON.stringify({
                    data: i,
                    ttl: (new Date).getTime() + b
                });
                v && (k = LZString.compressToUTF16(k)), window[y].setItem("TYPEAHEAD_" + this.selector + ":" + t, k)
            }
            this.incrementGeneratedGroup(t)
        },
        incrementGeneratedGroup: function(t) {
            if (this.generatedGroupCount++, this.generatedGroupCount === this.generateGroups.length || this.options.asyncResult) {
                this.xhr && this.xhr[t] && delete this.xhr[t];
                for (var e = 0, i = this.generateGroups.length; e < i; e++) this.source[this.generateGroups[e]] = this.tmpSource[this.generateGroups[e]];
                this.hasDynamicGroups || this.buildDropdownItemLayout("dynamic"), this.generatedGroupCount === this.generateGroups.length && (this.xhr = {}, this.options.loadingAnimation && this.container.removeClass("loading")), this.node.trigger("search" + this.namespace)
            }
        },
        navigate: function(t) {
            if (this.helper.executeCallback.call(this, this.options.callback.onNavigateBefore, [this.node, this.query, t]), 27 === t.keyCode) return t.preventDefault(), void(this.query.length ? (this.resetInput(), this.node.trigger("input" + this.namespace, [t])) : (this.node.blur(), this.hideLayout()));
            if (this.result.length) {
                var e, i = this.resultContainer.find("." + this.options.selector.item).not("[disabled]"),
                    s = i.filter(".active"),
                    o = s[0] ? i.index(s) : null,
                    n = s[0] ? s.attr("data-index") : null,
                    r = null;
                if (this.clearActiveItem(), this.helper.executeCallback.call(this, this.options.callback.onLeave, [this.node, null !== o && i.eq(o) || void 0, null !== n && this.result[n] || void 0, t]), 13 === t.keyCode) return t.preventDefault(), void(0 < s.length ? "javascript:;" === s.find("a:first")[0].href ? s.find("a:first").trigger("click", t) : s.find("a:first")[0].click() : this.node.closest("form").trigger("submit"));
                if (39 !== t.keyCode) {
                    9 === t.keyCode ? this.options.blurOnTab ? this.hideLayout() : 0 < s.length ? o + 1 < i.length ? (t.preventDefault(), r = o + 1, this.addActiveItem(i.eq(r))) : this.hideLayout() : i.length ? (t.preventDefault(), r = 0, this.addActiveItem(i.first())) : this.hideLayout() : 38 === t.keyCode ? (t.preventDefault(), 0 < s.length ? 0 <= o - 1 && (r = o - 1, this.addActiveItem(i.eq(r))) : i.length && (r = i.length - 1, this.addActiveItem(i.last()))) : 40 === t.keyCode && (t.preventDefault(), 0 < s.length ? o + 1 < i.length && (r = o + 1, this.addActiveItem(i.eq(r))) : i.length && (r = 0, this.addActiveItem(i.first()))), e = null !== r ? i.eq(r).attr("data-index") : null, this.helper.executeCallback.call(this, this.options.callback.onEnter, [this.node, null !== r && i.eq(r) || void 0, null !== e && this.result[e] || void 0, t]), t.preventInputChange && ~[38, 40].indexOf(t.keyCode) && this.buildHintLayout(null !== e && e < this.result.length ? [this.result[e]] : null), this.options.hint && this.hint.container && this.hint.container.css("color", t.preventInputChange ? this.hint.css.color : null === e && this.hint.css.color || this.hint.container.css("background-color") || "fff");
                    var a = null === e || t.preventInputChange ? this.rawQuery : this.getTemplateValue.call(this, this.result[e]);
                    this.node.val(a), this.isContentEditable && this.node.text(a), this.helper.executeCallback.call(this, this.options.callback.onNavigateAfter, [this.node, i, null !== r && i.eq(r).find("a:first") || void 0, null !== e && this.result[e] || void 0, this.query, t])
                } else null !== o ? i.eq(o).find("a:first")[0].click() : this.options.hint && "" !== this.hint.container.val() && this.helper.getCaret(this.node[0]) >= this.query.length && i.filter('[data-index="' + this.hintIndex + '"]').find("a:first")[0].click()
            }
        },
        getTemplateValue: function(i) {
            if (i) {
                var t = i.group && this.options.source[i.group].templateValue || this.options.templateValue;
                if ("function" == typeof t && (t = t.call(this)), !t) return this.helper.namespace.call(this, i.matchedKey, i).toString();
                var s = this;
                return t.replace(/\{\{([\w\-.]+)}}/gi, function(t, e) {
                    return s.helper.namespace.call(s, e, i, "get", "")
                })
            }
        },
        clearActiveItem: function() {
            this.resultContainer.find("." + this.options.selector.item).removeClass("active")
        },
        addActiveItem: function(t) {
            t.addClass("active")
        },
        searchResult: function() {
            this.resetLayout(), !1 !== this.helper.executeCallback.call(this, this.options.callback.onSearch, [this.node, this.query]) && (!this.searchGroups.length || this.options.multiselect && this.options.multiselect.limit && this.items.length >= this.options.multiselect.limit || this.searchResultData(), this.helper.executeCallback.call(this, this.options.callback.onResult, [this.node, this.query, this.result, this.resultCount, this.resultCountPerGroup]), this.isDropdownEvent && (this.helper.executeCallback.call(this, this.options.callback.onDropdownFilter, [this.node, this.query, this.filters.dropdown, this.result]), this.isDropdownEvent = !1))
        },
        searchResultData: function() {
            var t, e, i, s, o, n, r, a, l, h, c, p = this.groupBy,
                u = null,
                d = this.query.toLowerCase(),
                f = this.options.maxItem,
                m = this.options.maxItemPerGroup,
                g = this.filters.dynamic && !this.helper.isEmpty(this.filters.dynamic),
                y = "function" == typeof this.options.matcher && this.options.matcher;
            this.options.accent && (d = this.helper.removeAccent.call(this, d));
            for (var v = 0, b = this.searchGroups.length; v < b; ++v)
                if (F = this.searchGroups[v], (!this.filters.dropdown || "group" !== this.filters.dropdown.key || this.filters.dropdown.value === F) && (o = void 0 !== this.options.source[F].filter ? this.options.source[F].filter : this.options.filter, r = "function" == typeof this.options.source[F].matcher && this.options.source[F].matcher || y, this.source[F]))
                    for (var k = 0, w = this.source[F].length; k < w && (!(this.resultItemCount >= f) || this.options.callback.onResult); k++)
                        if ((!g || this.dynamicFilter.validate.apply(this, [this.source[F][k]])) && null !== (t = this.source[F][k]) && "boolean" != typeof t && (!this.options.multiselect || this.isMultiselectUniqueData(t)) && (!this.filters.dropdown || (t[this.filters.dropdown.key] || "").toLowerCase() === (this.filters.dropdown.value || "").toLowerCase())) {
                            if ((u = "group" === p ? F : t[p] ? t[p] : t.group) && !this.tmpResult[u] && (this.tmpResult[u] = [], this.resultCountPerGroup[u] = 0), m && "group" === p && this.tmpResult[u].length >= m && !this.options.callback.onResult) break;
                            for (var x = 0, C = (S = this.options.source[F].display || this.options.display).length; x < C; ++x) {
                                if (!1 !== o) {
                                    if (void 0 === (s = /\./.test(S[x]) ? this.helper.namespace.call(this, S[x], t) : t[S[x]]) || "" === s) continue;
                                    s = this.helper.cleanStringFromScript(s)
                                }
                                if ("function" == typeof o) {
                                    if (void 0 === (n = o.call(this, t, s))) break;
                                    if (!n) continue;
                                    "object" == typeof n && (t = n)
                                }
                                if (~[void 0, !0].indexOf(o)) {
                                    if (null === s) continue;
                                    if (i = (i = s).toString().toLowerCase(), this.options.accent && (i = this.helper.removeAccent.call(this, i)), e = i.indexOf(d), this.options.correlativeTemplate && "compiled" === S[x] && e < 0 && /\s/.test(d)) {
                                        l = !0, c = i;
                                        for (var q = 0, A = (h = d.split(" ")).length; q < A; q++)
                                            if ("" !== h[q]) {
                                                if (!~c.indexOf(h[q])) {
                                                    l = !1;
                                                    break
                                                }
                                                c = c.replace(h[q], "")
                                            }
                                    }
                                    if (e < 0 && !l) continue;
                                    if (this.options.offset && 0 !== e) continue;
                                    if (r) {
                                        if (void 0 === (a = r.call(this, t, s))) break;
                                        if (!a) continue;
                                        "object" == typeof a && (t = a)
                                    }
                                }
                                if (this.resultCount++, this.resultCountPerGroup[u]++, this.resultItemCount < f) {
                                    if (m && this.tmpResult[u].length >= m) break;
                                    this.tmpResult[u].push(j.extend(!0, {
                                        matchedKey: S[x]
                                    }, t)), this.resultItemCount++
                                }
                                break
                            }
                            if (!this.options.callback.onResult) {
                                if (this.resultItemCount >= f) break;
                                if (m && this.tmpResult[u].length >= m && "group" === p) break
                            }
                        } if (this.options.order) {
                var O, S = [];
                for (var F in this.tmpResult)
                    if (this.tmpResult.hasOwnProperty(F)) {
                        for (v = 0, b = this.tmpResult[F].length; v < b; v++) O = this.options.source[this.tmpResult[F][v].group].display || this.options.display, ~S.indexOf(O[0]) || S.push(O[0]);
                        this.tmpResult[F].sort(this.helper.sort(S, "asc" === this.options.order, function(t) {
                            return t ? t.toString().toUpperCase() : ""
                        }))
                    }
            }
            var L = [],
                I = [];
            for (v = 0, b = (I = "function" == typeof this.options.groupOrder ? this.options.groupOrder.apply(this, [this.node, this.query, this.tmpResult, this.resultCount, this.resultCountPerGroup]) : Array.isArray(this.options.groupOrder) ? this.options.groupOrder : "string" == typeof this.options.groupOrder && ~["asc", "desc"].indexOf(this.options.groupOrder) ? Object.keys(this.tmpResult).sort(this.helper.sort([], "asc" === this.options.groupOrder, function(t) {
                    return t.toString().toUpperCase()
                })) : Object.keys(this.tmpResult)).length; v < b; v++) L = L.concat(this.tmpResult[I[v]] || []);
            this.groups = JSON.parse(JSON.stringify(I)), this.result = L
        },
        buildLayout: function() {
            this.buildHtmlLayout(), this.buildBackdropLayout(), this.buildHintLayout(), this.options.callback.onLayoutBuiltBefore && (this.tmpResultHtml = this.helper.executeCallback.call(this, this.options.callback.onLayoutBuiltBefore, [this.node, this.query, this.result, this.resultHtml])), this.tmpResultHtml instanceof j ? this.resultContainer.html(this.tmpResultHtml) : this.resultHtml instanceof j && this.resultContainer.html(this.resultHtml), this.options.callback.onLayoutBuiltAfter && this.helper.executeCallback.call(this, this.options.callback.onLayoutBuiltAfter, [this.node, this.query, this.result])
        },
        buildHtmlLayout: function() {
            if (!1 !== this.options.resultContainer) {
                var h;
                if (this.resultContainer || (this.resultContainer = j("<div/>", {
                        class: this.options.selector.result
                    }), this.container.append(this.resultContainer)), !this.result.length && this.generatedGroupCount === this.generateGroups.length)
                    if (this.options.multiselect && this.options.multiselect.limit && this.items.length >= this.options.multiselect.limit) h = this.options.multiselect.limitTemplate ? "function" == typeof this.options.multiselect.limitTemplate ? this.options.multiselect.limitTemplate.call(this, this.query) : this.options.multiselect.limitTemplate.replace(/\{\{query}}/gi, j("<div>").text(this.helper.cleanStringFromScript(this.query)).html()) : "Can't select more than " + this.items.length + " items.";
                    else {
                        if (!this.options.emptyTemplate || "" === this.query) return;
                        h = "function" == typeof this.options.emptyTemplate ? this.options.emptyTemplate.call(this, this.query) : this.options.emptyTemplate.replace(/\{\{query}}/gi, j("<div>").text(this.helper.cleanStringFromScript(this.query)).html())
                    } this.displayEmptyTemplate = !!h;
                var o = this.query.toLowerCase();
                this.options.accent && (o = this.helper.removeAccent.call(this, o));
                var c = this,
                    t = this.groupTemplate || "<ul></ul>",
                    p = !1;
                this.groupTemplate ? t = j(t.replace(/<([^>]+)>\{\{(.+?)}}<\/[^>]+>/g, function(t, e, i, s, o) {
                    var n = "",
                        r = "group" === i ? c.groups : [i];
                    if (!c.result.length) return !0 === p ? "" : (p = !0, "<" + e + ' class="' + c.options.selector.empty + '">' + h + "</" + e + ">");
                    for (var a = 0, l = r.length; a < l; ++a) n += "<" + e + ' data-group-template="' + r[a] + '"><ul></ul></' + e + ">";
                    return n
                })) : (t = j(t), this.result.length || t.append(h instanceof j ? h : '<li class="' + c.options.selector.empty + '">' + h + "</li>")), t.addClass(this.options.selector.list + (this.helper.isEmpty(this.result) ? " empty" : ""));
                for (var e, i, n, s, r, a, l, u, d, f, m, g, y, v = this.groupTemplate && this.result.length && c.groups || [], b = 0, k = this.result.length; b < k; ++b) e = (n = this.result[b]).group, s = !this.options.multiselect && this.options.source[n.group].href || this.options.href, u = [], d = this.options.source[n.group].display || this.options.display, this.options.group && (e = n[this.options.group.key], this.options.group.template && ("function" == typeof this.options.group.template ? i = this.options.group.template.call(this, n) : "string" == typeof this.options.group.template && (i = this.options.group.template.replace(/\{\{([\w\-\.]+)}}/gi, function(t, e) {
                        return c.helper.namespace.call(c, e, n, "get", "")
                    }))), t.find('[data-search-group="' + e + '"]')[0] || (this.groupTemplate ? t.find('[data-group-template="' + e + '"] ul') : t).append(j("<li/>", {
                        class: c.options.selector.group,
                        html: j("<a/>", {
                            href: "javascript:;",
                            html: i || e,
                            tabindex: -1
                        }),
                        "data-search-group": e
                    }))), this.groupTemplate && v.length && ~(m = v.indexOf(e || n.group)) && v.splice(m, 1), r = j("<li/>", {
                        class: c.options.selector.item + " " + c.options.selector.group + "-" + this.helper.slugify.call(this, e),
                        disabled: !!n.disabled,
                        "data-group": e,
                        "data-index": b,
                        html: j("<a/>", {
                            href: s && !n.disabled ? (g = s, y = n, y.href = c.generateHref.call(c, g, y)) : "javascript:;",
                            html: function() {
                                if (a = n.group && c.options.source[n.group].template || c.options.template) "function" == typeof a && (a = a.call(c, c.query, n)), l = a.replace(/\{\{([^\|}]+)(?:\|([^}]+))*}}/gi, function(t, e, i) {
                                    var s = c.helper.cleanStringFromScript(String(c.helper.namespace.call(c, e, n, "get", "")));
                                    return ~(i = i && i.split("|") || []).indexOf("slugify") && (s = c.helper.slugify.call(c, s)), ~i.indexOf("raw") || !0 === c.options.highlight && o && ~d.indexOf(e) && (s = c.helper.highlight.call(c, s, o.split(" "), c.options.accent)), s
                                });
                                else {
                                    for (var t = 0, e = d.length; t < e; t++) void 0 !== (f = /\./.test(d[t]) ? c.helper.namespace.call(c, d[t], n, "get", "") : n[d[t]]) && "" !== f && u.push(f);
                                    l = '<span class="' + c.options.selector.display + '">' + c.helper.cleanStringFromScript(String(u.join(" "))) + "</span>"
                                }(!0 === c.options.highlight && o && !a || "any" === c.options.highlight) && (l = c.helper.highlight.call(c, l, o.split(" "), c.options.accent)), j(this).append(l)
                            }
                        })
                    }),
                    function(t, i, e) {
                        e.on("click", function(t, e) {
                            i.disabled ? t.preventDefault() : (e && "object" == typeof e && (t.originalEvent = e), c.options.mustSelectItem && c.helper.isEmpty(i) ? t.preventDefault() : (c.options.multiselect || (c.item = i), !1 !== c.helper.executeCallback.call(c, c.options.callback.onClickBefore, [c.node, j(this), i, t]) && (t.originalEvent && t.originalEvent.defaultPrevented || t.isDefaultPrevented() || (c.options.multiselect ? (c.query = c.rawQuery = "", c.addMultiselectItemLayout(i)) : (c.focusOnly = !0, c.query = c.rawQuery = c.getTemplateValue.call(c, i), c.isContentEditable && (c.node.text(c.query), c.helper.setCaretAtEnd(c.node[0]))), c.hideLayout(), c.node.val(c.query).focus(), c.options.cancelButton && c.toggleCancelButtonVisibility(), c.helper.executeCallback.call(c, c.options.callback.onClickAfter, [c.node, j(this), i, t])))))
                        }), e.on("mouseenter", function(t) {
                            i.disabled || (c.clearActiveItem(), c.addActiveItem(j(this))), c.helper.executeCallback.call(c, c.options.callback.onEnter, [c.node, j(this), i, t])
                        }), e.on("mouseleave", function(t) {
                            i.disabled || c.clearActiveItem(), c.helper.executeCallback.call(c, c.options.callback.onLeave, [c.node, j(this), i, t])
                        })
                    }(0, n, r), (this.groupTemplate ? t.find('[data-group-template="' + e + '"] ul') : t).append(r);
                if (this.result.length && v.length)
                    for (b = 0, k = v.length; b < k; ++b) t.find('[data-group-template="' + v[b] + '"]').remove();
                this.resultHtml = t
            }
        },
        generateHref: function(t, o) {
            var n = this;
            return "string" == typeof t ? t = t.replace(/\{\{([^\|}]+)(?:\|([^}]+))*}}/gi, function(t, e, i) {
                var s = n.helper.namespace.call(n, e, o, "get", "");
                return ~(i = i && i.split("|") || []).indexOf("slugify") && (s = n.helper.slugify.call(n, s)), s
            }) : "function" == typeof t && (t = t.call(this, o)), t
        },
        getMultiselectComparedData: function(t) {
            var e = "";
            if (Array.isArray(this.options.multiselect.matchOn))
                for (var i = 0, s = this.options.multiselect.matchOn.length; i < s; ++i) e += void 0 !== t[this.options.multiselect.matchOn[i]] ? t[this.options.multiselect.matchOn[i]] : "";
            else {
                var o = JSON.parse(JSON.stringify(t)),
                    n = ["group", "matchedKey", "compiled", "href"];
                for (i = 0, s = n.length; i < s; ++i) delete o[n[i]];
                e = JSON.stringify(o)
            }
            return e
        },
        buildBackdropLayout: function() {
            this.options.backdrop && (this.backdrop.container || (this.backdrop.css = j.extend({
                opacity: .6,
                filter: "alpha(opacity=60)",
                position: "fixed",
                top: 0,
                right: 0,
                bottom: 0,
                left: 0,
                "z-index": 1040,
                "background-color": "#000"
            }, this.options.backdrop), this.backdrop.container = j("<div/>", {
                class: this.options.selector.backdrop,
                css: this.backdrop.css
            }).insertAfter(this.container)), this.container.addClass("backdrop").css({
                "z-index": this.backdrop.css["z-index"] + 1,
                position: "relative"
            }))
        },
        buildHintLayout: function(t) {
            if (this.options.hint)
                if (this.node[0].scrollWidth > Math.ceil(this.node.innerWidth())) this.hint.container && this.hint.container.val("");
                else {
                    var e = this,
                        i = "",
                        s = (t = t || this.result, this.query.toLowerCase());
                    if (this.options.accent && (s = this.helper.removeAccent.call(this, s)), this.hintIndex = null, this.searchGroups.length) {
                        if (this.hint.container || (this.hint.css = j.extend({
                                "border-color": "transparent",
                                position: "absolute",
                                top: 0,
                                display: "inline",
                                "z-index": -1,
                                float: "none",
                                color: "silver",
                                "box-shadow": "none",
                                cursor: "default",
                                "-webkit-user-select": "none",
                                "-moz-user-select": "none",
                                "-ms-user-select": "none",
                                "user-select": "none"
                            }, this.options.hint), this.hint.container = j("<" + this.node[0].nodeName + "/>", {
                                type: this.node.attr("type"),
                                class: this.node.attr("class"),
                                readonly: !0,
                                unselectable: "on",
                                "aria-hidden": "true",
                                tabindex: -1,
                                click: function() {
                                    e.node.focus()
                                }
                            }).addClass(this.options.selector.hint).css(this.hint.css).insertAfter(this.node), this.node.parent().css({
                                position: "relative"
                            })), this.hint.container.css("color", this.hint.css.color), s)
                            for (var o, n, r, a = 0, l = t.length; a < l; a++)
                                if (!t[a].disabled) {
                                    n = t[a].group;
                                    for (var h = 0, c = (o = this.options.source[n].display || this.options.display).length; h < c; h++)
                                        if (r = String(t[a][o[h]]).toLowerCase(), this.options.accent && (r = this.helper.removeAccent.call(this, r)), 0 === r.indexOf(s)) {
                                            i = String(t[a][o[h]]), this.hintIndex = a;
                                            break
                                        } if (null !== this.hintIndex) break
                                } var p = 0 < i.length && this.rawQuery + i.substring(this.query.length) || "";
                        this.hint.container.val(p), this.isContentEditable && this.hint.container.text(p)
                    }
                }
        },
        buildDropdownLayout: function() {
            if (this.options.dropdownFilter) {
                var i = this;
                j("<span/>", {
                    class: this.options.selector.filter,
                    html: function() {
                        j(this).append(j("<button/>", {
                            type: "button",
                            class: i.options.selector.filterButton,
                            style: "display: none;",
                            click: function() {
                                i.container.toggleClass("filter");
                                var e = i.namespace + "-dropdown-filter";
                                j("html").off(e), i.container.hasClass("filter") && j("html").on("click" + e + " touchend" + e, function(t) {
                                    j(t.target).closest("." + i.options.selector.filter)[0] && j(t.target).closest(i.container)[0] || i.hasDragged || (i.container.removeClass("filter"), j("html").off(e))
                                })
                            }
                        })), j(this).append(j("<ul/>", {
                            class: i.options.selector.dropdown
                        }))
                    }
                }).insertAfter(i.container.find("." + i.options.selector.query))
            }
        },
        buildDropdownItemLayout: function(t) {
            if (this.options.dropdownFilter) {
                var e, i, o = this,
                    n = "string" == typeof this.options.dropdownFilter && this.options.dropdownFilter || "All",
                    r = this.container.find("." + this.options.selector.dropdown);
                "static" !== t || !0 !== this.options.dropdownFilter && "string" != typeof this.options.dropdownFilter || this.dropdownFilter.static.push({
                    key: "group",
                    template: "{{group}}",
                    all: n,
                    value: Object.keys(this.options.source)
                });
                for (var s = 0, a = this.dropdownFilter[t].length; s < a; s++) {
                    i = this.dropdownFilter[t][s], Array.isArray(i.value) || (i.value = [i.value]), i.all && (this.dropdownFilterAll = i.all);
                    for (var l = 0, h = i.value.length; l <= h; l++) l === h && s !== a - 1 || l === h && s === a - 1 && "static" === t && this.dropdownFilter.dynamic.length || (e = this.dropdownFilterAll || n, i.value[l] ? e = i.template ? i.template.replace(new RegExp("{{" + i.key + "}}", "gi"), i.value[l]) : i.value[l] : this.container.find("." + o.options.selector.filterButton).html(e), function(e, i, s) {
                        r.append(j("<li/>", {
                            class: o.options.selector.dropdownItem + " " + o.helper.slugify.call(o, i.key + "-" + (i.value[e] || n)),
                            html: j("<a/>", {
                                href: "javascript:;",
                                html: s,
                                click: function(t) {
                                    t.preventDefault(), c.call(o, {
                                        key: i.key,
                                        value: i.value[e] || "*",
                                        template: s
                                    })
                                }
                            })
                        }))
                    }(l, i, e))
                }
                this.dropdownFilter[t].length && this.container.find("." + o.options.selector.filterButton).removeAttr("style")
            }

            function c(t) {
                "*" === t.value ? delete this.filters.dropdown : this.filters.dropdown = t, this.container.removeClass("filter").find("." + this.options.selector.filterButton).html(t.template), this.isDropdownEvent = !0, this.node.trigger("input" + this.namespace), this.options.multiselect && this.adjustInputSize(), this.node.focus()
            }
        },
        dynamicFilter: {
            init: function() {
                this.options.dynamicFilter && (this.dynamicFilter.bind.call(this), this.isDynamicFilterEnabled = !0)
            },
            validate: function(t) {
                var e, i, s = null,
                    o = null;
                for (var n in this.filters.dynamic)
                    if (this.filters.dynamic.hasOwnProperty(n) && (i = ~n.indexOf(".") ? this.helper.namespace.call(this, n, t, "get") : t[n], "|" !== this.filters.dynamic[n].modifier || s || (s = i == this.filters.dynamic[n].value || !1), "&" === this.filters.dynamic[n].modifier)) {
                        if (i != this.filters.dynamic[n].value) {
                            o = !1;
                            break
                        }
                        o = !0
                    } return e = s, null !== o && !0 === (e = o) && null !== s && (e = s), !!e
            },
            set: function(t, e) {
                var i = t.match(/^([|&])?(.+)/);
                e ? this.filters.dynamic[i[2]] = {
                    modifier: i[1] || "|",
                    value: e
                } : delete this.filters.dynamic[i[2]], this.isDynamicFilterEnabled && this.generateSource()
            },
            bind: function() {
                for (var t, e = this, i = 0, s = this.options.dynamicFilter.length; i < s; i++) "string" == typeof(t = this.options.dynamicFilter[i]).selector && (t.selector = j(t.selector)), t.selector instanceof j && t.selector[0] && t.key && function(t) {
                    t.selector.off(e.namespace).on("change" + e.namespace, function() {
                        e.dynamicFilter.set.apply(e, [t.key, e.dynamicFilter.getValue(this)])
                    }).trigger("change" + e.namespace)
                }(t)
            },
            getValue: function(t) {
                var e;
                return "SELECT" === t.tagName ? e = t.value : "INPUT" === t.tagName && ("checkbox" === t.type ? e = t.checked && t.getAttribute("value") || t.checked || null : "radio" === t.type && t.checked && (e = t.value)), e
            }
        },
        buildMultiselectLayout: function() {
            if (this.options.multiselect) {
                var t, e = this;
                this.label.container = j("<span/>", {
                    class: this.options.selector.labelContainer,
                    "data-padding-left": parseFloat(this.node.css("padding-left")) || 0,
                    "data-padding-right": parseFloat(this.node.css("padding-right")) || 0,
                    "data-padding-top": parseFloat(this.node.css("padding-top")) || 0,
                    click: function(t) {
                        j(t.target).hasClass(e.options.selector.labelContainer) && e.node.focus()
                    }
                }), this.node.closest("." + this.options.selector.query).prepend(this.label.container), this.options.multiselect.data && (Array.isArray(this.options.multiselect.data) ? this.populateMultiselectData(this.options.multiselect.data) : "function" == typeof this.options.multiselect.data && (t = this.options.multiselect.data.call(this), Array.isArray(t) ? this.populateMultiselectData(t) : "function" == typeof t.promise && j.when(t).then(function(t) {
                    t && Array.isArray(t) && e.populateMultiselectData(t)
                })))
            }
        },
        isMultiselectUniqueData: function(t) {
            for (var e = !0, i = 0, s = this.comparedItems.length; i < s; ++i)
                if (this.comparedItems[i] === this.getMultiselectComparedData(t)) {
                    e = !1;
                    break
                } return e
        },
        populateMultiselectData: function(t) {
            for (var e = 0, i = t.length; e < i; ++e) this.addMultiselectItemLayout(t[e]);
            this.node.trigger("search" + this.namespace, {
                origin: "populateMultiselectData"
            })
        },
        addMultiselectItemLayout: function(t) {
            if (this.isMultiselectUniqueData(t)) {
                this.items.push(t), this.comparedItems.push(this.getMultiselectComparedData(t));
                var e, i = this.getTemplateValue(t),
                    s = this,
                    o = this.options.multiselect.href ? "a" : "span",
                    n = j("<span/>", {
                        class: this.options.selector.label,
                        html: j("<" + o + "/>", {
                            text: i,
                            click: function(t) {
                                var e = j(this).closest("." + s.options.selector.label),
                                    i = s.label.container.find("." + s.options.selector.label).index(e);
                                s.options.multiselect.callback && s.helper.executeCallback.call(s, s.options.multiselect.callback.onClick, [s.node, s.items[i], t])
                            },
                            href: this.options.multiselect.href ? (e = s.items[s.items.length - 1], s.generateHref.call(s, s.options.multiselect.href, e)) : null
                        })
                    });
                return n.append(j("<span/>", {
                    class: this.options.selector.cancelButton,
                    html: "×",
                    click: function(t) {
                        var e = j(this).closest("." + s.options.selector.label),
                            i = s.label.container.find("." + s.options.selector.label).index(e);
                        s.cancelMultiselectItem(i, e, t)
                    }
                })), this.label.container.append(n), this.adjustInputSize(), !0
            }
        },
        cancelMultiselectItem: function(t, e, i) {
            var s = this.items[t];
            (e = e || this.label.container.find("." + this.options.selector.label).eq(t)).remove(), this.items.splice(t, 1), this.comparedItems.splice(t, 1), this.options.multiselect.callback && this.helper.executeCallback.call(this, this.options.multiselect.callback.onCancel, [this.node, s, i]), this.adjustInputSize(), this.focusOnly = !0, this.node.focus().trigger("input" + this.namespace, {
                origin: "cancelMultiselectItem"
            })
        },
        adjustInputSize: function() {
            var i = this.node[0].getBoundingClientRect().width - (parseFloat(this.label.container.data("padding-right")) || 0) - (parseFloat(this.label.container.css("padding-left")) || 0),
                s = 0,
                o = 0,
                n = 0,
                r = !1,
                a = 0;
            this.label.container.find("." + this.options.selector.label).filter(function(t, e) {
                0 === t && (a = j(e)[0].getBoundingClientRect().height + parseFloat(j(e).css("margin-bottom") || 0)), s = j(e)[0].getBoundingClientRect().width + parseFloat(j(e).css("margin-right") || 0), .7 * i < n + s && !r && (o++, r = !0), n + s < i ? n += s : (r = !1, n = s)
            });
            var t = parseFloat(this.label.container.data("padding-left") || 0) + (r ? 0 : n),
                e = o * a + parseFloat(this.label.container.data("padding-top") || 0);
            this.container.find("." + this.options.selector.query).find("input, textarea, [contenteditable], .typeahead__hint").css({
                paddingLeft: t,
                paddingTop: e
            })
        },
        showLayout: function() {
            !this.container.hasClass("result") && (this.result.length || this.displayEmptyTemplate || this.options.backdropOnFocus) && (function() {
                var e = this;
                j("html").off("keydown" + this.namespace).on("keydown" + this.namespace, function(t) {
                    t.keyCode && 9 === t.keyCode && setTimeout(function() {
                        j(":focus").closest(e.container).find(e.node)[0] || e.hideLayout()
                    }, 0)
                }), j("html").off("click" + this.namespace + " touchend" + this.namespace).on("click" + this.namespace + " touchend" + this.namespace, function(t) {
                    j(t.target).closest(e.container)[0] || j(t.target).closest("." + e.options.selector.item)[0] || t.target.className === e.options.selector.cancelButton || e.hasDragged || e.hideLayout()
                })
            }.call(this), this.container.addClass([this.result.length || this.searchGroups.length && this.displayEmptyTemplate ? "result " : "", this.options.hint && this.searchGroups.length ? "hint" : "", this.options.backdrop || this.options.backdropOnFocus ? "backdrop" : ""].join(" ")), this.helper.executeCallback.call(this, this.options.callback.onShowLayout, [this.node, this.query]))
        },
        hideLayout: function() {
            (this.container.hasClass("result") || this.container.hasClass("backdrop")) && (this.container.removeClass("result hint filter" + (this.options.backdropOnFocus && j(this.node).is(":focus") ? "" : " backdrop")), this.options.backdropOnFocus && this.container.hasClass("backdrop") || (j("html").off(this.namespace), this.helper.executeCallback.call(this, this.options.callback.onHideLayout, [this.node, this.query])))
        },
        resetLayout: function() {
            this.result = [], this.tmpResult = {}, this.groups = [], this.resultCount = 0, this.resultCountPerGroup = {}, this.resultItemCount = 0, this.resultHtml = null, this.options.hint && this.hint.container && (this.hint.container.val(""), this.isContentEditable && this.hint.container.text(""))
        },
        resetInput: function() {
            this.node.val(""), this.isContentEditable && this.node.text(""), this.query = "", this.rawQuery = ""
        },
        buildCancelButtonLayout: function() {
            if (this.options.cancelButton) {
                var e = this;
                j("<span/>", {
                    class: this.options.selector.cancelButton,
                    html: "×",
                    mousedown: function(t) {
                        t.stopImmediatePropagation(), t.preventDefault(), e.resetInput(), e.node.trigger("input" + e.namespace, [t])
                    }
                }).insertBefore(this.node)
            }
        },
        toggleCancelButtonVisibility: function() {
            this.container.toggleClass("cancel", !!this.query.length)
        },
        __construct: function() {
            this.extendOptions(), this.unifySourceFormat() && (this.dynamicFilter.init.apply(this), this.init(), this.buildDropdownLayout(), this.buildDropdownItemLayout("static"), this.buildMultiselectLayout(), this.delegateEvents(), this.buildCancelButtonLayout(), this.helper.executeCallback.call(this, this.options.callback.onReady, [this.node]))
        },
        helper: {
            isEmpty: function(t) {
                for (var e in t)
                    if (t.hasOwnProperty(e)) return !1;
                return !0
            },
            removeAccent: function(t) {
                if ("string" == typeof t) {
                    var e = o;
                    return "object" == typeof this.options.accent && (e = this.options.accent), t = t.toLowerCase().replace(new RegExp("[" + e.from + "]", "g"), function(t) {
                        return e.to[e.from.indexOf(t)]
                    })
                }
            },
            slugify: function(t) {
                return "" !== (t = String(t)) && (t = (t = this.helper.removeAccent.call(this, t)).replace(/[^-a-z0-9]+/g, "-").replace(/-+/g, "-").replace(/^-|-$/g, "")), t
            },
            sort: function(s, i, o) {
                function n(t) {
                    for (var e = 0, i = s.length; e < i; e++)
                        if (void 0 !== t[s[e]]) return o(t[s[e]]);
                    return t
                }
                return i = [-1, 1][+!!i],
                    function(t, e) {
                        return t = n(t), e = n(e), i * ((e < t) - (t < e))
                    }
            },
            replaceAt: function(t, e, i, s) {
                return t.substring(0, e) + s + t.substring(e + i)
            },
            highlight: function(t, e, i) {
                t = String(t);
                var s = i && this.helper.removeAccent.call(this, t) || t,
                    o = [];
                Array.isArray(e) || (e = [e]), e.sort(function(t, e) {
                    return e.length - t.length
                });
                for (var n = e.length - 1; 0 <= n; n--) "" !== e[n].trim() ? e[n] = e[n].replace(/[-[\]{}()*+?.,\\^$|#\s]/g, "\\$&") : e.splice(n, 1);
                s.replace(new RegExp("(?:" + e.join("|") + ")(?!([^<]+)?>)", "gi"), function(t, e, i) {
                    o.push({
                        offset: i,
                        length: t.length
                    })
                });
                for (n = o.length - 1; 0 <= n; n--) t = this.helper.replaceAt(t, o[n].offset, o[n].length, "<strong>" + t.substr(o[n].offset, o[n].length) + "</strong>");
                return t
            },
            getCaret: function(t) {
                var e = 0;
                if (t.selectionStart) return t.selectionStart;
                if (document.selection) {
                    var i = document.selection.createRange();
                    if (null === i) return e;
                    var s = t.createTextRange(),
                        o = s.duplicate();
                    s.moveToBookmark(i.getBookmark()), o.setEndPoint("EndToStart", s), e = o.text.length
                } else if (window.getSelection) {
                    var n = window.getSelection();
                    if (n.rangeCount) {
                        var r = n.getRangeAt(0);
                        r.commonAncestorContainer.parentNode == t && (e = r.endOffset)
                    }
                }
                return e
            },
            setCaretAtEnd: function(t) {
                if (void 0 !== window.getSelection && void 0 !== document.createRange) {
                    var e = document.createRange();
                    e.selectNodeContents(t), e.collapse(!1);
                    var i = window.getSelection();
                    i.removeAllRanges(), i.addRange(e)
                } else if (void 0 !== document.body.createTextRange) {
                    var s = document.body.createTextRange();
                    s.moveToElementText(t), s.collapse(!1), s.select()
                }
            },
            cleanStringFromScript: function(t) {
                return "string" == typeof t && t.replace(/<\/?(?:script|iframe)\b[^>]*>/gm, "") || t
            },
            executeCallback: function(t, e) {
                if (t) {
                    var i;
                    if ("function" == typeof t) i = t;
                    else if (("string" == typeof t || Array.isArray(t)) && ("string" == typeof t && (t = [t, []]), "function" != typeof(i = this.helper.namespace.call(this, t[0], window)))) return;
                    return i.apply(this, (t[1] || []).concat(e || []))
                }
            },
            namespace: function(t, e, i, s) {
                if ("string" != typeof t || "" === t) return !1;
                var o = void 0 !== s ? s : void 0;
                if (!~t.indexOf(".")) return e[t] || o;
                for (var n = t.split("."), r = e || window, a = (i = i || "get", ""), l = 0, h = n.length; l < h; l++) {
                    if (void 0 === r[a = n[l]]) {
                        if (~["get", "delete"].indexOf(i)) return void 0 !== s ? s : void 0;
                        r[a] = {}
                    }
                    if (~["set", "create", "delete"].indexOf(i) && l === h - 1) {
                        if ("set" !== i && "create" !== i) return delete r[a], !0;
                        r[a] = o
                    }
                    r = r[a]
                }
                return r
            },
            typeWatch: (i = 0, function(t, e) {
                clearTimeout(i), i = setTimeout(t, e)
            })
        }
    }, j.fn.typeahead = j.typeahead = function(t) {
        return e.typeahead(this, t)
    };
    var e = {
        typeahead: function(t, e) {
            if (e && e.source && "object" == typeof e.source) {
                if ("function" == typeof t) {
                    if (!e.input) return;
                    t = j(e.input)
                }
                if (t.length) {
                    if (void 0 === t[0].value && (t[0].value = t.text()), 1 === t.length) return t[0].selector = t.selector || e.input || t[0].nodeName.toLowerCase(), window.Typeahead[t[0].selector] = new r(t, e);
                    for (var i, s = {}, o = 0, n = t.length; o < n; ++o) void 0 !== s[i = t[o].nodeName.toLowerCase()] && (i += o), t[o].selector = i, window.Typeahead[i] = s[i] = new r(t.eq(o), e);
                    return s
                }
            }
        }
    };
    return window.console = window.console || {
        log: function() {}
    }, Array.isArray || (Array.isArray = function(t) {
        return "[object Array]" === Object.prototype.toString.call(t)
    }), "trim" in String.prototype || (String.prototype.trim = function() {
        return this.replace(/^\s+/, "").replace(/\s+$/, "")
    }), "indexOf" in Array.prototype || (Array.prototype.indexOf = function(t, e) {
        void 0 === e && (e = 0), e < 0 && (e += this.length), e < 0 && (e = 0);
        for (var i = this.length; e < i; e++)
            if (e in this && this[e] === t) return e;
        return -1
    }), Object.keys || (Object.keys = function(t) {
        var e, i = [];
        for (e in t) Object.prototype.hasOwnProperty.call(t, e) && i.push(e);
        return i
    }), r
});