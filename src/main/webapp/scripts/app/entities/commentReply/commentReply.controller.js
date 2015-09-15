'use strict';

angular.module('dtsnsApp')
    .controller('CommentReplyController', function ($scope, CommentReply, ParseLinks) {
        $scope.commentReplys = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            CommentReply.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.commentReplys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            CommentReply.get({id: id}, function(result) {
                $scope.commentReply = result;
                $('#deleteCommentReplyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            CommentReply.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteCommentReplyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.commentReply = {content: null, jid: null, createDate: null, id: null};
        };
    });
