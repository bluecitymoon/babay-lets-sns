'use strict';

angular.module('dtsnsApp')
    .controller('PostCommentController', function ($scope, PostComment, ParseLinks) {
        $scope.postComments = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            PostComment.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.postComments = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PostComment.get({id: id}, function(result) {
                $scope.postComment = result;
                $('#deletePostCommentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PostComment.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePostCommentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.postComment = {content: null, createDate: null, type: null, id: null};
        };
    });
