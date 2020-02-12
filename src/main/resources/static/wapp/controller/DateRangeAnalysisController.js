app.controller('DateRangeAnalysisController', function ($scope, $rootScope, $http, $location, $window, AuthenticationService, Pop) {
    $rootScope.pageTitle = "Data Analysis Based on Date Range";

    $scope.statics = {};
    $scope.statics.genderbytest = [];
    $scope.statics.genderbypatient = [];
    $scope.testList = [];
    $scope.statics.testLst = [];
    $scope.medicalTestList = [];
    $scope.uicompo = {};
    $scope.uicompo.selectedTestId = "";

    var testStatics = function () {
        c3.generate({
            bindto: '#testChart',
            data: {
                columns: $scope.statics.genderbytest,
                type: 'pie'
            },
            pie: {
            label: {
                format: function(value, ratio, id) {
                    ratio = d3.format("%")(ratio); // format ratio
                    //return [id, value, ratio].join(); // used to pass values to the onrender function
                    return value + " - " + ratio;

                }
            }
        }
        });
    }

    var patientStatics = function () {
        c3.generate({
            bindto: '#patientChart',
            data: {
                columns: $scope.statics.genderbypatient,
                type: 'pie'
            },
            pie: {
                label: {
                    format: function(value, ratio, id) {
                        ratio = d3.format("%")(ratio); // format ratio
                        //return [id, value, ratio].join(); // used to pass values to the onrender function
                        return value + " - " + ratio;

                    }
                }
            }
        });
    }

    $scope.genderStatics = function () {
        var res = $http.get("analysis/genderStatics")
            .then(function (response) {
                $scope.statics.genderbytest = response.data;
                testStatics();
            }, function (response) {

            }).catch(function () {

            });
    }

    $scope.genderStaticsByPatient = function () {
        var res = $http.get("analysis/genderStaticsByPatient?testid=12")
            .then(function (response) {
                $scope.statics.genderbypatient = response.data;
                patientStatics();
            }, function (response) {

            }).catch(function () {

            });
    }

    $scope.onChangeTestName = function (key) {
        var res = $http.get("analysis/genderStaticsByTest?testid=" + $scope.uicompo.selectedTestId)
        .then(function (response) {
            $scope.statics.genderbypatient = response.data;
            patientStatics();
        }, function (response) {

        }).catch(function () {

        });
    }



    var loadList = function () {
        $http.get("medicaltest/getIdNameList").then(function (jsn) {
            $scope.medicalTestList = jsn.data;
        });
    };

    $scope.genderStatics();
    $scope.genderStaticsByPatient();
    loadList();


});


/*        c3.generate({
            bindto: '#chart',
            data: {
                columns: [
                    ['data1', 30, 200, 100, 400, 150, 250],
                    ['data2', 50, 20, 10, 40, 15, 25]
                ],
                axes: {
                    data2: 'y2'
                }
            },
            axis: {
                y: {
                    label: { // ADD
                        text: 'Y Label',
                        position: 'outer-middle'
                    }
                },
                y2: {
                    show: true,
                    label: { // ADD
                        text: 'Y2 Label',
                        position: 'outer-middle'
                    }
                }
            }
        });*/

/*var charThree = c3.generate({
    bindto: "#chartThree",
    size: {
        width: 500,
        height: 300
    },
    data: {
        colors: {
            A: 'yellow',
            B: 'red',
            C: 'green',
            D: 'orange',
            E: 'blue'
        },
        columns: [
            ['A',20],
            ['B',40],
            ['C',20],
            ['D',10],
            ['E',9]
        ],
        type: 'pie'
    },
    pie: {
        labels: {
            show: true,
            threshold: 0.1,
            format: {
                A: function (value, ratio, id) {
                    if(value=20) {
                        return "A<br/>9item<br/>20.2%";
                    }
                }
            }
        }
    }

});*/
