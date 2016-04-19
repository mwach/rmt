'use strict';

angular.module('rmtApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


