'use strict';

angular.module('dtsnsApp').controller('PostImageDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'PostImage', 'UserPost',
        function($scope, $stateParams, $modalInstance, entity, PostImage, UserPost) {

        $scope.postImage = entity;
        $scope.userposts = UserPost.query();
        $scope.load = function(id) {
            PostImage.get({id : id}, function(result) {
                $scope.postImage = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('dtsnsApp:postImageUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.postImage.id != null) {
                PostImage.update($scope.postImage, onSaveFinished);
            } else {
                PostImage.save($scope.postImage, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
