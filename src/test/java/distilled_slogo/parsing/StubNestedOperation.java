package distilled_slogo.parsing;

import java.util.List;

public class StubNestedOperation {
    private List<StubNestedOperation> children;
    private String name;
    public StubNestedOperation(String name, List<StubNestedOperation>children){
        this.name = name;
        this.children = children;
    }
    public List<StubNestedOperation> children(){
        return children;
    }
    public String name(){
        return name;
    }
}
