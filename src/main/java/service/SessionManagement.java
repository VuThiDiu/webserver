package service;

import config.ApplicationConfig;
import dto.HttpSession;
import exception.InvalidSession;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class SessionManagement {
    private volatile HashMap<String, HttpSession> sessionMaps;
    private static volatile SessionManagement instance;

    private SessionManagement() {
        sessionMaps = new HashMap<>();
    }

    public SessionManagement getInstance() {
        if (instance == null) {
            synchronized (SessionManagement.class) {
                if (instance == null) {
                    instance = new SessionManagement();
                }
            }
        }
        return instance;
    }


    public HttpSession getSession(String userId) throws InvalidSession {
        HttpSession httpSession = sessionMaps.get(userId);
        if (Objects.isNull(httpSession)) return createNewSession(userId);

        if (isValidSession(httpSession)) return httpSession;
        return httpSession;
    }

    private boolean isValidSession(HttpSession httpSession) throws InvalidSession {
        Long createdTime = httpSession.getCreatedAt();
        Long now = System.currentTimeMillis();
        if (now - createdTime >= ApplicationConfig.getInstance().getSessionTimeout()) {
            throw new InvalidSession("Session is invalid");
        }
        return true;
    }

    private HttpSession createNewSession(String userId) {
        String sessionID = UUID.randomUUID().toString().replaceAll("-", "");
        Long now = System.currentTimeMillis();

        return new HttpSession(userId,
                sessionID,
                now,
                now + ApplicationConfig.getInstance().getSessionTimeout());
    }

    private void destroySession(String userId) {
        sessionMaps.remove(userId);
    }


}
