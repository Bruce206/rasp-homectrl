homectrlapp.config(function($stateProvider, $urlRouterProvider) {

    $stateProvider
        .state('livingroom', {
           url: '/livingroom',
           controller: "LivingroomController",
           templateUrl: "../templates/livingroom.html",
           resolve: {
                stripe1 : function() {
                    return $.get('/led/1');
                }
           }
        })

        .state('aquarium', {
           url: '/aquarium',
           controller: "AquariumController",
           templateUrl: "../templates/aquarium.html",
           resolve: {
                stripe2 : function() {
                    return $.get('/led/2');
                },
                temp : function() {
                    return $.get('/api/temp/28-8000001e8c58');
                }
           }
        })

});