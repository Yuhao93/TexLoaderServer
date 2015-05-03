haodev.tex.convertEquations();

function get(selector) {
  return document.querySelectorAll(selector);
}

function each(elements, func) {
  for (var i = 0; i < elements.length; i++) {
    func(elements[i]);
  }
}

function addClass(element, className) {
  var classes = element.className.split(' ');
  for (var i = 0; i < classes.length; i++) {
    if (classes[i] == className) {
      return;
    }
  }
  classes.push(className);
  element.className = classes.join(' ');
}

function removeClass(element, className) {
  var classes = element.className.split(' ');
  for (var i = 0; i < classes.length; i++) {
    if (classes[i] == className) {
      classes.splice(i, 1);
      element.className = classes.join(' ');
      return;
    }
  }
}

function setPage(pageId) {
  if (get('.page#' + pageId).length > 0) {
    each(get('.page.shown'), function(element) {
      removeClass(element, 'shown');  
    });
    each(get('ul.nav li.active'), function(element) {
      removeClass(element, 'active');  
    });
    addClass(get('.page#' + pageId)[0], 'shown');
    addClass(get('ul.nav li a[href="#' + pageId + '"]')[0].parentNode,
        'active');
  }
}

var hash = window.location.hash;
if (hash && hash.substring(1)) {
  var pageId = hash.substring(1);
  setPage(pageId);
}

each(get('ul.nav li a'), function(element) {
  element.onclick = function() {
    var pageId = this.href.split('#')[1];
    setPage(pageId);
  };  
});
