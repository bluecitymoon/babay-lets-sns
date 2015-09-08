'use strict';

angular.module('dtsnsApp')
    .controller('PostImageController', function ($scope, PostImage, ParseLinks) {
        $scope.postImages = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            PostImage.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.postImages = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            PostImage.get({id: id}, function(result) {
                $scope.postImage = result;
                $('#deletePostImageConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PostImage.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePostImageConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.postImage = {src: null, src1: null, src2: null, src3: null, id: null};
        };
    });
