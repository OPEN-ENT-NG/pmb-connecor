package fr.openent.pmb.core.constants;

public class Field {

    private Field() {
        throw new IllegalStateException("Utility class");
    }

    public static final String ID = "id";
    public static final String ERROR = "error";
    public static final String USER_IDS = "user_ids";
    public static final String GROUP_NAME = "group_name";
    public static final String GROUP_MANAGER_LEND_MANUAL = "group_manager_lend_manual";
    public static final String UAIS = "uais";
    public static final String EXTERNAL_ID = "externalId";

    public static final String EMAIL = "email";
    public static final String ADDRESS = "address";
    public static final String ZIP_CODE = "zipCode";
    public static final String COUNTRY = "country";
    public static final String CITY = "city";
}
