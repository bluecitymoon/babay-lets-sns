'use strict';

angular.module('dtsnsApp')
    .controller('SystemConfigurationController', function ($scope, SystemConfiguration, ParseLinks) {
        $scope.systemConfigurations = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            SystemConfiguration.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.systemConfigurations = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            SystemConfiguration.get({id: id}, function(result) {
                $scope.systemConfiguration = result;
                $('#deleteSystemConfigurationConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            SystemConfiguration.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSystemConfigurationConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.systemConfiguration = {identity: null, currentValue: null, column1: null, value1: null, column2: null, value2: null, column3: null, value3: null, id: null};
        };
    });
