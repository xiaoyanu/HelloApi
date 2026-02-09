package ee.zxz.helloapi.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;

public class JwtUtil {

    private static final String KEY = "HelloAPI-test";
//    private static final String KEY = "HelloAPI-test" + new Date();

    /**
     * 生成JWT token
     *
     * @param uid      用户ID
     * @param username 用户名
     * @return JWT token
     */
    public static String getToken(int uid, String username) {
        Calendar instance = Calendar.getInstance();
        // 7天过期
        instance.add(Calendar.DATE, 7);

        return JWT.create()
                .withClaim("id", uid)  // 用户UID
                .withClaim("name", username)  // 用户名
                .withExpiresAt(instance.getTime()) // 过期时间
                .sign(Algorithm.HMAC256(KEY));// 签名
    }

    /**
     * 通过token获取UID
     *
     * @param token JWT token
     * @return 用户ID(0表示无效Token)
     */
    public static int deToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return 0;
        }
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(KEY)).build();
            DecodedJWT verify = jwtVerifier.verify(token);
            if (verify.getClaim("uid").isNull()) {
                return 0;
            }
            return verify.getClaim("uid").asInt();
        } catch (Exception e) {
            return 0;
        }
    }
}
