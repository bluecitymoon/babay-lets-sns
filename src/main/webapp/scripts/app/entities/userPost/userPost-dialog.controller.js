'use strict';

angular.module('dtsnsApp').controller('UserPostDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'UserPost',
        function($scope, $stateParams, $modalInstance, entity, UserPost) {

        $scope.userPost = entity;
        $scope.load = function(id) {
            UserPost.get({id : id}, function(result) {
                $scope.userPost = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('dtsnsApp:userPostUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.userPost.id != null) {
                UserPost.update($scope.userPost, onSaveFinished);
            } else {
                UserPost.save($scope.userPost, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
