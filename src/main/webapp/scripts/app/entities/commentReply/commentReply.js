'use strict';

angular.module('dtsnsApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('commentReply', {
                parent: 'entity',
                url: '/commentReplys',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'CommentReplys'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/commentReply/commentReplys.html',
                        controller: 'CommentReplyController'
                    }
                },
                resolve: {
                }
            })
            .state('commentReply.detail', {
                parent: 'entity',
                url: '/commentReply/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'CommentReply'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/commentReply/commentReply-detail.html',
                        controller: 'CommentReplyDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'CommentReply', function($stateParams, CommentReply) {
                        return CommentReply.get({id : $stateParams.id});
                    }]
                }
            })
            .state('commentReply.new', {
                parent: 'commentReply',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/commentReply/commentReply-dialog.html',
                        controller: 'CommentReplyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {content: null, jid: null, createDate: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('commentReply', null, { reload: true });
                    }, function() {
                        $state.go('commentReply');
                    })
                }]
            })
            .state('commentReply.edit', {
                parent: 'commentReply',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/commentReply/commentReply-dialog.html',
                        controller: 'CommentReplyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['CommentReply', function(CommentReply) {
                                return CommentReply.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('commentReply', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
