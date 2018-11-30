(function() {
    'use strict';

    angular
        .module('gatewayApp')
        .controller('BankDetailController', BankDetailController);

    BankDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Bank'];

    function BankDetailController($scope, $rootScope, $stateParams, previousState, entity, Bank) {
        var vm = this;

        vm.bank = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('gatewayApp:bankUpdate', function(event, result) {
            vm.bank = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
