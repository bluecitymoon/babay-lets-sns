'use strict';

angular.module('dtsnsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('postComment', {
                parent: 'entity',
                url: '/postComments',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'PostComments'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/postComment/postComments.html',
                        controller: 'PostCommentController'
                    }
                },
                resolve: {
                }
            })
            .state('postComment.detail', {
                parent: 'entity',
                url: '/postComment/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'PostComment'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/postComment/postComment-detail.html',
                        controller: 'PostCommentDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'PostComment', function($stateParams, PostComment) {
                        return PostComment.get({id : $stateParams.id});
                    }]
                }
            })
            .state('postComment.new', {
                parent: 'postComment',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/postComment/postComment-dialog.html',
                        controller: 'PostCommentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {content: null, createDate: null, type: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('postComment', null, { reload: true });
                    }, function() {
                        $state.go('postComment');
                    })
                }]
            })
            .state('postComment.edit', {
                parent: 'postComment',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/postComment/postComment-dialog.html',
                        controller: 'PostCommentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PostComment', function(PostComment) {
                                return PostComment.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('postComment', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
