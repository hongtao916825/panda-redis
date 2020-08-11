var Action = {
	callback : function(e, callbackFn) {
		if (e.expression) {
			eval(e.expression);
			return;
		}
		if (e.callbackFn) {
			e.callbackFn();
		}
		if (callbackFn) {
			callbackFn(e);
		}
	},
	/**
	 * 异步请求
	 */
	jsonAsync : function(url, callbackFn) {
		jQuery.ajax({
			type : "POST",
			url : url,
			async : true,
			dataType : "json",
			success : function(e) {
				Action.callback(e, callbackFn);
			},
			error : function(e) {
			}
		});
	},
	/**
	 * 异步请求
	 */
	jsonAsyncByData : function(url, data, callbackFn,errorFn) {
		jQuery.ajax({
			type : "POST",
			url : url,
			async : true,
			dataType : "json",
			data : data,
			success : function(e) {
				Action.callback(e, callbackFn);
			},
			error : function(e) {
				if(errorFn) {
					errorFn(e);
				}
			}
		});
	},
	/**
	 * 同步请求
	 */
	jsonSyncByData : function(url, data, callbackFn) {
		jQuery.ajax({
			type : "POST",
			url : url,
			async : false,
			dataType : "json",
			data : data,
			success : function(e) {
				Action.callback(e, callbackFn);
			},
			error : function(e) {
			}
		});
	},
	/**
	 * 同步请求
	 */
	jsonSyncByDataForProcess : function(url, data, callbackFn) {
		jQuery.ajax({
			type : "POST",
			url : url,
			async : false,
			dataType : "json",
			contentType : "application/json",
			data : data,
			success : function(e) {
				Action.callback(e, callbackFn);
			},
			error : function(e) {
			}
		});
	},
	/**
	 * 同步请求
	 */
	jsonSync : function(url, callbackFn) {
		jQuery.ajax({
			type : "POST",
			url : url,
			async : false,
			dataType : "json",
			success : function(e) {
				Action.callback(e, callbackFn);
			},
			error : function(e) {
			}
		});
	},
	/**
	 * 返回当前url的所有参数字符串
	 */
	getParamStr : function() {
		var locationStr = new String(document.location);
		var paramIndex = locationStr.indexOf("?");
		if (paramIndex == -1) {
			return "";
		} else {
			return loc.substring(1 + paramIndex);
		}
	},
	/**
	 * 为一个url增加参数
	 */
	addParam : function(url, paramStr) {
		if (url.indexOf("?") == -1) {
			return url + "?" + paramStr;
		} else {
			return url + "&" + paramStr;
		}
	},
	/**
	 * 根据参数key获得value
	 */
	getParamValue : function(key) {
		var url = location.href;
		url = url.toLowerCase();
		key = key.toLowerCase();
		if (url.indexOf('?') == -1)
			return "";
		var urlarr = url.split("?");
		urlarr = urlarr[urlarr.length - 1];
		urlarr = urlarr.split("&");
		for ( var i = 0; i < urlarr.length; i++) {
			var s = urlarr[i].split("=");
			if (s[0] == key) {
				return s[1].replace(/#/g, "");
			}
		}
		return "";
	},
	buildRefreshUrl : function() {
		// 去除地址的jsessionid参数
		var url = location.href;
		return this.removeJsessionid(url);
	},
	removeJsessionid : function(url) {
		var jsessionidIndex = url.indexOf(";jsessionid=");
		if (jsessionidIndex == -1) {
			return url;
		} else {
			var paramIndex = url.indexOf("?");
			if (paramIndex == -1) {
				return url.substring(0, jsessionidIndex);
			} else {
				return url.substring(0, jsessionidIndex)
						+ url.substring(paramIndex);
			}
		}
	}
};