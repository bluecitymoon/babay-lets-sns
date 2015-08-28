'use strict';

angular.module('dtsnsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('dTUser', {
                parent: 'entity',
                url: '/dTUsers',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'DTUsers'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dTUser/dTUsers.html',
                        controller: 'DTUserController'
                    }
                },
                resolve: {
                }
            })
            .state('dTUser.detail', {
                parent: 'entity',
                url: '/dTUser/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'DTUser'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/dTUser/dTUser-detail.html',
                        controller: 'DTUserDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'DTUser', function($stateParams, DTUser) {
                        return DTUser.get({id : $stateParams.id});
                    }]
                }
            })
            .state('dTUser.new', {
                parent: 'dTUser',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dTUser/dTUser-dialog.html',
                        controller: 'DTUserDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {username: null, password: null, avatar: null, phone: null, sign: null, birthday: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('dTUser', null, { reload: true });
                    }, function() {
                        $state.go('dTUser');
                    })
                }]
            })
            .state('dTUser.edit', {
                parent: 'dTUser',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/dTUser/dTUser-dialog.html',
                        controller: 'DTUserDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['DTUser', function(DTUser) {
                                return DTUser.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('dTUser', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
