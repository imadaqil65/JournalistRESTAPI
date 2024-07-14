package nl.fontys.s3.grp1.configuration.security.token;

public interface AccessTokenEncoder {
    String encode(AccessToken accessToken);
}
