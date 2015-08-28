'use strict';

angular.module('dtsnsApp').controller('ChatRoomDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'ChatRoom',
        function($scope, $stateParams, $modalInstance, entity, ChatRoom) {

        $scope.chatRoom = entity;
        $scope.load = function(id) {
            ChatRoom.get({id : id}, function(result) {
                $scope.chatRoom = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('dtsnsApp:chatRoomUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.chatRoom.id != null) {
                ChatRoom.update($scope.chatRoom, onSaveFinished);
            } else {
                ChatRoom.save($scope.chatRoom, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
