'use strict';

angular.module('dtsnsApp')
    .controller('PostCommentDetailController', function ($scope, $rootScope, $stateParams, entity, PostComment, UserPost) {
        $scope.postComment = entity;
        $scope.load = function (id) {
            PostComment.get({id: id}, function(result) {
                $scope.postComment = result;
            });
        };
        $rootScope.$on('dtsnsApp:postCommentUpdate', function(event, result) {
            $scope.postComment = result;
        });
    });
