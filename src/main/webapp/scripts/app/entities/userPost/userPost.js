'use strict';

angular.module('dtsnsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('userPost', {
                parent: 'entity',
                url: '/userPosts',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'UserPosts'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userPost/userPosts.html',
                        controller: 'UserPostController'
                    }
                },
                resolve: {
                }
            })
            .state('userPost.detail', {
                parent: 'entity',
                url: '/userPost/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'UserPost'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/userPost/userPost-detail.html',
                        controller: 'UserPostDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'UserPost', function($stateParams, UserPost) {
                        return UserPost.get({id : $stateParams.id});
                    }]
                }
            })
            .state('userPost.new', {
                parent: 'userPost',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userPost/userPost-dialog.html',
                        controller: 'UserPostDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {content: null, createDate: null, greetCount: null, commentsCount: null, jid: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('userPost', null, { reload: true });
                    }, function() {
                        $state.go('userPost');
                    })
                }]
            })
            .state('userPost.edit', {
                parent: 'userPost',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/userPost/userPost-dialog.html',
                        controller: 'UserPostDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['UserPost', function(UserPost) {
                                return UserPost.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('userPost', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
