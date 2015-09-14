'use strict';

angular.module('dtsnsApp')
    .factory('PostComment', function ($resource, DateUtils) {
        return $resource('api/postComments/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.createDate = DateUtils.convertDateTimeFromServer(data.createDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
