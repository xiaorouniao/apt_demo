package cn.gd.snm.annotation_complier;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;

/**
 * @author by Low_Power on 2023/6/25
 * 功能：
 */
public class ElementForType {
    //成员变量节点 VariableElement
    private List<VariableElement> viewElement;
    private List<VariableElement> stringElement;
    //方法节点 ExecutableElement
    private List<ExecutableElement> methodElement;

    public ElementForType() {
        viewElement = new ArrayList<>();
        stringElement = new ArrayList<>();
        methodElement = new ArrayList<>();
    }

    public List<VariableElement> getViewElement() {
        return viewElement;
    }

    public void setViewElement(List<VariableElement> viewElement) {
        this.viewElement = viewElement;
    }

    public List<VariableElement> getStringElement() {
        return stringElement;
    }

    public void setStringElement(List<VariableElement> stringElement) {
        this.stringElement = stringElement;
    }

    public List<ExecutableElement> getMethodElement() {
        return methodElement;
    }

    public void setMethodElement(List<ExecutableElement> methodElement) {
        this.methodElement = methodElement;
    }
}
