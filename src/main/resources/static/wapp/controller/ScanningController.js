app.controller('ScanningController', function($scope, $rootScope, $http, $location, $window, AuthenticationService, Pop) {
    $rootScope.pageTitle = "Patient Medical Scanning";
    var loggedUser = '-';
    if ($rootScope.globals && $rootScope.globals.currentUser) {
        loggedUser = $rootScope.globals.currentUser.username;
    }

    $scope.heading = '';
    $scope.uicompo = {};
    $scope.patient = {};
    $scope.patientTest = {};
    $scope.patientScan = {};
    $scope.scanList = [];
    $scope.scanHistortyList = [];
    $scope.uicompo.itemDisabled = false;
    $scope.uicompo.history = {};
    $scope.uicompo.historyLst = [];
    $scope.currentAge  = 0;
    $scope.uicompo.showItem  = false;
    $scope.uicompo.showSave  = false;


    // $scope.patientScan.dateCreated = new Date();

    var resetComponent = function(){
        $scope.patient = {};
        $scope.patientTest = {};
        $scope.patientScan = {};
        $scope.scanList = [];
        $scope.scanHistortyList = [];
        $scope.uicompo.showItem  = false;
        $scope.uicompo.showSave  = false;
    }

    $scope.searchBilling = function () {
        resetComponent();
        var billingNumber = $scope.uicompo.billingNumber;
        if(!billingNumber){
            Pop.timeMsg('success', 'SEARCH SCAN', ' SCAN NUMMBER NOT FOUND ', 2000);
            return;
        }

        $http.get("scan/getPatientByBilling?billingNumber=" + billingNumber)
            .then(function(resp) {
                if (resp.data.success) {
                    var obj = resp.data.response;
                  try{
                        $scope.patientTest = obj.patientmedicaltest;
                    }catch (e) {
                        console.log('error retreving patientTest');
                      $scope.patientTest = {};
                    }
                    try{
                        $scope.patient = obj.patient;
                        if($scope.patient.dateOfBirth){
                            $scope.currentAge = (new Date().getFullYear() - new Date($scope.patient.dateOfBirth).getFullYear());
                        }
                    }catch (e) {
                        console.log('error retreving patient');
                        $scope.patient = {};
                    }
                    try{
                        $scope.scanHistortyList = obj.bypatientidlist;
                    }catch (e) {
                        console.log('error retreving scanHistortyList');
                        $scope.patientScan = [];
                    }
                    try{
                        $scope.patientScan = obj.patientscan;
                    }catch (e) {
                        console.log('error retreving patientScan');
                        $scope.patientScan = {};
                    }

                    $scope.patientScan.scanNumber = $scope.patientScan.scanNumber;
                    $scope.patientScan.billingNumber = billingNumber;
                    $scope.patientScan.patientId = $scope.patient.patientId;
                    $scope.uicompo.showItem  = true;
                    $scope.uicompo.showSave  = false;

                } else {
                    Pop.timeMsg('error', 'SEARCH SCAN', ' SCAN NUMMBER NOT FOUND ', 2000);
                }
            });

    }

    $scope.clearScreen = function() {
        $scope.uicompo = {};
        $scope.patientTest = {};
        $scope.patient = {};
        $scope.scanHistortyList = [];
        $scope.patientScan = {};
        $scope.uicompo.showItem  = false;
        $scope.uicompo.showSave  = false;
    }

    $scope.saveModal = function() {
        $scope.patientScan.actionBy= loggedUser;
        $scope.patientScan.lastDateModified = new Date();
        $scope.patientScan.dateCreated = new Date();
        $scope.patientScan.status = 'ACTIVE';
        $http.post('/scan/save', $scope.patientScan)
            .then(function(resp) {
                Pop.timeMsg('success', 'SAVE SCAN', ' SCAN HAS BEEN SAVED SUCCESSFULLY ', 2000);
                $scope.uicompo.showSave  = false;
            }, function(resp) {
                Pop.timeMsg('error', 'SAVE SCAN', ' SCAN SAVING NOT SUCCESS', 2000);
            }).catch(function(e) {
            Pop.timeMsg('error', 'SAVE SCAN', ' SCAN SAVING NOT SUCCESS ' + e, 5000);
        });
    };

    $scope.showImage = function() {
        Pop.timeMsg('success', 'SAVE SCAN', 'showImage', 2000);
    }

    $scope.showHistory = function(itm) {
        $scope.heading = 'History'
        var selectedBillingNumber = itm.substr(0,itm.indexOf("-"));
        $http.get("scan/getHistoryByPatient?billingNumber=" + selectedBillingNumber)
            .then(function(resp) {
                if (resp.data.success) {
                    $scope.uicompo.history = resp.data.response.prop;
                    $scope.uicompo.historyLst = resp.data.response.propList;
                     $("#modal-scan-history").modal("show");
                } else {
                    Pop.timeMsg('error', 'SEARCH SCAN', ' SCAN NUMBER NOT FOUND ', 2000);
                }
            });
        //$scope.patientTest = ;

    }

    var loadList = function () {
        $http.get("scan/getList").then(function (response) {
            $scope.scanHistortyList = response.data;
        });
    };

    var loadList = function () {
        $http.get("scan/getList").then(function (response) {
            $scope.scanList = response.data;
        });
    };

    $scope.enableSave = function(itm) {
        $scope.uicompo.showSave  = true;
    }

    loadList();
});