public enum SolvingStatusEnum {
	NOT_STARTED("nie uruchomiono") {
	},
	STARTED("uruchomiono") {
	},
	IN_PROGRESS("w toku...") {
	},
	COMPLETED("rozwiązano!") {
	},
	ITERATION_COMPLETED("rozwiązano iterację!") {
	},
	STOPPED_BY_USER("przerwano z menu") {
	},
	STOPPED_BY_TIMEOUT("przekroczono czas") {
	},
	STOPPED_BY_GENERATIONS_LIMIT("przekroczono limit pokoleń") {
	},
	FAILED("nie rozwiązano") {
	},
	ERROR("błąd programu") {
	};

	private final String displayName;

	SolvingStatusEnum(String _displayName) {
		this.displayName = _displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
