app.controller('BillingController', function($scope, $rootScope, $http, $location, $window, AuthenticationService, Pop) {
    $rootScope.pageTitle = "Patient Medical Test Billing";
    var loggedUser = '-';
    if ($rootScope.globals && $rootScope.globals.currentUser) {
        loggedUser = $rootScope.globals.currentUser.username;
    }

    $scope.btnAddShow = true;
    $scope.btnSearch = true;
    $scope.btnEditShow = false;
    $scope.btnDelShow = false;
    $scope.btnReset = true;
    $scope.isDisabledSave = true;

    $scope.ptestList = [];
    $scope.testTypeList = [];
    $scope.mediTestList = [];
    $scope.patientMediTestList = [];
    $scope.seenByList = [];
    $scope.districtByList = [];

    $scope.billingList = [];

    $scope.patient = {};
    $scope.patient.dateCreated = new Date();
    $scope.patient.currentAge = 0;
    $scope.showMediTest = false;
    $scope.uicompo = {};
    $scope.uicompo.selectedTest = {};
    $scope.uicompo.itemDisabled = true;
    $scope.uicompo.selectedTotal = 0.0;

    $scope.disabledAddTest = false;
    $scope.disabledSaveTestList = true;
    $scope.disabledTxtBillingNumber = false;
    $scope.disabledLoadByBillingNum = false;
    $scope.disabledPrintPage = true;
    $scope.disabledResetTestList = false;
    $scope.disabledTestSelection = false;
    $scope.rePrint = false;




    var onload = function() {
        $scope.testTypeList.push({
            key: "P-NMU Clinic",
            value: 'P'
        });
        $scope.testTypeList.push({
            key: "S-PVT",
            value: 'S'
        });
        $scope.testTypeList.push({
            key: "H-HOSPITAL",
            value: 'H'
        });
        $scope.testTypeList.push({
            key: "H-HOSPITAL",
            value: 'W'
        });

        $scope.seenByList.push({
            name: "Dr.D.K.K Nanayakkara"
        });

        $scope.seenByList.push({
            name: "Dr.L Watawana"
        });

        $scope.districtByList.push({
            name: "KANDY"
        });
        $scope.districtByList.push({
            name: "MATALE"
        });
        $scope.districtByList.push({
            name: "NUWARA-ELIYA"
        });
        //$scope.loadPatientList();
    };

    $scope.showUI = function(itm, opType) {
        $scope.actionType = opType;
        if ('delete' === $scope.actionType) {
            if($scope.billingList.length > 0){
                for (var i = 0; i < $scope.billingList.length; i++) {
                  if($scope.billingList[i].tmpid === itm.tmpid){
                      $scope.billingList.splice(i, 1);
                      onchangeSelectedList();
                  }
                }
            }
        }
    };

    $scope.saveModal = function() {
        $scope.patient.lastModified = new Date();
        $scope.patient.actionBy = loggedUser;
        $scope.patient.dateCreated = new Date();
        var patientId = $scope.patient.patientId;
        $scope.patient.status = 'ACTIVE';
        $http.post('/patient/save', $scope.patient)
            .then(function(resp) {
                Pop.timeMsg('success', 'ADDED PATIENT', ' PATIENT HAS BEEN SAVED SUCCESSFULLY ', 2000);
                $scope.clearCurrentPatient();
                $scope.findByPatientId(patientId);
            }, function(resp) {

            }).catch(function(e) {
            Pop.timeMsg('error', 'ADDED PATIENT', ' PATIENT SAVING NOT SUCCESS ' + e, 5000);
        });
    };

    $scope.onSelectTestType = function() {
        loadMedicalTestList($scope.uicompo.selectedTestType);
        if ($scope.patient) {
            loadPatientMediTestList($scope.patient.patientId, $scope.uicompo.selectedTestType);
            getNextNumber($scope.uicompo.selectedTestType);
        } else {
            $scope.mediTestList = [];
            $scope.patientMediTestList = [];
        }
    };


    $scope.saveUpdatePatient = function() {

    };

    $scope.addSelectedTest = function() {

        var selected = $scope.uicompo.selectedTest;

        var saveTest = {};

        if (!selected) {
            Pop.timeMsg('error', 'MEDICAL TEST ADDED ', ' PLEASE SELECT THE TEST ', 2000);
            return;
        }

        saveTest = JSON.parse(selected);

        if (!$scope.patient) {
            Pop.msgWithButton('ADD MEDICAL TEST', 'Patient not selected', 'error');
            return;
        }

        if (!saveTest.testType) {
            Pop.msgWithButton('ADD MEDICAL TEST', 'Test type not selected', 'error');
            return;
        }

        if (!saveTest.name) {
            Pop.msgWithButton('ADD MEDICAL TEST', 'Test name not selected', 'error');
            return;
        }

        if (!$scope.uicompo.billingNumber || $scope.uicompo.billingNumber.length < 5) {
            Pop.msgWithButton('ADD MEDICAL TEST', 'Invalid Bill #', 'error');
            return;
        }

        saveTest.patientId = $scope.patient.patientId;
        saveTest.lastDateModified = new Date();
        saveTest.dateCreated = new Date();
        saveTest.actionBy = loggedUser;
        saveTest.seenBy = $scope.patient.seenBy;
        saveTest.id = null;
        saveTest.billingNumber = $scope.uicompo.billingNumber;
        saveTest.status = "OPEN";

        if(validateAlreadyAdded(saveTest)){
            Pop.msgWithButton('ADD MEDICAL TEST', 'Test Already Added', 'error');
            return;
        }

        if($scope.billingList.length == 0){
           saveTest.tmpid = 1;
        }else{
          saveTest.tmpid = $scope.billingList.length + 1;
        }
        $scope.billingList.push(saveTest);

        onchangeSelectedList();

    };

    var validateAlreadyAdded = function(itm){
        for (var i = 0; i < $scope.billingList.length; i++) {
            if($scope.billingList[i].testNumber === itm.testNumber){
                return true;
            }
        }
        return false;
    }

    var onchangeSelectedList = function(){
        if($scope.billingList.length > 0){
            $scope.disabledSaveTestList = false;
            $scope.disabledTxtBillingNumber = true;
            $scope.disabledLoadByBillingNum = true;
            var total =0.0;
            for (var i = 0; i < $scope.billingList.length; i++) {
                total = total + $scope.billingList[i].price;
            }
            $scope.uicompo.selectedTotal = total;
            $scope.disabledTestSelection = true;
        }else{
            $scope.disabledSaveTestList = true;
            $scope.uicompo.selectedTotal = 0.0;
            $scope.disabledTestSelection = false;
        }
    }

    $scope.saveTestList = function() {
        if ($scope.billingList.length == 0) {
            $scope.disabledPrintPage = true;    Pop.msgWithButton('ADD MEDICAL TEST', 'NO TEST ITEM ADDED', 'error');
            return;
        }

        $http.post('/patientmedicaltest/saveList', $scope.billingList)
            .then(function(resp) {
                $scope.disabledPrintPage = false;
                $scope.disabledSaveTestList = true;
                $scope.disabledAddTest = true;
                $scope.disabledTxtBillingNumber = true;
                $scope.disabledLoadByBillingNum = true;
                $scope.disabledTestSelection = false;
                Pop.timeMsg('success', 'MEDICAL TEST ADDED ', ' TEST HAS BEEN ADDED TO PATIENT ', 2000);
            });
    };


    $scope.clearCurrentPatient = function() {
        console.log('clearCurrentPatient');
        $scope.medicalTest = {};
        $scope.patientMediTestList = [];
        $scope.mediTestList = [];
        $scope.showMediTest = false;
        $scope.uicompo.itemDisabled = true;
        $scope.isDisabledSave = true;
        $scope.patient = {};
        $scope.patient.patientId = null;
    };

    $scope.loadPatientList = function() {
        if ($scope.patient.patientId.length > 5) {
            $http.get("patient/findByPatientListById?patientId=" + $scope.patient.patientId)
                .then(function(response) {
                    $scope.ptestList = response.data.response;
                });
        }
    };

    $scope.loadPatientListByNIC = function() {
        if ($scope.patient.nicNumber.length > 5) {
            $http.get("patient/findByPatientListById?nicNumber=" + $scope.patient.nicNumber)
                .then(function(response) {
                    $scope.ptestList = response.data.response;
                });
        }
    };

    $scope.findByPatientId = function(patientId) {
        $http.get("patient/findByPatientId?patientId=" + patientId)
            .then(function(resp) {
                $scope.patient = resp.data.response;
                $scope.patient.dateOfBirth = new Date(resp.data.response.dateOfBirth);
                $scope.showMediTest = true;
                $scope.uicompo.itemDisabled = true;
                $scope.isDisabledSave = true;
                $scope.patient.currentAge = (new Date().getFullYear() - $scope.patient.dateOfBirth.getFullYear());
                $("#modal-billing-search").modal("hide");
            }, function(response) {
                //Pop.msgWithButton('UPDATE','Fail User '+ item.fistName + ' Saving', 'error');
            }).catch(function() {
            //Pop.timeMsg('error', 'ADDED PATIENT', ' PATIENT SAVING NOT SUCCESS ' + e, 3000);
        });
    };

    $scope.findByNicNumber = function() {
        var res = $http.get("patient/findByNicNumber?id=" + $scope.patient.nicNumber)
            .then(function(resp) {
                $scope.patient = resp.data.response;
                $scope.patient.dateOfBirth = new Date(resp.data.response.dateOfBirth);
                $scope.showMediTest = true;
                $scope.uicompo.itemDisabled = true;
                $scope.isDisabledSave = true;
                $scope.patient.currentAge = (new Date().getFullYear() - $scope.patient.dateOfBirth.getFullYear());
                resetTestList();
                $("#modal-billing-search").modal("hide");
            }, function(response) {
                //Pop.msgWithButton('UPDATE','Fail User '+ item.fistName + ' Saving', 'error');
            });
    };


    var loadMedicalTestList = function(selectedTestType) {
        $http.get("medicaltest/findAllByType?testType=" + selectedTestType)
            .then(function(resp) {
                if (resp.data.success) {
                    $scope.mediTestList = resp.data.response;

                } else {
                    $scope.mediTestList = [];
                }
            });
    };

    var loadPatientMediTestList = function(patientId, selectedTestType) {
        var res = $http.get("patientmedicaltest/findAllByPatientIdAndType?patientid=" + $scope.patient.patientId + "&type=" + selectedTestType).then(function(response) {
            if (response.data.success) {
                $scope.patientMediTestList = response.data.response;
            } else {
                $scope.patientMediTestList = [];
            }
        }, function(response) {

        });
    };

    $scope.keypressId = function(e) {
        if (e.keyCode == 13) {
            $scope.mediTestList = [];
            $scope.patientMediTestList = [];
            $scope.findByPatientId($scope.patient.patientId);
        }
    }

    $scope.copyBillNumber = function() {
        $scope.uicompo.billingNumber = $scope.uicompo.showBillingNumber;
    }

    $scope.loadPatientId = function() {
        if ($scope.patient.patientId) {
            $scope.mediTestList = [];
            $scope.patientMediTestList = [];
            $scope.findByPatientId($scope.patient.patientId);
        }
    }

    $scope.keypressNic = function(e) {
        if (e.keyCode == 13) {
            $scope.mediTestList = [];
            $scope.patientMediTestList = [];
            $scope.findByNicNumber();
        }
    }

    $scope.showSearch = function search() {
        $scope.patient = {};
        resetTestList();
        $("#modal-billing-search").modal("show");
    }

    $scope.showAddTest = function search() {
        $scope.uicompo.selectedTest = '';
        $scope.rePrint = false;
        $("#modal-add-test").modal("show");
    }

    $scope.serchOldBillNo = function search() {
        $scope.rePrint = true;
        $("#modal-old-billing-search").modal("show");
    }


    $scope.loadByBillingNum = function search() {
        var res = $http.get("patientmedicaltest/findAllByPatientIdAndBillingNumber?patientid=" + $scope.patient.patientId + "&billingNumber=" + $scope.uicompo.billingNumber)
            .then(function(response) {
                if (response.data.success) {
                    $scope.billingList = response.data.response;
                    if($scope.billingList.length > 0){
                        $scope.disabledPrintPage = false;
                        $scope.disabledAddTest = true;
                        $scope.disabledSaveTestList = true;
                    }else{
                        Pop.timeMsg('danger', 'Search By Billing # ', ' Records not found ', 2000);
                        resetTestListButtons();
                    }
                } else {
                    $scope.billingList = [];
                    resetTestListButtons();
                    Pop.timeMsg('danger', 'Search By Billing # ', ' Records not found ', 2000);
                }
            }, function(response) {
                $scope.billingList = [];
                resetTestListButtons();
            });
    }

    $scope.addNewPatient = function search() {
        $http.get("patient/getNextPatientId")
            .then(function(resp) {
                $scope.clearCurrentPatient();
                $scope.uicompo.itemDisabled = false;
                $scope.isDisabledSave = false;
                $scope.patient.patientId = resp.data.response;
                resetTestList();
            }, function(error) {
                //console.log('error > ' + error);
            });
    }

    var getNextNumber = function(selectedTestType) {
        var res = $http.get("patientmedicaltest/getAllByTestTypeOrderByIdDesc?type=" + selectedTestType)
            .then(function(response) {
                if (response.data.success) {
                    $scope.uicompo.showBillingNumber = response.data.response;
                } else {
                    $scope.uicompo.showBillingNumber = '';
                }
            }, function(response) {

            });
    }

    var resetTestList = function() {
        console.log('resetTestList');
        $scope.uicompo = {};
        $scope.billingList = [];
        $scope.mediTestList = [];
        $scope.patientMediTestList = [];
        $scope.showMediTest = false;
        resetTestListButtons();
        $scope.rePrint = false;
        $scope.disabledTestSelection = false;
    }

    var resetTestListButtons = function() {
        $scope.disabledAddTest = false;
        $scope.disabledSaveTestList = true;
        $scope.disabledTxtBillingNumber = false;
        $scope.disabledLoadByBillingNum = false;
        $scope.disabledPrintPage = true;
        $scope.disabledResetTestList = false;
    }

    $scope.resetTList = function() {
        // $scope.uicompo = {};
        $scope.billingList = [];
        $scope.mediTestList = [];
        $scope.patientMediTestList = [];
        resetTestListButtons();
        onchangeSelectedList();
    }
    //function printPage() {
    $scope.printPage = function() {
        var contentToPrint =  'jshjdhjshdhjshjdhs';// document.getElementById('#myDiv').innerHTML;
        var windowPopup = window.open('', '_blank', 'width=500,height=500');
        windowPopup.document.open();
        windowPopup.document.write('<html><head><link rel="stylesheet" type="text/css" href="" /></head><body onload="window.print()">' + contentToPrint + '</body></html>');
        windowPopup.document.close();
    }

    onload();
});
