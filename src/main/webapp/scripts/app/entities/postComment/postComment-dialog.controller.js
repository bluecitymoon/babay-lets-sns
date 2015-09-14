'use strict';

angular.module('dtsnsApp').controller('PostCommentDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'PostComment', 'UserPost',
        function($scope, $stateParams, $modalInstance, entity, PostComment, UserPost) {

        $scope.postComment = entity;
        $scope.userposts = UserPost.query();
        $scope.load = function(id) {
            PostComment.get({id : id}, function(result) {
                $scope.postComment = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('dtsnsApp:postCommentUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.postComment.id != null) {
                PostComment.update($scope.postComment, onSaveFinished);
            } else {
                PostComment.save($scope.postComment, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
