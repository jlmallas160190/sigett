PrimeFaces.widget.Dialog = PrimeFaces.widget.BaseWidget.extend({
    init: function(a) {
        this._super(a);
        this.content = this.jq.children(".ui-dialog-content");
        this.titlebar = this.jq.children(".ui-dialog-titlebar");
        this.footer = this.jq.find(".ui-dialog-footer");
        this.icons = this.titlebar.children(".ui-dialog-titlebar-icon");
        this.closeIcon = this.titlebar.children(".ui-dialog-titlebar-close");
        this.minimizeIcon = this.titlebar.children(".ui-dialog-titlebar-minimize");
        this.maximizeIcon = this.titlebar.children(".ui-dialog-titlebar-maximize");
        this.blockEvents = "focus.dialog mousedown.dialog mouseup.dialog keydown.dialog keyup.dialog";
        this.cfg.width = this.cfg.width || "auto";
        this.cfg.height = this.cfg.height || "auto";
        this.cfg.draggable = this.cfg.draggable === false ? false : true;
        this.cfg.resizable = this.cfg.resizable === false ? false : true;
        this.cfg.minWidth = this.cfg.minWidth || 150;
        this.cfg.minHeight = this.cfg.minHeight || this.titlebar.outerHeight();
        this.cfg.position = this.cfg.position || "center";
        this.parent = this.jq.parent();
        this.jq.css({width: this.cfg.width, height: "auto"});
        this.content.height(this.cfg.height);
        this.bindEvents();
        if (this.cfg.draggable) {
            this.setupDraggable()
        }
        if (this.cfg.resizable) {
            this.setupResizable()
        }
        if (this.cfg.modal) {
            this.syncWindowResize()
        }
        if (this.cfg.appendTo) {
            this.jq.appendTo(PrimeFaces.Expressions.resolveComponentsAsSelector(this.cfg.appendTo))
        } else {
            if (this.cfg.appendToBody) {
                this.jq.appendTo("body")
            }
        }
        if ($(document.body).children(".ui-dialog-docking-zone").length === 0) {
            $(document.body).append('<div class="ui-dialog-docking-zone"></div>')
        }
        var b = $(this.jqId + "_modal");
        if (b.length > 0) {
            b.remove()
        }
        this.applyARIA();
        if (this.cfg.visible) {
            this.show()
        }
    }, refresh: function(a) {
        this.positionInitialized = false;
        this.loaded = false;
        $(document).off("keydown.dialog_" + a.id);
        if (a.appendTo) {
            var b = $(this.jqId);
            if (b.length > 1) {
                PrimeFaces.Expressions.resolveComponentsAsSelector(a.appendTo).children(this.jqId).remove()
            }
        } else {
            if (a.appendToBody) {
                var b = $(this.jqId);
                if (b.length > 1) {
                    $(document.body).children(this.jqId).remove()
                }
            }
        }
        this.init(a)
    }, enableModality: function() {
        var a = this;
        $(document.body).append('<div id="' + this.id + '_modal" class="ui-widget-overlay"></div>').children(this.jqId + "_modal").css({
            width: $(document).width(), height: $(document).height(), "z-index": this.jq.css("z-index") - 1});
        $(document).bind("keydown.modal-dialog", function(d) {
            if (d.keyCode == $.ui.keyCode.TAB) {
                var c = a.content.find(":tabbable"), e = c.filter(":first"), b = c.filter(":last");
                if (d.target === b[0] && !d.shiftKey) {
                    e.focus(1);
                    return false
                } else {
                    if (d.target === e[0] && d.shiftKey) {
                        b.focus(1);
                        return false
                    }
                }
            }
        }).bind(this.blockEvents, function(b) {
            if ($(b.target).zIndex() < a.jq.zIndex()) {
                return false
            }
        }
        )
    }, disableModality: function() {
        $(document.body).children(this.jqId + "_modal").remove();
        $(document).unbind(this.blockEvents).unbind("keydown.modal-dialog")
    }, syncWindowResize: function() {
        $(window).resize(function() {
            $(document.body).children(".ui-widget-overlay").css({width: $(document).width(), height: $(document).height()
            })
        })
    }, show: function() {
        if (this.jq.hasClass("ui-overlay-visible")) {
            return
        }
        if (!this.loaded && this.cfg.dynamic) {
            this.loadContents()
        } else {
            if (!this.positionInitialized) {
                this.initPosition()
            }
            this._show()
        }
    }, _show: function() {
        this.jq.removeClass("ui-overlay-hidden").addClass("ui-overlay-visible").css({display: "none", visibility: "visible"
        });
        if (this.cfg.showEffect) {
            var a = this;
            this.jq.show(this.cfg.showEffect, null, "normal", function() {
                a.postShow()
            })
        } else {
            this.jq.show();
            this.postShow()
        }
        this.moveToTop();
        if (this.cfg.modal) {
            this.enableModality()
        }
    }, postShow: function() {
        if (this.cfg.onShow) {
            this.cfg.onShow.call(this)
        }
        this.jq.attr({"aria-hidden": false, "aria-live": "polite"});
        this.applyFocus()
    }, hide: function() {
        if (this.jq.hasClass("ui-overlay-hidden")) {
            return
        }
        if (this.cfg.hideEffect) {
            var a = this;
            this.jq.hide(this.cfg.hideEffect, null, "normal", function() {
                if (a.cfg.modal) {
                    a.disableModality()
                }
                a.onHide()
            })
        } else {
            this.jq.hide();
            if (this.cfg.modal) {
                this.disableModality()
            }
            this.onHide()
        }
    }, applyFocus: function() {
        if (this.cfg.focus) {
            PrimeFaces.Expressions.resolveComponentsAsSelector(this.cfg.focus).focus()
        } else {
            this.jq.find(":not(:submit):not(:button):input:visible:enabled:first").focus()
        }
    }, bindEvents: function() {
        var a = this;
        this.jq.mousedown(function(b) {
            if (!$(b.target).data("primefaces-overlay-target")) {
                a.moveToTop()
            }
        });
        this.icons.mouseover(function() {
            $(this).addClass("ui-state-hover")
        }).mouseout(function() {
            $(this).removeClass("ui-state-hover")
        });
        this.closeIcon.click(function(b) {
            a.hide();
            b.preventDefault()
        });
        this.maximizeIcon.click(function(b) {
            a.toggleMaximize();
            b.preventDefault()
        });
        this.minimizeIcon.click(function(b) {
            a.toggleMinimize();
            b.preventDefault()
        });
        if (this.cfg.closeOnEscape) {
            $(document).on("keydown.dialog_" + this.id, function(d) {
                var c = $.ui.keyCode, b = parseInt(a.jq.css("z-index")) === PrimeFaces.zindex;
                if (d.which === c.ESCAPE && a.jq.hasClass("ui-overlay-visible") && b) {
                    a.hide()
                }
            })
        }
    }, setupDraggable: function() {
        this.jq.draggable({cancel: ".ui-dialog-content, .ui-dialog-titlebar-close", handle: ".ui-dialog-titlebar", containment: "document"})
    }, setupResizable: function() {
        var a = this;
        this.jq.resizable({handles: "n,s,e,w,ne,nw,se,sw", minWidth: this.cfg.minWidth, minHeight: this.cfg.minHeight, alsoResize: this.content, containment: "document", start: function(b, c) {
                a.jq.data("offset", a.jq.offset())
            }, stop: function(b, c) {
                var d = a.jq.data("offset");
                a.jq.css("position", "fixed");
                a.jq.offset(d)
            }});
        this.resizers = this.jq.children(".ui-resizable-handle")
    }, initPosition: function() {
        this.jq.css({left: 0, top: 0});
        if (/(center|left|top|right|bottom)/.test(this.cfg.position)) {
            this.cfg.position = this.cfg.position.replace(",", " ");
            this.jq.position({my: "center", at: this.cfg.position, collision: "fit", of: window, using: function(f) {
                    var d = f.left < 0 ? 0 : f.left, e = f.top < 0 ? 0 : f.top;
                    $(this).css({left: d, top: e})
                }})
        } else {
            var b = this.cfg.position.split(","), a = $.trim(b[0]), c = $.trim(b[1]);
            this.jq.offset({left: a, top: c})
        }
        this.positionInitialized = true
    }, onHide: function(b, c) {
        this.jq.removeClass("ui-overlay-visible").addClass("ui-overlay-hidden").css({display: "block", visibility: "hidden"});
        if (this.cfg.behaviors) {
            var a = this.cfg.behaviors.close;
            if (a) {
                a.call(this)
            }
        }
        this.jq.attr({"aria-hidden": true, "aria-live": "off"});
        if (this.cfg.onHide) {
            this.cfg.onHide.call(this, b, c)
        }
    }, moveToTop: function() {
        this.jq.css("z-index", ++PrimeFaces.zindex)
    }, toggleMaximize: function() {
        if (this.minimized) {
            this.toggleMinimize()
        }
        if (this.maximized) {
            this.jq.removeClass("ui-dialog-maximized");
            this.restoreState();
            this.maximizeIcon.children(".ui-icon").removeClass("ui-icon-newwin").addClass("ui-icon-extlink");
            this.maximized = false
        } else {
            this.saveState();
            var b = $(window);
            this.jq.addClass("ui-dialog-maximized").css({width: b.width() - 6, height: b.height()}).offset({top: b.scrollTop(), left: b.scrollLeft()});
            this.content.css({width: "auto", height: "auto"});
            this.maximizeIcon.removeClass("ui-state-hover").children(".ui-icon").removeClass("ui-icon-extlink").addClass("ui-icon-newwin");
            this.maximized = true;
            if (this.cfg.behaviors) {
                var a = this.cfg.behaviors.maximize;
                if (a) {
                    a.call(this)
                }
            }
        }
    }, toggleMinimize: function() {
        var a = true, c = $(document.body).children(".ui-dialog-docking-zone");
        if (this.maximized) {
            this.toggleMaximize();
            a = false
        }
        var b = this;
        if (this.minimized) {
            this.jq.appendTo(this.parent).removeClass("ui-dialog-minimized").css({position: "fixed", "float": "none"});
            this.restoreState();
            this.content.show();
            this.minimizeIcon.removeClass("ui-state-hover").children(".ui-icon").removeClass("ui-icon-plus").addClass("ui-icon-minus");
            this.minimized = false;
            if (this.cfg.resizable) {
                this.resizers.show()
            }
        } else {
            this.saveState();
            if (a) {
                this.jq.effect("transfer", {to: c, className: "ui-dialog-minimizing"}, 500, function() {
                    b.dock(c);
                    b.jq.addClass("ui-dialog-minimized")
                })
            } else {
                this.dock(c)
            }
        }
    }, dock: function(a) {
        this.jq.appendTo(a).css("position", "static");
        this.jq.css({height: "auto", width: "auto", "float": "left"});
        this.content.hide();
        this.minimizeIcon.removeClass("ui-state-hover").children(".ui-icon").removeClass("ui-icon-minus").addClass("ui-icon-plus");
        this.minimized = true;
        if (this.cfg.resizable) {
            this.resizers.hide()
        }
        if (this.cfg.behaviors) {
            var b = this.cfg.behaviors.minimize;
            if (b) {
                b.call(this)
            }
        }
    }, saveState: function() {
        this.state = {width: this.jq.width(), height: this.jq.height()};
        var a = $(window);
        this.state.offset = this.jq.offset();
        this.state.windowScrollLeft = a.scrollLeft();
        this.state.windowScrollTop = a.scrollTop()
    }, restoreState: function(a) {
        this.jq.width(this.state.width).height(this.state.height);
        var b = $(window);
        this.jq.offset({top: this.state.offset.top + (b.scrollTop() - this.state.windowScrollTop), left: this.state.offset.left + (b.scrollLeft() - this.state.windowScrollLeft)})
    }, loadContents: function() {
        var a = {source: this.id, process: this.id, update: this.id}, b = this;
        a.onsuccess = function(g) {
            var e = $(g.documentElement), f = e.find("update");
            for (var c = 0; c < f.length; c++) {
                var j = f.eq(c), h = j.attr("id"), d = j.get(0).childNodes[0].nodeValue;
                if (h === b.id) {
                    b.content.html(d)
                } else {
                    PrimeFaces.ajax.AjaxUtils.updateElement.call(this, h, d)
                }
            }
            PrimeFaces.ajax.AjaxUtils.handleResponse.call(this, e);
            return true
        };
        a.oncomplete = function() {
            b.loaded = true;
            b.show()
        };
        a.params = [{name: this.id + "_contentLoad", value: true}];
        PrimeFaces.ajax.AjaxRequest(a)
    }, applyARIA: function() {
        this.jq.attr({role: "dialog", "aria-labelledby": this.id + "_title", "aria-hidden": !this.cfg.visible});
        this.titlebar.children("a.ui-dialog-titlebar-icon").attr("role", "button")
    }});
