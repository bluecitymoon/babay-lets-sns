'use strict';

angular.module('dtsnsApp')
    .controller('UserPostDetailController', function ($scope, $rootScope, $stateParams, entity, UserPost) {
        $scope.userPost = entity;
        $scope.load = function (id) {
            UserPost.get({id: id}, function(result) {
                $scope.userPost = result;
            });
        };
        $rootScope.$on('dtsnsApp:userPostUpdate', function(event, result) {
            $scope.userPost = result;
        });
    });
