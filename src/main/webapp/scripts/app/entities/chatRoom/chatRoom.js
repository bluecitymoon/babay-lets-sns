'use strict';

angular.module('dtsnsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('chatRoom', {
                parent: 'entity',
                url: '/chatRooms',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ChatRooms'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/chatRoom/chatRooms.html',
                        controller: 'ChatRoomController'
                    }
                },
                resolve: {
                }
            })
            .state('chatRoom.detail', {
                parent: 'entity',
                url: '/chatRoom/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ChatRoom'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/chatRoom/chatRoom-detail.html',
                        controller: 'ChatRoomDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'ChatRoom', function($stateParams, ChatRoom) {
                        return ChatRoom.get({id : $stateParams.id});
                    }]
                }
            })
            .state('chatRoom.new', {
                parent: 'chatRoom',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/chatRoom/chatRoom-dialog.html',
                        controller: 'ChatRoomDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {name: null, description: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('chatRoom', null, { reload: true });
                    }, function() {
                        $state.go('chatRoom');
                    })
                }]
            })
            .state('chatRoom.edit', {
                parent: 'chatRoom',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/chatRoom/chatRoom-dialog.html',
                        controller: 'ChatRoomDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['ChatRoom', function(ChatRoom) {
                                return ChatRoom.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('chatRoom', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
