app.controller('PatientController', function ($scope, $rootScope, $http, $location, $window, Pop) {
    $rootScope.pageTitle = "Patient Setup";

    $scope.patientList = [];
    $scope.patient = {};
    $scope.heading = 'Edit Patient Details';
    $scope.itemDisabled = false;
    $scope.actionType = '';
    $scope.genderLst = ['MALE', 'FEMALE'];
    $scope.uicompo = {};
    $scope.uicompo.patientId = '';
    $scope.ptestList = [];
    $scope.btnDisabledRecSearch = true;

    var loggedUser = '-';
    if ($rootScope.globals && $rootScope.globals.currentUser) {
        loggedUser = $rootScope.globals.currentUser.username;
    }

    $scope.showRecordSearch = function () {
        $scope.btnDisabledRecSearch = true;
        $("#modal-p-search").modal("show");
    }

    $scope.recordSearch = function () {
        if ($scope.uicompo.patientId.length > 4) {
        $http.get("patient/findByPatientIdContainingPages?patientId=" + $scope.uicompo.patientId)
        .then(function(response) {
            $scope.patientList = response.data.response;
            $("#modal-p-search").modal("hide");
        });
        }else{
            Pop.timeMsg('error',' PATIENT # SEARCH SHOULD HAVE MINIMUM 5 NUMBERS','PATIENT SEARCH', 2000);
        }
    }

    $scope.changeSearchNumber = function () {
        if ($scope.uicompo.patientId.length > 5) {
            $http.get("patient/findByPatientListById?patientId=" + $scope.uicompo.patientId)
            .then(function(response) {
                $scope.ptestList = response.data.response;
                $scope.btnDisabledRecSearch = false;
            });
        }
    }

    $scope.keypressId = function(e) {
        if (e.keyCode == 13) {
            $scope.mediTestList = [];
            $scope.patientMediTestList = [];
            $scope.findByPatientId($scope.patient.patientId);
        }
    }

    $scope.showUI = function (itm, opType) {
        $scope.actionType = opType;
        if ('add' === $scope.actionType) {
            $scope.heading = 'Add Patient Details';
            $scope.itemDisabled = false;
            $scope.patient = {};
            $scope.patient.status = 'ACTIVE';
        } else if ('edit' === $scope.actionType) {
            $scope.heading = 'Edit Patient Details :' + itm.id;
            $scope.itemDisabled = false;
            $scope.patient = itm;
            $scope.patient.dateOfBirth = new Date(itm.dateOfBirth);
        } else if ('delete' === $scope.actionType) {
            $scope.heading = 'Delete Patient Details :' + itm.id;
            $scope.itemDisabled = true;
            $scope.patient = itm;
        }
        $("#modal-inv").modal("show");
    };

    $scope.saveModal = function () {
        $scope.patient.lastModified = new Date();
        $scope.patient.actionBy = loggedUser;

        if ('add' === $scope.actionType) {
            $scope.patient.dateCreated = new Date();
            $http.post('/patient/save', $scope.patient).then(function (response) {
             }, function (response) {
            });
        } else if ('edit' === $scope.actionType) {
            $scope.patient.status = 'ACTIVE';
            $http.post('/patient/save', $scope.patient).then(function (response) {
             }, function (response) {
            });
        } else if ('delete' === $scope.actionType) {
            $http.delete('/patient/delete?id='+$scope.patient.id).then(function (response) {
             }, function (response) {
            });
        }
        loadList();
    };

    var loadList = function () {
        $http.get("patient/getList").then(function (response) {
            $scope.patientList = response.data.content;
        });
    };
    loadList();
});
