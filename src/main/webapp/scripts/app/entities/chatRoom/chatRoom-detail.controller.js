'use strict';

angular.module('dtsnsApp')
    .controller('ChatRoomDetailController', function ($scope, $rootScope, $stateParams, entity, ChatRoom) {
        $scope.chatRoom = entity;
        $scope.load = function (id) {
            ChatRoom.get({id: id}, function(result) {
                $scope.chatRoom = result;
            });
        };
        $rootScope.$on('dtsnsApp:chatRoomUpdate', function(event, result) {
            $scope.chatRoom = result;
        });
    });
