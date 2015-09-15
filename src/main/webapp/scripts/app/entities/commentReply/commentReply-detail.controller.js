'use strict';

angular.module('dtsnsApp')
    .controller('CommentReplyDetailController', function ($scope, $rootScope, $stateParams, entity, CommentReply, PostComment) {
        $scope.commentReply = entity;
        $scope.load = function (id) {
            CommentReply.get({id: id}, function(result) {
                $scope.commentReply = result;
            });
        };
        $rootScope.$on('dtsnsApp:commentReplyUpdate', function(event, result) {
            $scope.commentReply = result;
        });
    });
