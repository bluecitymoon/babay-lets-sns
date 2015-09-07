'use strict';

angular.module('dtsnsApp')
    .controller('UserPostController', function ($scope, UserPost, ParseLinks) {
        $scope.userPosts = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            UserPost.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.userPosts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            UserPost.get({id: id}, function(result) {
                $scope.userPost = result;
                $('#deleteUserPostConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            UserPost.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUserPostConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.userPost = {content: null, createDate: null, greetCount: null, commentsCount: null, jid: null, id: null};
        };
    });
