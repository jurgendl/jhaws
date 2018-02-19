package org.tools.hqlbuilder.webservice.bootstrap4.notify;

import org.tools.hqlbuilder.webservice.bootstrap4.Bootstrap4;
import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// call:
// $.notify({message:'Done'},{delay:0,type:'info',animate: { enter: 'animated fadeInRight', exit: 'animated fadeOutRight' } } );
//
// css:
// [data-notify="progressbar"] {
// margin-bottom: 0px;
// position: absolute;
// bottom: 0px;
// left: 0px;
// width: 100%;
// height: 5px;
// }
//
// call full:
// $.notify({
// icon: 'glyphicon glyphicon-warning-sign',
// title: 'Bootstrap notify',
// message: 'Turning standard Bootstrap alerts into "notify" like notifications',
// url: 'https://github.com/mouse0270/bootstrap-notify',
// target: '_blank'
// },{
// element: 'body',
// position: null,
// type: "info",
// allow_dismiss: true,
// newest_on_top: false,
// showProgressbar: false,
// placement: {
// from: "top",
// align: "right"
// },
// offset: 20,
// spacing: 10,
// z_index: 1031,
// delay: 5000,
// timer: 1000,
// url_target: '_blank',
// mouse_over: null,
// animate: {
// enter: 'animated fadeInDown',
// exit: 'animated fadeOutUp'
// },
// onShow: null,
// onShown: null,
// onClose: null,
// onClosed: null,
// icon_type: 'class',
// template: '<div data-notify="container" class="col-xs-11 col-sm-3 alert alert-{0}" role="alert">' +
// '<button type="button" aria-hidden="true" class="close" data-notify="dismiss">×</button>' +
// '<span data-notify="icon"></span> ' +
// '<span data-notify="title">{1}</span> ' +
// '<span data-notify="message">{2}</span>' +
// '<div class="progress" data-notify="progressbar">' +
// '<div class="progress-bar progress-bar-{0}" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%;"></div>' +
// '</div>' +
// '<a href="{3}" target="{4}" data-notify="url"></a>' +
// '</div>'
// });
//
// ref:
// https://github.com/mouse0270/bootstrap-notify/releases
// http://bootstrap-notify.remabledesigns.com/
// 3.1.3
public class BootstrapNotify {
    public static final JavaScriptResourceReference JS = new JavaScriptResourceReference(BootstrapNotify.class, "bootstrap-notify.js")
            .addJavaScriptResourceReferenceDependency(Bootstrap4.JS)
            .addCssResourceReferenceDependency(org.tools.hqlbuilder.webservice.css.WicketCSSRoot.ANIMATE);
}
