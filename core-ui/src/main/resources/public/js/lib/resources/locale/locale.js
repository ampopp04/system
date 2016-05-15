function loadTranslations(){

    var lang = localStorage ? (localStorage.getItem('user-lang') || 'en') : 'en',
        file = 'js/lib/resources/locale/' + lang + '.js';
    document.write('<script type="text/javascript" src="' + file + '"></script>');
}

loadTranslations();