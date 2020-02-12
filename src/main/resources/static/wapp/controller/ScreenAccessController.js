app.controller('ScreenAccessController', function($scope, $rootScope, $http, $location, $window, AuthenticationService, Pop) {
    $rootScope.pageTitle = "Screen Administration";
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
            $http.post('/screen/save', $scope.mtest).then(function (response) {
                loadList();
             }, function (response) {
            });
        } else if ('edit' === $scope.actionType) {
            $scope.mtest.status = 'ACTIVE';
            $http.post('/screen/save', $scope.mtest).then(function (response) {
                loadList();
             }, function (response) {
            });
        } else if ('delete' === $scope.actionType) {
            $http.delete('/screen/delete?id=', $scope.mtest.id).then(function (response) {
             }, function (response) {
            });
        }

        $http.post('/screen/save', $scope.mtest).then(function (response) {
            loadList();
         }, function (response) {
        });
    };


    var loadList = function () {
        $http.get("screen/getList").then(function (response) {
            $scope.resultsList = response.data;
        });
    };
    loadList();
});

