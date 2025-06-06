public enum SolvingStatusEnum {
	NOT_STARTED("nie uruchomiono") {
	},
	IN_PROGRESS("w toku...") {
	},
	COMPLETED("rozwiązano!") {
	},
	STOPPED_BY_USER("przerwano z menu") {
	},
	STOPPED_BY_TIMEOUT("przekroczono czas") {
	},
	STOPPED_BY_GENERATION_LIMIT("przekroczono limit pokoleń") {
	},
	FAILED("nie rozwiązano") {
	};

	private final String displayName;

	SolvingStatusEnum(String _displayName) {
		this.displayName = _displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
