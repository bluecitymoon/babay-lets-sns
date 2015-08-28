'use strict';

angular.module('dtsnsApp')
    .controller('OpenFireConfigurationDetailController', function ($scope, $rootScope, $stateParams, entity, OpenFireConfiguration) {
        $scope.openFireConfiguration = entity;
        $scope.load = function (id) {
            OpenFireConfiguration.get({id: id}, function(result) {
                $scope.openFireConfiguration = result;
            });
        };
        $rootScope.$on('dtsnsApp:openFireConfigurationUpdate', function(event, result) {
            $scope.openFireConfiguration = result;
        });
    });
