app.controller('MedicalTestController', function ($scope, $rootScope, $http, $location, $window, AuthenticationService, Pop) {
    $rootScope.pageTitle = "Medical Test Setup";
    $scope.uicompo = {};

    $scope.medicalTestList = [];
    $scope.mtest = {};
    $scope.heading = 'Edit Medical Test Details';
    $scope.itemDisabled = false;
    $scope.actionType = '';
    $scope.uicompo.modalpagetitle = 'MEDICAL TEST';
    $scope.uicompo.modalScreen = 'Medical Test';
    $scope.uicompo.search = '';
    $scope.uicompo.ref = {};
    $scope.uicompo.refLst = [];
    $scope.uicompo.saveObj = [];
    var loggedUser = '-';
    if ($rootScope.globals && $rootScope.globals.currentUser) {
        loggedUser = $rootScope.globals.currentUser.username;
    }


    $scope.showUI = function (itm, opType) {
        $scope.actionType = opType;
        if ('add' === $scope.actionType) {
            $scope.heading = 'Add Medical Test Details';
            $scope.itemDisabled = false;
            $scope.mtest = {};
        } else if ('edit' === $scope.actionType) {
            $scope.heading = 'Edit Medical Test Details';
            $scope.itemDisabled = false;
            $scope.mtest = itm;
            loadRefList(itm.id);
        } else if ('delete' === $scope.actionType) {
            $scope.heading = 'Delete Medical Test Details';
            $scope.itemDisabled = true;
            $scope.mtest = itm;
        }
        $scope.mtest.optTests ="FSH,TSH";
        $("#modal-inv").modal("show");
    };

    $scope.addScanType = function() {
        $("#modal-scan-types").modal("show");
    }

    $scope.saveModal = function () {
        $scope.mtest.lastModified = new Date();
        $scope.mtest.actionBy = loggedUser;

        if ('add' === $scope.actionType) {
            $scope.mtest.dateCreated = new Date();
            $scope.mtest.status = 'ACTIVE';
        } else if ('edit' === $scope.actionType) {
            $scope.mtest.status = 'ACTIVE';

        } else if ('delete' === $scope.actionType) {
            $scope.mtest.status = 'DELETED';
        }

        $http.post('/medicaltest/save', $scope.mtest).then(function (response) {
            saveRefList();
            loadList();
            $("#modal-inv").modal("hide");
            Pop.msgWithButton($scope.uicompo.modalScreen + ' Record Saved Successfully', $scope.uicompo.modalpagetitle , 'success');
        }, function (response) {
            Pop.msgWithButton('Error Saving ' + $scope.uicompo.modalScreen, $scope.uicompo.modalpagetitle , 'error');
        });
    };

    var saveRefList = function(){
        $http.post('/reference/save', $scope.uicompo.refLst).then(function (response) {
            $("#modal-inv").modal("hide");
            Pop.msgWithButton($scope.uicompo.modalScreen + ' Record Saved Successfully', $scope.uicompo.modalpagetitle , 'success');
        }, function (response) {
            Pop.msgWithButton('Error Saving ' + $scope.uicompo.modalScreen, $scope.uicompo.modalpagetitle , 'error');
        });
    }


    var searchByName = function(strName){
        $http.get("medicaltest/getListByName?name=" + strName)
            .then(function(jsn) {
                $scope.medicalTestList = jsn.data.response;
            }, function(response) {
            }).catch(function() {
            //Pop.timeMsg('error', 'ADDED PATIENT', ' PATIENT SAVING NOT SUCCESS ' + e, 3000);
        });
    }

    $scope.keypressId = function(e) {
        if (e.keyCode == 13) {

            if ($scope.uicompo.search.length > 0) {
                searchByName($scope.uicompo.search);
            }

            if($scope.medicalTestList.length == 0){
                Pop.timeMsg('error', 'SEARCH MEDICAL TEST', ' TEST NOT FOUND ' + e, 2000);
            }
        }

        if ($scope.uicompo.search.length > 2) {
            searchByName($scope.uicompo.search);
            if($scope.medicalTestList.length == 0){
                Pop.timeMsg('error', 'SEARCH MEDICAL TEST', ' TEST NOT FOUND ' + e, 2000);
            }
        }
    }

    var loadList = function () {
        $http.get("medicaltest/getList").then(function (jsn) {
            $scope.medicalTestList = jsn.data.response;
        });
    };

    $scope.addReference = function() {
        $scope.uicompo.ref.medicalTest = $scope.mtest.id;
        var refItem = $scope.uicompo.ref;
        if(!refItem.gender){Pop.msgWithButton('Reference field SEX cannot be empty.', $scope.uicompo.modalpagetitle , 'error'); return;}
        if(!refItem.ageMin){Pop.msgWithButton('Reference Minimum Age Range cannot be empty.', $scope.uicompo.modalpagetitle , 'error'); return;}
        if(!refItem.ageMax){Pop.msgWithButton('Reference field  Maximum Age Range  cannot be empty.', $scope.uicompo.modalpagetitle , 'error'); return;}
        if(!refItem.unit){Pop.msgWithButton('Reference field UNIT cannot be empty.', $scope.uicompo.modalpagetitle , 'error'); return;}
        if(!refItem.reference){Pop.msgWithButton('Reference field Reference cannot be empty.', $scope.uicompo.modalpagetitle , 'error'); return;}
        refItem.tmpid = $scope.uicompo.refLst.length + 1;
        $scope.uicompo.refLst.push(refItem);
        $scope.uicompo.ref = {};
    }

    $scope.deleteRefItem = function (itm) {
        for (var i = 0; i < $scope.uicompo.refLst.length; i++) {
            if($scope.uicompo.refLst[i].tmpid === itm.tmpid){
                var itemid =  $scope.uicompo.refLst[i].id;
                $scope.uicompo.refLst.splice(i, 1);
                if(itemid){
                    $http.delete('/reference/delete?id='+itemid)
                    .then(function (response) {
                    });
                }
            }
        }
    }

    var loadRefList = function (testid) {
        $scope.uicompo.refLst =[];
        $http.get("reference/getRefList?testId=" + testid).then(function (jsn) {
            var reftmpLst =  jsn.data.response;
            for (var i = 0; i < reftmpLst.length; i++) {
                var itm = reftmpLst[i];
                itm.tmpid = i + 1;
                $scope.uicompo.refLst.push(itm);
            }
        });
    };

    $scope.loadBulkData = function () {
        $http.get("medicaltest/loadBulk").then(function (response) {
            loadList();
        });
    };
    loadList();
});
