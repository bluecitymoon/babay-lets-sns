'use strict';

angular.module('dtsnsApp').controller('CommentReplyDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'CommentReply', 'PostComment',
        function($scope, $stateParams, $modalInstance, entity, CommentReply, PostComment) {

        $scope.commentReply = entity;
        $scope.postcomments = PostComment.query();
        $scope.load = function(id) {
            CommentReply.get({id : id}, function(result) {
                $scope.commentReply = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('dtsnsApp:commentReplyUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.commentReply.id != null) {
                CommentReply.update($scope.commentReply, onSaveFinished);
            } else {
                CommentReply.save($scope.commentReply, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
