package service;

import annotation.http.DeleteMapping;
import annotation.http.GetMapping;
import annotation.http.PostMapping;
import annotation.http.PutMapping;
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


    /* volatile key word ensure that : any write to it is immediately visible to all other thread.  */
    private static volatile Dispatcher instance;

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

    public void registerController(Object controller) {
        mapMethodsFromController(controller.getClass());
        mapsController.put(controller.getClass().getName(), controller);
    }

    private Dispatcher() {
        /* Init loading hashMap*/
        getMethod = new HashMap<>();
        postMethod = new HashMap<>();
        putMethod = new HashMap<>();
        deleteMethod = new HashMap<>();

        mapsController = new HashMap<>();

        /* Load all controller*/
    }

    private void mapMethodsFromController(Class controller) {
        for (Method method : controller.getDeclaredMethods()) {
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


    public Object execute(String method, String path, String requestBody) {
        try {
            switch (method) {
                case "GET":
                    return handleRequest(getMethod, path);
                case "POST":
                    return handleRequest(postMethod, path, requestBody);
                case "PUT":
                    return handleRequest(putMethod, path, requestBody);
                case "DELETE":
                    return handleRequest(deleteMethod, path, requestBody);
                default:
                    throw new MethodNotDeclared("Method not declared");
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public Object handleRequest(Map<String, Method> methodMaps, String path, Object... args) throws InvocationTargetException, IllegalAccessException {
        Method handler = methodMaps.get(path);
        if (Objects.isNull(handler)) return null;


        String className = handler.getDeclaringClass().getName();
        Object controller = mapsController.get(className);
        return handler.invoke(controller, args);

    }

}
