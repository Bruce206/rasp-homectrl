homectrlapp.config(function($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.when('', '/control');

    $stateProvider
        .state('led', {
           url: '/control',
           controller: "LedController",
           template: "templates/led.html"
        })

});