var homectrlapp = angular.module('homectrlapp', ["ngResource", "ui.router", "angularSpectrumColorpicker"]);

homectrlapp.controller('LedController', function($scope, $rootScope, $timeout, stripe1, stripe2){
	$scope.color1 = stripe1;
	$scope.color2 = stripe2;

	$scope.options = {
		showInput: true, 
		flat: true, 
		preferredFormat: "rgb",
		palette: [
        	['black', 'white', 'blue', 'rgb(0, 254, 0)', 'red', 'rgb(95, 0, 254)']
    	],
    	showPalette: true,
    	clickoutFiresChange: false,
    	maxSelectionSize: 0
	}

	
	$scope.options1 = angular.extend({}, $scope.options, {'color':stripe1});
	$scope.options2 = angular.extend({}, $scope.options, {'color':stripe2});

	$scope.changed = function(stripe) {
		if (!$scope.sending) {
			$scope.sending = true;
			if (stripe == 1) {
				$.post('/led/1', {rgb: $scope.color1}, function(data) {
					$scope.color1 = data;
				});
			} else {
				$.post('/led/2', {rgb: $scope.color2}, function(data) {
					$scope.color2 = data;
				});
			}
			$timeout(function() {$scope.sending = false;}, 200);
		}
	}
});