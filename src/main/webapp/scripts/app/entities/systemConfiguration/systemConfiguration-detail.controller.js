'use strict';

angular.module('dtsnsApp')
    .controller('SystemConfigurationDetailController', function ($scope, $rootScope, $stateParams, entity, SystemConfiguration) {
        $scope.systemConfiguration = entity;
        $scope.load = function (id) {
            SystemConfiguration.get({id: id}, function(result) {
                $scope.systemConfiguration = result;
            });
        };
        $rootScope.$on('dtsnsApp:systemConfigurationUpdate', function(event, result) {
            $scope.systemConfiguration = result;
        });
    });
