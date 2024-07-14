package nl.fontys.s3.grp1.configuration.security.token;


public interface AccessToken {
    String getSubject();

    Long getUserId();

    String getRole();

    boolean hasRole(String roleName);
}
