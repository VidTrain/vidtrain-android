/*!CK:4253995636!*//*1456788339,*/

if (self.CavalryLogger) { CavalryLogger.start_js(["hivrN"]); }

__d('HighContrastMode',['AccessibilityLogger','CSS','CurrentUser','DOM','Style','URI','emptyFunction'],function a(b,c,d,e,f,g){if(c.__markCompiled)c.__markCompiled();var h={init:function(i){var j=new (c('URI'))(window.location.href);if(j.getPath().indexOf('/intern/')===0)return;if(window.top!==window.self)return;var k=c('DOM').create('div');c('DOM').appendContent(document.body,k);k.style.cssText='border: 1px solid !important;'+'border-color: red green !important;'+'position: fixed;'+'height: 5px;'+'top: -999px;'+'background-image: url('+i.spacerImage+') !important;';var l=c('Style').get(k,'background-image'),m=c('Style').get(k,'border-top-color'),n=c('Style').get(k,'border-right-color'),o=m==n&&l&&(l=='none'||l=='url(invalid-url:)');if(o){c('CSS').conditionClass(document.documentElement,'highContrast',o);if(c('CurrentUser').getID())c('AccessibilityLogger').logHCM();}c('DOM').remove(k);h.init=c('emptyFunction');}};f.exports=h;},null);