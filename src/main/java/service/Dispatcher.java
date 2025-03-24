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
    private Map<String, Method> getController;
    private Map<String, Method> postController;
    private Map<String, Method> putController;
    private Map<String, Method> deleteController;

    private Object controller;


    private static Dispatcher instance;

    public void setController(Object controller) {
        this.controller = controller;
        scanHandlers();
    }

    private Dispatcher() {
        getController = new HashMap<>();
        postController = new HashMap<>();
        putController = new HashMap<>();
        deleteController = new HashMap<>();
    }

    private void scanHandlers() {
        for (Method method : controller.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                getController.put(getMapping.value(), method);
            }
            if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                postController.put(postMapping.value(), method);
            }
            if (method.isAnnotationPresent(PutMapping.class)) {
                PutMapping putMapping = method.getAnnotation(PutMapping.class);
                putController.put(putMapping.value(), method);
            }
            if (method.isAnnotationPresent(DeleteMapping.class)) {
                DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                deleteController.put(deleteMapping.value(), method);
            }
        }
    }

    public String execute(String method, String path, String requestBody) {
        try {
            if (method.equals(HttpMethod.GET.getMethod())) {
                Method handler = getController.get(path);
                if (Objects.isNull(handler)) return null;
                return (String) handler.invoke(controller);
            } else if (method.equals(HttpMethod.POST.getMethod())) {
                Method handler = postController.get(path);
                if (Objects.isNull(handler)) return null;

                return (String) handler.invoke(controller, requestBody);
            } else if (method.equals(HttpMethod.PUT.getMethod())) {
                Method handler = putController.get(path);
                if (Objects.isNull(handler)) return null;

                return (String) handler.invoke(controller, requestBody);
            } else if (method.equals(HttpMethod.DELETE.getMethod())) {
                Method handler = deleteController.get(path);
                if (Objects.isNull(handler)) return null;

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
