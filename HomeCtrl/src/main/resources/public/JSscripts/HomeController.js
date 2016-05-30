var homectrlapp = angular.module('homectrlapp', ["ngResource", "ui.router", "angularSpectrumColorpicker", 'angularScreenfull', 'ngAnimate']);
homectrlapp.factory('Images', function($resource) {
    return $resource('../api/images', {}, {});
});
homectrlapp.controller('HomeCtrl', function($scope, $rootScope, $timeout, $state, Images) {
    $scope.slideshow = false;
    var t;
    window.onload = resetTimer;
    window.onmousedown = resetTimer; // catches touchscreen presses
    window.onclick = resetTimer; // catches touchpad clicks
    window.onscroll = resetTimer; // catches scrolling with arrow keys
    window.onkeypress = resetTimer;

    function idleDetected() {
        $scope.slideshow = true;
    }

    function resetTimer() {
        $scope.slideshow = false;
        if ($scope.timer) {
            $timeout.cancel($scope.timer);
        }
        $scope.timer = $timeout(idleDetected, 120000); // time is in milliseconds
    }

    $scope.$watch("slideshow", function() {
        console.log("starting")
        if ($scope.slideshow) {
            var INTERVAL = 7000;

            function nextSlide() {
                if ($scope.slideshow) {
                    $scope.image = Images.get();
                    $timeout(nextSlide, INTERVAL);
                }
            }

            function loadSlides() {
                $timeout(nextSlide, INTERVAL);
            }
            loadSlides();
        }
    }, true);

    $scope.goImages = function() {
        $timeout.cancel($scope.timer);
        idleDetected();
    }
});
homectrlapp.directive('bgImage', function($window, $timeout) {
    return function(scope, element, attrs) {
        var resizeBG = function() {
            var bgwidth = element.width();
            var bgheight = element.height();
            var winwidth = $window.innerWidth;
            var winheight = $window.innerHeight;
            var widthratio = winwidth / bgwidth;
            var heightratio = winheight / bgheight;
            var widthdiff = heightratio * bgwidth;
            var heightdiff = widthratio * bgheight;
            if (heightdiff > winheight) {
                element.css({
                    width: winwidth + 'px',
                    height: heightdiff + 'px'
                });
            } else {
                element.css({
                    width: widthdiff + 'px',
                    height: winheight + 'px'
                });
            }
        };
        var windowElement = angular.element($window);
        windowElement.resize(resizeBG);
        element.bind('load', function() {
            resizeBG();
        });
    }
});

homectrlapp.controller('LivingroomController', function($scope, $rootScope, $timeout, stripe1, $http) {
    $scope.color1 = stripe1;
    $scope.options = {
        showInput: true,
        // flat: true, 
        preferredFormat: "rgb",
        palette: [
            ['black', 'white', 'blue', 'rgb(0, 254, 0)', 'red', 'rgb(95, 0, 254)']
        ],
        showPalette: true,
        clickoutFiresChange: false,
        maxSelectionSize: 0
    }
    $scope.options1 = angular.extend({}, $scope.options, {
        'color': $scope.color1
    });

    $scope.togglePlug = function(groupAddress, plugAddress, targetState) {
        console.log("switch")
        
        $http({
            method: 'GET',
            url: '/rc/switch',
            params: {
                   'groupAddress': groupAddress, 'plugAddress': plugAddress, 'targetState':targetState
                },
            }).then(function successCallback(response) {
                // this callback will be called asynchronously
                // when the response is available
            }, function errorCallback(response) {
                // called asynchronously if an error occurs
                // or server returns response with an error status.
            });
    }

    $scope.changed = function(stripe) {
        if (!$scope.sending) {
            $scope.sending = true;
            if (stripe == 1) {
                $.post('/led/1', {
                    rgb: $scope.color1
                }, function(data) {
                    $scope.color1 = data;
                });
            } else {
                $.post('/led/2', {
                    rgb: $scope.color2
                }, function(data) {
                    $scope.color2 = data;
                });
            }
            $timeout(function() {
                $scope.sending = false;
            }, 200);
        }
    }
});

homectrlapp.controller('AquariumController', function($scope, $rootScope, $timeout, stripe2, temp) {
    $scope.temp = JSON.parse(temp).value;
	$scope.color2 = stripe2;
    $scope.options = {
        showInput: true,
        // flat: true, 
        preferredFormat: "rgb",
        palette: [
            ['black', 'white', 'blue', 'rgb(0, 254, 0)', 'red', 'rgb(95, 0, 254)']
        ],
        showPalette: true,
        clickoutFiresChange: false,
        maxSelectionSize: 0
    }
    $scope.options2 = angular.extend({}, $scope.options, {
        'color': $scope.color2
    });
    $scope.changed = function(stripe) {
        if (!$scope.sending) {
            $scope.sending = true;
            if (stripe == 1) {
                $.post('/led/1', {
                    rgb: $scope.color1
                }, function(data) {
                    $scope.color1 = data;
                });
            } else {
                $.post('/led/2', {
                    rgb: $scope.color2
                }, function(data) {
                    $scope.color2 = data;
                });
            }
            $timeout(function() {
                $scope.sending = false;
            }, 200);
        }
    }
});