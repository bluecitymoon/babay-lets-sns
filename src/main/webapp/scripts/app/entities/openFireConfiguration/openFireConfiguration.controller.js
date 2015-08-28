'use strict';

angular.module('dtsnsApp')
    .controller('OpenFireConfigurationController', function ($scope, OpenFireConfiguration) {
        $scope.openFireConfigurations = [];
        $scope.loadAll = function() {
            OpenFireConfiguration.query(function(result) {
               $scope.openFireConfigurations = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            OpenFireConfiguration.get({id: id}, function(result) {
                $scope.openFireConfiguration = result;
                $('#deleteOpenFireConfigurationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            OpenFireConfiguration.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteOpenFireConfigurationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.openFireConfiguration = {authenticationToken: null, serverAddress: null, restApiPort: null, identifier: null, id: null};
        };
    });
