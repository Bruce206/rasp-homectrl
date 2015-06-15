homectrlapp.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.when('', '/control');

    $stateProvider
        .state('led', {
           url: '/control',
           controller: "LedController",
           templateUrl: "../templates/led.html",
           resolve: {
                stripe1 : function() {
                    return $.get('/led/1');
                },
                stripe2 : function() {
                    return $.get('/led/2');
                }
           }
        })

});