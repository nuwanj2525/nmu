app.controller('UserScreenController', function($scope, $rootScope, $http, $location, $window, AuthenticationService, Pop) {
    $rootScope.pageTitle = "User Screen Setup";
    $scope.resultsList = [];
    $scope.itemDisabled = false;
    $scope.actionType = '';

    var loggedUser = '-';
    if ($rootScope.globals && $rootScope.globals.currentUser) {
        loggedUser = $rootScope.globals.currentUser.username;
    }

    $scope.showUI = function (itm, opType) {
        $scope.actionType = opType;
        if ('add' === $scope.actionType) {
            $scope.heading = 'Add Screen Details';
            $scope.itemDisabled = false;
            $scope.mtest = {};
        } else if ('edit' === $scope.actionType) {
            $scope.heading = 'Edit Screen Details';
            $scope.itemDisabled = false;
            $scope.mtest = itm;
            $scope.mtest.dateOfBirth = new Date(itm.dateOfBirth);
        } else if ('delete' === $scope.actionType) {
            $scope.heading = 'Delete Screen Details';
            $scope.itemDisabled = true;
            $scope.mtest = itm;
        }
        $("#modal-inv").modal("show");
    };

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

        $http.post('/userscreen/save', $scope.mtest).then(function (response) {
            loadList();
            //reset_screen();
            //Pop.msgWithButton('New User <<'+ item.fistName + '>> Created','New user <<'+ item.userId + '>>has been created, Auto generated password for the first login user : <<'+item.userId+'>> is : <<' + item.passWord +'>>', 'success');
        }, function (response) {
            //Pop.msgWithButton('UPDATE','Fail User '+ item.fistName + ' Saving', 'error');
        });
    };


    var loadList = function () {
        $http.get("userscreen/getList").then(function (response) {
            $scope.resultsList = response.data;
        });
    };
    loadList();
});

