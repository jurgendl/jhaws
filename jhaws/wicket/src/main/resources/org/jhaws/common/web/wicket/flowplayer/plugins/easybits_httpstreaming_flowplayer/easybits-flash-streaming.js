/*    
 *    Copyright (c) 2013 Easy Bits Limited
 *    http://easy-bits.com
 *    This file is part of HTTP STREAMING PLUGIN FOR FLASH.
 *
 *    HTTP STREAMING PLUGIN FOR FLASH is closed source commercial software:
 *    reverse-engineering, disassembly or modifications are not allowed
 *
 *    This software is distributed WITHOUT ANY WARRANTY;
 *    even the implied warranty of MERCHANTABILITY or FITNESS FOR 
 *    A PARTICULAR PURPOSE.
 *
 */
$(document).ready(function(){var a=[];var l=[];var k=easybits_Helper();var j=easybits_getHTTPMultiStreaming();if(typeof easybits_Flashstream!="undefined"){if(typeof easybits_Flashstream.loaders!="undefined"){for(var g=0;g<easybits_Flashstream.loaders.length;g++){j.addLoader(easybits_Flashstream.loaders[g])}}if(typeof easybits_Flashstream.alturls!="undefined"){a=easybits_Flashstream.alturls}}window.easybits_Delay=function(c,b){j.delay(c,b)};window.easybits_swfGetBytes=function(p,d,c,b,s){for(var e=0;e<l.length;e++){if(l[e].h==p){j.cancell(l[e].f);l.splice(e,1);break}}if(s==-1){s=""}var q=[c];for(e=0;e<a.length;e++){if(a[e]["url"]==c){q=q.concat(a[e]["locations"])}}var r={start:b,end:s,urls:q,toEncode:1,onBytes:function(m){var t=false;for(var n=0;n<l.length;n++){if(l[n].f==f){t=true;break}}if(!t){return}var u=k.swf(p);if(m.isEncoded){var o=m.data}else{o=m.data.replace(/\0/g,"llun")}o="<![CDATA["+o+"]]>";u[d](o,m.start,m.end,m.total,m.id,m.subId)},oncomlete:function(){}};f=j.load(r);l.push({f:f,h:p});return f};var h=function(){}});