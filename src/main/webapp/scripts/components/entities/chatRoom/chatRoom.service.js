'use strict';

angular.module('dtsnsApp')
    .factory('ChatRoom', function ($resource, DateUtils) {
        return $resource('api/chatRooms/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
