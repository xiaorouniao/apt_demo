package cn.gd.snm.annotation_complier;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import cn.gd.snm.annotation.BindString;
import cn.gd.snm.annotation.BindView;
import cn.gd.snm.annotation.OnClick;

/**
 * @author by Low_Power on 2023/6/25
 * 功能：
 */
@AutoService(Processor.class)
public class Annotation extends AbstractProcessor {
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        Messager messager = this.processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "zoukun init.....");
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(BindView.class.getCanonicalName());
        set.add(OnClick.class.getCanonicalName());
        set.add(BindString.class.getCanonicalName());
        return set;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //寻找标记
        Messager messager = this.processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "zoukun init.....");
        Map<TypeElement, ElementForType> typeMap = new HashMap<>();
        Set<? extends Element> viewElement = roundEnv.getElementsAnnotatedWith(BindView.class);
        Set<? extends Element> methodElement = roundEnv.getElementsAnnotatedWith(OnClick.class);
        Set<? extends Element> stringElement = roundEnv.getElementsAnnotatedWith(BindString.class);
        //处理标记
        for (Element element : viewElement) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            ElementForType elementForType = typeMap.get(typeElement);
            if (elementForType == null) {
                elementForType = new ElementForType();
            }
            elementForType.getViewElement().add(variableElement);
            typeMap.put(typeElement, elementForType);
        }
        for (Element element : stringElement) {
            VariableElement variableElement = (VariableElement) element;
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            ElementForType elementForType = typeMap.get(typeElement);
            if (elementForType == null) {
                elementForType = new ElementForType();
            }
            elementForType.getStringElement().add(variableElement);
            typeMap.put(typeElement, elementForType);
        }
        for (Element element : methodElement) {
            ExecutableElement variableElement = (ExecutableElement) element;
            TypeElement typeElement = (TypeElement) variableElement.getEnclosingElement();
            ElementForType elementForType = typeMap.get(typeElement);
            if (elementForType == null) {
                elementForType = new ElementForType();
            }
            elementForType.getMethodElement().add(variableElement);

            typeMap.put(typeElement, elementForType);
        }
        //编写代码
        Filer filer = processingEnv.getFiler();
        Iterator<TypeElement> iterator = typeMap.keySet().iterator();
        while (iterator.hasNext()) {
            Writer writer = null;
            TypeElement next = iterator.next();
            String className = next.getSimpleName().toString();
            String packageName = processingEnv.getElementUtils().getPackageOf(next).getQualifiedName().toString();
            try {
                JavaFileObject sourceFile = filer.createSourceFile(packageName + "." + className + "$$ViewBind");
                writer = sourceFile.openWriter();
                StringBuilder sb = new StringBuilder();
                sb.append("package " + packageName + ";\n")
                        .append("import android.view.View;")
                        .append("public class " + className + "$$ViewBind{\n")
                        .append("public void bind(final " + className + " activity){\n");
                ElementForType elementForType = typeMap.get(next);
                List<VariableElement> viewElements = elementForType.getViewElement();
                for (VariableElement viewElement1 : viewElements) {
                    String viewName = viewElement1.getSimpleName().toString();
                    int viewId = viewElement1.getAnnotation(BindView.class).value();
                    TypeMirror viewType = viewElement1.asType();
                    sb.append("activity." + viewName + " = (" + viewType + ")activity.findViewById(" + viewId + ");\n");
                }
                List<ExecutableElement> methodElements = elementForType.getMethodElement();
                for (ExecutableElement methodElement1 : methodElements) {
                    String methodName = methodElement1.getSimpleName().toString();
                    int[] value = methodElement1.getAnnotation(OnClick.class).value();
                    for (int id : value) {
                        sb.append("activity.findViewById(" + id + ").setOnClickListener(new View.OnClickListener(){" +
                                "public void onClick(View p){" +
                                "activity." + methodName + "(p);" +
                                "}" +
                                "});");
                    }
                }
                sb.append("}");
                sb.append("}");
                writer.write(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {

                    }
                }
            }

        }
        return false;
    }
}
