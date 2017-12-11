package org.tools.hqlbuilder.webservice.riot;

import org.tools.hqlbuilder.webservice.wicket.JavaScriptResourceReference;

// todo.tag:
//
// <todo>
//
// <h3>{ opts.title }</h3>
//
// <ul>
// <li each={ items.filter(whatShow) }>
// <label class={ completed: done }>
// <input type="checkbox" checked={ done } onclick={ parent.toggle }> { title }
// </label>
// </li>
// </ul>
//
// <form onsubmit={ add }>
// <input ref="input" onkeyup={ edit }>
// <button disabled={ !text }>Add #{ items.filter(whatShow).length + 1 }</button>
//
// <button type="button" disabled={ items.filter(onlyDone).length == 0 } onclick={ removeAllDone }>
// X{ items.filter(onlyDone).length } </button>
// </form>
//
// <!-- this script tag is optional -->
// <script>
// this.items = opts.items
//
// edit(e) {
// this.text = e.target.value
// }
//
// add(e) {
// if (this.text) {
// this.items.push({ title: this.text })
// this.text = this.refs.input.value = ''
// }
// e.preventDefault()
// }
//
// removeAllDone(e) {
// this.items = this.items.filter(function(item) {
// return !item.done
// })
// }
//
// // an two example how to filter items on the list
// whatShow(item) {
// return !item.hidden
// }
//
// onlyDone(item) {
// return item.done
// }
//
// toggle(e) {
// var item = e.item
// item.done = !item.done
// return true
// }
// </script>
//
// </todo>
//
//
// html: <todo></todo>
//
// response.render(JavaScriptHeaderItem.forReference(Riot.JS_COMPILER));
// response.render(new RiotHeaderItem(getClass(), "todo.tag", true));
//// <script>
//// riot.compile(function() {
//// // here tags are compiled and riot.mount works synchronously
//// var tags = riot.mount('*')
//// })
//// </script>
//// response.render(JavaScriptHeaderItem.forScript(
//// ";riot.mount('todo', { title: 'I want to behave!', items: [ { title: 'Avoid excessive caffeine', done: true }, { title: 'Hidden item', hidden:
// true }, { title: 'Be less
//// provocative' }, { title: 'Be nice to people' } ] });", "todo.tag"));
// response.render(JavaScriptHeaderItem.forScript(";riot.mount('todo',{items:[]});", "todo.tag"));
//
// http://riotjs.com/download/
// http://riotjs.com/
// https://github.com/riot/riot
// 3.7.4
public class Riot {
    public static JavaScriptResourceReference JS = new JavaScriptResourceReference(Riot.class, "riot.js");

    public static JavaScriptResourceReference JS_COMPILER = new JavaScriptResourceReference(Riot.class, "riot+compiler.js");
}
