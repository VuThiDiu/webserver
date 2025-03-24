package service;

import annotation.DeleteMapping;
import annotation.GetMapping;
import annotation.PostMapping;
import annotation.PutMapping;
import constants.HttpMethod;
import exception.MethodNotDeclared;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*Guarantee this is a singleton class  */
public class Dispatcher {
    /* Handler mapping controller */
    private Map<String, Method> getMethod;
    private Map<String, Method> postMethod;
    private Map<String, Method> putMethod;
    private Map<String, Method> deleteMethod;
    private Map<String, Object> mapsController;


    private static Dispatcher instance;

    public void setController(Object controller) {
        scanHandlers(controller);
        mapsController.put(controller.getClass().getName(), controller);
    }

    private Dispatcher() {
        getMethod = new HashMap<>();
        postMethod = new HashMap<>();
        putMethod = new HashMap<>();
        deleteMethod = new HashMap<>();

        mapsController = new HashMap<>();
    }

    private void scanHandlers(Object controller) {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                getMethod.put(getMapping.value(), method);
            }
            if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                postMethod.put(postMapping.value(), method);
            }
            if (method.isAnnotationPresent(PutMapping.class)) {
                PutMapping putMapping = method.getAnnotation(PutMapping.class);
                putMethod.put(putMapping.value(), method);
            }
            if (method.isAnnotationPresent(DeleteMapping.class)) {
                DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                deleteMethod.put(deleteMapping.value(), method);
            }
        }
    }

    public String execute(String method, String path, String requestBody) {
        try {
            if (method.equals(HttpMethod.GET.getMethod())) {
                Method handler = getMethod.get(path);
                if (Objects.isNull(handler)) return null;
                String className = handler.getDeclaringClass().getName();
                Object controller = mapsController.get(className);
                return (String) handler.invoke(controller);
            } else if (method.equals(HttpMethod.POST.getMethod())) {
                Method handler = postMethod.get(path);
                if (Objects.isNull(handler)) return null;
                String className = handler.getDeclaringClass().getName();
                Object controller = mapsController.get(className);
                return (String) handler.invoke(controller, requestBody);
            } else if (method.equals(HttpMethod.PUT.getMethod())) {
                Method handler = putMethod.get(path);
                if (Objects.isNull(handler)) return null;
                String className = handler.getDeclaringClass().getName();
                Object controller = mapsController.get(className);
                return (String) handler.invoke(controller, requestBody);
            } else if (method.equals(HttpMethod.DELETE.getMethod())) {
                Method handler = deleteMethod.get(path);
                if (Objects.isNull(handler)) return null;
                String className = handler.getDeclaringClass().getName();
                Object controller = mapsController.get(className);
                return (String) handler.invoke(controller, requestBody);
            } else throw new MethodNotDeclared("Method not declared");

        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Dispatcher getInstance() {
        if (instance == null) {
            synchronized (Dispatcher.class) {
                if (instance == null) {
                    instance = new Dispatcher();
                }
            }
        }
        return instance;
    }


}
