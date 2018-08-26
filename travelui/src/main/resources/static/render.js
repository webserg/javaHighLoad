var templates = {};

function render(template, model, url) {
    var compiledTemplate;
    if (templates[url] === undefined) {
        compiledTemplate = Handlebars.compile(template);
        templates[url] = compiledTemplate;
    }
    else {
        compiledTemplate = templates[url];
    }
    return compiledTemplate(toJsonObject(model))
}

function toJsonObject(model) {
    var o = {};
    for (var k in model) {
        if (model[k] instanceof Java.type("java.lang.Iterable")) {
            o[k] = Java.from(model[k]);
        } else {
            o[k] = model[k];
        }
    }
    return o;
}