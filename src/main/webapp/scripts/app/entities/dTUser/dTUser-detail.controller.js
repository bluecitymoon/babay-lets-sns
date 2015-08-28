'use strict';

angular.module('dtsnsApp')
    .controller('DTUserDetailController', function ($scope, $rootScope, $stateParams, entity, DTUser) {
        $scope.dTUser = entity;
        $scope.load = function (id) {
            DTUser.get({id: id}, function(result) {
                $scope.dTUser = result;
            });
        };
        $rootScope.$on('dtsnsApp:dTUserUpdate', function(event, result) {
            $scope.dTUser = result;
        });
    });
