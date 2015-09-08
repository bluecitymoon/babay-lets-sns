'use strict';

angular.module('dtsnsApp')
    .controller('PostImageDetailController', function ($scope, $rootScope, $stateParams, entity, PostImage, UserPost) {
        $scope.postImage = entity;
        $scope.load = function (id) {
            PostImage.get({id: id}, function(result) {
                $scope.postImage = result;
            });
        };
        $rootScope.$on('dtsnsApp:postImageUpdate', function(event, result) {
            $scope.postImage = result;
        });
    });
