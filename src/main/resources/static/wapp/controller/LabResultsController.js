app.controller('LabResultsController', function($scope, $rootScope, $http, $location, $window, AuthenticationService, Pop) {
    $rootScope.pageTitle = "Lab Results";
    var loggedUser = '-';
    if ($rootScope.globals && $rootScope.globals.currentUser) {
        loggedUser = $rootScope.globals.currentUser.username;
    }

    $scope.patientMediTestList = [];
    $scope.patient = {};
    $scope.uicompo = {};
    $scope.uicompo.billingNumber = "";
    $scope.uicompo.showContent = false;

    $scope.loadByBillingNum = function() {
        $scope.uicompo.showContent = false;
        var res = $http.get("patientmedicaltest/findAllByBillingNumber?billingNumber=" + $scope.uicompo.billingNumber)
            .then(function(response) {
                if (response.data.success) {
                    var dList = response.data.response;
                    $scope.patient = dList[0];
                    $scope.patientMediTestList = dList[1];
                    $scope.uicompo.showContent = true;
                } else {
                    $scope.patientMediTestList = [];
                    $scope.uicompo.showContent = false;
                }
            }, function(response) {
                $scope.uicompo.showContent = false;
            });
    }

    $scope.upDateResult = function(itm) {
        if(!itm.results){
            Pop.timeMsg('error', 'UPDATE RESULTS', 'Results cannot be empty !', 2000);
            return;
        }

        itm.status = "CLOSED";
        itm.lastDateModified = new Date();
        itm.actionBy = loggedUser;

        var res = $http.post("patientmedicaltest/save",itm)
            .then(function(response) {
                if (response.data.success) {
                    Pop.timeMsg('success', 'UPDATE RESULTS', 'Results has been updated', 2000);
                }
                $scope.loadByBillingNum();
                if($scope.patientMediTestList.length < 1){
                    Pop.msgWithButton('UPDATE RESULTS', 'Please print the report', 'success');
                }
            }, function(response) {

            }).catch(function() {

            });


    }
});