PrimeFaces.widget.ConfirmDialog = PrimeFaces.widget.Dialog.extend({init: function(cfg) {
        cfg.draggable = false;
        cfg.resizable = false;
        cfg.modal = true;
        cfg.appendToBody = cfg.appendToBody || cfg.global;
        this._super(cfg);
        this.title = this.titlebar.children(".ui-dialog-title");
        this.message = this.content.children(".ui-confirm-dialog-message");
        this.icon = this.content.children(".ui-confirm-dialog-severity");
        if (this.cfg.global) {
            PrimeFaces.confirmDialog = this;
            this.jq.find(".ui-confirmdialog-yes").on("click.ui-confirmdialog", function(e) {
                if (PrimeFaces.confirmSource) {
                    var fn = eval("(function(){" + PrimeFaces.confirmSource.data("pfconfirmcommand") + "})");
                    fn.call(PrimeFaces.confirmSource);
                    PrimeFaces.confirmDialog.hide();
                    PrimeFaces.confirmSource = null
                }
                e.preventDefault()
            });
            this.jq.find(".ui-confirmdialog-no").on("click.ui-confirmdialog", function(e) {
                PrimeFaces.confirmDialog.hide();
                PrimeFaces.confirmSource = null;
                e.preventDefault()
            })
        }
    }, applyFocus: function() {
        this.jq.find(":button,:submit").filter(":visible:enabled").eq(0).focus()
    }, showMessage: function(a) {
        if (a.header) {
            this.title.text(a.header)
        }
        if (a.message) {
            this.message.text(a.message)
        }
        if (a.icon) {
            this.icon.removeClass().addClass("ui-icon ui-confirm-dialog-severity " + a.icon)
        }
        this.show()
    }});


