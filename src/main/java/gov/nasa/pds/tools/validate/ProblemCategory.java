package gov.nasa.pds.tools.validate;

/**
 * Defines categories of problems. Beyond the ExceptionType (ERROR, WARNING, etc.) there is a need 
 * for an additional level of grouping errors and exceptions.
 */
public enum ProblemCategory {

	/**
	 * Problems with individual product validation
	 */
    PRODUCT("product.problem"),
    
	/**
	 * Problems with referential integrity validation
	 */
    INTEGRITY("integrity.problem"),

    /**
     * General problems with overall validation of the known targets.
     */
    GENERAL("general.problem"),

    /**
     * Problems with execution of the tool.
     */
    EXECUTION("execution.problem"); 
	
    private final String key;

    private ProblemCategory(String key) {
        this.key = key;
    }

    /**
     * Gets the key for mapping the problem type to a UI string.
     *
     * @return the key string
     */
    public String getKey() {
        return key;
    }
}