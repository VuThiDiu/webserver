package dto;


import lombok.Data;

@Data
public class HttpSession {
    private String userId;
    private String sessionId;
    private Long createdAt;
    private Long lastAccessedAt;

    public HttpSession(String userId, String sessionId, Long createdAt, Long lastAccessedAt) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.createdAt = createdAt;
        this.lastAccessedAt = lastAccessedAt;
    }
}
