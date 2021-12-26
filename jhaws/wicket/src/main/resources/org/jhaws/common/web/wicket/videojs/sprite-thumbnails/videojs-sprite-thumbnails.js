/*! @name videojs-sprite-thumbnails @version 0.6.0 @license MIT */
(function (global, factory) {
	typeof exports === 'object' && typeof module !== 'undefined' ? module.exports = factory(require('video.js'), require('global/window')) :
	typeof define === 'function' && define.amd ? define(['video.js', 'global/window'], factory) :
	(global = typeof globalThis !== 'undefined' ? globalThis : global || self, global.videojsSpriteThumbnails = factory(global.videojs, global.window));
}(this, (function (videojs, window) { 'use strict';

	function _interopDefaultLegacy (e) { return e && typeof e === 'object' && 'default' in e ? e : { 'default': e }; }

	var videojs__default = /*#__PURE__*/_interopDefaultLegacy(videojs);
	var window__default = /*#__PURE__*/_interopDefaultLegacy(window);

	function createCommonjsModule(fn, basedir, module) {
		return module = {
		  path: basedir,
		  exports: {},
		  require: function (path, base) {
	      return commonjsRequire(path, (base === undefined || base === null) ? module.path : base);
	    }
		}, fn(module, module.exports), module.exports;
	}

	function commonjsRequire () {
		throw new Error('Dynamic requires are not currently supported by @rollup/plugin-commonjs');
	}

	var assertThisInitialized = createCommonjsModule(function (module) {
	  function _assertThisInitialized(self) {
	    if (self === void 0) {
	      throw new ReferenceError("this hasn't been initialised - super() hasn't been called");
	    }

	    return self;
	  }

	  module.exports = _assertThisInitialized;
	  module.exports["default"] = module.exports, module.exports.__esModule = true;
	});

	var setPrototypeOf = createCommonjsModule(function (module) {
	  function _setPrototypeOf(o, p) {
	    module.exports = _setPrototypeOf = Object.setPrototypeOf || function _setPrototypeOf(o, p) {
	      o.__proto__ = p;
	      return o;
	    };

	    module.exports["default"] = module.exports, module.exports.__esModule = true;
	    return _setPrototypeOf(o, p);
	  }

	  module.exports = _setPrototypeOf;
	  module.exports["default"] = module.exports, module.exports.__esModule = true;
	});

	var inheritsLoose = createCommonjsModule(function (module) {
	  function _inheritsLoose(subClass, superClass) {
	    subClass.prototype = Object.create(superClass.prototype);
	    subClass.prototype.constructor = subClass;
	    setPrototypeOf(subClass, superClass);
	  }

	  module.exports = _inheritsLoose;
	  module.exports["default"] = module.exports, module.exports.__esModule = true;
	});

	/**
	 * Set up sprite thumbnails for a player.
	 *
	 * @function spriteThumbs
	 * @param {Player} player
	 *        The current player instance.
	 * @param {Plugin} plugin
	 *        The current spriteThumbnails plugin instance.
	 * @param {Object} options
	 *        Plugin configuration options.
	 */

	var spriteThumbs = function spriteThumbs(player, plugin, options) {
	  var url = options.url;
	  var height = options.height;
	  var width = options.width;
	  var isPreloading = false;
	  var sprites = {};
	  var dom = videojs__default['default'].dom || videojs__default['default'];
	  var controls = player.controlBar; // default control bar component tree is expected
	  // https://docs.videojs.com/tutorial-components.html#default-component-tree

	  var progress = controls && controls.progressControl;
	  var seekBar = progress && progress.seekBar;
	  var mouseTimeTooltip = seekBar && seekBar.mouseTimeDisplay && seekBar.mouseTimeDisplay.timeTooltip;

	  var tooltipStyle = function tooltipStyle(obj) {
	    var ttstyle = mouseTimeTooltip.el().style;
	    Object.keys(obj).forEach(function (key) {
	      var val = obj[key];

	      if (val !== '') {
	        ttstyle.setProperty(key, val);
	      } else {
	        ttstyle.removeProperty(key);
	      }
	    });
	  };

	  var resetMouseTooltip = function resetMouseTooltip() {
	    tooltipStyle({
	      'width': '',
	      'height': '',
	      'background-image': '',
	      'background-repeat': '',
	      'background-position': '',
	      'background-size': '',
	      'top': '',
	      'color': '',
	      'text-shadow': '',
	      'border': '',
	      'margin': ''
	    });
	  };

	  var hijackMouseTooltip = function hijackMouseTooltip(evt) {
	    var sprite = sprites[url];
	    var imgWidth = sprite.naturalWidth;
	    var imgHeight = sprite.naturalHeight;
	    var seekBarEl = seekBar.el();

	    if (sprite.complete && imgWidth && imgHeight) {
	      var position = dom.getPointerPosition(seekBarEl, evt).x * player.duration();
	      position = position / options.interval;
	      var responsive = options.responsive;
	      var playerWidth = player.currentWidth();
	      var scaleFactor = responsive && playerWidth < responsive ? playerWidth / responsive : 1;
	      var columns = imgWidth / width;
	      var scaledWidth = width * scaleFactor;
	      var scaledHeight = height * scaleFactor;
	      var cleft = Math.floor(position % columns) * -scaledWidth;
	      var ctop = Math.floor(position / columns) * -scaledHeight;
	      var bgSize = imgWidth * scaleFactor + 'px ' + imgHeight * scaleFactor + 'px';
	      var controlsTop = dom.findPosition(controls.el()).top;
	      var seekBarTop = dom.findPosition(seekBarEl).top; // top of seekBar is 0 position

	      var topOffset = -scaledHeight - Math.max(0, seekBarTop - controlsTop);
	      tooltipStyle({
	        'width': scaledWidth + 'px',
	        'height': scaledHeight + 'px',
	        'background-image': 'url(' + url + ')',
	        'background-repeat': 'no-repeat',
	        'background-position': cleft + 'px ' + ctop + 'px',
	        'background-size': bgSize,
	        'top': topOffset + 'px',
	        'color': '#fff',
	        'text-shadow': '1px 1px #000',
	        'border': '1px solid #000',
	        'margin': '0 1px'
	      });
	    } else {
	      resetMouseTooltip();
	    }
	  };

	  var spriteready = function spriteready(preload) {
	    var spriteEvents = ['mousemove', 'touchmove'];
	    var win = window__default['default'];
	    var navigator = win.navigator;
	    var log = plugin.log;
	    var downlink = options.downlink;
	    var connection = navigator.connection || navigator.mozConnection || navigator.webkitConnection;
	    var dl = !connection || connection.downlink >= downlink;
	    var ready = mouseTimeTooltip && (width && height || preload);
	    var cached = sprites[url];

	    var setReady = function setReady(bool) {
	      plugin.setState({
	        ready: bool
	      });
	    };

	    if (mouseTimeTooltip) {
	      resetMouseTooltip();
	    }

	    if (ready && (url && dl || cached)) {
	      var msg = 'loading ' + url;

	      if (!cached) {
	        sprites[url] = new win.Image();
	        sprites[url].src = url;

	        if (preload) {
	          isPreloading = true;
	          msg = 'pre' + msg;
	        }
	      } else {
	        msg = 're' + msg;
	      }

	      log.debug(msg);
	      progress.on(spriteEvents, hijackMouseTooltip);
	      setReady(true);
	    } else if (!preload) {
	      progress.off(spriteEvents, hijackMouseTooltip);
	      setReady();
	      ['url', 'width', 'height'].forEach(function (key) {
	        if (!options[key]) {
	          log('no thumbnails ' + key + ' given');
	        }
	      });

	      if (!dl) {
	        log.warn('connection.downlink < ' + downlink);
	      }
	    }
	  }; // preload sprite image if url configured at plugin level
	  // NOTE: must be called before loadstart, otherwise
	  // neither this call nor the first loadstart has any effect


	  spriteready(true);
	  player.on('loadstart', function () {
	    if (isPreloading) {
	      isPreloading = false;
	    } else {
	      // load sprite configured as source property
	      player.currentSources().forEach(function (src) {
	        var spriteOpts = src.spriteThumbnails;

	        if (spriteOpts) {
	          options = videojs__default['default'].mergeOptions(options, spriteOpts);
	          url = options.url;
	          height = options.height;
	          width = options.width;
	        }
	      });
	      spriteready();
	    }
	  });
	  player.addClass('vjs-sprite-thumbnails');
	};

	var version = "0.6.0";

	var Plugin = videojs__default['default'].getPlugin('plugin');
	/**
	 * Default plugin options
	 *
	 * @param {String} url
	 *        Sprite location. Must be set by user.
	 * @param {Integer} width
	 *        Width in pixels of a thumbnail. Must be set by user.
	 * @param {Integer} height
	 *        Height in pixels of a thumbnail. Must be set by user.
	 * @param {Number} interval
	 *        Interval between thumbnail frames in seconds. Default: 1.
	 * @param {Integer} responsive
	 *        Width of player below which thumbnails are reponsive. Default: 600.
	 * @param {Number} downlink
	 *        Minimum of NetworkInformation downlink where supported. Default: 2.
	 *        https://developer.mozilla.org/docs/Web/API/NetworkInformation/downlink
	 */

	var defaults = {
	  url: '',
	  width: 0,
	  height: 0,
	  interval: 1,
	  responsive: 600,
	  downlink: 2
	};
	/**
	 * An advanced Video.js plugin. For more information on the API
	 *
	 * See: https://blog.videojs.com/feature-spotlight-advanced-plugins/
	 */

	var SpriteThumbnails = /*#__PURE__*/function (_Plugin) {
	  inheritsLoose(SpriteThumbnails, _Plugin);

	  /**
	   * Create a SpriteThumbnails plugin instance.
	   *
	   * @param  {Player} player
	   *         A Video.js Player instance.
	   *
	   * @param  {Object} [options]
	   *         An optional options object.
	   */
	  function SpriteThumbnails(player, options) {
	    var _this;

	    // the parent class will add player under this.player
	    _this = _Plugin.call(this, player) || this;

	    var that = assertThisInitialized(_this);

	    var log = that.log;
	    that.options = videojs__default['default'].mergeOptions(defaults, options);
	    that.setState({
	      ready: false
	    });
	    that.on('statechanged', function () {
	      log.debug('will' + (that.state.ready ? '' : ' not') + ' show thumbnails');
	    });
	    that.player.ready(function () {
	      spriteThumbs(that.player, that, that.options);
	    });
	    return _this;
	  }

	  return SpriteThumbnails;
	}(Plugin); // Define default values for the plugin's `state` object here.


	SpriteThumbnails.defaultState = {}; // Include the version number.

	SpriteThumbnails.VERSION = version; // Register the plugin with video.js.

	videojs__default['default'].registerPlugin('spriteThumbnails', SpriteThumbnails);

	return SpriteThumbnails;

})));
