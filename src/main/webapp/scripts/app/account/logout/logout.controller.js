'use strict';

angular.module('dtsnsApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
