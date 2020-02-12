app.controller('LoginController', function ($scope, $rootScope, $http, $location, $window, AuthenticationService, UserService, Pop) {
    $rootScope.pageTitle = "Medical Data Analysis System Login";
    $scope.dataLoading = false;
    $scope.auser = {};
    $scope.dataLoading = true;

    AuthenticationService.ClearCredentials();

    $scope.loginToSystem = function (username, password) {
        $http.post('/users/authenticate', {
            username: $scope.auser.username,
            password: $scope.auser.password
        }).then(function (response) {
            // console.log('response > ' + JSON.stringify(response));
            if (response.data.success) {
                AuthenticationService.SetCredentials($scope.auser.username, $scope.auser.password, response);
                $location.path('/dashboard');
            } else {
                Pop.timeMsg('warning', 'ERROR LOGGING', response.data.exception, 2000);
                exception;
                $location.path('/');
            }

        }, function (response) {
            FlashService.Error(response.exception);
            $scope.dataLoading = false;

        });

        /*        AuthenticationService.Login($scope.auser.username, $scope.auser.password, function (responce) {
                    if (response.success) {
                        AuthenticationService.SetCredentials($scope.auser.username, $scope.auser.password,response.resObjects);
                        $location.path('/');
                    } else {
                        FlashService.Error(response.message);
                        $scope.dataLoading = false;
                    }
                });*/
    };

    var validateField = function (fieldToValidate, errmessage) {
        // AuthenticationService.Login();
    }
});
