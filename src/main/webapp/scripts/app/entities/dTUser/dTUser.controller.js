'use strict';

angular.module('dtsnsApp')
    .controller('DTUserController', function ($scope, DTUser, ParseLinks) {
        $scope.dTUsers = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            DTUser.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.dTUsers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            DTUser.get({id: id}, function(result) {
                $scope.dTUser = result;
                $('#deleteDTUserConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            DTUser.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteDTUserConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.dTUser = {username: null, password: null, avatar: null, phone: null, sign: null, birthday: null, id: null};
        };
    });
