'use strict';

angular.module('rmtApp')
    .factory('Msu', function ($resource, DateUtils) {
        return $resource('api/msus/:id', {}, {
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
