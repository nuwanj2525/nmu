app.controller('UserAdminController', function ($scope, $rootScope, $http, $location, $window, Pop, UserService) {
    $rootScope.pageTitle = "User Admin Setup";

    var loggedUser = '-';
    if ($rootScope.globals && $rootScope.globals.currentUser) {
        loggedUser = $rootScope.globals.currentUser.username;
    }

    $scope.btnAddShow = true;
    $scope.btnEditShow = true;
    $scope.btnDelShow = true;
    $scope.btnPwRstShow = true;
    $scope.btnResetShow = false;
    $scope.save_is_disabled = true;

    $scope.useris_show = true;
    $scope.actionType = '';
    $scope.usr = {};
    $scope.component_is_disabled = true;
    $scope.selectuser = {}; // used selector
    $scope.btnSubmitText = 'SAVE';
    $scope.userAdminList = [];

    $scope.reset = function () {
        $scope.useris_show = true;
        reset_screen();
    };

    var reset_component = function () {
        $scope.usr = {};
    };

    var reset_screen = function () {
        $scope.component_is_disabled = true;
        $scope.actionType = '';
        $scope.usr = {};
        $scope.useris_show = true;
        restore_Button();
        loadList();
    };

    var restore_Button = function () {
        console.log('call restore_Button');
        $scope.btnAddShow = true;
        $scope.btnEditShow = true;
        $scope.btnDelShow = true;
        $scope.btnPwRstShow = true;
        $scope.btnResetShow = false;
        $scope.save_is_disabled = true;
        $scope.btnSubmitText = 'SAVE';
    };

    var disable_Button = function () {
        console.log('call disable_Button');
        $scope.btnAddShow = false;
        $scope.btnEditShow = false;
        $scope.btnDelShow = false;
        $scope.btnPwRstShow = false;
        $scope.btnResetShow = true;
        $scope.save_is_disabled = false;
    };

    $scope.add = function () {
        $scope.usr = {};
        $scope.selectuser = {};
        disable_Button();
        $scope.actionType = 'add';
        $scope.useris_show = false;
        $scope.component_is_disabled = false;
        $scope.btnSubmitText = 'SAVE';
    };

    $scope.edit = function () {
        $scope.usr = $scope.selectuser;
        if (!$scope.selectuser.id) {
            Pop.msgWithButton('Please select user', 'USER NOT SELECTED', 'warning');
            return;
        }
        disable_Button();
        $scope.usr = $scope.selectuser;
        $scope.actionType = 'edit';
        $scope.useris_show = false;
        $scope.component_is_disabled = false;
        $scope.btnSubmitText = 'UPDATE';
    };

    $scope.delete = function () {
        $scope.usr = $scope.selectuser;
        if (!$scope.selectuser.id) {
            Pop.msgWithButton('Please select user', 'USER NOT SELECTED', 'warning');
            return;
        }
        disable_Button();
        $scope.actionType = 'delete';
        $scope.useris_show = false;
        $scope.btnSubmitText = 'DELETE';
        Pop.msgWDelete('error', 'DELETE', 'Are you sure to delete the user ' + $scope.usr.fistName, 3000)
    };

    $scope.reset_password = function () {
        var item = $scope.selectuser;
        if (!item.id) {
            Pop.msgWithButton('RESET PASSWORD', 'User not selected', 'warning');
            return;
        }
        $scope.actionType = 'reset_password';
        item.lastDateModified = new Date();
        var randomPassword = item.userId + '@' + item.userPFNumber;
        item.passWord = randomPassword;
        $http.post('/users/save', item).then(function (response) {
            loadList();
            reset_screen();
            Pop.msgWithButton('Password has been reset for user <<' + item.fistName + '>> ', 'User <<' + item.userId + '>> password has been reset, Auto generated password for the user : <<' + item.userId + '>> is : <<' + item.passWord + '>>', 'success');
        }, function (response) {
            Pop.msgWithButton('RESET PASSWORD', 'Fail to reset password for User ' + item.fistName, 'error');
        });
    };

    $scope.save_submit = function () {
        var actionType = $scope.actionType;
        var item = $scope.usr;
        item.dateCreated = new Date();
        item.actionBy = loggedUser;
        // console.log('actionType >' + actionType);
        // console.log('item >' + JSON.stringify(item));
        if (actionType === 'add') {
            /*if(isEmpty(item.userId)){
                Pop.timeMsg('warning','USERID Cannot be empty','Warning Message',1000);
                return true;
            }*/
            //if(validateFields(item)){return;}
            item.lastDateModified = new Date();
            var randomPassword = item.userId + '@' + item.userPFNumber;
            item.passWord = randomPassword;
            $http.post('/users/save', item).then(function (response) {
                reset_screen();
                Pop.msgWithButton('New User <<' + item.fistName + '>> Created', 'New user <<' + item.userId + '>>has been created, Auto generated password for the first login user : <<' + item.userId + '>> is : <<' + item.passWord + '>>', 'success');
            }, function (response) {
                Pop.msgWithButton('UPDATE', 'Fail User ' + item.fistName + ' Saving', 'error');
            });
        } else if (actionType === 'edit') {
            $http.post('/users/save', item).then(function (response) {
                reset_screen();
                Pop.msgWithButton('SAVE', 'Update User ' + item.fistName, 'success');
            }, function (response) {
                Pop.msgWithButton('UPDATE', 'Fail User ' + item.fistName + ' Updat', 'error');
            });
        } else if (actionType === 'delete') {
            $http.delete("users/delete?id=" + item.id);
            Pop.msgWithButton('DELETE', 'Deleted USER <<' + item.fistName + '>> ', 'success');
            reset_screen();
        }
    };

    var isEmpty = function (val) {
        if (!val)
            return true;
        else
            return false;
    };

    var validateFields = function (item) {
        if (isEmpty(item.fistName)) {
            Pop.timeMsg('warning', 'fistName Cannot be empty', 'Warning Message', 1000);
            return true;
        } else if (isEmpty(item.middleName)) {
            Pop.timeMsg('warning', 'middleName Cannot be empty', 'Warning Message', 1000);
            return true;
        } else if (isEmpty(item.lastName)) {
            Pop.timeMsg('warning', 'lastName Cannot be empty', 'Warning Message', 1000);
            return true;
        } else if (isEmpty(item.passWord)) {
            Pop.timeMsg('warning', 'passWord Cannot be empty', 'Warning Message', 1000);
            return true;
        } else if (isEmpty(item.userRoles)) {
            Pop.timeMsg('warning', 'userRoles Cannot be empty', 'Warning Message', 1000);
            return true;
        } else if (isEmpty(item.userEmail)) {
            Pop.timeMsg('warning', 'userEmail Cannot be empty', 'Warning Message', 1000);
            return true;
        } else if (isEmpty(item.userPFNumber)) {
            Pop.timeMsg('warning', 'userPFNumber Cannot be empty', 'Warning Message', 1000);
            return true;
        } else if (isEmpty(item.telNumber)) {
            Pop.timeMsg('warning', 'telNumber Cannot be empty', 'Warning Message', 1000);
            return true;
        } else if (isEmpty(item.mobNumber)) {
            Pop.timeMsg('warning', 'mobNumber Cannot be empty', 'Warning Message', 1000);
            return true;
        }
        return false;
    };

    $scope.onSelectAdminUser = function () {
        if ($scope.selectuser) {
            $http.get("users/getById?id=" + $scope.selectuser.userId).then(function (response) {
                $scope.usr = response.data.response;
            }, function (response) {
                // Pop.msgWithButton('UPDATE','Fail User '+ item.fistName + ' Updat', 'error');
            });
        }
    };

    var loadList = function () {
        $http.get("users/getList").then(function (response) {
            $scope.userAdminList = response.data;
        });
    };
    loadList();
});

