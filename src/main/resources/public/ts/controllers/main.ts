import {ng, template} from 'entcore';
import {COMBO_LABELS} from "../enum/comboLabels";

/**
	Wrapper controller
	------------------
	Main controller.
**/
export const mainController = ng.controller('MainController', ['$scope', 'route', ($scope, route) => {

	$scope.comboLabels = COMBO_LABELS;

	// Routing & template opening
	route({
		default: () => {
			template.open('main', 'containers/schools');
		}
	});

	$scope.safeApply = (fn?) => {
		const phase = $scope.$root.$$phase;
		if (phase == '$apply' || phase == '$digest') {
			if (fn && (typeof (fn) === 'function')) {
				fn();
			}
		} else {
			$scope.$apply(fn);
		}
	};

}]);
