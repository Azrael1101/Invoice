package tw.com.tm.erp.standardie;

import java.util.HashMap;
import java.util.List;

public abstract class HDDataInfo extends DataInfo {

    public static final String MASTERSYMBOL = "H";
    
    public static final String DETAILSYMBOL = "D";

    public abstract List generateOutputData(HashMap essentialInfoMap) throws Exception;
}
