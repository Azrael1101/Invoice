package tw.com.tm.erp.standardie;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class DataInfo implements java.io.Serializable {
  
    private Object headData;

    private Object lineData;
    
    private List outputData;

    public Object getHeadData() {
        return headData;
    }

    public void setHeadData(Object headData) {
        this.headData = headData;
    }

    public Object getLineData() {
        return lineData;
    }

    public void setLineData(Object lineData) {
        this.lineData = lineData;
    }

    public List getOutputData() {
        return outputData;
    }

    public void setOutputData(List outputData) {
        this.outputData = outputData;
    }
    
    public abstract List generateOutputData(HashMap essentialInfoMap) throws Exception;
	
}
