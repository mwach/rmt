'use strict';

angular.module('rmtApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('msu', {
                parent: 'entity',
                url: '/msus',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Msus'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/msu/msus.html',
                        controller: 'MsuController'
                    }
                },
                resolve: {
                }
            })
            .state('msu.detail', {
                parent: 'entity',
                url: '/msu/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'Msu'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/msu/msu-detail.html',
                        controller: 'MsuDetailController'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Msu', function($stateParams, Msu) {
                        return Msu.get({id : $stateParams.id});
                    }]
                }
            })
            .state('msu.new', {
                parent: 'msu',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/msu/msu-dialog.html',
                        controller: 'MsuDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    code: null,
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('msu', null, { reload: true });
                    }, function() {
                        $state.go('msu');
                    })
                }]
            })
            .state('msu.edit', {
                parent: 'msu',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/msu/msu-dialog.html',
                        controller: 'MsuDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Msu', function(Msu) {
                                return Msu.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('msu', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('msu.delete', {
                parent: 'msu',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/msu/msu-delete-dialog.html',
                        controller: 'MsuDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Msu', function(Msu) {
                                return Msu.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('msu', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
