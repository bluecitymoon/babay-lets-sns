'use strict';

angular.module('dtsnsApp')
    .controller('ChatRoomController', function ($scope, ChatRoom) {
        $scope.chatRooms = [];
        $scope.loadAll = function() {
            ChatRoom.query(function(result) {
               $scope.chatRooms = result;
            });
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            ChatRoom.get({id: id}, function(result) {
                $scope.chatRoom = result;
                $('#deleteChatRoomConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ChatRoom.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteChatRoomConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.chatRoom = {name: null, description: null, id: null};
        };
    });
