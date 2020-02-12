app.controller('LogoutController', function ($scope, $rootScope, $http, $location, $window, AuthenticationService) {
    $rootScope.pageTitle = "Medical Data Analysis System Logout";
    var init = function () {
        AuthenticationService.ClearCredentials();
        $location.path('/login');
    };
    init();
});
