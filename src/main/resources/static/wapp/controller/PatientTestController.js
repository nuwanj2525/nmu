app.controller('PatientTestController', function($scope, $rootScope, $http, $location, $window, AuthenticationService, Pop) {
    $rootScope.pageTitle = "Patient Test Page";
    var loggedUser = '-';

    if ($rootScope.globals && $rootScope.globals.currentUser) {
        loggedUser = $rootScope.globals.currentUser.username;
    }

    $scope.patientMediTestList = [];
    $scope.uicompo = {};
    $scope.uicompo.billingNumber = "";
    $scope.uicompo.patientId = "";
    $scope.uicompo.showPatient = false;

    $scope.loadInitList = function() {
        $scope.uicompo.showPatient = false;
        var res = $http.get("patientmedicaltest/getList")
            .then(function(resp) {
                $scope.patientMediTestList = resp.data;
            }, function(response) {

            });
    }

    $scope.loadByBillingNum = function() {
    var res = $http.get("patientmedicaltest/findAllActiveByBillingNumber?billingNumber=" + $scope.uicompo.billingNumber)
        .then(function(response) {
            if (response.data.success) {
                var dList = response.data.response;
                $scope.uicompo.showPatient = true;
                $scope.patient = dList[0];
                $scope.patientMediTestList = dList[1];
            } else {
                $scope.uicompo.showPatient = false;
                $scope.patientMediTestList = [];
            }
        }, function(response) {

        });
    }

    $scope.loadByPatientId = function() {
        var res = $http.get("patientmedicaltest/findAllByBillingNumber?billingNumber=" + $scope.uicompo.billingNumber)
            .then(function(response) {
                if (response.data.success) {
                    $scope.uicompo.showPatient = true;
                    $scope.patientMediTestList = response.data.response;
                } else {
                    $scope.patientMediTestList = [];
                    $scope.uicompo.showPatient = false;
                }
            }, function(response) {

            });
    }

    $scope.loadInitList();
});

