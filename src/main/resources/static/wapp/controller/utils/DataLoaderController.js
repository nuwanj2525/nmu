app.controller('DataLoaderController', function ($scope, $rootScope, $http, $location, $window, Pop) {
    $rootScope.pageTitle = "DataLoader";

    $scope.patientList = [];

    $scope.patientLoader = function () {
        $http.get("loader/loadPatient").
        then(function (response) {
        });
    };

    $scope.testLoader = function () {
        $http.get("loader/testLoader").
        then(function (response) {
        });
    };

    $scope.resultsLoader = function () {
        $http.get("loader/resultsLoader").
        then(function (response) {
        });
    };

    $scope.scanLoader = function () {
        $http.get("loader/scanLoader").
        then(function (response) {
        });
    };

});
